import { ChangeEvent, HTMLInputTypeAttribute } from "react";

const ValidatedInput = (props: {
    inputId: string;
    labelValue: string;
    type?: HTMLInputTypeAttribute;
    placeholder?: string;
    value?: any;
    isRequired?: boolean;
    onChange: (value: any) => void;
}) => {

    return (
        <div className="form-element">
            <label htmlFor={props.inputId}>{props.labelValue}:</label>
            <input
                type={props.type ? props.type : "text"}
                name={props.inputId}
                placeholder={props.placeholder}
                value={props.value}
                onChange={(event: ChangeEvent<HTMLInputElement>) => props.onChange(event.target.value)}
            />
        </div>
    )
}

export default ValidatedInput;