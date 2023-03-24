import { Button } from "@mui/material"
import { useState } from "react"
import { updateUserInfo } from "../../api/member"

function UpdateUser(params) {
    const [isUpdating, setIsUpdating] = useState(false)
    const { userInfo } = params

    async function update() {
        if (isUpdating) {
            await updateUserInfo(
                userInfo,
                (data) => {
                    console.log(data)
                }, 
                (err) => console.log(err)
            )
        } else {
            setIsUpdating(true)
        }
    }

    return (
        <Button variant="contained" onClick={update} >
            {isUpdating ? '수정 완료' : '정보 수정'}
        </Button>
    )
}


export default UpdateUser