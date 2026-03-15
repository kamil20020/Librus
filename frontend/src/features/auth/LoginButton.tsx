import { useNavigate } from "react-router";

const LoginButton = () => {

    const navigtate = useNavigate();

    return (
        <button
            className="button-action"
            onClick={() => navigtate("/login")}
        >
            Logowanie
        </button>
    )
}

export default LoginButton;