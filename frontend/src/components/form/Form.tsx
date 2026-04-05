import { FormEvent, useState } from "react";
import ValidatedInput from "./ValidatedInput";
import Validator from "../../features/auth/validation/Validator";
import FormValidateService from "../../services/FormValidateService";

export interface FormElementProps{
    isRequired?: boolean,
    isValidated?: boolean,
    inputId: string,
    labelValue: string,
    placeholder?: string,
    type?: string;
    validations?: Validator[];
}

interface Base{

}

const Form = <F extends Base, E extends Base,>(props: {
    initialForm: F;
    initialErrors: E;
    elements: FormElementProps[]
    submitButtonLabel?: string;
    onSubmit: (form: F) => void;
}) => {

    const [form, setForm] = useState<F>(props.initialForm);
    const [errors, setErrors] = useState<E>(props.initialErrors);

    const validateForm = () => {

        const newErrors: E = {...props.initialErrors};

        const formKeys: string[] = Object.keys(form);
        const formValues: any[] = Object.values(form);

        let isFormValid: boolean = true;

        for(let formElementIndex = 0; formElementIndex < formValues.length; formElementIndex++){

            const formKey = formKeys[formElementIndex]
            const formValue = formValues[formElementIndex];
            const field = props.elements[formElementIndex] as FormElementProps;
 
            if(!field.isValidated || !field.validations){
                continue;
            }

            try{
                FormValidateService.validateFieldWithValidators(formValue, field.validations);
            }
            catch(rawError: any){
                const error = rawError as Error;
                newErrors[formKey as keyof E] = error.message as any;
                isFormValid = false;
            }
        }

        setErrors(newErrors);

        return isFormValid;
    }

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {

        event.preventDefault();

        if(!validateForm()){
            return;
        }

        props.onSubmit(form);
    }

    const mapPropsToComponent = (props: FormElementProps, index: number): React.ReactNode => {

        const fieldKey: string = Object.keys(form)[index]
        const fieldValue: string = Object.values(form)[index];
        const errorValue: string = errors[fieldKey as keyof E] as string;
        return(
            <ValidatedInput
                key={props.inputId}
                {...props}
                type={props.type ? props.type : "text"}
                value={fieldValue}
                errorMessage={errorValue}
                onChange={(value?: any) => {
                    setErrors({...errors, [fieldKey]: ''})
                    setForm({...form, [fieldKey]: value})
                }}
            />
        )
    }

    return (
        <form onSubmit={handleSubmit}>
            {props.elements.map(mapPropsToComponent)}
            <button
                type="submit"
                className="button-success"
                style={{width: "50%"}}
            >
                {props.submitButtonLabel ? props.submitButtonLabel : "Zatwierdź"}
            </button>
        </form>
    )
}

export default Form;