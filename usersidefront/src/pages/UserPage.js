import { LogedPageTemplate } from "../components/common"
import { ProfileUserInfo } from "../components/user"
import { OverviewDailyJanban } from "../components/janban"
import { useNavigate } from "react-router-dom"

import styled from "styled-components"
import { Button } from "@mui/material"
import { Container } from "@mui/system"

function UserPage() {
    const navigate = useNavigate()

    const onLogoutHandler = async () => {
        // 로그아웃과 관련된 await request가 들어가야함
        navigate('/')
        alert('로그아웃!')
        localStorage.clear()
      }

    return (
        <>
        <LogedPageTemplate />
        <Container sx={styleForContainer} >
            <JanbanSection>
                <OverviewDailyJanban />
            </JanbanSection>
            <UserInfoSection to='/'>
                <ProfileUserInfo />
            </UserInfoSection>
            <StatisticsSection>
                여긴 통계가 들어가야해요    
            </StatisticsSection> 
            <Button onClick={onLogoutHandler} color='error' variant="contained" style={{zIndex: 0, marginBottom: 100, width: '30%'}}>
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

const JanbanSection = styled.section`
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