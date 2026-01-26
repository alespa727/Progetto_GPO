import placeholder from "../../assets/placeholder.png";
import { useChatContext } from "@/context/ChatContext";

export function ProfilePicture({ className, src="" }: { className: string, src?: string }) {
    const imagePath = getImagePath();
    console.log(imagePath);
    if(imagePath) src = imagePath;
    return (
        <div className={`rounded-full overflow-hidden ${className}`}>
            <img
                src={src!=="" ? (src ? src : placeholder) : ""}
                alt=""
                className="w-full h-full object-cover"
            />
        </div>
    );
}

export function getImagePath(){
    const chat = useChatContext();
    
    if(chat){
        console.log("Chat",chat, chat.friend.imagePath)
        return chat.friend.imagePath;
    } 
}