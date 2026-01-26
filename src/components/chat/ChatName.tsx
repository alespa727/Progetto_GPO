import { useChatContext } from "../../context/ChatContext";

export function ChatName() {
    const chat = useChatContext();
    if (!chat) return;
    let username = chat.friend.username;
    if (username.length > 9) {
        username = username.slice(0, 9) + "...";
    }

    return (
        <>
            {username}
        </>
    );
}