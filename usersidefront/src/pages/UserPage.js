import { LogedPageTemplate } from "../components/common"

import styled from "styled-components"

function UserPage() {
    return (
        <>
            <LogedPageTemplate />
            <JanvanSection />
            <UserInfoSection />
            <StatisticsSection /> 
        </>
    )
}

const styleForSection = `
    width: calc(100% - 40px);
    height: 200px;
    background: #E5E5E5;

    padding-top: 15px;
    margin: 25px 20px 25px 20px;

    display: flex;
    justify-content: space-between;
`

const JanvanSection = styled.section`
    ${styleForSection}
    border-radius: 30px;
`
const UserInfoSection = styled.section`
    ${styleForSection}
    height: 240px;

`
const StatisticsSection = styled.section`
    ${styleForSection}

`

export default UserPage