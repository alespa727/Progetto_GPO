
import { useActiveRoomContext } from "../../context/CallContext";
import { useEffect, useState } from "react";
import { RoomEvent, Track } from "livekit-client";
import { ParticipantContext, TrackRefContext, TrackReference, useParticipants, useTracks } from "@livekit/components-react";
import { useChatContext } from "@/context/ChatContext";
import { Camera, CameraOff, Mic, MicOff, PhoneOff, ScreenShare, ScreenShareOff } from "lucide-react";
import { Button } from "../animate-ui/primitives/buttons/button";
import { useSocket } from "@/context/SocketProvider";
import { ParticipantTileCustom } from "./ParticipantTile";
import { useScreenShare } from "./ScreenShareHelper";
import { AnimatePresence, motion } from "framer-motion";



function ChatCall() {
    const room = useActiveRoomContext().room;
    const { isScreenSharing, toggleScreenShare } = useScreenShare(room);
    const [isLeaving, setIsLeaving] = useState(false);

    const chat = useChatContext();
    const socket = useSocket();
    const trackReferences: TrackReference[] = useTracks(
        [
            Track.Source.Camera,
            Track.Source.Microphone,
            Track.Source.ScreenShare,
        ],
        { onlySubscribed: false }
    );

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
        room.on(RoomEvent.Connected, handleConnected);
        room.on(RoomEvent.Disconnected, handleDisconnected);

        return () => {
            room.off(RoomEvent.Connected, handleConnected);
            room.off(RoomEvent.Disconnected, handleDisconnected);
        };
    }, [room, socket, chat?.id]);

    const participantMap: Record<string, { screen?: TrackReference, video?: TrackReference; audio?: TrackReference }> = {};

    trackReferences.forEach((t) => {

        if (!participantMap[t.participant.sid]) participantMap[t.participant.sid] = {};
        if (t.source === Track.Source.Camera) participantMap[t.participant.sid].video = t;
        if (t.source === Track.Source.Microphone) participantMap[t.participant.sid].audio = t;
        if (t.source === Track.Source.ScreenShare) participantMap[t.participant.sid].screen = t;
    });

    const participants = useParticipants();
    useEffect(() => {
        const setCamera = async () => {
            try {
                await room.localParticipant.setCameraEnabled(isCameraEnabled);
            } catch (error) {
                console.error("Videocamera già in uso");
            }
        }
        setCamera();

        room.localParticipant.setMicrophoneEnabled(isMicrophoneEnabled);

    }, [isMicrophoneEnabled, isCameraEnabled, isScreenSharing]);

    useEffect(() => {
        if (!room) return;
        toggleScreenShare(isScreenSharing);
    }, [isScreenSharing, room]);

    if (!isActive) return;

    return (
        <>
            <AnimatePresence>
                {!isLeaving && (
                    <motion.div
                        initial={{ scale: 0.5, opacity: 0, y: -100 }}
                        animate={{ scale: 1, opacity: 1, y: 0 }}
                        exit={{ scale: 0.96, opacity: 0, y: -30 }}
                        transition={{ type: "spring", stiffness: 260, damping: 25 }}
                        className="w-full relative flex flex-col h-100 bg-black"
                    >
                        
                <div className="flex p-3 justify-center items-center gap-3 h-100">
                    {participants.map((p) => {
                        const tracks = participantMap[p.sid];
                        return (p.isScreenShareEnabled || p.isCameraEnabled) && tracks ? (
                            <TrackRefContext.Provider key={p.sid} value={tracks.screen ?? tracks.video ?? tracks.audio}>
                                <ParticipantContext.Provider value={p}>
                                    <ParticipantTileCustom audioTrack={tracks.audio} videoTrack={tracks.video} screenShare={tracks.screen} />
                                </ParticipantContext.Provider>
                            </TrackRefContext.Provider>
                        ) : <ParticipantContext.Provider key={p.sid} value={p}>
                            <ParticipantTileCustom />
                        </ParticipantContext.Provider>;
                    })}

                </div>

                <div className="absolute bottom-0 flex items-center justify-center w-full h-25 px-10 gap-3 sm:gap-6 transition-all ease-in duration-200">
                    <Button onClick={() => toggleScreenShare(!isScreenSharing)} className={"group aspect-square flex items-center justify-center  h-12 sm:h-17 " + (!isScreenSharing ? "bg-red-700" : "bg-gray-700") + " border border-red-950 rounded-full shadow-md " + (isScreenSharing ? "hover:bg-blue-950" : "") + " transition-colors duration-200"}>
                        {
                            isScreenSharing ? (
                                <ScreenShare className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                            ) :
                                (
                                    <ScreenShareOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                                )

                        }
                    </Button>
                    <Button onClick={() => { setCameraEnabled(!isCameraEnabled) }} className={"group aspect-square flex items-center justify-center h-12 sm:h-17 " + (!isCameraEnabled ? "bg-red-700" : "bg-gray-700") + " border border-red-950 rounded-full shadow-md " + (isCameraEnabled ? "hover:bg-red-950" : "") + " transition-colors duration-200"}>
                        {
                            isCameraEnabled ? (
                                <Camera className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                            ) :
                                (
                                    <CameraOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                                )

                        }
                    </Button>
                    <Button onClick={() => { setMicrophoneEnabled(!isMicrophoneEnabled) }} className={"group aspect-square flex items-center justify-center h-12 sm:h-17 " + (!isMicrophoneEnabled ? "bg-red-700" : "bg-gray-700") + " border border-red-950 rounded-full shadow-md " + (isMicrophoneEnabled ? "hover:bg-red-950" : "") + " transition-colors duration-200"}>
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
                        className="group aspect-square flex items-center justify-center h-12 sm:h-17 bg-red-600 border border-red-950 rounded-full shadow-md hover:bg-red-700 transition-colors duration-200"
                        onClick={() => {
                        
                            setIsLeaving(true);
                           
                            setTimeout(() => room.disconnect(), 300); 

                            setIsLeaving(false)
                        }}
                    >
                        <PhoneOff className="h-6 w-6 text-red-100 group-hover:text-red-200 transition-colors" />
                    </Button>

                </div>
                    </motion.div>
                )}
            </AnimatePresence>
        </>
    );
}



export default ChatCall;
