import { createContext, useContext, useState } from "react";

interface AuthProps{
    isLogged: boolean;
    accessToken?: string;
    refreshToken?: string;
}

const initialProps: AuthProps = {
    isLogged: false
}

export interface LoginProps{
    accessToken?: string;
    refreshToken?: string;
}

interface State {
    authProps: AuthProps,
    setLogged: (loginProps: LoginProps) => void;
    setLogout: () => void;
}

const AuthContext = createContext<State | undefined>(undefined)

const AuthProvider = (props: {
    children: React.ReactNode;
}) => {

    const [authProps, setAuthProps] = useState<AuthProps>(initialProps);

    const setLogged = (loginProps: LoginProps): void => {
        setAuthProps({
            ...authProps,
            accessToken: loginProps.accessToken,
            isLogged: true
        })
    }

    const setLogout = (): void => {
        setAuthProps({...initialProps})
    }

    return (
        <AuthContext.Provider value={{authProps, setLogged, setLogout}} >
            {props.children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {

    const context = useContext(AuthContext);

    if(!context){
        throw new Error("Auth context is not set");
    }

    return context;
}

export default AuthProvider;