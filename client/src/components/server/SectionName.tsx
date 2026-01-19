import { useSectionContext } from "../../context/SezioneContext";

export function SectionName({onClick}:{onClick: ()=>void}) {
    const section = useSectionContext();
    return (
        <div
            onClick={onClick}
            className="text-center p-3 m-1 rounded-md font-semibold cursor-pointer
                        hover:bg-white/10 transition-colors duration-200"
            >
            {section.title}
        </div>
    );
}