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
  id: number;
  text: string;
  sender: string;
  time: Date;

  constructor(id: number, text: string, sender: string, time?: Date) {
    this.id = id;
    this.text = text;
    this.sender = sender;
    this.time = time ?? new Date();
  }

  static fromJSON(json: any): Message {
    return new Message(json.id, json.text, json.sender, new Date(json.time));
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