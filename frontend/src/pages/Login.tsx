import { DOMAttributes, FormEvent, FormEventHandler } from "react";
import { LoginRequest } from "../api/requests/LoginRequest";
import ValidatedInput from "../components/ValidatedInput";
import SimplePage from "../layout/SimplePage";
import AuthService from "../services/AuthService";

const Login = () => {

    const onSumbmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        handleLogin();
    }

    const handleLogin = () => {

        const request: LoginRequest = {
            username: "kamil",
            password: "kamil123"
        };

        AuthService.login(request)
        .then((response) => {
            console.log(response);
        })
        .catch((error) => {
            console.log(error);
        })
    }

    return (
        <SimplePage
            title="Logowanie"
            content={
                <form onSubmit={onSumbmit}>
                    <ValidatedInput
                        inputId="login"
                        labelValue="Login lub e-mail"
                        placeholder="mail@mail.com"
                        onChange={(newValue: string) => console.log(newValue)}                        
                    />
                    <ValidatedInput
                        inputId="password"
                        labelValue="Hasło"
                        type="password"
                        placeholder="*****"
                        onChange={(newValue: string) => console.log(newValue)}                        
                    />
                    <button type="submit" className="button-success" style={{width: "50%"}}>Zaloguj</button>
                </form>
            }
        />
    );
}

export default Login;