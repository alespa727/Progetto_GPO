import { useActiveServerContext } from "@/context/ActiveServerProvider";
import { ArrowLeft } from "lucide-react";

export function Header({ value }: { value: string }) {
    const setActiveChannel = useActiveServerContext().setActiveChannel;
    return (
        <div className={"border-white/10 justify-center  transition-colors duration-300 ease-in border-b items-center flex w-full pl-3 p-4 text-center"}>
            <ArrowLeft className="md:hidden block cursor-pointer text-white/60" onClick={()=>setActiveChannel(null)}></ArrowLeft>
            <p className="w-full h-full ">{value}</p>
        
        </div>
    )
}