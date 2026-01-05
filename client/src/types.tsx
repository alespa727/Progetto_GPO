
export enum chat_type {
  FRIEND = "FRIEND",
  CHANNEL = "CHANNEL"
}

export type chat = {
  type: chat_type,
  name: string,
  id: number
}

export type channel = chat & {
  type: chat_type.CHANNEL; 
  description?: string; 
  createdAt?: Date;
};

export type server = {
    id: number;
    name: string;
    description?: string; 
    default_channel: channel;
    createdAt?: Date;
}