import { useAccount } from "@/context/UserProvider";
import { ProfilePicture } from "../chat/ProfilePicture";
import { SectionName } from "../server/SectionName";
import { SectionLoop } from "../server/SezioneLoop";
import Option from "./SettingsOption";

function Settings() {
   const account = useAccount();
  return (
    <div className=
    {'pt-0 w-full gap-2 h-full flex flex-col transition-opacity duration-50'}>
        <div className="h-full flex border-[#313244] border border-solid w-full">
          
          <div className="w-[30vw] border-[rgb(49,50,68)] border-r h-full ">
            <div className="flex items-center p-2 m-5">
              
              <ProfilePicture src="/placeholder.png" className="w-[40%] aspect-square"></ProfilePicture>
              <div className="ml-3 flex flex-col">
                <div>{account?.username}</div>
                <div>Stato</div>
              </div>
            </div>
            
          </div>
          <div className="w-[70vw] h-full ">
            
          </div>



        </div>
    </div>
  );
}

export default Settings;
