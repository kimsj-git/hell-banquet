import { LoginForm } from "../components/login"

import styled from "styled-components"
import { LinkDecoNone } from "../components/common"

function LoginPage() {

    const logInOptions = [
        {name: "회원가입", url: "/signup"},
        {name: "비밀번호 찾기", url: "/find-password"},
    ]


    return(
        <Login>
            <LinkDecoNone to='/login' style={styleForLogo}>
                <LogoSection />
            </LinkDecoNone>
            <LoginForm />
            {logInOptions.map(item => {
                return (
                    <div key={item.name}>
                        <br />
                        <LinkDecoNone  to={item.url}>
                            {item.name}
                        </LinkDecoNone>
                    </div>
                )
            })}
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


const styleForLogo = {
    width: "60%",
    height: 130,
    background: '#B8DDFF',
}

export default LoginPage