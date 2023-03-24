import { useState } from "react"

import { Typography } from "@mui/material"
import styled from "styled-components"

function DetailUserInfo() {
    const [userInfo, setUserInfo] = useState({
        id: 0,
        userId: 'ssafy',
        name: '야돈존맛',
        email: 'kimjih94@naver.com',
        group: '역삼 멀티캠퍼스',
        regTime: '뭐 대충 23일',
    })

    const userInfoForm = {
        id: false,
        userId: 'ID',
        name: '이름',
        email: '이메일',
        regTime: '가입일자',
        group: '소속 그룹',
    }

    return (
        <UserInfoBox>
            {Object.keys(userInfo).map((key) => {
                if (userInfoForm[key]) {
                    // 그리드로 변경!!!
                    return (
                        <>
                        <Typography style={{...styleForTypo, width: '20%',}} fontSize={25} key={key} >
                            {userInfoForm[key]}
                        </Typography>
                        <Typography style={{...styleForTypo, width: '60%'}} fontSize={25} key={key} >
                            {userInfo[key]}
                        </Typography>
                        
                        </>
                    )
                }
            })}
        </UserInfoBox>
    )
}

const UserInfoBox = styled.div`
    display: flex;
    flex-direction: column;

    width: 90%;
    height: auto;
    margin: 5% 5% 5% 5%;
    border-radius: 20px;
`

const styleForTypo = {
    background: 'rgb(255, 255, 255, 0.4)', 
    margin: '5% 1% 5% 1%',
    padding: '5% 2% 5% 2%',
    borderRadius: 20,
}

export default DetailUserInfo