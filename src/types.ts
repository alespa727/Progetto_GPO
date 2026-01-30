export const portServer = 8080;
export const endpoint = `http://localhost:${portServer}/api`


export enum ChatType {
  FRIEND = "FRIEND",
  CHANNEL = "CHANNEL",
}

export class Account {
  createdAt: Date;
  isAdmin: boolean;
  path: string;
  username: string;

  constructor(
    createdAt: Date,
    isAdmin: boolean,
    path: string,
    username: string
  ) {
    this.createdAt = createdAt;
    this.isAdmin = isAdmin;
    this.path = path;
    this.username = username;
  }

  static fromJSON(json: any): Account {
    return new Account(json.createdAt, json.isAdmin, json.path, json.username);
  }
}

export class Friend {
  imagePath: string;
  username: string;

  constructor(
    path: string,
    username: string
  ) {
    this.imagePath = path;
    this.username = username;
  }

  static fromJSON(json: any): Friend {
    return new Friend(json.imagePath, json.username);
  }
}

export class Chat {
  id: number;
  friend: Friend

  constructor(id: number, friend: Friend) {
    this.id = id;
    this.friend = friend;
  }

  static fromJSON(json: any): Chat {
    return new Chat(json.id, json.friend);
  }
}

// ================= Message =================
export class Message {
  messageId: number;
  username: string;
  message: string;
  sentAt: Date;

  constructor(messageId: number, username: string, message: string, sentAt?: Date) {
    this.messageId = messageId;
    this.username = username;
    this.message = message;
    this.sentAt = sentAt ?? new Date();
   
  }

  static fromJSON(json: any): Message {
    let message = new Message(json.messageId, json.username, json.message, new Date(json.sentAt));
     console.log(json, message)
    return message
  }
}

// Classe Channel
export class Channel{
  id: number;
  type: ChannelType;
  title: string;
  description?: string;
  createdAt?: Date;

  constructor(id: number, type: ChannelType, title: string, description?: string, createdAt?: Date) {
    this.id = id;
    this.type = type;
    this.title = title;
    this.description = description;
    this.createdAt = createdAt;
  }
}


export enum ChannelType{
  VOICE, TEXT
}

// Classe Channel
export class TextChannel extends Channel {
   messages: Message[];

  constructor(id: number, title: string, description?: string, createdAt?: Date) {
    super(id, ChannelType.TEXT, title, description, createdAt);
    this.messages = [];
  }

  addMessages(messages: Message[]) {
    this.messages.push(...messages);
  }
  removeMessage(id: number) {
    const index = this.messages.findIndex(m => m.messageId === id);
    if (index !== -1) {
      this.messages.splice(index, 1);
    }
  }

}

export class VoiceChannel extends Channel {

  constructor(id: number, title: string, description?: string, createdAt?: Date) {
    super(id, ChannelType.VOICE, title, description, createdAt);
  }

}


// Sezione dei canali
export class Section {
  id: number;
  title: string;
  channels: Channel[];

  constructor(id: number, title: string, channels: Channel[] = []) {
    this.id = id;
    this.title = title;
    this.channels = channels;
  }

  addChannel(channel: Channel) {
    this.channels.push(channel);
  }
}

// Aggiorniamo Server
export class Server {
  id: number;
  name: string;
  description?: string;
  createdAt?: Date;
  sections: Section[] = []; // array di sezioni

  constructor(
    id: number,
    name: string,
    sections: Section[] = [],
    description?: string,
    createdAt?: Date
  ) {
    this.id = id;
    this.name = name;
    this.sections = sections;
    this.description = description;
    this.createdAt = createdAt;
  }

  addSection(section: Section) {
    this.sections.push(section);
  }
}
