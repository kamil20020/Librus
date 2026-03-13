import { NavLink } from "react-router";
import NavigationItemIcon from "./NavigationItemIcon";

const NavigationItem = (props: {
    pathTo: string,
    iconCode: string,
    text: string
}) => {

    return (
         <NavLink to={props.pathTo}>
            <div className="navigation-item">
                <NavigationItemIcon code={props.iconCode}/>
                <span className="navigation-item-text">
                    {props.text}
                </span>
            </div>
        </NavLink>
    )
}

export default NavigationItem;