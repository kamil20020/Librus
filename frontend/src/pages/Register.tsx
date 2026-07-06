import { useEffect } from "react";
import { RegisterRequest } from "../api/requests/RegisterRequest";
import RegisterResponse from "../api/responses/RegisterResponse";
import Form, { FormElementProps } from "../components/form/Form";
import RequiredValidator from "../components/form/validation/RequiredValidator";
import { RequestState } from "../hooks/useHandleRequest";
import { useLoadingHandleRequest } from "../hooks/useLoadingHandleRequest";
import SimplePage from "../layout/SimplePage";
import UserService from "../services/UserService";
import { NotificationType, useNotification } from "../store/NotificationStore";
import CustomValidator from "../components/form/validation/CustomValidator";
import { useNavigate } from "react-router";

interface RegisterProps{
    firstname: string;
    surname: string;
    username: string;
    email: string;
    phone?: string;
    password: string;
    repeatPassword: string;
}

const initialForm: RegisterProps = {
    firstname: '',
    surname: '',
    username: '',
    email: '',
    password: '',
    repeatPassword: ''
}

interface RegisterErrorsProps{
    firstname: string;
    surname: string;
    username: string;
    email: '',
    password: string;
    repeatPassword: string;
}

const initialErrors: RegisterErrorsProps = {
    firstname: '',
    surname: '',
    username: '',
    email: '',
    password: '',
    repeatPassword: ''
}

const formProps: FormElementProps[] = [
    {
        isRequired: true,
        isValidated: true,
        inputId:"firstname",
        labelValue:"Imię",
        validations: [RequiredValidator]
    },
    {
        isRequired: true,
        isValidated: true,
        inputId:"surname",
        labelValue:"Nazwisko",
        validations: [RequiredValidator]
    },
    {
        isRequired: true,
        isValidated: true,
        inputId:"username",
        labelValue:"Login",
        validations: [RequiredValidator]
    },
    {
        isRequired: true,
        isValidated: true,
        inputId:"email",
        labelValue:"Email",
        validations: [RequiredValidator]
    },
    {
        isRequired: true,
        isValidated: true,
        inputId:"password",
        labelValue:"Hasło",
        validations: [RequiredValidator]
    },
    {
        isRequired: true,
        isValidated: true,
        inputId:"repeatPassword",
        labelValue:"Powtórz hasło",
        validations: [
            RequiredValidator,
            new CustomValidator(
                "Hasła nie są zgodne",
                [4, 5],
                (values: string[]) => values[0] === values[1]
            )
        ]
    },
]

const Register = () => {

    const registerRequest = useLoadingHandleRequest<RegisterRequest, RegisterResponse>({
        getRequest: (request: RegisterRequest) => UserService.register(request)
    });
    const notification = useNotification();
    const navigate = useNavigate();

    const handleRegister = (form: RegisterProps) => {
    
        if(registerRequest.info.state == RequestState.IS_LOADING){
            return;
        }

        const request: RegisterRequest = {...form};

        registerRequest.sendRequest(request);
    }

    useEffect(() => {

        if(!registerRequest.isEnded()){
            return;
        }

        if(registerRequest.info.state == RequestState.IS_SUCCEEDED){

            notification.setNotification("Zarejestrowano się", NotificationType.SUCCEESS);

            navigate("/")
            return;
        }

        let errorMessage = registerRequest.info.errorMessage;

        if(registerRequest.info.statusCode == 401 || !errorMessage){

            errorMessage = "Wprowadzono niepoprawny login lub hasło";
        }

        notification.setNotification(errorMessage, NotificationType.ERROR);
    
    }, [registerRequest.info.state])

    return (
        <SimplePage
            title="Rejestracja"
            content={
                <Form<RegisterProps, RegisterErrorsProps>
                    initialForm={initialForm}
                    initialErrors={initialErrors}
                    elements={formProps}
                    onSubmit={handleRegister}
                />
            }
        />
    )
}

export default Register;