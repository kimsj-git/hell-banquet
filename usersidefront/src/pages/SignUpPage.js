import { SignUpForm } from "../components/signup"

import styled from "styled-components"
import { LinkDecoNone } from "../components/common"

function SignUpPage() {

    return(
        <Login>
            <LinkDecoNone to='/login' style={styleForLogo}>
                <LogoSection />
            </LinkDecoNone>
            <SignUpForm />
        </Login>
    )
}

const Login = styled.div`
    display: flex;
    flex-direction: column;

    justify-content: center;
    align-items: center;

    width: 100%;
    height: 932px;

`

const LogoSection = styled.section`
    width: 100%;
    height: 100%;

    background: #B8DDFF;
`

const styleForLogo = {
    width: "60%",
    height: 130,
    background: '#B8DDFF',
}

export default SignUpPage