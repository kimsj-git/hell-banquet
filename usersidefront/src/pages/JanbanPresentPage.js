import { LinkDecoNone, LogedPageTemplate } from "../components/common"

import styled from "styled-components"
import staticJanban from "../assets/images/janban.png"
import { Icon } from "@mui/material"
import { Gesture, Create } from "@mui/icons-material"

function JanbanPresentPage() {
    return (
        <>
        <LogedPageTemplate />
        <JanbanImg src={staticJanban} />
        <LinkDecoNone to={'/drawing'}>
        <IconBox >
            <StyledGestureIcon component={Gesture} style={{width: '70%', height: '70%'}}/>
            <StyledCreateIcon component={Create} style={{width: '50%', height: '50%'}}/>
        </IconBox>
        </LinkDecoNone>
        </>
    )
}

const JanbanImg = styled.img`
    width: 90%;
    background: rgb(211, 188, 240);

    margin: 5% 5% 0% 5%;
    border-radius: 20px;
`

const IconBox = styled.div`
    position: relative;
    width: 390px;
    height: 390px;
    background: rgba(191, 192, 187, 0.5);

    margin: 5% 5% 25% 5%;
    border-radius: 20px;
`
const styleForIcons = `
    font-size: 20rem; 
    position: absolute;
    top: 50%;
    left: 50%;
`

const StyledGestureIcon = styled(Icon)`
    ${styleForIcons}
    
    transform: translate(-70%, -50%);
`
const StyledCreateIcon = styled(Icon)`
    ${styleForIcons}
    color: #999999;
    transform: translate(0%, -80%);
`
export default JanbanPresentPage