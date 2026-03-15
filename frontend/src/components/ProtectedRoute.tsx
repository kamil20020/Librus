import { useNavigate } from "react-router";
import { useAuth } from "../store/AuthStore";

const ProtectedRoute = (props: {
    requiresLogin?: boolean;
    content: React.ReactNode;
}) => {

    const { authProps } = useAuth();
    const isUserLogged = authProps.isLogged;

    const navigate = useNavigate();

    if(props.requiresLogin && !isUserLogged){
        navigate("/login");
        return <></>
    }

    return (
        <>
            {props.content}
        </>
    )
}

export default ProtectedRoute;