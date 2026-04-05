import { LoginRequest } from "../api/requests/LoginRequest";
import SimplePage from "../layout/SimplePage";
import AuthService from "../services/AuthService";
import Form, { FormElementProps } from "../components/form/Form";
import RequiredValidator from "../features/auth/validation/RequiredValidator";
import FieldTooShortValidator from "../features/auth/validation/FieldTooShortValidator";
import FieldTooLongValidator from "../features/auth/validation/FieldTooLongValidator";

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
        validations: [RequiredValidator, new FieldTooShortValidator(8), new FieldTooLongValidator(20)]
    },
]

const Login = () => {

    const handleLogin = (form: LoginProps) => {

        const request: LoginRequest = {...form};

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