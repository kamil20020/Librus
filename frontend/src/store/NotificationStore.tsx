import React, { useRef, useState } from "react";
import { useContext } from "react";

export enum NotificationType{
    SUCCEESS, ERROR
}

interface NotificationState{
    message: string,
    type: NotificationType,
    isVisible: boolean
}

interface NotificationProps{
    state: NotificationState,
    setNotification: (message: string, type: NotificationType) => void;
}

const NotificationContext = React.createContext<NotificationProps | null>(null);

const notificationTimeDuration = 5000;

const NotificationProvider = (props: {children: React.ReactNode}) => {

    const [state, setState] = useState<NotificationState>({
        message: '',
        type: NotificationType.SUCCEESS,
        isVisible: false
    });

    const initTimeout = (): NodeJS.Timeout  => {

        return setTimeout(() => {

            setState({
                message: '',
                type: NotificationType.SUCCEESS,
                isVisible: false
            });

        }, notificationTimeDuration);
    }

    const timeout = useRef<NodeJS.Timeout | null>(null);

    const setNotification = (message: string, type: NotificationType) => {

        setState({
            message: message,
            type: type,
            isVisible: true
        })

        if(timeout.current){
            clearTimeout(timeout.current);
        }

        timeout.current = initTimeout();
    }

    return (
        <NotificationContext.Provider value={{state, setNotification}}>
            {props.children}
            {state.isVisible &&
                <div className="notification" style={{position: "absolute", bottom: 20, right: 20}}>
                    {state.message}
                </div>
            }
        </NotificationContext.Provider>
    )
};

export const useNotification = () => {

    const context = useContext(NotificationContext);

    if(!context){
        throw new Error("Notification context was not loaded")
    }

    return context;
}

export default NotificationProvider;