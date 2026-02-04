import { useState, useRef } from "react";
import { LocalVideoTrack, Room } from "livekit-client";


export function useScreenShare(room: Room | null) {
  const [isScreenSharing, setIsScreenSharing] = useState(false);
  const screenTrackRef = useRef<LocalVideoTrack | null>(null);
  
  const toggleScreenShare = async (enable: boolean) => {
    if (!room) return;

    if (!enable) {
      if (screenTrackRef.current) {
        room.localParticipant.unpublishTrack(screenTrackRef.current);
        screenTrackRef.current.stop();
        screenTrackRef.current = null;
      }
      setIsScreenSharing(false);
      return;
    }

    if (screenTrackRef.current) return;

    try {
      let stream: MediaStream;
      const isElectron = !!window.electronAPI?.getSources;

      if (isElectron) {
        const sources = await window.electronAPI.getSources?.({ types: ["screen", "window"] });
        if (!sources || sources.length === 0) throw new Error("No screen sources found");

        const source = sources[0]; // pick first, or implement a picker

        stream = await (navigator.mediaDevices as any).getUserMedia({
          audio: false,
          video: {
            chromeMediaSource: "desktop",
            chromeMediaSourceId: source.id,
            width: { max: 1920 },
            height: { max: 1080 },
            frameRate: { max: 30 },
          } as any,
        });
      } else {
        stream = await navigator.mediaDevices.getDisplayMedia({ video: true, audio: false });
      }

      const videoTrack = stream.getVideoTracks()[0];

      videoTrack.onended = () => {
        if (screenTrackRef.current) {
          room.localParticipant.unpublishTrack(screenTrackRef.current);
          screenTrackRef.current = null;
          setIsScreenSharing(false);
        }
      };

      screenTrackRef.current = new LocalVideoTrack(videoTrack);
      await room.localParticipant.publishTrack(screenTrackRef.current);

      setIsScreenSharing(true);
    } catch (err) {
      console.error("Could not start screen share:", err);
      setIsScreenSharing(false);
    }
  };

  return { isScreenSharing, toggleScreenShare, setIsScreenSharing };
}
