import Validator from "../features/auth/validation/Validator";

class FormValidateService {

    fieldInvalid = "Wprowadzono niepoprawną wartość"

    public validateFieldWithValidators(value?: string, validators?: Validator[]){

        if(!validators){
            return;
        }

        for(const validator of validators){

            const isValidated = validator.validate(value);

            if(isValidated){
                continue;
            }

            throw new Error(validator.getErrorMessage());
        }
    }
}

export default new FormValidateService();