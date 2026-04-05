import Validator from "./Validator";

export default class FieldTooShortValidator extends Validator{

    minLength: number;

    errorMessage: string = "Wymagane co najmniej {} znaki"

    constructor(minlength: number){
        super();

        this.minLength = minlength;
        this.errorMessage = this.errorMessage.replace("{}", "" + this.minLength);
    }

    public validate(value: string): boolean {

        return value.length >= this.minLength;
    }

    public getErrorMessage(): string {

        return this.errorMessage;
    }
}