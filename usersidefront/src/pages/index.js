import { createBrowserRouter } from "react-router-dom";

import Error404Page from "./Error404Page"
import LandingPage from "./LandingPage";
import LoginPage from "./LoginPage"
import SignUpPage from "./SignUpPage"
import PasswordFind from "./PasswordFindPage"
import BoardPage from "./BoardPage";
import BoardDetailPage from "./BoardDetailPage";
import UserPage from "./UserPage";
import LoadingPage from "./LoadingPage";
import UserInfoChange from "./UserInfoChange";

const routes = [
    // error pages
    {
        path: "*",
        element: <Error404Page/>,
    },
    {
        path: "/loading",
        element: <LoadingPage/>,
    },
    // member pages
    {
        path: "/",
        element: <LandingPage/>,
    },
    {  
        path: "/login",
        element: <LoginPage/>,
    },
    {
        path: "/signup",
        element: <SignUpPage/>
    },
    {
        path: "/find-password",
        element: <PasswordFind />
    },
    {
        path: "/user/:string",
        element: <UserPage />
    },
    {
        path: "/user/:string/update",
        element: <UserInfoChange />
    },
    // board page
    {
        path: "/board",
        element: <BoardPage />
    },
    {
        path: "/board/:int",
        element: <BoardDetailPage />
    }
]

const router = createBrowserRouter(routes);

export default router;