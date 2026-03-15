import Content from "./Content";
import Footer from "./Footer";
import Header from "./header/Header";
import "./layout.css";

const NotLoggedUserPage = () => {

    return (
        <div className="page">
            <Header/>
            <Content/>
            <Footer/>
        </div>
    )
}

export default NotLoggedUserPage;