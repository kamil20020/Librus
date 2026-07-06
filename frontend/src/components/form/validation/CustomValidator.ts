import Validator from "./Validator";

class CustomValidator extends Validator{

    private errorMessage: string;
    private fields: number[];
    private values: string[];
    private customValidate: (values: string[]) => boolean;

    constructor(errorMessage: string, fields: number[], customValidate: (values: string[]) => boolean){
        super();
        this.errorMessage = errorMessage;
        this.fields = fields;
        this.values = ([] as string[]).fill("", fields.length);
        this.customValidate = customValidate;
    }

    public validate(): boolean {

        return this.customValidate(this.values);
    }

    public setValues(values: string[]){

        this.values = values;
    }

    public getErrorMessage(): string {

        return this.errorMessage;
    }

    public getFields(): number[]{
        return this.fields;
    }
}

export default CustomValidator;