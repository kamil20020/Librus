import CustomValidator from "../components/form/validation/CustomValidator";
import Validator from "../components/form/validation/Validator";

class FormValidateService {

    fieldInvalid = "Wprowadzono niepoprawną wartość"

    public validateFieldWithValidators(value?: string, validators?: Validator[], allValues?: string[]){

        if(!validators){
            return;
        }

        for(const validator of validators){

            let isValidated = false;

            if(validator instanceof CustomValidator){
                isValidated = this.validateWithCustomValidator(validator, allValues as string[]);
            }
            else{
                isValidated = validator.validate(value);
            }

            if(isValidated){
                continue;
            }

            throw new Error(validator.getErrorMessage());
        }
    }

    private validateWithCustomValidator(validator: CustomValidator, allValues: string[]){
        const fields: number[] = validator.getFields();
        const values = this.getSelectedValues(fields, allValues);
        validator.setValues(values);
        return validator.validate();
    }

    private getSelectedValues(fields: number[], allValues: string[]){
        const result: string[] = [];
        let fieldIndex = 0
        for(let valueIndex = 0; valueIndex < allValues.length || fieldIndex < fields.length; valueIndex++){
            const field = fields[fieldIndex];
            if(field != valueIndex){
                continue;
            }
            const value: string = allValues[valueIndex];
            result.push(value);
            fieldIndex++;
        }
        return result;
    }
}

export default new FormValidateService();