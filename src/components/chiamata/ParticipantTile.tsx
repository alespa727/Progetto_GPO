import { endpoint } from "@/types";
import {
    ParticipantName,
    TrackRefContext,
    TrackReference,
    useParticipantContext,
    useRoomContext,
    useTrackRefContext,
    useTracks,
    VideoConference,
    VideoTrack,
} from "@livekit/components-react";
import axios from "axios";
import { Track } from "livekit-client";

import * as motion from "motion/react-client"
import { useEffect, useState } from "react";


export function ParticipantTileCustom({ videoTrack }: { videoTrack?: TrackReference }) {
    const { audioLevel } = useParticipantContext();
    const room = useRoomContext();
    const participant = useParticipantContext();
    const [pfp, setPfp] = useState("");
    const [state, setState] = useState("");
 

    useEffect(() => {
        console.log(participant)
        const downloadPfp = async () => {
            console.log(participant.identity)
            if (!participant.identity || participant.identity === "") return;
            const res = await axios.get(endpoint+"/services/profileImage?username=" + participant.identity, {
                withCredentials: true
            })

            setPfp(res.data.path);
        }
        downloadPfp();
    }, [])

    useEffect(() => {
        setState(room.state);
    }, [room.state])

    if (videoTrack) {
        return (
            <TrackRefContext value={videoTrack} key={participant.identity}>
                <motion.div
                    initial={{ opacity: 0, scale: 0.1 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{
                        type: "spring",
                        stiffness: 500,
                        damping: 30,
                        mass: 0.1
                    }}
                    className={`aspect-video h-50 mb-5 p-1/2 flex items-center backdrop-blur-2xl justify-center m-1 rounded-md border-4
        ${audioLevel > 0 ? "border-green-500" : "border-gray-500"}`}
                >

                    <div className="relative w-full h-full">
                        <VideoTrack trackRef={videoTrack} className="w-full h-full" />
                        <div className="absolute w-full h-full px-4 p-3 top-0 left-0 text-gray-200">
                            <h3 className="text-xl font-medium">
                                {participant.identity}
                            </h3>
                        </div>
                    </div>
                </motion.div>
            </TrackRefContext>

        )
    }
    if (state === "connected") return (
        <motion.div
            initial={{ opacity: 0, scale: 0.1 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{
                type: "spring",
                stiffness: 500,
                damping: 30,
                mass: 0.1
            }}
            className={`aspect-square h-32 p-1/2 flex items-center backdrop-blur-2xl justify-center m-1 rounded-full border-4
        ${audioLevel > 0 ? "border-green-500" : "border-gray-500"}`}
        >
            <img src={pfp ? pfp : "null"} alt="" className="aspect-square h-full rounded-full" />
        </motion.div>
    );
}