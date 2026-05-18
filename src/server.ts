import 'dotenv/config';
import express from "express";
import { createServer } from "http";
import { Server as SocketServer } from "socket.io";
import cors from "cors";
import { AccessToken } from 'livekit-server-sdk';
import axios from "axios";
import jwt, { JwtPayload } from "jsonwebtoken";

type SocketState = {
    currentServerId?: number;
    currentVC?: number;
}; 

const app = express();
app.use(cors());
app.use(express.json());

const httpServer = createServer(app);
const io = new SocketServer(httpServer, {
    cors: { origin: "*" },
});

let cookies: any[] | null | undefined = null;
let deviceCounters: Record<string, number> = {};

const apiKey = process.env.LIVEKIT_API_KEY;
const apiSecret = process.env.LIVEKIT_API_SECRET;

/* ===================== UTILS ===================== */


interface Notification {
  label: string;
  from: {
      username: string;
      description?: string;
      path?: string;
    };
  text: string;
}
 

function sendNotification(room: string, notification: Notification){
    console.log("notifica da community", room, "da", notification)
    io.to(room).emit("notification", notification)
}

function getUsersInVoiceChannel(channelId: number) {
    const roomName = `vc_${channelId}`;
    const room = io.sockets.adapter.rooms.get(roomName);

    if (!room) return [];

    const users: any[] = [];

    for (const socketId of room) {
        const socket = io.sockets.sockets.get(socketId);

        if (socket?.user) {
            users.push({
                socketId,
                username: socket.user.username,
                description: socket.user.description,
                path: socket.user.path
            });
        }
    }

    return users;
}

const broadcastVoiceState = async (serverId: number) => {
    const server = await getServer(serverId);

    if (!server || !server.sections) {
        console.log("[voice_state] server invalido:", serverId);
        return;
    }

    const voiceState: Record<number, any[]> = {};

    for (const section of server.sections) {
        for (const channel of section.channels ?? []) {

            if (channel.type !== "vocale") continue;

            voiceState[channel.id] = getUsersInVoiceChannel(channel.id);
        }
    }

    console.log("[voice_state] broadcast server:", serverId, voiceState);

    io.to(`server_${serverId}`).emit(
        "voice_state_update",
        voiceState
    );
};

/* ===================== AUTH ===================== */

export async function verifyAccessToken(token: string) {
    try {
        const response = await fetch("http://localhost:8080/api/services/profile", {
            headers: {
                Cookie: `AccessCookie=${token}`
            }
        });

        if (!response.ok) return null;

        const data = await response.json();

        return {
            username: data.username,
            description: data.description,
            path: data.path
        };
    } catch (err) {
        return null;
    }
}

/* ===================== LOGIN ===================== */

async function doLoginWithRetry(attempt = 0) {
    try {
        const response = await axios.post("http://localhost:8080/api/login", {
            username: "ale727",
            password: "727"
        }, {
            withCredentials: true,
            headers: { "Content-Type": "application/json" }
        });

        cookies = response.headers["set-cookie"];
    } catch {
        const delay = Math.min(1000 * 2 ** attempt, 60000);
        setTimeout(() => doLoginWithRetry(attempt + 1), delay);
    }
}

doLoginWithRetry();

/* ===================== API ===================== */

const getChat = async (id: number, retry = true) => {
    try {
        const res = await axios.post(
            "http://localhost:8080/api/admin/chat?chatId=" + id,
            {},
            { headers: { Cookie: cookies?.join("; ") } }
        );

        return {
            user_1: res.data.fkUser1.username,
            user_2: res.data.fkUser2.username,
        };
    } catch {
        await doLoginWithRetry();
    }

    if (retry) return getChat(id, false);
    return null;
};

const getServer = async (id: number, retry = true) => {
    try {
        const res = await axios.post(
            "http://localhost:8080/api/admin/server?serverId=" + id,
            {},
            { headers: { Cookie: cookies?.join("; ") } }
        );

        return res.data;
    } catch {
        await doLoginWithRetry();
    }

    if (retry) return getChat(id, false);
    return null;
};

/* ===================== CONNECTION ===================== */

io.on("connection", async (socket) => {

    const token = socket.handshake.auth.token;
    const user = await verifyAccessToken(token);

    if (!user) {
        socket.disconnect();
        return;
    }


    socket.state = {};
    socket.user = user;

    if (!deviceCounters[user.username]) deviceCounters[user.username] = 0;
    deviceCounters[user.username]++;

    socket.join(`user_${user.username}`);

    /* ===================== DISCONNECT ===================== */
    socket.on("disconnect", () => {
        deviceCounters[user.username]--;
        if (deviceCounters[user.username] <= 0) {
            delete deviceCounters[user.username];
        }

        const serverId = socket.state.currentServerId;
        const vcId = socket.state.currentVC;

        if (serverId && vcId) {
            console.log("Rimozione automatica VC:", vcId);

            broadcastVoiceState(serverId)
        }

    });

    /* ===================== CHAT ===================== */
    socket.on("join_chat", async ({ chatId }) => {
        const chat = await getChat(chatId);

        if (chat?.user_1 === user.username || chat?.user_2 === user.username) {
            socket.join(`chat_${chatId}`);
        }
    });

    socket.on("leave_chat", async ({ chatId }) => {
        const chat = await getChat(chatId);

        if (chat?.user_1 === user.username || chat?.user_2 === user.username) {
            socket.leave(`chat_${chatId}`);
        }
    });

    /* ===================== CALL ===================== */
    socket.on("join_call", async ({ chatId }) => {
        socket.join(`call_${chatId}`);
    });

    socket.on("leave_call", ({ chatId }) => {
        socket.leave(`call_${chatId}`);
    });

    socket.on("get_token_call", async ({ identity, roomName }) => {
        const at = new AccessToken(apiKey, apiSecret, { identity });
        at.addGrant({ roomJoin: true, room: roomName });

        const token = await at.toJwt();

        io.to(`user_${user.username}`).emit("tokenCall", { token });
    });

    /* ===================== SERVER ===================== */
    socket.on("join_server", async ({ serverId }) => {
        if (!serverId) return;

        socket.join(`server_${serverId}`);
        socket.state.currentServerId = serverId;

        broadcastVoiceState(serverId)
    });

    socket.on("leave_server", ({ serverId }) => {
        socket.leave(`server_${serverId}`);
    });

    /* ===================== CHANNEL ===================== */
    socket.on("join_channel", ({ channelId }) => {
        socket.join(`channel_${channelId}`);
    });

    socket.on("leave_channel", ({ channelId }) => {
        socket.leave(`channel_${channelId}`);
    });

    socket.on("join_vc", async ({ serverId, channelId }) => {
        socket.join(`vc_${channelId}`);
        let identity = socket.user?.username;
        if (!identity) return;

        socket.state.currentVC = channelId;


        broadcastVoiceState(serverId)
       

        const at = new AccessToken(apiKey, apiSecret, { identity });
        at.addGrant({ roomJoin: true, room: "vc_" + channelId });

        const token = await at.toJwt();

        io.to(`user_${user.username}`).emit("tokenVC", { token });
    });

    socket.on("leave_vc", async ({ serverId, channelId }) => {
        socket.leave(`vc_${channelId}`);

        broadcastVoiceState(serverId)
       
        socket.state.currentVC = undefined;
    });

    /* ===================== MESSAGES ===================== */
    socket.on("sendMessage", ({ id, type, message }) => {
        sendNotification(`${type}_${id}`, {
            label: "Messaggio su",
            from: message.username,
            text: message.message
        })
        io.to(`${type}_${id}`).emit("newMessage", message);
    });

    socket.on("deleteMessage", ({ id, type, messageId }) => {
        io.to(`${type}_${id}`).emit("deletedMessage", { messageId });
    });

    socket.on("deletedMessageChat", ({ chatId, messageId }) => {
        io.to(`chat_${chatId}`).emit("deletedMessage", { messageId });
    });

    socket.on("modifiedMessageChat", ({ chatId, messageId, message }) => {
        io.to(`chat_${chatId}`).emit("modifiedMessage", {
            messageId,
            text: message
        });
    });

});


/* ===================== START ===================== */

httpServer.listen(4000, () => {
    console.log(`Server in ascolto sulla porta 4000`);
});