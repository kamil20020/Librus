import Validator from "./Validator";

class FieldTooLongValidator extends Validator{

    maxLength: number;

    errorMessage: string = "Wymagane maksimum {} znaki"

    constructor(maxLength: number){
        super();

        this.maxLength = maxLength;
        this.errorMessage = this.errorMessage.replace("{}", "" + this.maxLength);
    }

    public validate(value: string): boolean {

        return value.length <= this.maxLength;
    }

    public getErrorMessage(): string {

        return this.errorMessage;
    }
}

export default FieldTooLongValidator;