import Logo from "../components/Logo";
import "./layout.css"

const SimplePage = (props: {
    title: string;
    content?: React.ReactNode;
}) => {

    return (
        <div className="page simple-page">
            <div className="simple-page-content">
                <Logo isSecondary/>
                <span className="simple-page-title">
                    {props.title}
                </span>
                {props.content}
            </div>
        </div>
    )
}

export default SimplePage;