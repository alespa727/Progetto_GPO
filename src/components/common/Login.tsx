import { endpoint, endpoint2 } from "@/types";
import axios from "axios";
import { useState } from "react";
import { Button } from "../animate-ui/primitives/buttons/button";

function Login() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [error, setError] = useState<string>("");

    const updateUsername = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value)
    }

    const updatePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    }

    const login = () => {
        const execute = async () => {
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
            console.log(res);
            window.location.reload();
        }
        execute()
    }

    return (
        <div onKeyDown={(e) => {
            if (e.key === "Enter") {
                login();

            }

        }}
            className=
            {'md:p-50 p-3 md:min-w-200 w-full transition-opacity duration-300'}>
            <div className=" bg-white/10 relative w-full h-full flex items-center justify-center flex-col gap-5">
                <h3 className="text-center border-white/10 w-full py-8 font-bold absolute top-0 text-white text-2xl border-b">Login</h3>
                <input className="border  border-white/30 bg-white/20 text-white p-2" type="text" value={username} onChange={updateUsername} />
                <input className="border border-white/30 bg-white/20 text-white p-2" type="password" value={password} onChange={updatePassword} />
                <div className="grid grid-cols-2 gap-2 w-full p-4 absolute bottom-0">
                   
                    <button
                        className="w-full h-16 hover:bg-black/20 bg-black/40 p-2 text-white transform transition-transform duration-200 hover:-translate-y-1"
                        onClick={login}
                    >
                        Registrati
                    </button>
                    
                     <button
                        className="w-full h-16  hover:bg-black/20 bg-black/40 p-2 text-white transform transition-transform duration-200 hover:-translate-y-1"
                        onClick={login}
                    >
                        Login
                    </button>
                </div>

            </div>


        </div>
    );
}

export default Login;
