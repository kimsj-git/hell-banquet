import { useState }  from 'react'
import { useNavigate } from 'react-router-dom'
import { useCookies } from 'react-cookie'
import { Box, Container, Grid, Button, TextField } from '@mui/material'

function LoginForm () {
    const navigate = useNavigate()
    const [ cookie, setCookie ] = useCookies(['userInfo'])
    const [ inputID, setInputID ] = useState()
    const [ inputPassword, setInputPassword ] = useState()

    const temp_user_info = {
        userId: inputID, 
        password: inputPassword,
    }

    const onTypingHandler = (e) => {
        switch (e.target.id) {
            case 'outlined-id':
                setInputID(e.target.value)
                break
            case 'outlined-password':
                setInputPassword(e.target.value)
                break
            default:
                // nothing
        }
    }

    async function axios_test() {
        await fetch('http://i8a703.p.ssafy.io:8000/login', {
            method: 'POST',
            body: JSON.stringify(temp_user_info),
            headers: {
                "Content-Type": `application/json`,
            }
        })
        .then(result => {
            const headers = result.headers
            setCookie(
                'userInfo',
                {
                    user_id: temp_user_info.userId,
                    jwt_token: headers.get('Authorization'),
                    refresh_token: headers.get('refreshToken'),
                },
                {path: '/'}
            )
            console.log(result)
            console.log(cookie)
            navigate("/")
        })
        .catch(error => {
            console.log(error)
            alert('다시 시도해주세요')
        })
    }

    const onClickHandler = (e) => {
        e.preventDefault()
        axios_test()

        // 로그인이 성공하면 home으로 navigate
        // navigate("/")

    }

    return (
        <Container fixed>
            <Box component="form">
                <Grid container spacing={2} style={{padding: '2rem', justifyContent: 'center'}}>
                    <Grid item xs={12}>
                        <TextField onChange={onTypingHandler} id="outlined-id" autoFocus label="ID or E-Mail" fullWidth />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField onChange={onTypingHandler} id="outlined-password" label="Password" type="password" fullWidth />
                    </Grid>
                    <Grid item xs={9}>
                        <Button onClick={onClickHandler} variant="contained" className="submit" style={{height: '3rem', background: "#B8DDFF"}} fullWidth> <b>로그인</b></Button>
                    </Grid>
                </Grid>
            </Box>
        </Container>
    )   
}
export default LoginForm