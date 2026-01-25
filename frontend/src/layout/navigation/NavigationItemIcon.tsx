import Icon from "../../components/Icon";

const NavigationItemIcon = (props: {
    code: string,
    text?: string
}) => {

    return(
        <Icon
            code={props.code}
            className="navigation-item-icon"
            style={{
                fontSize: 42,
                border: "4px solid var(--primary-color)",
                borderRadius: "50%",
                width: 62,
                height: 62,
                display: "flex",
                justifyContent: "center",
                alignItems: "center"
            }}
        />
    )
}

export default NavigationItemIcon;