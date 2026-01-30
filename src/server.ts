import {
    Channel,
    TextChannel,
    Server,
    Section,
    Message,
    ChannelType,
    VoiceChannel,
    endpoint
} from "./types";
 
import 'dotenv/config';
import express from "express";
import { createServer } from "http";
import { Socket, Server as SocketServer } from "socket.io";
import cors from "cors";
import { send } from "process";
import { AccessToken } from 'livekit-server-sdk';
import { randomInt } from "crypto";
import axios from "axios";
// proxy.js
import { createProxyMiddleware } from "http-proxy-middleware";

const apiKey = process.env.LIVEKIT_API_KEY;
const apiSecret = process.env.LIVEKIT_API_SECRET;

const app = express();
app.use(cors());
app.use(express.json());



const httpServer = createServer(app);
const io = new SocketServer(httpServer, {
    cors: { origin: "*" },
});

let map: Map<string, Socket> = new Map();

function getKeyBySocket(targetSocket: Socket): string | null {
    for (const [key, socket] of map.entries()) {
        if (socket === targetSocket) {
            return key; 
        }
    }
    return null; 
}

let i = 0;

io.on("connection", (socket) => {
    console.log(`Utente connesso: ${socket.id}`);
    let key: string;

    socket.on("login", (data) => {
        const { username } = data;
        key = username
    });

    socket.on("disconnecting", () => {
        socket.rooms.forEach((room: string) => {
            if (room !== socket.id) {
                console.log(key + " è uscito da " + room);
            }
        });
    })

    socket.on("disconnect", () => {
        map.delete(key);
        console.log(key + " è andato offline");
    });

    socket.on("join_chat", (data) => {
        const { chatId } = data;

        socket.join(`chat_${chatId}`);
        console.log(key + " è entrato nella chat_" + chatId);
    });

    socket.on("join_call", (data) => {
        const { chatId } = data;

        socket.join(`call_${chatId}`);
        console.log(key + " è entrato nella call_" + chatId);
    });

     socket.on("leave_call", (data) => {
        const { chatId } = data;

        socket.leave(`call_${chatId}`);
        console.log(key + " è uscito dalla call " + chatId);
    });

    socket.on("join_server", (data) => {
        const { serverId } = data;
        const server = servers.find(c => c.id.toString() === serverId.toString());

        if (!server) return;

        socket.join(`server_${serverId}`);
        console.log(key + " è entrato nel server_" + serverId);
        server.sections.forEach((s)=>{
            s.channels.forEach((c)=>{
                if(c.type === ChannelType.VOICE){
                    emitVoiceUsersUpdate(c.id);
                }
            })
        })
        
    });

    socket.on("leave_server", (data) => {
        const { serverId } = data;
        const server = servers.find(c => c.id.toString() === serverId.toString());

        if (!server) return;

        socket.leave(`server_${serverId}`);
    });

    socket.on("join_channel", ({ channelId }) => {
        socket.join(`channel_${channelId}`);
        console.log(typeof(channelId), channelId)
        emitVoiceUsersUpdate(parseInt(channelId));
    });

     socket.on("leave_channel", ({ channelId }) => {
        socket.leave(`channel_${channelId}`);
        emitVoiceUsersUpdate(parseInt(channelId));
    });

    socket.on("leave_chat", (data) => {
        const { chatId } = data;

        socket.leave(`chat_${chatId}`);
        console.log(key + " è uscito dalla chat " + chatId);
    });

    socket.on("sendMessage", (data) => {
        const { id, type, message } = data;
        console.log(data);
       
        if (type === "channel") {
            io.to(`channel_${id}`).emit("newMessage", message);
        } else if (type === "chat") {
            io.to(`chat_${id}`).emit("newMessage", message);
        }
       
    })


    socket.on("deleteMessage", (data) => {
        const { id, type, messageId } = data;
        console.log("messaggio da eliminare", data);

        if (type === "channel") {
            io.to(`channel_${id}`).emit("deletedMessage", {messageId});
        }else if(type === "chat"){
            io.to(`chat_${id}`).emit("deletedMessage", {messageId});
            
        }
    })


});

function getKeyBySocketId(socketId: string): string | null {
    for (const [key, socket] of map.entries()) {
        if (socket.id === socketId) {
            return key;
        }
    }
    return null;
}

// ================= Servers =================
let servers: Server[] = [
    new Server(1, "Server 1", [], "Il primo server di prova"),
    new Server(2, "Server 2", [], "Il secondo server di prova")
];


app.get("/servers", (req, res) => {
    res.json(servers);
});

app.post('/token', async (req, res) => {
    const { identity, roomName } = req.body;

    const name = identity;

    const at = new AccessToken(apiKey, apiSecret, { identity: name });
    at.addGrant({
        roomJoin: true,
        room: roomName,
    });

    const token = await at.toJwt();
    console.log('Generated token for', name, 'room:', roomName);
    console.log(token);
    res.json({ token });
});

httpServer.listen(4000, () => {
    console.log(`Server in ascolto sulla porta ${4000}`);
});

function emitVoiceUsersUpdate(channelId: number) {
  const room = io.sockets.adapter.rooms.get(`channel_${channelId}`);
  const users: string[] = [];

  if (room) {
    for (const socketId of room) {
      const userId = getKeyBySocketId(socketId);
      if (userId) users.push(userId);
    }
  }
  const serverId = getServerIdByChannel(channelId);
  console.log(serverId)
  console.log("Utenti nel server partendo dal ", channelId, io.sockets.adapter.rooms.get(`server_${serverId}`));

  io.to(`server_${serverId}`).emit("voice_users_update", {
    channelId,
    users
  });
  io.to(`server_${serverId}`).emit("message", {
    channelId,
    users
  });
}

function getServerIdByChannel(channelId: number): number | undefined {
  for (const server of servers) {
    for (const section of server.sections) {
      for (const channel of section.channels) {
        // handle both TextChannel and VoiceChannel
        if ("id" in channel && channel.id === channelId) {
          console.log("Trovato serverId:", server.id, "per channelId:", channelId);
          return server.id;
        }
      }
    }
  }
  console.log("ChannelId non trovato:", channelId);
  return undefined;
}

console.log("Test",getServerIdByChannel(2))