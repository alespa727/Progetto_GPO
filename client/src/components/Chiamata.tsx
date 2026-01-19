import { ConnectionState, ParticipantLoop, ParticipantName, ParticipantTile, RoomContext, TrackLoop, useEnsureRoom, useParticipants, useRoomContext, useRoomInfo, useTracks } from '@livekit/components-react';
function Chiamata({isActive}: {isActive: boolean}) {
  if(!isActive) return;
  const room = useRoomContext();
  if(!room) return;
  
  const participants = useParticipants();
   return (
      <>
      <ConnectionState />
      <div className='w-full h-1/2 p-3 bg-black'>
        <div className=' h-full grid grid-cols-1 gap-1  box-content'>
          <ParticipantLoop participants={participants}>
            <div className='bg-white  rounded-[2px] text-black'>
              <ParticipantName></ParticipantName>
              
            </div>
          </ParticipantLoop>
        </div>
      </div>
     </>
  );
}


export default Chiamata