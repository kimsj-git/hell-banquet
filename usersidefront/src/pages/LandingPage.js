import { Container } from "@mui/system"
import styled from "styled-components"

import {LogedPageTemplate, } from "../components/common"

function LandingPage() {
    /*
        첫 방문여부를 확인하고 첫 방문 시에는 안내 적용
    */
   
    return(
        <>
        <LogedPageTemplate />
        <Container>
            <JanvanSection />
            <UserInfoSection />
            <RecommendArticleSection />
        </Container>
        </>
    )
}

const styleForSection = `
    width: 100%;
    height: 200px;
    background: #E5E5E5;

    margin: 15px 0px 15px 0px;
    border-radius: 30px;

    display: flex;
    justify-content: space-between;
`

const JanvanSection = styled.section`
    ${styleForSection}
`
const UserInfoSection = styled.section`
    ${styleForSection}
    height: 280px;
    background: #D0BCFF;

`
const RecommendArticleSection = styled.section`
    ${styleForSection}
    height: 240px;

`

export default LandingPage