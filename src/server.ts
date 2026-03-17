
import 'dotenv/config';
import express from "express";
import { createServer } from "http";
import { Server as SocketServer } from "socket.io";
import cors from "cors";
import { AccessToken } from 'livekit-server-sdk';
import axios from "axios";
import jwt, { JwtPayload } from "jsonwebtoken";
import { use } from 'react';

const apiKey = process.env.LIVEKIT_API_KEY;
const apiSecret = process.env.LIVEKIT_API_SECRET;

const app = express();
app.use(cors());
app.use(express.json());


const keyBase64 = "Ink1VTlacUY0djlVLzVkRXFLc2pRWGZTZ1QwUUg4elh3WDdPZm1BNE1acTg9Ig==";
const secret = Buffer.from(keyBase64, "base64");

export function verifyAccessToken(token: string) {
    try {
        const decoded = jwt.verify(token, secret, {
            algorithms: ["HS256"]
        })
        if (typeof decoded === "object" && decoded !== null && "username" in decoded) {
            return { username: (decoded as JwtPayload).username as string };
        }
    } catch (err) {
        console.error("Access token non valido:", err);
        return null;
    }
}

let cookies: any[] | null | undefined = null;
const LOGIN_INTERVAL = (10+1) * 24 * 60 * 60 * 1000; 

function doLogin() {
    axios.post("http://localhost:8080/api/login", {
        username: "ale",
        password: "727"
    }, {
        withCredentials: true,
        headers: { "Content-Type": "application/json" }
    })
    .then((response) => {
        cookies = response.headers["set-cookie"];
        console.log("Login riuscito, cookies salvati:", cookies);
    })
    .catch((error) => {
        console.log("Errore login:", error.response?.data || error.message);
    })
    .finally(() => {
        setTimeout(doLogin, LOGIN_INTERVAL);
    });
}

doLogin();


const httpServer = createServer(app);
const io = new SocketServer(httpServer, {
    cors: { origin: "*" },
});

const getChat = async (id: number) => {
    const res = await axios.post(
        "http://localhost:8080/api/admin/chat?chatId=" + id,
        {},
        {
            withCredentials: true,
            headers: {
                Cookie: cookies?.join("; ")
            }
        }
    )

    const friendship = {
        user_1: res.data.fkUser1.username,
        user_2: res.data.fkUser2.username,
    }
    return friendship;
}

let deviceCounters: Record<string, number> = {};

io.on("connection", (socket) => {

    // Codice di autenticazione
    const token = socket.handshake.auth.token;

    // Verifica il token di accesso
    const user = verifyAccessToken(token);

    // Chiusura connessione se token non valido o scaduto
    if (!user) {
        console.log("Token non valido, chiudo connessione");
        socket.disconnect();
        return;
    }

    // Salva l'essere online di un determinato utente
    if (!deviceCounters[user.username]) deviceCounters[user.username] = 0;
    deviceCounters[user.username] += 1;

    // Salva il numero di dispositivo con cui è online (incrementale)
    const deviceId = deviceCounters[user.username];
    const usernameWithDevice = `${user.username}:${deviceId}`;

    // Logs
    console.log("Utenti online:", deviceCounters);
    console.log("Nuovo utente autenticato:", usernameWithDevice);

    // Room di socket per comunicazione diretta (notifiche)
    socket.join(`user_${user.username}`);

    // Gestione disconnessione
    socket.on("disconnect", () => {
        console.log("Utente disconnesso:", usernameWithDevice);
        console.log("Utenti online:", deviceCounters);

        // Toglie 1 al counter dei dispositivi online
        deviceCounters[user.username] -= 1;
        if (deviceCounters[user.username] <= 0) delete deviceCounters[user.username];
    });

    // Entra in una chat tra utenti privati
    socket.on("join_chat", async (data) => {
        const { chatId } = data;

        const chat = getChat(chatId);
        console.log(user, "è entrato in", await chat, "con chatId =", chatId);

        // Entra in una room di socket con id della chat
        socket.join(`chat_${chatId}`);
    });


    // Entra in una chat tra utenti privati
    socket.on("leave_chat", async (data) => {
        const { chatId } = data;

        const chat = getChat(chatId);
        console.log(user, "lascia la stanza", await chat, "con chatId =", chatId);


        // Lascia la room di socket con id della chat
        socket.leave(`chat_${chatId}`);
    });

    // Entra in una chiamata
    socket.on("join_call", async (data) => {
        const { chatId } = data;
        const roomName = `call_${chatId}`;

        // Recupera le informazioni della chat
        const chat = await getChat(chatId);
        console.log(user, "vuole entrare in", chat, "con chatId =", chatId);

        // controlla se la room esiste
        const room = io.sockets.adapter.rooms.get(roomName);
        const callExists = room && room.size > 0;

        if (callExists) {
            console.log("La chiamata esiste già, utenti presenti:", room.size);
        } else {
            console.log("La chiamata NON esiste, la creo ora");
            let otherUsername;
            let caller;
            if(chat.user_1===user.username){
                caller = chat.user_1;
                otherUsername = chat.user_2
            }else{
                caller = chat.user_2;
                otherUsername = chat.user_1
            }
            io.to("user_"+otherUsername).emit("incomingCall", { caller });
        }

        socket.join(roomName);
    });

    socket.on("leave_call", (data) => {
        const { chatId } = data;

        socket.leave(`call_${chatId}`);
    });

    socket.on("join_server", (data) => {
        const { serverId } = data;
        if (!serverId) return;

        socket.join(`server_${serverId}`);

    });

    socket.on("leave_server", (data) => {
        const { serverId } = data;
        if (!serverId) return;

        socket.leave(`server_${serverId}`);
    });

    socket.on("join_channel", (data) => {
        const { channelId } = data;
        socket.join(`channel_${channelId}`);
    });

    socket.on("leave_channel", (data) => {

        const { channelId } = data;
        socket.leave(`channel_${channelId}`);

    });


    socket.on("sendMessage", (data) => {
        const { id, type, message } = data;
        console.log(data);

        io.to(`${type}_${id}`).emit("newMessage", message);

    })


    socket.on("deleteMessage", (data) => {
        const { id, type, messageId } = data;
        console.log("messaggio da eliminare", data);

        if (type === "channel") {
            io.to(`channel_${id}`).emit("deletedMessage", { messageId });
        } else if (type === "chat") {
            io.to(`chat_${id}`).emit("deletedMessage", { messageId });

        }
    })


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
