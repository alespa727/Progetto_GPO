import React, { useState } from "react";
import Modal from "../common/Modal";
import {
    Pen, Plus, Trash,
    Hash, Volume, Link, Settings, LayoutList, RefreshCw, Copy, Check, Save
} from "lucide-react";
import * as ContextMenu from "@radix-ui/react-context-menu";
import { useActiveServerContext } from "@/context/ActiveServerProvider";
import { ClientHttp } from "@/types";
import axios from "axios";
import { updateServers } from "@/context/ServerListContext";

type ManageTab = "canali" | "invito" | "impostazioni";
type ChannelType = "testo" | "vocale";

type LocalChannel = {
    tempKey: string;
    id: number | null;
    name: string;
    description: string;
    type: ChannelType;
    deleted: boolean;
    isNew: boolean;
};

type LocalSection = {
    tempKey: string;
    id: number | null;
    name: string;
    channels: LocalChannel[];
    isNew: boolean;
    deleted: boolean;
};

const makeChannel = (overrides?: Partial<LocalChannel>): LocalChannel => ({
    tempKey: crypto.randomUUID(),
    id: null,
    name: "nuovo-canale",
    description: "",
    type: "testo",
    deleted: false,
    isNew: true,
    ...overrides,
});

const makeSection = (withDefaultChannel = true): LocalSection => ({
    tempKey: crypto.randomUUID(),
    id: null,
    name: "nuova-sezione",
    channels: withDefaultChannel ? [makeChannel()] : [],
    isNew: true,
    deleted: false,
});

const toLocalSections = (server: any): LocalSection[] =>
    server?.sections.map((s: any) => ({
        tempKey: String(s.id),
        id: s.id,
        name: s.name,
        isNew: false,
        deleted: false,
        channels: s.channels.map((c: any): LocalChannel => ({
            tempKey: String(c.id),
            id: c.id,
            name: c.name,
            description: c.description ?? "",
            type: c.type as ChannelType,
            deleted: false,
            isNew: false,
        })),
    })) ?? [];

export function ManageServer({ active, setActive }: { active: boolean; setActive: (a: boolean) => void }) {
    const activeServer = useActiveServerContext().activeServer;
    const forceUpdate = updateServers();

    const [tab, setTab] = useState<ManageTab>("canali");

    // ── Canali ──
    const [sections, setSections] = useState<LocalSection[]>(() => toLocalSections(activeServer));
    const [channelsDirty, setChannelsDirty] = useState(false);
    const [channelsSaving, setChannelsSaving] = useState(false);
    const [channelsSaveResponse, setChannelsSaveResponse] = useState<{ msg: string; ok: boolean } | null>(null);

    // ── Invito ──
    const [inviteCode, setInviteCode] = useState<string>(activeServer?.inviteCode ?? "");
    const [copied, setCopied] = useState(false);

    // ── Impostazioni ──
    const [serverName, setServerName] = useState<string>(activeServer?.name ?? "");
    const [serverDesc, setServerDesc] = useState<string>(activeServer?.description ?? "");
    const [saveResponse, setSaveResponse] = useState<{ msg: string; ok: boolean } | null>(null);

    // ── Guardie "un solo nuovo alla volta" ──
    const hasPendingSection = sections.some(s => s.isNew && !s.deleted);
    const hasPendingChannelInSection = (sectionTempKey: string) =>
        sections.find(s => s.tempKey === sectionTempKey)
            ?.channels.some(c => c.isNew && !c.deleted) ?? false;

    // ────────────────────────────────────────────
    //  Helpers sezioni
    // ────────────────────────────────────────────

    const addSection = () => {
        if (hasPendingSection) return;
        setSections(prev => [...prev, makeSection(true)]);
        setChannelsDirty(true);
    };

    const updateSectionName = (sectionTempKey: string, name: string) => {
        setSections(prev => prev.map(s =>
            s.tempKey !== sectionTempKey ? s : { ...s, name }
        ));
        setChannelsDirty(true);
    };

    const markSectionDeleted = (sectionTempKey: string, isNew: boolean) => {
        if (isNew) {
            setSections(prev => prev.filter(s => s.tempKey !== sectionTempKey));
        } else {
            setSections(prev => prev.map(s =>
                s.tempKey !== sectionTempKey ? s : {
                    ...s,
                    deleted: true,
                    channels: s.channels.map(c => ({ ...c, deleted: true })),
                }
            ));
        }
        setChannelsDirty(true);
    };

    // ────────────────────────────────────────────
    //  Helpers canali
    // ────────────────────────────────────────────

    const addChannel = (sectionTempKey: string) => {
        if (hasPendingChannelInSection(sectionTempKey)) return;
        setSections(prev => prev.map(s =>
            s.tempKey !== sectionTempKey ? s : {
                ...s,
                channels: [...s.channels, makeChannel()],
            }
        ));
        setChannelsDirty(true);
    };

    const updateChannel = (sectionTempKey: string, channelTempKey: string, patch: Partial<LocalChannel>) => {
        setSections(prev => prev.map(s =>
            s.tempKey !== sectionTempKey ? s : {
                ...s,
                channels: s.channels.map(c =>
                    c.tempKey !== channelTempKey ? c : { ...c, ...patch }
                ),
            }
        ));
        setChannelsDirty(true);
    };

    const markChannelDeleted = (sectionTempKey: string, channel: LocalChannel) => {
        if (channel.isNew) {
            setSections(prev => prev.map(s =>
                s.tempKey !== sectionTempKey ? s : {
                    ...s,
                    channels: s.channels.filter(c => c.tempKey !== channel.tempKey),
                }
            ));
        } else {
            updateChannel(sectionTempKey, channel.tempKey, { deleted: true });
        }
        setChannelsDirty(true);
    };

    // ────────────────────────────────────────────
    //  Commit
    // ────────────────────────────────────────────

    const commitChannels = async () => {
        console.log("ciao")
        if (!activeServer) return;
        console.log("ciao2")
        setChannelsSaving(true);
        setChannelsSaveResponse(null);
        try {
            for (const section of sections) {
                if (section.isNew && !section.deleted) {
                    // Crea sezione + suoi canali nuovi
                    const newSection = await ClientHttp.createSection(activeServer.id, section.name);
                    for (const ch of section.channels.filter(c => !c.deleted)) {
                        await ClientHttp.createChannel(activeServer.id, newSection.id, ch.name, ch.description, ch.type);
                    }
                } else if (!section.isNew && section.deleted && section.id !== null) {
                    await ClientHttp.deleteSection(activeServer.id, section.id);
                } else if (!section.isNew && !section.deleted && section.id !== null) {
                    for (const ch of section.channels) {
                        if (ch.isNew && !ch.deleted) {
                            await ClientHttp.createChannel(activeServer.id, section.id, ch.name, ch.description, ch.type);
                        } else if (!ch.isNew && ch.deleted && ch.id !== null) {
                            await ClientHttp.deleteChannel(activeServer.id,section.id, ch.id);
                        } else if (!ch.isNew && !ch.deleted && ch.id !== null) {
                            await ClientHttp.updateChannel(activeServer.id, section.id, ch.id, ch.name, ch.description, ch.type);
                        }
                    }
                }
            }
            forceUpdate();
            setChannelsDirty(false);
            setChannelsSaveResponse({ msg: "Canali salvati!", ok: true });
        } catch (error: unknown) {
            console.log(error)
            if (axios.isAxiosError(error)) {
                setChannelsSaveResponse({ msg: error.response?.data?.message ?? "Errore durante il salvataggio.", ok: false });
            }
        } finally {
            setChannelsSaving(false);
        }
    };

    const resetChannels = () => {
        setSections(toLocalSections(activeServer));
        setChannelsDirty(false);
        setChannelsSaveResponse(null);
    };

    // ────────────────────────────────────────────
    //  Invito
    // ────────────────────────────────────────────

    const copyCode = () => {
        navigator.clipboard?.writeText(inviteCode);
        setCopied(true);
        setTimeout(() => setCopied(false), 1800);
    };

    const regenerateCode = async () => {
        try {
            const res = await ClientHttp.regenerateInviteCode(activeServer!.id);
            setInviteCode(res);
            if (activeServer) activeServer.inviteCode = res;
        } catch (e) {
            console.error(e);
        }
    };

    // ────────────────────────────────────────────
    //  Impostazioni
    // ────────────────────────────────────────────

    const saveSettings = async () => {
        if (!serverName.trim()) {
            setSaveResponse({ msg: "Il nome non può essere vuoto.", ok: false });
            return;
        }
        try {
            await ClientHttp.updateCommunity(activeServer!.id, serverName, serverDesc);
            setSaveResponse({ msg: "Modifiche salvate!", ok: true });
            forceUpdate();
            if (activeServer) {
                activeServer.name = serverName;
                activeServer.description = serverDesc;
            }
        } catch (error: unknown) {
            if (axios.isAxiosError(error)) {
                setSaveResponse({ msg: error.response?.data?.message ?? "Errore.", ok: false });
            }
        }
    };

    // ────────────────────────────────────────────
    //  Nav
    // ────────────────────────────────────────────

    const navItem = (id: ManageTab, Icon: React.ElementType, label: string) => (
        <button
            onClick={() => setTab(id)}
            className={`flex items-center gap-2 px-3 py-2 rounded-md text-sm w-full text-left transition-colors
                ${tab === id ? "bg-white/10 text-white" : "text-gray-400 hover:bg-white/7 hover:text-white"}`}
        >
            <Icon className="w-4 h-4" />
            {label}
        </button>
    );

    // ────────────────────────────────────────────
    //  Render
    // ────────────────────────────────────────────

    return (
        <Modal open={active} onOpenChange={setActive} className="w-[560px]">
            <div className="flex bg-(--crust) rounded-(--radius) overflow-hidden max-h-[600px]">

                {/* Sidebar */}
                <div className="w-[160px] flex-shrink-0 bg-black/20 border-r border-white/8 p-2 flex flex-col gap-1">
                    <p className="px-2 pt-2 pb-1 text-[10px] uppercase tracking-widest text-gray-600 font-medium">
                        Gestione
                    </p>
                    {navItem("canali", LayoutList, "Canali")}
                    {navItem("invito", Link, "Codice invito")}
                    {navItem("impostazioni", Settings, "Impostazioni")}
                </div>

                {/* Contenuto */}
                <div className="flex-1 p-5 overflow-y-auto flex flex-col min-h-0">

                    {/* ── Scheda: Canali ── */}
                    {tab === "canali" && (
                        <div className="flex flex-col gap-1 flex-1">

                            {sections.filter(s => !s.deleted).map(s => (
                                <div key={s.tempKey}>

                                    {/* Header sezione */}
                                    <div className="flex items-center gap-1 px-2 py-1 mt-2 group/section">
                                        <input
                                            className="text-xs font-medium text-gray-500 uppercase tracking-widest bg-transparent outline-none flex-1 min-w-0
                                                focus:text-gray-300 focus:border-b focus:border-white/20"
                                            value={s.name}
                                            onChange={e => updateSectionName(s.tempKey, e.target.value)}
                                        />
                                        {s.isNew && (
                                            <span className="text-[10px] text-green-400/70 shrink-0">nuova</span>
                                        )}
                                        <button
                                            onClick={() => markSectionDeleted(s.tempKey, s.isNew)}
                                            className="opacity-0 group-hover/section:opacity-100 transition-opacity text-gray-600 hover:text-red-400 shrink-0"
                                        >
                                            <Trash className="w-3 h-3" />
                                        </button>
                                    </div>

                                    {/* Canali */}
                                    {s.channels.filter(c => !c.deleted).map(c => (
                                        <ContextMenu.Root key={c.tempKey}>
                                            <ContextMenu.Trigger asChild>
                                                <div className="flex items-center gap-2 px-2 py-1.5 rounded-md cursor-pointer hover:bg-white/10 group/channel">
                                                    <div className="w-4" />
                                                    {c.type === "vocale"
                                                        ? <Volume className="w-4 h-4 shrink-0 text-gray-500" />
                                                        : <Hash className="w-4 h-4 shrink-0 text-gray-500" />}
                                                    <input
                                                        className="text-sm text-gray-300 bg-transparent flex-1 min-w-0 outline-none
                                                            focus:border-b focus:border-white/20"
                                                        value={c.name}
                                                        onClick={e => e.stopPropagation()}
                                                        onChange={e => updateChannel(s.tempKey, c.tempKey, { name: e.target.value })}
                                                    />
                                                    {c.isNew && (
                                                        <span className="text-[10px] text-green-400/70 shrink-0">nuovo</span>
                                                    )}
                                                    <button
                                                        onClick={e => { e.stopPropagation(); markChannelDeleted(s.tempKey, c); }}
                                                        className="opacity-0 group-hover/channel:opacity-100 transition-opacity text-gray-600 hover:text-red-400 shrink-0"
                                                    >
                                                        <Trash className="w-3 h-3" />
                                                    </button>
                                                </div>
                                            </ContextMenu.Trigger>

                                            <ContextMenu.Content className="bg-[#1e1e2e] text-white rounded-xl shadow-2xl border border-white/10 p-1.5 z-100 min-w-[200px]">
                                                <div className="px-3 py-1.5 text-xs text-gray-500 uppercase tracking-widest">Tipo</div>
                                                {(["testo", "vocale"] as ChannelType[]).map(t => (
                                                    <ContextMenu.Item
                                                        key={t}
                                                        onSelect={() => updateChannel(s.tempKey, c.tempKey, { type: t })}
                                                        className={`px-3 py-2 rounded-lg flex items-center gap-2.5 text-sm cursor-pointer transition-colors hover:bg-white/10
                                                            ${c.type === t ? "text-white" : "text-white/50"}`}
                                                    >
                                                        {t === "testo" ? <Hash className="w-4 h-4" /> : <Volume className="w-4 h-4" />}
                                                        <span className="capitalize">{t}</span>
                                                        {c.type === t && <Check className="w-3 h-3 ml-auto" />}
                                                    </ContextMenu.Item>
                                                ))}
                                                <ContextMenu.Separator className="my-1 border-t border-white/10" />
                                                <ContextMenu.Item
                                                    onSelect={() => {
                                                        const desc = window.prompt("Nuova descrizione:", c.description);
                                                        if (desc !== null) updateChannel(s.tempKey, c.tempKey, { description: desc });
                                                    }}
                                                    className="px-3 py-2 rounded-lg flex items-center gap-2.5 text-sm cursor-pointer hover:bg-white/10 text-white/90"
                                                >
                                                    <Pen className="w-4 h-4" />
                                                    <span>Cambia descrizione</span>
                                                </ContextMenu.Item>
                                                <ContextMenu.Separator className="my-1 border-t border-white/10" />
                                                <ContextMenu.Item
                                                    onSelect={() => markChannelDeleted(s.tempKey, c)}
                                                    className="px-3 py-2 rounded-lg flex items-center gap-2.5 text-sm cursor-pointer hover:bg-red-500/20 text-red-400"
                                                >
                                                    <Trash className="w-4 h-4" />
                                                    <span>Elimina</span>
                                                </ContextMenu.Item>
                                            </ContextMenu.Content>
                                        </ContextMenu.Root>
                                    ))}

                                    {/* Aggiungi canale */}
                                    {(() => {
                                        const blocked = hasPendingChannelInSection(s.tempKey);
                                        return (
                                            <div
                                                onClick={() => addChannel(s.tempKey)}
                                                className={`flex items-center gap-2 px-2 py-1.5 rounded-md text-sm transition-colors
                                                    ${blocked
                                                        ? "text-gray-700 cursor-not-allowed"
                                                        : "text-gray-600 cursor-pointer hover:bg-white/10 hover:text-gray-400"}`}
                                            >
                                                <div className="w-4" />
                                                <Plus className="w-4 h-4" />
                                                <span>Aggiungi canale</span>
                                            </div>
                                        );
                                    })()}
                                </div>
                            ))}

                            {/* Aggiungi sezione */}
                            <div
                                onClick={addSection}
                                className={`flex items-center gap-2 px-2 py-1 mt-2 rounded-md text-sm transition-colors
                                    ${hasPendingSection
                                        ? "text-gray-700 cursor-not-allowed"
                                        : "text-gray-600 cursor-pointer hover:bg-white/10 hover:text-gray-400"}`}
                            >
                                <Plus className="w-4 h-4" />
                                <span>Aggiungi sezione</span>
                            </div>

                            {/* Commit bar */}
                            {channelsDirty && (
                                <div className="mt-auto pt-4 border-t border-white/10 flex items-center gap-3">
                                    {channelsSaveResponse && (
                                        <span className={`text-xs flex-1 ${channelsSaveResponse.ok ? "text-green-400" : "text-red-400"}`}>
                                            {channelsSaveResponse.msg}
                                        </span>
                                    )}
                                    <button
                                        onClick={resetChannels}
                                        className="text-xs text-gray-500 hover:text-white transition-colors"
                                    >
                                        Annulla
                                    </button>
                                    <button
                                        onClick={commitChannels}
                                        disabled={channelsSaving}
                                        className="flex items-center gap-2 text-sm bg-white/10 hover:bg-white/15 disabled:opacity-50 text-white rounded-md px-4 py-2 transition-colors"
                                    >
                                        {channelsSaving
                                            ? <div className="w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin" />
                                            : <Save className="w-4 h-4" />}
                                        Salva canali
                                    </button>
                                </div>
                            )}
                        </div>
                    )}

                    {/* ── Scheda: Codice invito ── */}
                    {tab === "invito" && (
                        <div className="flex flex-col gap-4">
                            <div>
                                <p className="text-xs text-gray-500 mb-2">Codice invito attuale</p>
                                <div className="flex items-center gap-2 bg-white/5 border border-white/10 rounded-md px-4 py-3">
                                    <span className="font-mono text-lg tracking-widest text-[#7ED957] flex-1">
                                        {inviteCode || "—"}
                                    </span>
                                    <button
                                        onClick={copyCode}
                                        className="flex items-center gap-1.5 text-sm text-gray-400 hover:text-white transition-colors"
                                    >
                                        {copied
                                            ? <><Check className="w-4 h-4 text-green-400" /> Copiato</>
                                            : <><Copy className="w-4 h-4" /> Copia</>}
                                    </button>
                                </div>
                                <p className="text-xs text-gray-600 mt-2">
                                    Condividi questo codice con chi vuoi invitare. Chiunque lo inserisca potrà entrare nel server.
                                </p>
                            </div>
                            <button
                                onClick={regenerateCode}
                                className="flex items-center gap-2 text-sm text-gray-400 hover:text-white bg-white/5 hover:bg-white/10 border border-white/10 rounded-md px-3 py-2 transition-colors w-fit"
                            >
                                <RefreshCw className="w-4 h-4" /> Rigenera codice
                            </button>
                            <p className="text-xs text-gray-600">Rigenerare invalida il codice attuale.</p>
                        </div>
                    )}

                    {/* ── Scheda: Impostazioni ── */}
                    {tab === "impostazioni" && (
                        <div className="flex flex-col gap-4">
                            <div>
                                <p className="text-xs text-gray-500 mb-1.5">Nome del server</p>
                                <input
                                    type="text"
                                    className="w-full p-2 rounded-md bg-(--surface1) border border-white/10 text-sm"
                                    placeholder="Es. Server di Tommy"
                                    value={serverName}
                                    onChange={(e) => setServerName(e.target.value)}
                                />
                            </div>
                            <div>
                                <p className="text-xs text-gray-500 mb-1.5">Descrizione</p>
                                <textarea
                                    className="w-full p-2 rounded-md bg-(--surface1) border border-white/10 text-sm resize-y"
                                    placeholder="Una breve descrizione del server..."
                                    rows={3}
                                    value={serverDesc}
                                    onChange={(e) => setServerDesc(e.target.value)}
                                />
                            </div>
                            {saveResponse && (
                                <div className={`text-sm px-3 py-2 rounded-md ${saveResponse.ok ? "bg-green-500/20 text-green-400" : "bg-red-500/20 text-red-400"}`}>
                                    {saveResponse.msg}
                                </div>
                            )}
                            <button
                                onClick={saveSettings}
                                className="self-start text-sm bg-white/10 hover:bg-white/15 text-white rounded-md px-4 py-2 transition-colors"
                            >
                                Salva modifiche
                            </button>

                            <hr className="border-white/10 my-1" />

                            <div>
                                <p className="text-xs text-red-500/70 mb-2 uppercase tracking-widest">Zona pericolo</p>
                                <button className="flex items-center gap-2 text-sm text-red-400 hover:bg-red-500/15 bg-red-500/10 rounded-md px-3 py-2 transition-colors">
                                    <Trash className="w-4 h-4" /> Elimina server
                                </button>
                            </div>
                        </div>
                    )}

                </div>
            </div>
        </Modal>
    );
}