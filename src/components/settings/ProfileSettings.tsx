import { useAccount, useSetAccount } from "@/context/UserProvider";
import { Account, ClientHttp } from "@/types";
import { useEffect, useRef, useState } from "react";
import myPlaceholder from "../../../public/placeholder.png";
import { useSettingsStatusContext } from "@/context/SettingsContext";
import { useDropzone } from "react-dropzone";
import { ChangePfp } from "./ChangePfp";
import MyCustomZoom from "../common/CustomZoom";

function ProfileSettings() {
    const [name, setName] = useState<string | null>(null);
    const [isFree, setFree] = useState<boolean>(true);
    const [message, setMessage] = useState<string>("");
    const [description, setDescription] = useState<string | null>(null);
    const account = useAccount();
    
    const setAccount = useSetAccount();
    const setState = useSettingsStatusContext().setState;
    const profileSrc = account?.path ? account.path.replace("http://localhost:8080/", "") : myPlaceholder;

    const patchPfp = async ()=>{

    };

    const patchProfile = async () => {
        if (setAccount && account?.username) {

            setState(
                !(await ClientHttp.patchProfile({
                    username: name ?? account.username,
                    description: description ?? account.description
                })
            ));

            setAccount(new Account(account.createdAt, account.isAdmin, account.path, description ?? account.description,name ?? account.username))

        }
    }
    return (
        <div className="flex flex-col gap-6 p-8 text-white">
            <h2 className="text-xl font-semibold">Profile</h2>

            <div className="flex items-center gap-4">
                <MyCustomZoom src={profileSrc ?? myPlaceholder} className="w-26 h-26 rounded-full bg-white/20" alt={"pfp"}></MyCustomZoom>
                
                <ChangePfp></ChangePfp>
            </div>

            <div className="flex flex-col gap-4">
                <div className="flex flex-col gap-1">
                    <label className="text-sm text-white/60">Nome</label>
                    <input
                        value={name === null ? account?.username : name}
                        onChange={async (e) => {
                            const value = e.target.value;
                            setName(value);

                            if (!value) return;

                            if (value.length > 20) {
                                setMessage("Nome troppo lungo");
                                setFree(false);
                                return;
                            }

                            if(value===account?.username){
                                setFree(true);
                            }else{
                                  const free = await ClientHttp.isUsernameFree({ username: value });
                                setFree(free);
                                setMessage(free ? "" : "Nome già utilizzato");
                            }
                          
                        }}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") patchProfile();
                        }}
                        className="bg-transparent border border-[#313244] rounded-md px-3 py-2 outline-none focus:border-white/40" />

                </div>
                <div className="flex flex-col gap-1">
                    <label className="text-sm text-white/60">Bio</label>
                    <textarea
                        value={description === null ? account?.description : description}
                        className="bg-transparent border border-[#313244] rounded-md px-3 py-2 outline-none focus:border-white/40 resize-none"
                        onChange={(e) => setDescription(e.target.value)}
                        onKeyDown={(e) => {
                            if (e.key === "Enter") patchProfile();
                        }}
                        placeholder="Scrivi qui..."
                        rows={3} />
                </div>
            </div>

            <button
                onClick={patchProfile}
                disabled={!isFree}
                className="self-start px-6 py-2 rounded-md bg-white text-black text-sm font-medium hover:bg-white/80 disabled:opacity-50 disabled:cursor-not-allowed">
                Salva
            </button>

            {message ?? <div className="w-full h-10 bg-red-400/10">
                {message}
            </div>}
        </div>
    );
}

export default ProfileSettings;

