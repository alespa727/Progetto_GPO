import { createProxyMiddleware } from "http-proxy-middleware";
import express from "express";
import cors from "cors";
import cookieParser from "cookie-parser";
import { createServer } from "http";
import { Socket } from "net";

const app = express();

app.use(cors({
    origin: true,
    credentials: true,
}));


app.use(express.json());
app.use(cookieParser());

const httpServer = createServer(app);

const server1Proxy = createProxyMiddleware({
    target: "http://localhost:4000",
    changeOrigin: true,
    ws: true,
    pathRewrite: { "^/server1": "" },
    on: {
        proxyReq: (proxyReq, req, res) => {
            const body = (req as express.Request).body;
            if (body && Object.keys(body).length > 0) {
                const bodyData = JSON.stringify(body);
                proxyReq.setHeader("Content-Type", "application/json");
                proxyReq.setHeader("Content-Length", Buffer.byteLength(bodyData));

                if (req.headers.cookie) {
                    proxyReq.setHeader("cookie", req.headers.cookie);
                }

                proxyReq.write(bodyData);
                proxyReq.end();
            }
        },

        proxyRes: (proxyRes, req, res) => {
            const setCookieHeader = proxyRes.headers['set-cookie'];
            if (setCookieHeader) {
                const setCookieArray = Array.isArray(setCookieHeader) ? setCookieHeader : [setCookieHeader];

                const rewritten = setCookieArray.map(cookie =>
                    cookie
                        .replace(/Path=[^;]+/i, 'Path=/')   
                        .replace(/Domain=[^;]+;?/i, '')     
                );

                delete proxyRes.headers['set-cookie'];
                res.setHeader('Set-Cookie', rewritten);
            }
        },

        error: (err, req, res) => {
            console.error("Proxy error:", err);
            res.end("Proxy error");
        }
    }
});


app.use("/server1", server1Proxy);

app.use("/server2", createProxyMiddleware({
    target: "http://localhost:8080",
    changeOrigin: true,
    pathRewrite: { "^/server2": "" },
    on: {
        proxyReq: (proxyReq, req, res) => {
            const body = (req as express.Request).body;
            if (body && Object.keys(body).length > 0) {
                const bodyData = JSON.stringify(body);
                proxyReq.setHeader("Content-Type", "application/json");
                proxyReq.setHeader("Content-Length", Buffer.byteLength(bodyData));

                if (req.headers.cookie) {
                    proxyReq.setHeader("cookie", req.headers.cookie);
                }

                proxyReq.write(bodyData);
                proxyReq.end();
            }
        },

        proxyRes: (proxyRes, req, res) => {
            const setCookieHeader = proxyRes.headers['set-cookie'];
            if (setCookieHeader) {
                const setCookieArray = Array.isArray(setCookieHeader) ? setCookieHeader : [setCookieHeader];

                const rewritten = setCookieArray.map(cookie =>
                    cookie
                        .replace(/Path=[^;]+/i, 'Path=/')   
                        .replace(/Domain=[^;]+;?/i, '')     
                );

                delete proxyRes.headers['set-cookie'];
                res.setHeader('Set-Cookie', rewritten);
            }
        },

        error: (err, req, res) => {
            console.error("Proxy error:", err);
            res.end("Proxy error");
        }
    }
}));


const viteProxy = createProxyMiddleware({
    target: "http://localhost:5173",
    changeOrigin: true,
    ws: true
});

app.use("/", viteProxy);


httpServer.on("upgrade", (req, socket, head) => {
    const url = req.url || "";

    if (url.startsWith("/server1")) {
        server1Proxy.upgrade(req, socket as Socket, head);
    } else {
        viteProxy.upgrade(req, socket as Socket, head);
    }
});


httpServer.listen(5000, () => {
    console.log("Server in ascolto sulla porta 5000");
});
