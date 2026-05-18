import { useState } from "react";
import Option from "./SettingsOption";
import ProfileSettings from "./ProfileSettings";
import { ArrowBigLeftIcon, User2, UserIcon } from "lucide-react";
import { useSettingsStatusContext } from "@/context/SettingsContext";

type SettingsPage = "profile" | "account" | "appearance";

function Settings() {
    const [activePage, setActivePage] = useState<SettingsPage>("profile");
    const { setState } = useSettingsStatusContext();
    const [showMenu, setShowMenu] = useState(true);

    return (
        <div className="pt-0 w-full gap-2 h-full flex flex-col transition-opacity duration-50">
            <div className="h-full flex border-[#313244] border border-solid w-full">
                
                {/* Sidebar */}
                <div className={`${showMenu ? "flex" : "hidden"} md:flex w-full md:w-[30vw] h-full`}>
                    <div className="flex flex-col p-2 m-5 gap-2 w-full">
                        <div onClick={() => setState(false)} className="bg-white/4 hover:bg-white/10 w-full items-center flex h-17 rounded-md border-[#313244]">
                            <ArrowBigLeftIcon className="m-5" />
                            <p className="w-full p-2">Go back</p>
                        </div>
                        <Option name="Profile" isActive={activePage === "profile"} onClick={() => { setActivePage("profile"); setShowMenu(false); }} icon={<UserIcon className="text-white w-6 h-6" />} />
                        <Option name="Account" isActive={activePage === "account"} onClick={() => { setActivePage("account"); setShowMenu(false); }} />
                        <Option name="Appearance" isActive={activePage === "appearance"} onClick={() => { setActivePage("appearance"); setShowMenu(false); }} />
                    </div>
                </div>

                {/* Content */}
                <div className={`${!showMenu ? "flex" : "hidden"} md:flex w-full md:w-[70vw] h-full flex-col`}>
                    <button onClick={() => setShowMenu(true)} className="md:hidden flex items-center gap-2 p-3 hover:bg-white/10">
                        <ArrowBigLeftIcon className="w-5 h-5" />
                        <span>Back</span>
                    </button>
                    {activePage === "profile" && <ProfileSettings />}
                </div>

            </div>
        </div>
    );
}
export default Settings;