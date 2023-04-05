import { createBrowserRouter } from "react-router-dom";

import Error404Page from "./user/Error404Page";
import LandingPage from "./user/LandingPage";
import LoginPage from "./user/LoginPage";
import SignUpPage from "./user/SignUpPage";
import PasswordFind from "./user/PasswordFindPage";
import BoardPage from "./user/BoardPage";
import BoardDetailPage from "./user/BoardDetailPage";
import UserPage from "./user/UserPage";
import LoadingPage from "./user/LoadingPage";
import UserInfoChangePage from "./user/UserInfoChangePage";
import RecordMeal from "./user/RecordMeal";
import DrawingPage from "./user/DrawingPage";
import JanbanPresentPage from "./user/JanbanPresentPage";
import MenusTestPage from "./test/MenusTestPage";
import TestRoutingPage from ".//test/TestRoutingPage";
import LeftoversTestPage from "./test/LeftoversTestPage";
import AnalysisPage from "./user/AnalysisPage";
import ABCGamePage from "./user/ABCGamePage";
import RankingPage from "./user/RankingPage";

const routes = [
  // error pages
  {
    path: "*",
    element: <Error404Page />,
  },
  {
    path: "/loading",
    element: <LoadingPage />,
  },
  // member pages
  {
    path: "/",
    element: <LandingPage />,
  },
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/signup",
    element: <SignUpPage />,
  },
  {
    path: "/find-password",
    element: <PasswordFind />,
  },
  {
    path: "/user/:string",
    element: <UserPage />,
  },
  {
    path: "/user/:string/update",
    element: <UserInfoChangePage />,
  },
  // board page
  {
    path: "/board",
    element: <BoardPage />,
  },
  {
    path: "/board/:int",
    element: <BoardDetailPage />,
  },
  // Stastics page
  {
    path: "/analysis",
    element: <AnalysisPage />,
  },
  {
    path: "/ranking",
    element: <RankingPage />,
  },
  // AI Func
  {
    path: "/record-meal",
    element: <RecordMeal />,
  },
  {
    path: "/drawing",
    element: <DrawingPage />,
  },
  {
    path: "/janban",
    element: <JanbanPresentPage />,
  },
  // Testing
  {
    path: "/test/test",
    element: <TestRoutingPage />,
  },
  {
    path: "/test/test/menus",
    element: <MenusTestPage />,
  },
  {
    path: "/test/test/leftovers",
    element: <LeftoversTestPage />,
  },
  {
    path: "/abcgame",
    element: <ABCGamePage />,
  },
];

const router = createBrowserRouter(routes);

export default router;
