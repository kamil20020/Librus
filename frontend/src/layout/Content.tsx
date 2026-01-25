import { Outlet } from "react-router";

const Content = () => {

    return (
        <main>
            <div id="navigation-content">
                <Outlet/>
            </div>
        </main>
    )
}

export default Content;