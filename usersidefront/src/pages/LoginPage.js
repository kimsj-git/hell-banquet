import { LoginForm } from "../components/login"
import { LinkDecoNone } from "../components/common"

import styled from "styled-components"

function LoginPage() {

    const logInOptions = {
        signUp: {name: "회원가입", url: "/signup"},
        idFind: {name: "아이디 찾기"},
        passwordFind: {name: "비밀번호 찾기"},
    }

    return(
        <Login>
            <LogoSection />
            <LoginForm />
            <LinkDecoNone to={logInOptions.signUp.url}>{logInOptions.signUp.name}</LinkDecoNone>
            <hr />
            <span>아이디 찾기 | 비밀번호 찾기</span>

        </Login>
    )
}

const Login = styled.section`
    display: flex;
    flex-direction: column;

    justify-content: center;
    align-items: center;

    width: 100%;
    height: 932px;

`

const LogoSection = styled.section`
    width: 60%;
    height: 130px;

    background: #B8DDFF;
`

export default LoginPage