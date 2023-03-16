import { createBrowserRouter } from "react-router-dom";

import Error404Page from "./Error404Page"
import LandingPage from "./LandingPage";
import LoginPage from "./LoginPage"

const routes = [
    {
        path: "*",
        element: <Error404Page/>,
    },
    {
        path: "/",
        element: <LandingPage/>,
    },
    {  
        path: "/login",
        element: <LoginPage/>,
    },
]

const router = createBrowserRouter(routes);

export default router;