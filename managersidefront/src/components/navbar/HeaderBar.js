import styled from "styled-components"
import { LinkDecoNone } from "../common"

function HeaderBar() {
    return (
        <NavbarContainer>
            <LinkDecoNone to={'/'}>
                <Typo >Logo</Typo>
            </LinkDecoNone>
        </NavbarContainer>
    )
}

const NavbarContainer = styled.div`
    width: 100vw;
    background: #990000;

    padding: 10px 0px 20px 10px;
`

const Typo = styled.span`
    font-size: 24px;
    font-weight: 1000;
`

export default HeaderBar