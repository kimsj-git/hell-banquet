import { createBrowserRouter } from "react-router-dom";

import Error404Page from "./Error404Page"
import LandingPage from "./LandingPage";

const routes = [
    {
        path: "*",
        element: <Error404Page/>,
    },
    {
        path: "/",
        element: <LandingPage/>,
    },
]

const router = createBrowserRouter(routes);

export default router;