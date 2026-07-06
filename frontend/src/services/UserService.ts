import axios from "axios";
import { LoginRequest } from "../api/requests/LoginRequest";
import { RegisterRequest } from "../api/requests/RegisterRequest";

class UserService {

    private API_URL = `${process.env.REACT_APP_API_URL}/users`;

    public register(request: RegisterRequest){

        return axios.post(`${this.API_URL}/register`, request);
    }
}

export default new UserService();