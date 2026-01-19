export enum ChatType {
  FRIEND = "FRIEND",
  CHANNEL = "CHANNEL",
}

// ================= User =================
export class User {
  id: number;
  username: string;
  password: string;

  constructor(id: number, username: string, password: string) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  static fromJSON(json: any): User {
    return new User(json.id, json.username, json.password);
  }
}

// ================= Message =================
export class Message {
  text: string;
  sender: User;
  time: Date;

  constructor(text: string, sender: User, time?: Date) {
    this.text = text;
    this.sender = sender;
    this.time = time ?? new Date();
  }

  static fromJSON(json: any): Message {
    return new Message(json.text, User.fromJSON(json.sender), new Date(json.time));
  }
}

// ================= Friendship =================
export class Friendship {
  id: number;
  user1: User;
  user2: User;

  constructor(id: number, user1: User, user2: User) {
    this.id = id;
    this.user1 = user1;
    this.user2 = user2;
  }

  static fromJSON(json: any): Friendship {
    return new Friendship(User.fromJSON(json.user1).id ? json.id : 0, User.fromJSON(json.user1), User.fromJSON(json.user2));
  }
}

// ================= PrivateChat =================
export class PrivateChat {
  type: ChatType.FRIEND;
  messages: Message[];
  friendship: Friendship;
  id: number;

  constructor(id: number, friendship: Friendship) {
    this.type = ChatType.FRIEND;
    this.friendship = friendship;
    this.messages = [];
    this.id = id;
  }

  public addMessages(messages: Message[]) {
    this.messages.push(...messages);
  }

  static fromJSON(json: any): PrivateChat {
    const chat = new PrivateChat(json.chatId, Friendship.fromJSON(json.friendship));
    if (json.messages) {
      chat.addMessages(json.messages.map((m: any) => Message.fromJSON(m)));
    }
    return chat;
  }
}

// ================= Channel & TextChannel =================

export enum ChannelType{
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

// ================= PrivateChatResponse =================
export class PrivateChatResponse {
  chatId: number;
  otherUser: User;

  constructor(privateChat: PrivateChat, otherUser: User) {
    this.chatId = privateChat.id;
    this.otherUser = otherUser;
  }


  static fromJSON(json: any): PrivateChatResponse {
    // richiede che json contenga privateChat e requester
    const chat = new PrivateChat(json.chatId, new Friendship(-1, new User(json.otherUser.id, json.otherUser.username,  json.otherUser.password), new User(-1, "", "")))
    const otherUser = User.fromJSON(json.otherUser);
    return new PrivateChatResponse(chat, otherUser);
  }
}
