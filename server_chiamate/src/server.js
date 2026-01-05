
import { AccessToken } from "livekit-server-sdk";

const apiKey = 'devkey';
const apiSecret = 'secret';
const identity = 'alice';
const roomName = 'testroom';

const at = new AccessToken(apiKey, apiSecret, {
  identity,
  room: roomName,
});

const token = await at.toJwt();
console.log('JWT token:', token);
