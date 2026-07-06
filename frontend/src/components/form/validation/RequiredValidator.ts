import Validator from "./Validator";

class RequiredValidator extends Validator{

    private errorMessage: string = "Pole jest wymagane";

    public validate(value?: string): boolean {
       
        return value != undefined && 
               value != null &&
               ("" + value).trim().length > 0
    }

    public getErrorMessage(): string {

        return this.errorMessage;
    }
}

export default new RequiredValidator();