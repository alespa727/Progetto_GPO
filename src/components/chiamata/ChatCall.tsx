
import { useActiveRoomContext } from "../../context/CallContext";
import { useEffect, useState } from "react";
import { RoomEvent, Track } from "livekit-client";
import { ConnectionState, ParticipantContext, ParticipantLoop, ParticipantName, ParticipantTile, TrackRefContext, TrackReference, useParticipants, useTracks } from "@livekit/components-react";
import { useChatContext } from "@/context/ChatContext";
import { Camera, CameraOff, Mic, MicOff, PhoneOff, ScreenShare, ScreenShareOff } from "lucide-react";
import { Button } from "../animate-ui/primitives/buttons/button";
import { useSocket } from "@/context/SocketProvider";
import { ParticipantTileCustom } from "./ParticipantTile";



function ChatCall() {
    const room = useActiveRoomContext().room;
    const chat = useChatContext();
    const socket = useSocket();
    const trackReferences: TrackReference[] = useTracks([Track.Source.Camera, Track.Source.Microphone]);
    const [isActive, setIsActive] = useState<boolean>(false);
    const [isMicrophoneEnabled, setMicrophoneEnabled] = useState(false);
    const [isCameraEnabled, setCameraEnabled] = useState(false);
    useEffect(() => {
        if (!room) return;

        const updateActiveState = () => {
            const active =
                room.state === "connected" || room.state === "connecting";
            setIsActive(active);
        };

        const handleConnected = () => {
            setIsActive(true);
            setMicrophoneEnabled(true)
            socket?.emit("join_call", { chatId: chat?.id });
        };

        const handleDisconnected = () => {
            setIsActive(false);
            socket?.emit("leave_call", { chatId: chat?.id });
        };

        updateActiveState();


        room.localParticipant.setMicrophoneEnabled(true);

        room.on(RoomEvent.Connected, handleConnected);
        room.on(RoomEvent.Disconnected, handleDisconnected);

        return () => {
            room.off(RoomEvent.Connected, handleConnected);
            room.off(RoomEvent.Disconnected, handleDisconnected);
        };
    }, [room, socket, chat?.id]);

    const participantMap: Record<string, { video?: TrackReference; audio?: TrackReference }> = {};

    trackReferences.forEach((t) => {
        if (!participantMap[t.participant.sid]) participantMap[t.participant.sid] = {};
        if (t.source === Track.Source.Camera) participantMap[t.participant.sid].video = t;
        if (t.source === Track.Source.Microphone) participantMap[t.participant.sid].audio = t;
    });

    const participants = useParticipants();
    useEffect(() => {
        const setCamera = async()=>{
            try {
            await room.localParticipant.setCameraEnabled(isCameraEnabled);
            } catch (error) {
                console.error("Videocamera già in uso");
            }
        }
        setCamera();
        
        room.localParticipant.setMicrophoneEnabled(isMicrophoneEnabled);
    }, [isMicrophoneEnabled, isCameraEnabled]);

    if (!isActive) return;

    return (
        <>
            <div className="w-full relative flex flex-col h-100 bg-black ">

                <div className="flex p-3 justify-center items-center gap-3 h-100">
                    {participants.map((p) => {
                        const tracks = participantMap[p.sid];
                        return p.isCameraEnabled && tracks ? (
                            <TrackRefContext.Provider key={p.sid} value={tracks.video ?? tracks.audio}>
                                <ParticipantContext.Provider value={p}>
                                    <ParticipantTileCustom audioTrack={tracks.audio} videoTrack={tracks.video} />
                                </ParticipantContext.Provider>
                            </TrackRefContext.Provider>
                        ) : <ParticipantContext.Provider value={p}>
                                    <ParticipantTileCustom />
                            </ParticipantContext.Provider>;
                    })}

                </div>

                <div className="absolute bottom-0 flex items-center justify-center w-full h-25 px-10 gap-6">
                    <Button onClick={() => { }} className={"group aspect-square flex items-center justify-center h-17 " + (!isCameraEnabled ? "bg-red-700" : "bg-gray-700") + " border border-red-950 rounded-full shadow-md " + (isCameraEnabled ? "hover:bg-blue-950" : "") + " transition-colors duration-200"}>
                        {
                            isCameraEnabled ? (
                                <ScreenShare className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                            ) :
                                (
                                    <ScreenShareOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                                )

                        }
                    </Button>
                    <Button onClick={() => { setCameraEnabled(!isCameraEnabled) }} className={"group aspect-square flex items-center justify-center h-17 " + (!isCameraEnabled ? "bg-red-700" : "bg-gray-700") + " border border-red-950 rounded-full shadow-md " + (isCameraEnabled ? "hover:bg-red-950" : "") + " transition-colors duration-200"}>
                        {
                            isCameraEnabled ? (
                                <Camera className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                            ) :
                                (
                                    <CameraOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                                )

                        }
                    </Button>
                    <Button onClick={() => { setMicrophoneEnabled(!isMicrophoneEnabled) }} className={"group aspect-square flex items-center justify-center h-17 " + (!isMicrophoneEnabled ? "bg-red-700" : "bg-gray-700") + " border border-red-950 rounded-full shadow-md " + (isMicrophoneEnabled ? "hover:bg-red-950" : "") + " transition-colors duration-200"}>
                        {
                            isMicrophoneEnabled ? (
                                <Mic className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                            ) :
                                (
                                    <MicOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                                )

                        }
                    </Button>

                    <Button
                        className="group aspect-square flex items-center justify-center h-17 bg-red-600 border border-red-950 rounded-full shadow-md hover:bg-red-700 transition-colors duration-200"
                        onClick={() => room.disconnect()}
                    >
                        <PhoneOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                    </Button>

                </div>
            </div>
        </>
    );
}



export default ChatCall;
