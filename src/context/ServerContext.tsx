import React from "react";
import {Server, } from "../types";

export const ServerContext = React.createContext<Server | null>(null);

export function useServerContext() {
  const server = React.useContext(ServerContext);
  return server;
}

