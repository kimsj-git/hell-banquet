import { createBrowserRouter } from "react-router-dom";

import Error404Page from "./Error404Page";
import LoginPage from "./LoginPage";
import LandingPage from "./LandingPage"
import DietUpload from "./DietUpload";

const router = createBrowserRouter([
    {
        path: '*',
        element: <Error404Page />
    },
    {
        path: '/login',
        element: <LoginPage />
    },
    {
        path: '/',
        element: <LandingPage />
    },
    {
        path: '/diet/upload',
        element: <DietUpload />
    },
    {
        path: '/diet/update',
        element: <LandingPage />
    },
    {
        path: '/statistics/daily',
        element: <LandingPage />
    },
    {
        path: '/statistics/weekly',
        element: <LandingPage />
    },
    {
        path: '/member/upload',
        element: <LandingPage />
    },
    {
        path: '/member/read',
        element: <LandingPage />
    },



])


export default router;
