import {
    User,
    Friendship,
    PrivateChat,
    Channel,
    TextChannel,
    Server,
    Section,
    Message,
    PrivateChatResponse,
    ChannelType,
    VoiceChannel
} from "./types";

import 'dotenv/config';
import express from "express";
import { createServer } from "http";
import { Socket, Server as SocketServer } from "socket.io";
import cors from "cors";
import { send } from "process";
import { AccessToken } from 'livekit-server-sdk';

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

io.on("connection", (socket) => {
    console.log(`Utente connesso: ${socket.id}`);
    let currentUser: null | User = null;
    let key: string;

    socket.on("login", (data) => {
        const { username, password } = data;

        const sender = Array.from(users.values()).find(u => u.username === username);

        if (sender !== undefined) {
            currentUser = sender;
        }


        if (sender?.password === password) {

            let i = 0;
            while (map.get(username + "-" + i)) {
                console.log(username + "-" + i + " già presente");
                i++;
            }
            key = username.concat("-" + i);
            socket.emit("message", { message: "ok" });
            map.set(key, socket);
            console.log(key + " connesso!");
        } else {
            console.log(username + " ha inserito la password errata!");
            socket.emit("message", { message: "wrong password" });
        }
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
        const chat = privateChats.find(c => c.id === chatId);

        if (!chat) return;

        socket.join(`chat_${chatId}`);
        console.log(key + " è entrato nella chat_" + chatId);
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
        const chat = privateChats.find(c => c.id === chatId);

        if (!chat) return;

        socket.leave(`chat_${chatId}`);
        console.log(key + " è uscito dalla chat " + chatId);
    });

  

    socket.on("sendMessage", (data) => {
        const { id, type, message } = data;
        console.log(data);
        if (type === "channel") {
            const channel = channels.find(c => c.id === id);
            if (!channel) return;
            if (channel instanceof TextChannel) {
                channel.addMessages([message]);
                io.to(`channel_${id}`).emit("newMessage", message);
            }
        } else if (type === "chat") {
            const chat = privateChats.find(c => c.id === id);
            if (!chat) return;
            chat.addMessages([message]);
            io.to(`chat_${id}`).emit("newMessage", message);
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

let users: Map<string, User> = new Map([
    ["ale", new User(1, "ale", "password1")],
    ["marialuisa della madonna mannara", new User(2, "marialuisa della madonna mannara", "password2")],
    ["luigi", new User(3, "luigi", "password3")],
    ["giulia", new User(4, "giulia", "password4")],
]);

// Estrai gli utenti dalla Map
const ale = users.get("ale")!;
const mario = users.get("marialuisa della madonna mannara")!;
const luigi = users.get("luigi")!;
const giulia = users.get("giulia")!;

// Ora creiamo le friendship
let friendships: Friendship[] = [
    new Friendship(1, ale, mario),
    new Friendship(2, ale, luigi),
    new Friendship(3, ale, giulia),
];

// ================= PRIVATE CHATS =================
let privateChats: PrivateChat[] = [
    new PrivateChat(1, friendships[0]), // Ale ↔ Mario
    new PrivateChat(2, friendships[1]), // Ale ↔ Luigi
    new PrivateChat(3, friendships[2]), // Ale ↔ Giulia
];

// Chat Ale ↔ Mario
privateChats[0].addMessages([
    new Message("[Ale] messaggio 1", ale, new Date()),
    new Message("[marialuisa della madonna mannara] messaggio 2", mario, new Date()),
    new Message("[Ale] messaggio 3", ale, new Date()),
    new Message("[marialuisa della madonna mannara] messaggio 4", mario, new Date()),
    new Message("[Ale] messaggio 5", ale, new Date()),
    new Message("[marialuisa della madonna mannara] messaggio 6", mario, new Date()),
]);

// Chat Ale ↔ Luigi
privateChats[1].addMessages([
    new Message("[Ale] messaggio 1", ale, new Date()),
    new Message("[Luigi] messaggio 2", luigi, new Date()),
    new Message("[Ale] messaggio 3", ale, new Date()),
    new Message("[Luigi] messaggio 4", luigi, new Date()),
    new Message("[Ale] messaggio 5", ale, new Date()),
    new Message("[Luigi] messaggio 6", luigi, new Date()),
]);

// Chat Ale ↔ Giulia
privateChats[2].addMessages([
    new Message("[Ale] messaggio 1", ale, new Date()),
    new Message("[Giulia] messaggio 2", giulia, new Date()),
    new Message("[Ale] messaggio 3", ale, new Date()),
    new Message("[Giulia] messaggio 4", giulia, new Date()),
    new Message("[Ale] messaggio 5", ale, new Date()),
    new Message("[Giulia] messaggio 6", giulia, new Date()),
]);

// ================= Canali =================
const generalChannel = new TextChannel(1, "general", "Canale generale");
const randomChannel = new VoiceChannel(2, "random", "Canale chiacchiere");
const memesChannel = new TextChannel(3, "memes", "Condividi meme e gif");
const techChannel = new TextChannel(4, "tech-talk", "Discussioni tecnologiche");
const musicChannel = new VoiceChannel(5, "music", "Condividi musica");
const gamesChannel = new TextChannel(6, "games", "Parliamo di videogiochi");

// ================= Messaggi placeholder =================
generalChannel.addMessages([
    new Message("Messaggio 1 da Ale", new User(1, "ale", "")),
    new Message("Messaggio 2 da Mario", new User(2, "mario", "")),
    new Message("Messaggio 3 da Luigi", new User(3, "luigi", "")),
]);
memesChannel.addMessages([
    new Message("Messaggio 1 da Mario", new User(2, "mario", "")),
    new Message("Messaggio 2 da Luigi", new User(3, "luigi", "")),
    new Message("Messaggio 3 da Giulia", new User(4, "giulia", "")),
]);

techChannel.addMessages([
    new Message("Messaggio 1 da Ale", new User(1, "ale", "")),
    new Message("Messaggio 2 da Mario", new User(2, "mario", "")),
]);

gamesChannel.addMessages([
    new Message("Messaggio 1 da Luigi", new User(3, "luigi", "")),
    new Message("Messaggio 2 da Mario", new User(2, "mario", "")),
    new Message("Messaggio 3 da Ale", new User(1, "ale", "")),
]);

const channels = [gamesChannel, musicChannel, techChannel, memesChannel, randomChannel, generalChannel];

// ================= Sezioni =================
const mainSection = new Section(1, "Principale", [generalChannel, randomChannel]);
const funSection = new Section(2, "Divertimento", [memesChannel, musicChannel]);
const techSection = new Section(3, "Tecnologia", [techChannel, gamesChannel]);

// ================= Servers =================
let servers: Server[] = [
    new Server(1, "Server 1", [mainSection, funSection], "Il primo server di prova"),
    new Server(2, "Server 2", [techSection], "Il secondo server di prova")
];


app.get("/servers", (req, res) => {
    res.json(servers);
});

app.get("/whoami", (req, res) => {
    res.json(users.get("ale"));
});

app.get("/friends", (req, res) => {
    console.log("GET /friends chiamato!");
    let yourFriends: User[] = [];
    friendships.forEach(element => {
        if (element.user1.username === "ale") {
            yourFriends.push(element.user2);
        } else if (element.user2.username === "ale") {
            yourFriends.push(element.user1);
        }
    });
    res.json(yourFriends);
})


app.get("/chats", (req, res) => {
    console.log("GET /chats chiamato!");
    let yourChats: PrivateChatResponse[] = [];
    privateChats.forEach(element => {
        if (element.friendship.user1.username === "ale") {
            yourChats.push(new PrivateChatResponse(element, element.friendship.user1));
        } else if (element.friendship.user2.username === "ale") {
            yourChats.push(new PrivateChatResponse(element, element.friendship.user2));
        }
    });
    res.json(yourChats);
});

app.get("/chat/:id/messages", (req, res) => {

    let yourMessages: Message[] = [];
    privateChats.forEach(element => {
        if (parseInt(req.params.id) == element.id) {
            yourMessages = element.messages;
        }
    });
    res.json(yourMessages);
});


app.get("/channel/:id/messages", (req, res) => {

    let yourMessages: Message[] = [];
    channels.map((c) => {
        if (c.id === parseInt(req.params.id) && c instanceof TextChannel) yourMessages = c.messages;
    })

    res.json(yourMessages);

});

let i = 0;
app.post('/token', async (req, res) => {
    const { identity, roomName } = req.body;

    const name = identity + i;
    i++;

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