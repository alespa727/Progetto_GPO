import { useMessageContext } from "../../context/MessageContext";

export function Message() {
    const message = useMessageContext();
    return (
      <>
        <div className="message">
            <div className="text"><b>{message.sender.username}</b> {message.text}</div>
        </div>
      </>
    );
}