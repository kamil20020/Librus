import Icon from "../../components/Icon";
import Logo from "../../components/Logo";
import LoginButton from "../../features/auth/LoginButton";
import RegisterButton from "../../features/auth/RegisterButton";

const Header = () => {

    return (
        <header>
            <Logo/>
            <div className="header-actions">
                <LoginButton/>
                <RegisterButton/>
            </div>
        </header>
    )
}

export default Header;