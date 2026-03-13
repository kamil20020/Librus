import Icon from "../../components/Icon";

const NavigationItemIcon = (props: {
    code: string,
    text?: string
}) => {

    return(
        <Icon
            code={props.code}
            className="navigation-item-icon"
        />
    )
}

export default NavigationItemIcon;