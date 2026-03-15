import { Link } from "react-router";

const Logo = (props: {
    isSecondary?: boolean
}) => {

    return (
        <Link className={`logo ${props.isSecondary && "secondary"}`} to="/">
            Librus
        </Link>
    )
}

export default Logo;