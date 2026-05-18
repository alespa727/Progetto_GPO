import axios, { AxiosResponse } from "axios";

export const portServer = 5000;

export const endpoint = "/server1";
export const websocket = endpoint;
export const endpoint2 = "/api"
export const services = endpoint2 + "/services"

export enum ChatType {
  FRIEND = "FRIEND",
  CHANNEL = "CHANNEL",
}


const friendsUrl = services + "/friends";
const chatsUrl = services + "/chats";
const communitiesUrl = services + "/communities";

export class ClientHttp {


  /**
    * @return i server
    */
  static async getProfile(): Promise<Account> {
    const res = await axios.get<Account>(services + "/profile",
      {
        withCredentials: true
      });


    return res.data;
  }

  static async createSection(serverId: number, name: string) {
    const res = await axios.post<any>(communitiesUrl + "/" + serverId + "/sections",
      {
        name
      }
      , {
        withCredentials: true
      });
    console.log(res.data)
    return res.data;
  }
  static async deleteSection(serverId: number, sectionId: number) {
    const res = await axios.delete<any>(communitiesUrl + "/" + serverId + "/sections/" + sectionId,
      {
        withCredentials: true
      });
    console.log(res.data)
    return res.data;
  }

  static async deleteChannel(serverId: number, sectionId: number, channelId: number) {
    const res = await axios.delete<any>(communitiesUrl + "/" + serverId + "/sections/" + sectionId + "/channels/" + channelId,
      {
        withCredentials: true
      });
    console.log(res.data)
    return res.data;
  }

  static async createChannel(serverId: number, sectionId: number, name: string, description: string, type: string) {
    const res = await axios.post<any>(communitiesUrl + "/" + serverId + "/sections/" + sectionId + "/channels",
      {
        name,
        description,
        type
      }
      , {
        withCredentials: true
      });
    console.log(res.data)
    return res.data;
  }

  static async updateChannel(serverId: number, sectionId: number, channelId: number, name: string, description: string, type: string) {
    const res = await axios.patch<any>(communitiesUrl + "/" + serverId + "/sections/" + sectionId + "/channels/" + channelId,
      {
        name,
        description,
        type
      }
      , {
        withCredentials: true
      });
    console.log(res.data)
    return res.data;
  }


  static async regenerateInviteCode(serverId: number): Promise<string> {
    const res = await axios.post<any>(communitiesUrl + "/" + serverId + "/inviteCode",
      {
        withCredentials: true
      });


    return res.data.code;
  }
  /**
     * @return i server
     */
  static async getMessages(chatId: number): Promise<Message[]> {
    const res = await axios.get<Message[]>(chatsUrl + "/" + chatId + "/messages",

      {
        withCredentials: true
      });

    return res.data;
  }

  static async joinByInviteCode(inviteCode: string): Promise<boolean> {
    try {
      const res = await axios.post<Message[]>(communitiesUrl + "/subscribe",
        {
          inviteCode
        },


        {
          withCredentials: true
        });
      console.log(res);
    } catch (error) {
      return false;
    }


    return true;
  }

  /**
     * @return i server
     */
  static async deleteMessage(chatId: number, messageId: number): Promise<boolean> {
    try {
      await axios.delete<DefaultResponse>(chatsUrl + "/" + chatId + "/messages/" + messageId,
        {
          withCredentials: true
        });

      return true;
    } catch (error) {

      return false;
    }


  }

  static async deleteMessageCommunity(commId: number, sectionId: number, channelId: number, messageId: number): Promise<boolean> {
    try {
      await axios.delete<DefaultResponse>(communitiesUrl + "/" + commId + "/sections/" + sectionId + "/channels/" + channelId + "/messages/" + messageId,
        {
          withCredentials: true
        });

      return true;
    } catch (error) {
      console.log(error)
      return false;
    }


  }

  static async modifyMessage(chatId: number, messageId: number, message: string): Promise<boolean> {
    try {
      await axios.patch<Message[]>(
        chatsUrl + "/" + chatId + "/messages/" + messageId,
        { message },
        { withCredentials: true }
      );
      return true;
    } catch (error) {
      return false;
    }
  }


  /**
     * @return i server
     */
  static async postCommunities(name: string, description: string): Promise<DefaultResponse> {
    const res = await axios.post<DefaultResponse>(communitiesUrl,
      {
        "isInviteCodeValid": true,
        "name": name,
        "description": description
      },
      {
        withCredentials: true
      });

    return res.data;
  }

  static async updateCommunity(id: number, name: string, description: string): Promise<DefaultResponse> {
    const res = await axios.patch<DefaultResponse>(communitiesUrl + "/" + id,
      {
        "isInviteCodeValid": true,
        "name": name,
        "description": description
      },
      {
        withCredentials: true
      });

    return res.data;
  }


  /**
   * @return i server
   */
  static async getCommunities(): Promise<Server[]> {
    const res = await axios.get<Server[]>(communitiesUrl,
      {
        withCredentials: true
      });

    return res.data;
  }

  /**
   * @returns gli account dei tuoi amici
   */
  static async fetchChats(fromChatId?: number): Promise<Chat[]> {
    const res = await axios.get<Chat[]>(chatsUrl,
      {
        withCredentials: true
      });
    console.log(res.data)
    return res.data;
  }


  /**
   * @param username username di chi vuoi aggiungere
   * @returns risposta http
   */
  static async addFriend(username: string): Promise<AxiosResponse<DefaultResponse>> {
    const res = await axios.post<DefaultResponse>(friendsUrl,
      {
        username,
      },
      {
        withCredentials: true
      });

    return res;
  }

  /**
   * @returns gli account dei tuoi amici
   */
  static async fetchFriends(): Promise<Account[]> {
    const res = await axios.get<any>(friendsUrl,
      {
        withCredentials: true
      });

    console.log(res.data.friends)
    return res.data.friends;
  }

  static async patchProfile({ username, description }: { username: string, description: string }): Promise<boolean> {
    await axios.patch(services + "/profile",
      {
        "username": username,
        "description": description
      },
      {
        withCredentials: true
      });

    return true;
  }

  static async patchPfp(file: File): Promise<boolean> {
    const formData = new FormData();
    formData.append("image", file);

    await axios.patch(services + "/profile/pfp", formData, {
      withCredentials: true,
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });
    return true;
  }

  /**
  * @returns gli account dei tuoi amici
  */
  static async isUsernameFree({ username }: { username: string }): Promise<boolean> {
    const res = await axios.post(services + "/isUsernameFree?username=" + username,
      {
      },
      {
        withCredentials: true
      });


    return res.data;
  }

}

export class DefaultResponse {
  message: string;

  constructor(
    message: string
  ) {
    this.message = message;
  }

}


export class Account {
  authorization?: string;
  createdAt: Date;
  isAdmin: boolean;
  path: string;
  description: string;
  username: string;

  constructor(
    createdAt: Date,
    isAdmin: boolean,
    path: string,
    description: string,
    username: string
  ) {
    this.createdAt = createdAt;
    this.isAdmin = isAdmin;
    this.description = description;
    this.path = path;
    this.username = username;
  }

  static fromJSON(json: any): Account | null {
    if (!json) return null;
    return new Account(json.createdAt, json.isAdmin, json.path, json.description, json.username);
  }
}

export class Friend {
  path: string;
  description: string;
  username: string;

  constructor(
    path: string,
    description: string,
    username: string
  ) {
    this.path = path;
    this.description = description;
    this.username = username;
  }

  static fromJSON(json: any): Friend {
    return new Friend(json.property, json.description, json.username);
  }
}

export class Chat {
  id: number;
  lastMessage: Date;
  friend: Friend;

  constructor(id: number, date: Date, friend: Friend) {
    this.id = id;
    this.lastMessage = date;
    this.friend = friend;
  }

  static fromJSON(json: any): Chat {
    return new Chat(json.id, json.lastMessage, json.friend);
  }
}

export class Attachment {
  id: number;
  path: string;
  filename: string;
  originalname: string;
  extension: string;

  constructor(id: number, path: string, originalname: string, filename: string, extension: string) {
    this.id = id;
    this.path = path;
    this.originalname = originalname;
    this.filename = filename;
    this.extension = extension;
  }

  static fromJSON(json: any): Attachment {

    let attachment = new Attachment(json.id, json.path, json.originalname, json.filename, json.extension);
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
  fail: boolean;
  attachments: Attachment[]

  constructor(messageId: number, username: string, message: string, attachments?: Attachment[], sentAt?: Date, sent?: boolean) {
    this.messageId = messageId;
    this.username = username;
    this.message = message;
    this.sentAt = sentAt ?? new Date();
    this.sent = sent ? sent : false;
    this.fail = false;
    this.attachments = attachments ? attachments : [];
  }

  static fromJSON(json: any): Message {
    if (!json) {
      throw new Error("Message.fromJSON called with null/undefined");
    }

    let arr = null;
    if (json.attachments) {
      arr = [];
      if (json.attachments.length !== 0)
        arr = json.attachments.map(
          (item: any) => Attachment.fromJSON(item)
        );
    }


    let message = new Message(json.messageId, json.username, json.message, arr ? arr : null, new Date(json.sentAt), true);
    return message
  }
}







// ================= Channel & TextChannel =================

export enum ChannelType {
  VOICE = "vocale", TEXT = "testo"
}
export class Channel {
  id: number;
  sectionId: number;
  communityId: number;
  type: ChannelType;
  name: string;
  description?: string;
  createdAt?: Date;
  users: Account[];

  constructor(id: number, sectionId: number, communityId: number, type: ChannelType, name: string, description?: string, createdAt?: Date) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
    this.sectionId = sectionId;
    this.communityId = communityId;
    this.users = [];
  }

  public addUsers(user: Account) {
    if (this.type === ChannelType.VOICE)
      this.users.push(user);
  }

  static fromJSON(json: any): Channel {

    return new Channel(json.id, json.sectionId, json.communityId, json.type, json.name, json.description);
  }
}

export class Section {
  id: number;
  name: string;
  channels: Channel[];

  constructor(id: number, name: string, channels: Channel[] = []) {
    this.id = id;
    this.name = name;
    this.channels = channels;
  }

  addChannel(channel: Channel) {
    this.channels.push(channel);
  }

  static fromJSON(json: any): Section {
    const section = new Section(json.id, json.name);
    if (json.channels) {
      section.channels = json.channels.map((c: any) => {
        c.sectionId = json.id;
        c.communityId = json.communityId;
        return Channel.fromJSON(c);
      });
    }
    return section;
  }
}

// ================= Server =================
export class Server {
  id: number;
  name: string;
  inviteCode?: string;
  description?: string;
  owner?: string;
  createdAt?: Date;
  sections: Section[];

  constructor(
    id: number,
    name: string,
    sections: Section[] = [],
    description?: string,
    owner?: string,
    createdAt?: Date,
    inviteCode?: string
  ) {
    this.id = id;
    this.name = name;
    this.sections = sections;
    this.owner = owner;
    this.description = description;
    this.createdAt = createdAt;
    this.inviteCode = inviteCode;
  }

  addSection(section: Section) {
    this.sections.push(section);
  }

  static fromJSON(json: any): Server {
    let server = new Server(
      json.id,
      json.name,
      json.sections ? json.sections.map((s: any) => {
        s.communityId = json.id;
        return Section.fromJSON(s)
      }
      ) : [],
      json.description,
      json.owner,
      json.createdAt ? new Date(json.createdAt) : undefined,
      json.inviteCode,
    );
    console.log(server)

    return server;
  }
}