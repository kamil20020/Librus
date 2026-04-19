import { AxiosResponse } from "axios";
import { useEffect, useState } from "react";

interface RequesInfo<R extends object>{
    state: RequestState,
    data?: R,
    statusCode?: number,
    errorMessage?: string,
}

export interface HandleRequestProps<I extends object, R extends object>{
    info: RequesInfo<R>,
    isEnded: () => boolean,
    sendRequest: (requestData: I) => void;
}

export enum RequestState{
    NOT_STARTED, IS_LOADING, IS_SUCCEEDED, IS_FAILED
}

export const useHandleRequest = <I extends object, R extends object,>(props: {
    getRequest: (requestParams: I) => Promise<AxiosResponse<R, any, {}>>,
    isAutoLoading?: boolean;
    inputData?: I,
}): HandleRequestProps<I, R> => {

    const sendRequest = (requestData: I) => {
        setState({
            ...state,
            state: RequestState.IS_LOADING
        });

        props.getRequest(requestData)
        .then((response) => {
            const data: R = response.data;
            setState({
                ...state,
                state: RequestState.IS_SUCCEEDED,
                statusCode: response.status,
                data: data
            })
        })
        .catch((error) => {
            setState({
                 ...state,
                state: RequestState.IS_FAILED,
                statusCode: error.status,
                errorMessage: error.message
            })
        })
    }

    const [state, setState] = useState<RequesInfo<R>>({
        state: RequestState.NOT_STARTED,
    });

    useEffect(() => {
        if(!props.isAutoLoading){
            return;
        }

        sendRequest(props.inputData!)
    }, [])

    const isEnded = (): boolean => {
        return state.state === RequestState.IS_SUCCEEDED ||
               state.state === RequestState.IS_FAILED
    }

    return {
        info: state,
        isEnded,
        sendRequest
    };
}