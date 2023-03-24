import { deleteUser } from "../../api/member"
import { Button } from "@mui/material"

function DeleteUser(params) {
    const { userInfo } = params

    async function deleteU() {
        console.log(userInfo)
        await deleteUser(
            userInfo,
            (data) => {
                console.log(data)
            }, 
            (err) => console.log(err)
        )
    }

    return (
        <Button variant="contained" color="error" onClick={deleteU}>
            계정 삭제
        </Button>

    )
}

export default DeleteUser