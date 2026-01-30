export const portServer = 5000;
export const base ="https://weightlessly-tres-dagmar.ngrok-free.dev"
export const websocket = base;
export const endpoint =base+"/server2/api";
export const endpoint2 = base+"/server1"

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

export class Attachment{
  id: number;
  attachedPath: string;

  constructor(id: number, attachedPath: string){
    this.id = id;
    this.attachedPath = attachedPath;
  }

  static fromJSON(json: any): Attachment {
   
    let attachment = new Attachment(json.id, json.attachedPath);
    return attachment
  }
}
// ================= Message =================
export class Message {
  messageId: number;
  username: string;
  message: string;
  sentAt: Date;
  sent: boolean;
  attachments: Attachment[]

  constructor(messageId: number, username: string, message: string, attachments?: Attachment[], sentAt?: Date, sent?: boolean) {
    this.messageId = messageId;
    this.username = username;
    this.message = message;
    this.sentAt = sentAt ?? new Date();
    this.sent = sent ? sent : false;
    this.attachments = attachments ? attachments : [];
  }

  static fromJSON(json: any): Message {
    let arr = [];
    if(json.attachments){
      arr = json.attachments.map(
        (item: any) => Attachment.fromJSON(item)
      );
    }
    

    let message = new Message(json.messageId, json.username, json.message, arr, new Date(json.sentAt), true);
    return message
  }
}







// ================= Channel & TextChannel =================

export enum ChannelType {
  VOICE, TEXT
}
export class Channel {
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

  static fromJSON(json: any): Channel {
    if (json.messages) {
      return TextChannel.fromJSON(json);
    }
    return new Channel(json.id, json.type, json.title, json.description);
  }
}

export class TextChannel extends Channel {
  messages: Message[];

  constructor(id: number, title: string, description?: string, createdAt?: Date) {
    super(id, ChannelType.TEXT, title, description, createdAt);
    this.messages = [];
  }

  addMessages(messages: Message[]) {
    this.messages.push(...messages);
  }

  static fromJSON(json: any): TextChannel {
    const ch = new TextChannel(
      json.id,
      json.title,
      json.description,
    );
    if (json.messages) {
      ch.addMessages(json.messages.map((m: any) => Message.fromJSON(m)));
    }
    return ch;
  }
}

// ================= Section =================
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

  static fromJSON(json: any): Section {
    const section = new Section(json.id, json.title);
    if (json.channels) {
      section.channels = json.channels.map((c: any) => Channel.fromJSON(c));
    }
    return section;
  }
}

// ================= Server =================
export class Server {
  id: number;
  name: string;
  description?: string;
  createdAt?: Date;
  sections: Section[];

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

  static fromJSON(json: any): Server {
    return new Server(
      json.id,
      json.name,
      json.sections ? json.sections.map((s: any) => Section.fromJSON(s)) : [],
      json.description,
      json.createdAt ? new Date(json.createdAt) : undefined
    );
  }
}