import { endpoint2 } from "@/types";
import {
    AudioTrack,
    TrackRefContext,
    TrackReference,
    useParticipantContext,
    useRoomContext,
    VideoTrack,
} from "@livekit/components-react";
import axios from "axios";
import * as motion from "motion/react-client";
import { useEffect, useState } from "react";
import { useScreenShare } from "./ScreenShareHelper";

export function ParticipantTileCustom({
    videoTrack,
    audioTrack,
    screenShare,
}: {
    videoTrack?: TrackReference;
    audioTrack?: TrackReference;
    screenShare?: TrackReference;
}) {

    const participant = useParticipantContext();
    const { audioLevel } = participant;
    const room = useRoomContext();

    const [pfp, setPfp] = useState("");
    const [state, setState] = useState("");

    useEffect(() => {
        const downloadPfp = async () => {
            if (!participant.identity) return;

            const res = await axios.post(
                endpoint2 + "/admin/profiles",
                JSON.stringify({
                    users: [
                        {
                            username: participant.identity
                        },
                    ]
                }),
                { 
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/json"
                    }
                 },
            );

            setPfp(res.data.friends[0].imagePath.replace("http://localhost:8080", ""));
        };

        downloadPfp();
    }, [participant.identity]);

    useEffect(() => {
        setState(room.state);
    }, [room.state]);

    if (screenShare && participant.isScreenShareEnabled) {
        console.log("SCREENSHARE")
        return (
            <TrackRefContext value={screenShare} key={participant.identity}>
                <motion.div
                    initial={{ opacity: 0, scale: 0.1 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ type: "spring", stiffness: 500, damping: 30, mass: 0.1 }}
                    className={`aspect-video h-30 sm:h-50 mb-5 p-1/2 flex items-center backdrop-blur-2xl justify-center m-1 rounded-md border-4
        ${audioLevel > 0 ? "border-green-500" : "border-gray-500"}`}
                >
                    <div className="relative w-full h-full">
                        {audioTrack && <AudioTrack trackRef={audioTrack} />}

                        {
                            screenShare && (
                                <VideoTrack style={{
                                    objectFit: 'cover',
                                }} trackRef={screenShare} className="w-full h-full" />
                            )
                        }

                        <div className="absolute top-0 left-0 w-full px-4 py-3 text-gray-200">
                            <h3 className="text-xl font-medium">
                                {participant.identity}
                            </h3>
                        </div>
                    </div>
                </motion.div>
            </TrackRefContext>
        );
    }

    if (participant.isCameraEnabled) {
        return (
            <motion.div
                initial={{ opacity: 0, scale: 0.1 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{
                    type: "spring",
                    stiffness: 500,
                    damping: 30,
                    mass: 0.1,
                }}
                className={`aspect-video h-30 sm:h-50 mb-5 p-1/2 flex items-center backdrop-blur-2xl justify-center m-1 rounded-md border-4
            ${audioLevel > 0 ? "border-green-500" : "border-gray-500"}`}
            >
                <div className="relative w-full h-full">
                    {audioTrack && <AudioTrack trackRef={audioTrack} />}

                    {
                        videoTrack && (
                            <VideoTrack style={{
                                transform: 'scaleX(-1)',
                                objectFit: 'cover',
                            }} trackRef={videoTrack} className="w-full h-full" />
                        )
                    }

                    <div className="absolute top-0 left-0 w-full px-4 py-3 text-gray-200">
                        <h3 className="text-xl font-medium">
                            {participant.identity}
                        </h3>
                    </div>
                </div>
            </motion.div>
        );
    }

    if (state === "connected") {
        return (
            <motion.div
                initial={{ opacity: 0, scale: 0.1 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{
                    type: "spring",
                    stiffness: 500,
                    damping: 30,
                    mass: 0.1,
                }}
                className={`aspect-square h-32 p-1/2 flex items-center backdrop-blur-2xl justify-center m-1 rounded-full border-4
          ${audioLevel > 0 ? "border-green-500" : "border-gray-500"}`}
            >
                <img
                    src={pfp || "null"}
                    alt=""
                    className="aspect-square h-full rounded-full"
                />
            </motion.div>
        );
    }

    return null;
}
