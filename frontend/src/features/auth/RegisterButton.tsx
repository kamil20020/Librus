import { useNavigate } from "react-router";

const RegisterButton = () => {

    const navigate = useNavigate();

    return (
        <button
            className="button-action"
            onClick={() => navigate("/register")}
        >
            Rejestracja
        </button>
    )
}

export default RegisterButton;