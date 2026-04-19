import { LoginRequest } from "../api/requests/LoginRequest";
import SimplePage from "../layout/SimplePage";
import AuthService from "../services/AuthService";
import Form, { FormElementProps } from "../components/form/Form";
import RequiredValidator from "../components/form/validation/RequiredValidator";
import FieldTooShortValidator from "../components/form/validation/FieldTooShortValidator";
import FieldTooLongValidator from "../components/form/validation/FieldTooLongValidator";
import { RequestState, useHandleRequest } from "../hooks/useHandleRequest";
import LoginResponse from "../api/responses/LoginResponse";
import { useLoading } from "../store/LoadingStore";
import { useEffect } from "react";
import { useLoadingHandleRequest } from "../hooks/useLoadingHandleRequest";
import { NotificationType, useNotification } from "../store/NotificationStore";

interface LoginProps{
    username: string;
    password: string;
}

const initialForm: LoginProps = {
    username: '',
    password: ''
}

interface LoginErrorProps{
    username: string;
    password: string;
}

const initialErrors: LoginErrorProps = {
    username: '',
    password: ''
}

const formProps: FormElementProps[] = [
    {
        isRequired: true,
        isValidated: true,
        inputId:"login",
        labelValue:"Login lub e-mail",
        placeholder:"mail@mail.com",
        validations: [RequiredValidator]
    },
    {
        isRequired: true,
        isValidated: true,
        type: "password",
        inputId:"password",
        labelValue:"Hasło",
        placeholder:"*****",
        validations: [
            RequiredValidator,
            new FieldTooShortValidator(8),
            new FieldTooLongValidator(20)
        ]
    },
]

const Login = () => {

    const loginRequest = useLoadingHandleRequest<LoginRequest, LoginResponse>({
        getRequest: (requestData: LoginRequest) => AuthService.login(requestData)
    });
    const notification = useNotification();

    const handleLogin = (form: LoginProps) => {

        if(loginRequest.info.state == RequestState.IS_LOADING){
            return;
        }

        const request: LoginRequest = {...form};

        loginRequest.sendRequest(request);
    }

    useEffect(() => {

        if(!loginRequest.isEnded()){
            return;
        }

        if(loginRequest.info.state == RequestState.IS_SUCCEEDED){

            notification.setNotification("Zalogowano się", NotificationType.SUCCEESS);

            return;
        }

        let errorMessage = loginRequest.info.errorMessage;

        if(loginRequest.info.statusCode == 401 || !errorMessage){

            errorMessage = "Wprowadzono niepoprawny login lub hasło";
        }

        notification.setNotification(errorMessage, NotificationType.ERROR);

    }, [loginRequest.info.state])

    return (
        <SimplePage
            title="Logowanie"
            content={
                <Form<LoginProps, LoginErrorProps>
                    initialForm={initialForm}
                    initialErrors={initialErrors}
                    elements={formProps}
                    onSubmit={handleLogin}
                />
            }
        />
    );
}

export default Login;