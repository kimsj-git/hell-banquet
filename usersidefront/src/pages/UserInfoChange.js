
import styled from "styled-components";
import { LogedPageTemplate, FormWithGrid } from "../components/common"

function UserInfoChange() {

    const userInfo = [
        {id: "userId", target:'', setTarget: 'setInputID', focus: true, type: "id"},
        {id: "password", target: 'inputPassword', setTarget: 'setInputPassword', type: "password"},
        {id: "nickname", target:'', setTarget: '', disabled: true, type: ""},
        {id: "email", target: '', setTarget: '', type: ""},
        {id: "group", target:'', setTarget: '', type: ""},
    ]

    const temp_user_info = Object.keys(userInfo).reduce((acc, key) => {
        acc[userInfo[key].id] = userInfo[key].target;
        return acc;
      }, {});

    const onTypingHandler = (e) => {
        for (const key in userInfo) {
            const option = userInfo[key];
            if (e.target.id === option.id) {
                option.setTarget(e.target.value);
                break;
            }
        }
    };

    const onClickHandler = (e) => {
        e.preventDefault()
        // 제대로된 request로 바꿔야 함
        axios_test()
        // console.log(inputID, inputPassword, temp_user_info)
    }

    async function axios_test() {
        console.log(temp_user_info)
        // await login(
        //     temp_user_info,
        //     (data) => {
        //         alert(data.data)
        //         navigate('/')
        //     },
        //     (err) => console.log(err)
        // )
    }

    return (
        <>
            <LogedPageTemplate />
            <UserInfoSection>
                <UserInfoBox>
                    {FormWithGrid({option: userInfo, onClickHandler: onClickHandler, onTypingHandler: onTypingHandler})}
                </UserInfoBox>
            </UserInfoSection>
        </>
    )
}

const UserInfoBox = styled.div`
    display: flex;
    flex-direction: column;

    width: 90%;
    height: auto;
    margin: 5% 5% 5% 5%;
    border-radius: 20px;
    background: rgb(255, 255, 255, 0.4);
`

const styleForSection = `
    width: 100%;
    background: #E5E5E5;

    margin: 15px 0px 15px 0px;
    border-radius: 30px;

    display: flex;
    justify-content: space-between;
`

const UserInfoSection = styled.section`
    ${styleForSection}
    height: auto;
    background: #D0BCFF;

`

export default UserInfoChange