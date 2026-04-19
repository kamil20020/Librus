import { AxiosResponse } from "axios";
import { HandleRequestProps, RequestState, useHandleRequest } from "./useHandleRequest";
import { useLoading } from "../store/LoadingStore";
import { useEffect } from "react";

export const useLoadingHandleRequest = <I extends object, R extends object,>(props: {
    getRequest: (requestParams: I) => Promise<AxiosResponse<R, any, {}>>,
    isAutoLoading?: boolean;
    inputData?: I,
}): HandleRequestProps<I, R> => {

    const apiRequest = useHandleRequest<I, R>(props);
    const loading = useLoading();

    useEffect(() => {

        if(!apiRequest.isEnded()){
            return;
        }

        loading.setIsLoading(false);

    }, [apiRequest.info.state]);

    const sendRequest = (form: I) => {

        if(apiRequest.info.state == RequestState.IS_LOADING){
            return;
        }

        const request: I = {...form};
        apiRequest.sendRequest(request);
        loading.setIsLoading(true);
    }

    return {
        info: apiRequest.info,
        isEnded: apiRequest.isEnded,
        sendRequest: sendRequest
    };
};