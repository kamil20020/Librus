import Content from "./Content"
import Footer from "./Footer"
import Header from "./Header"
import Navigation from "./navigation/Navigation"
import "./layout.css";

const LoggedUserPage = () => {

    return (
        <div className="page">
            <Header/>
            <Navigation/>
            <Content/>
            <Footer/>
        </div>
    )
}

export default LoggedUserPage;