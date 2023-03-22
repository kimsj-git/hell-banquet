import { LogedPageTemplate } from "../components/common"

import styled from "styled-components"
import { Button } from "@mui/material"
import { Container } from "@mui/system"
import { ProfileUserInfo } from "../components/user"

function UserPage() {
    return (
        <>
        <LogedPageTemplate />
        <Container sx={styleForContainer} >
            <JanvanSection />
            <UserInfoSection to='/'>
                <ProfileUserInfo />
            </UserInfoSection>
            <StatisticsSection /> 
            <Button variant="contained" color="error" style={{zIndex: -1, marginBottom: 100, width: '30%'}}>
                로그아웃
            </Button>
        </Container>
        </>
    )
}

const styleForContainer = {
    display: 'flex', 
    flexDirection: 'column', 
    justifyContent: 'center', 
    alignItems:'center',
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
    height: 240px;
    background: #D0BCFF;

`
const StatisticsSection = styled.section`
    ${styleForSection}

`

export default UserPage