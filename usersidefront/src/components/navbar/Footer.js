import NavbarItem from "./NavbarItem"

import { useState, } from "react"
// import { useCookies } from 'react-cookie'
// import { Grid } from '@mui/material'

function Navbar() {
    // const [ cookie, ] = useCookies(["userInfo"])
    const [ userId, ] = useState(localStorage.userId)

    const navlist = [
        {name: "게시판", url: "/board",},
        {name: "식사 기록하기", url: "/siksa",},
        // 나중에 동적인 이름으로 변경할 것
        {name: "프로필", url: `/useri/ssafy`,},
    ]

    return (
        <footer>

        </footer>
    )
}


// const Nav ={
//     display: 'flex',
//     justifyContent: 'space-between',
//     alignItems: 'center',
//     paddingTop: '13px',
//     paddingBottom: '13px',
// }


export default Navbar