import axios from "axios";
import { useRef, useState } from "react";

function Login() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
   
    const updateUsername = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value)
    }

    const updatePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
        setPassword(e.target.value)
    }

    const login = () => {
        const execute = async () => {
            const res = await axios.post(
                "http://localhost:8080/api/login",
                { username, password },
                {
                    withCredentials: true,
                    headers: {
                        "Content-Type": "application/json"
                    }
                }
            );
            console.log(res);
        }
        execute()
    }

    return (
        <div onKeyDown={(e) => {
            if (e.key === "Enter")
                login();
        }}
            className=
            {'p-3 pt-0 w-full h-full transition-opacity duration-50'}>
            <div className="rounded-xl bg-black w-full h-full flex items-center justify-center flex-col gap-5">
                <input className="border" type="text" value={username} onChange={updateUsername} />
                <input className="border" type="password" value={password} onChange={updatePassword} />
                <p className="text-white">Username: {username}</p>
                <p className="text-white">Password: {password}</p>
            </div>


        </div>
    );
}

export default Login;
