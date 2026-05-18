import "socket.io";

type SocketState = {
    currentServerId?: number;
    currentVC?: number;
};

declare module "socket.io" {
  interface Socket {
    user?: {
      username: string;
      description?: string;
      path?: string;
    };
    state: SocketState
  }
}