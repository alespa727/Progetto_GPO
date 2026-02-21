import { endpoint2 } from "@/types";
import axios, { AxiosError, HttpStatusCode } from "axios";
import { useState } from "react";

enum Mode {
    REGISTER,
    LOGIN
}

enum AuthError {
    // Login
    WRONG_PASSWORD = "WRONG_PASSWORD",
    USER_NOT_FOUND = "USER_NOT_FOUND",
    INVALID_CREDENTIALS = "INVALID_CREDENTIALS",

    // Register
    USERNAME_ALREADY_EXISTS = "USERNAME_ALREADY_EXISTS",
    PASSWORDS_DO_NOT_MATCH = "PASSWORDS_DO_NOT_MATCH",

    // --- Generico ---
    VALIDATION_ERROR = "VALIDATION_ERROR",
    NETWORK_ERROR = "NETWORK_ERROR"
}

const getAuthErrorMessage = (error: AuthError): string => {
    switch (error) {
        // Login
        case AuthError.WRONG_PASSWORD:
            return "Password errata.";
        case AuthError.USER_NOT_FOUND:
            return "Utente non trovato.";
        case AuthError.INVALID_CREDENTIALS:
            return "Credenziali non valide.";

        // Register
        case AuthError.USERNAME_ALREADY_EXISTS:
            return "Username già in uso.";
        case AuthError.PASSWORDS_DO_NOT_MATCH:
            return "Le password non coincidono.";
        case AuthError.NETWORK_ERROR:
            return "Errore di connessione";

        case AuthError.VALIDATION_ERROR:
            return "Dati non validi.";
        default:
            return "Si è verificato un errore imprevisto.";
    }
};


function countdown(seconds: number = 3): Promise<void> {
    return new Promise<void>((resolve) => {
        let remaining = seconds;

        const interval = setInterval(() => {
            console.log(`Reload tra ${remaining}...`);
            remaining--;

            if (remaining < 0) {
                clearInterval(interval);
                resolve(); // ok, perché Promise<void>
            }
        }, 1000);
    });
}

function Start() {
    const [errore, setErrore] = useState<AuthError | null>(null);
    const [successo, setSuccesso] = useState<boolean>(false);

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [confirm, setConfirm] = useState<string>("");
    const [mode, setMode] = useState<Mode>(Mode.LOGIN);

    const updateUsername = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value);
    };

    const updatePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value);
    };

    const updateConfirmedPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setConfirm(e.target.value);
    };

    const areCredentialsCorrect = () => {
        if (username === "" || password === "") {
            setSuccesso(false);
            setErrore(AuthError.INVALID_CREDENTIALS)
            return false;
        }

        return true;
    }

    const reload = () => {
        (async () => {
            await countdown(3);
            window.location.reload();
        })();
    }

    const login = async () => {
        if (!areCredentialsCorrect()) return;
        const res = await axios.post(
            endpoint2 + "/login",
            { username, password },
            {
                withCredentials: true,
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );

        if (res.status === HttpStatusCode.Ok) {
            setErrore(null)
            setSuccesso(true);
        }

        localStorage.setItem("accessToken", res.data.accessToken);
        reload()
    };
    
    const reset=()=>{
        setConfirm("")
        setPassword("")
        setUsername("")
    }

    const registra = async () => {
        if (!areCredentialsCorrect()) return;
        if (password !== confirm) {
            setErrore(AuthError.PASSWORDS_DO_NOT_MATCH)
            setSuccesso(false);
            return;
        }


        try {
            
            const res = await axios.post(
                endpoint2 + "/registration",
                { username, password },
                {
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/json"
                    }
                }
            );

            if (res.status === HttpStatusCode.Ok) {
                setErrore(null);
                setSuccesso(true);
                reset()
            }

        } catch (error: any) {
          
            if (error.response) {
                // Il server ha risposto con uno status fuori dal range 2xx
                if (error.response.status === HttpStatusCode.Conflict) {
                    setErrore(AuthError.USERNAME_ALREADY_EXISTS);
                } else {
                    setErrore(AuthError.INVALID_CREDENTIALS);
                }
            } else {
                // Errore di rete o richiesta non partita
                setErrore(AuthError.NETWORK_ERROR);
            }

            setSuccesso(false);
            return;
        }
        reload()
    };

    return (
        <div
            onKeyDown={(e) => e.key === "Enter" && ((mode === Mode.LOGIN && login()) || (mode === Mode.REGISTER && registra()))}
            className="min-h-screen flex w-full  overflow-y-auto items-center justify-center"
        >
            <div className="w-full max-w-md bg-white/10 backdrop-blur-lg border border-white/20 rounded-2xl shadow-2xl p-8 flex flex-col gap-6 transition-all duration-300">

                {/* Title */}
                <h3 className="text-center text-white text-3xl font-bold border-b border-white/20 pb-4">
                    {mode === Mode.LOGIN ? "Login" : "Registrazione"}
                </h3>

                {/* Username */}
                <div className="flex flex-col gap-2">
                    <label className="text-white/80 text-sm">Username</label>
                    <input
                        className="bg-white/20 border border-white/30 text-white p-3 rounded-lg outline-none focus:ring-2 focus:ring-white/40 transition-all"
                        type="text"
                        value={username}
                        onChange={updateUsername}
                        placeholder="Inserisci username"
                    />
                </div>

                {/* Password */}
                <div className="flex flex-col gap-2">
                    <label className="text-white/80 text-sm">Password</label>
                    <input
                        className="bg-white/20 border border-white/30 text-white p-3 rounded-lg outline-none focus:ring-2 focus:ring-white/40 transition-all"
                        type="password"
                        value={password}
                        onChange={updatePassword}
                        placeholder="Inserisci password"
                    />
                </div>

                {/* Confirm password solo in REGISTER */}
                {mode === Mode.REGISTER && (
                    <div className="flex flex-col gap-2">
                        <label className="text-white/80 text-sm">
                            Conferma password
                        </label>
                        <input
                            className="bg-white/20 border border-white/30 text-white p-3 rounded-lg outline-none focus:ring-2 focus:ring-white/40 transition-all"
                            type="password"
                            value={confirm}
                            onChange={updateConfirmedPassword}
                            placeholder="Ripeti password"
                        />
                    </div>
                )}

                {
                    errore && <div className={"w-full p-2 bg-(--base) rounded-(--radius)" + (" bg-red-500")}>
                        <p className={"text-white text-center"}>{getAuthErrorMessage(errore)}</p>
                    </div>
                }

                {
                    successo && <div className={"w-full p-2 bg-(--base) rounded-(--radius)" + (" bg-green-500")}>
                        <p className={"text-white text-center"}>{(mode === Mode.LOGIN ? "Ti sei autenticato con successo" : "Ti sei registrato con successo!") + " Reload in 3s"}</p>
                    </div>
                }


                {/* Buttons */}
                <div className={"flex gap-3 " + (!errore && "pt-4")}>
                    <button
                        className="flex-1 bg-black/40 hover:bg-black/60 text-white py-3 rounded-lg transition-all duration-200 hover:-translate-y-1"
                        onClick={() =>
                            setMode(
                                mode === Mode.LOGIN
                                    ? Mode.REGISTER
                                    : Mode.LOGIN
                            )
                        }
                    >
                        {mode === Mode.LOGIN ? "Registrati" : "Torna al Login"}
                    </button>

                    <button
                        className="flex-1 bg-white/20 hover:bg-white/30 text-white py-3 rounded-lg transition-all duration-200 hover:-translate-y-1"
                        onClick={mode === Mode.LOGIN ? login : registra}
                    >
                        {mode === Mode.LOGIN ? "Login" : "Crea Account"}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Start;