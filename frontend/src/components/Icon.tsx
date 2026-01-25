import React from "react";

const Icon = (props: {
    code: string,
    className?: string,
    style: React.CSSProperties
}) => {

    return (
        <span className={`material-symbols-outlined icon ${props.className}`}
            style={{
                color: 'var(--primary-color)',
                backgroundColor: 'var(--secondary-color)',
                ...props.style
            }}
        >
            {props.code}
        </span>
    )
}

export default Icon;