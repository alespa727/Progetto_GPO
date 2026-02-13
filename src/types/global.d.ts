interface ElectronAPI {
  copy: (text: string) => void;
  paste: () => string;
  getSources: (opts: { types: string[] }) => Promise<{ id: string; name: string }[]>;
  downloadRemote: (url: string) => Promise<{ success: boolean; path?: string; error?: string }>;
  downloadLocal: (filename: string, extension: string) => Promise<{ success: boolean; path?: string }>;
}

declare global {
  interface Window {
    electronAPI: ElectronAPI; 
    versions: {
      node: () => string;
      chrome: () => string;
      electron: () => string;
    };
  }
}

export {}; 
