export enum ChatType {
  FRIEND = "FRIEND",
  CHANNEL = "CHANNEL",
}

// Classe User
export class User {
  id: number;
  username: string;
  password: string;

  constructor(id: number, username: string, password: string) {
    this.id = id;
    this.username = username;
    this.password = password;
  }
}

// Classe Friendship
export class Friendship {
  id: number;
  user1: User;
  user2: User;

  constructor(id: number, user1: User, user2: User) {
    this.id = id;
    this.user1 = user1;
    this.user2 = user2;
  }
}

// Classe PrivateChat
export class PrivateChat{
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

    /**
   * addMessages
   */
  public addMessages(messages: Message[]) {
    messages.forEach((message)=>{
      this.messages.push(message);
    })
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

// Classe Message
export class Message {
  text: string;
  sender: User;
  time: Date;

  constructor(text: string, sender: User, time?: Date) {
    this.text = text;
    this.sender = sender;
    this.time = time ?? new Date();
  }
}


export class PrivateChatResponse {
  chatId: number;
  otherUser: User;

  constructor(privateChat: PrivateChat, requester: User) {
    this.chatId = privateChat.id;

    if (privateChat.friendship.user1.id === requester.id) {
      this.otherUser = privateChat.friendship.user2;
    } else if (privateChat.friendship.user2.id === requester.id) {
      this.otherUser = privateChat.friendship.user1;
    } else {
      throw new Error("Requester non fa parte di questa chat");
    }
  }
}