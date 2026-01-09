import axios from 'axios';
import {
  LocalParticipant,
  LocalTrackPublication,
  Participant,
  RemoteParticipant,
  RemoteTrack,
  RemoteTrackPublication,
  Room,
  RoomEvent,
  Track,
  VideoPresets,
} from 'livekit-client';
import { useEffect, useState } from 'react';
import { useUser } from '../context/UserProvider';
import { useActiveChatContext } from '../context/ActiveChatProvider';

function Chiamata({isActive}:{isActive: boolean}) {
  const user = useUser();
  const chat = useActiveChatContext().activeChat;
  const url = "ws://localhost:7880";
  const room = new Room();
  const [token, setToken] = useState("");

  useEffect(()=>{
    if(!user || !chat) return;
    const fetchToken = async () => {
      try {
        let identity: string = user.username;
        let roomName: string = "chat_"+chat.chatId;
        const res = await axios.post("http://localhost:4000/token",{
          identity, roomName
        });
        
        setToken(res.data.token);
      } catch (err) {
        console.error("Errore nel fetch utente:", err);
      }
    };
    fetchToken();

  }, [user, chat]);

  useEffect(()=>{
    const connectRoom = async ()=>{
      if(token.length === 0) return;
      await room.prepareConnection(url, token);
      await room.connect(url, token);
      console.log('connected to room', room.name);
      const p = room.localParticipant;
      console.log(p);
      await p.setMicrophoneEnabled(true);

       room.on(RoomEvent.ActiveSpeakersChanged, (speakers: Participant[]) => {
          console.log("Active speakers:", speakers.map(p => p.identity));
          speakers.forEach(p => console.log(`${p.identity} level:`, p.audioLevel));
        });
    }
    connectRoom();

    return () => { 
      console.log('disconnection to room', room.name);
      room.disconnect();
     };
  }, [token]);

   return (
   <div className="w-full h-1/2 bg-red-500 absolute top-12.25"></div>
  );
}


export default Chiamata