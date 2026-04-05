import { ChangeEvent, HTMLInputTypeAttribute } from "react";

export interface ValidatedInputProps{
    inputId: string;
    labelValue: string;
    type?: HTMLInputTypeAttribute;
    placeholder?: string;
    value?: any;
    isRequired?: boolean;
    errorMessage?: string;
    onChange: (value?: any) => void;
}

const ValidatedInput = (props: ValidatedInputProps) => {

    return (
        <div className="form-element">
            <label htmlFor={props.inputId}>{props.labelValue}:</label>
            <div className="form-element-content">
                <input
                    type={props.type ? props.type : "text"}
                    name={props.inputId}
                    placeholder={props.placeholder}
                    value={props.value}
                    onChange={(event: ChangeEvent<HTMLInputElement>) => props.onChange(event.target.value)}
                />
                {props.isRequired && <span className="required-field">*</span>}
            </div>
            <span className="error-message">{props.errorMessage} &nbsp;</span>
        </div>
    )
}

export default ValidatedInput;