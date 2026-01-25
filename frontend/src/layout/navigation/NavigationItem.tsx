import { NavLink } from "react-router";
import NavigationItemIcon from "./NavigationItemIcon";

const NavigationItem = (props: {
    pathTo: string,
    iconCode: string,
    text: string
}) => {

    return (
        <div
            className="navigation-item"
            style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center"
            }}
        >
            <NavLink to={props.pathTo}>
                 <NavigationItemIcon code={props.iconCode}/>
            </NavLink>
            <span
                className="navigation-item-text"
                style={{
                    color: 'var(--primary-color)',
                    fontWeight: 800
                }}
            >
                {props.text}
            </span>
        </div>
    )
}

export default NavigationItem;