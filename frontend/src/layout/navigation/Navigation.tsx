import { NavLink } from "react-router";
import HeaderIcon from "./NavigationItemIcon";
import NavigationItem from "./NavigationItem";

const Navigation = () => {

    return (
        <nav>
            <NavigationItem pathTo="/notes" iconCode="dictionary" text="Oceny"/>
            <NavigationItem pathTo="/attendances" iconCode="book" text="Frekwencja"/>
            <NavigationItem pathTo="/plan" iconCode="data_table" text="Plan lekcji"/>
            <NavigationItem pathTo="/chat" iconCode="chat" text="Wiadomości"/>
            <NavigationItem pathTo="/notifications" iconCode="notifications" text="Ogłoszenia"/>
            <NavigationItem pathTo="/calendar" iconCode="calendar_month" text="Kalendarz"/>
        </nav>
    )
}

export default Navigation;