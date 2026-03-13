import React from "react";
import "./components.css";

const Icon = (props: {
    code: string,
    className?: string,
    style?: React.CSSProperties
}) => {

    return (
        <span className={`material-symbols-outlined icon ${props.className}`}
            style={{
                ...props.style
            }}
        >
            {props.code}
        </span>
    )
}

export default Icon;