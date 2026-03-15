import axios from "axios";
import { LoginRequest } from "../api/requests/LoginRequest";

class AuthService {

    private API_URL = `${process.env.REACT_APP_API_URL}/auth`;

    public login(request: LoginRequest){

        return axios.post(`${this.API_URL}/token`, request);
    }
}

export default new AuthService();