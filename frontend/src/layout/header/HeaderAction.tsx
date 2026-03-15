import Icon from "../../components/Icon";

const HeaderAction = (props: {
    iconCode: string;
    text?: string;
}) => {

    return (
        <div className="header-action">
            <Icon className="header-action-icon" code={props.iconCode}/>
            <span className="header-action-text">
                {props.text}
            </span>
        </div>
    )
}

export default HeaderAction;