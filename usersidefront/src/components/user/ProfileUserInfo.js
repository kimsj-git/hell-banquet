import { Button, Typography } from "@mui/material"
import styled from "styled-components"
import { LinkDecoNone } from "../common"

function ProfileUserInfo() {
    const tempUserInfo = {
        userId: 'ssafy',
        nickname: '야돈존맛',
        eMail: 'kimjih94@naver.com',
        group: '역삼 멀티캠퍼스'
    }

    return (
        <UserInfoBox>
            {Object.keys(tempUserInfo).map((key) => {
                return (
                    <Typography fontSize={20} key={key} >
                        {key} | {tempUserInfo[key]}
                    </Typography>
                )
            })}
            <LinkDecoNone to={'/user/ssafy/update'} style={{alignSelf: 'end'}} >
                <Button variant="contained">정보 수정하기</Button>
            </LinkDecoNone>
        </UserInfoBox>
    )
}

const UserInfoBox = styled.div`
    display: flex;
    flex-direction: column;

    width: 90%;
    height: 80%;
    margin: 5% 5% 5% 5%;
    border-radius: 20px;
    background: rgb(255, 255, 255, 0.4);
`

export default ProfileUserInfo