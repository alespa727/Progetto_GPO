import { useChatContext } from "@/context/ChatContext";

export function ProfilePicture({ className, src="" }: { className: string, src?: string }) {
    const imagePath = getImagePath();
    console.log(imagePath);
    if(imagePath) src = imagePath;
    return (
        <div className={`rounded-full overflow-hidden ${className}`}>
            <img
                src={src!=="" ? (src ? src : "/placeholder.png") : "/placeholder.png"}
                alt=""
                className="w-full h-full object-cover"
            />
        </div>
    );
}

export function getImagePath(){
    const chat = useChatContext();
    
    if(chat){
        if(chat.friend.imagePath){
            console.log("Chat",chat, chat.friend.imagePath.replace("http://localhost:8080/images","/pfp"));
            return chat.friend.imagePath.replace("http://localhost:8080/","");
        }
        return "";
    } 
}