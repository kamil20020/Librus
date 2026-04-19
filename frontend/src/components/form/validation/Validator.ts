export default abstract class Validator{

    public abstract validate(value?: string): boolean;
    public abstract getErrorMessage(): string;
}