import { SectionName } from "../server/SectionName";
import { SectionLoop } from "../server/SezioneLoop";
import Option from "./SettingsOption";

function Settings() {
   
  return (
    <div className=
    {'p-3 m-4 pt-0 w-full gap-2 h-full flex flex-col transition-opacity duration-50'}>
        <div className="border-[#313244] border border-solid w-full">
          <Option></Option>
          <Option></Option>
          <Option></Option>
          <Option></Option>
        </div>
        
    </div>
  );
}

export default Settings;
