/* eslint-disable */

import { useEffect } from "react"
import { useNavigate } from "react-router-dom"
// import { test } from "../../api/board"
import { StickyFooter, StickyHeader } from "../navbar"

function LogedPageTemplate() {
    const navigate = useNavigate()
    // useEffect(()=> {
    //     if (localStorage.getItem('auth') === null) {
    //         localStorage.clear()
    //         navigate('/login')
    //         alert('로그인이 안돼있네요...?')
    //     }

    // }, [navigate])

    useEffect(()=> {
        localStorage.setItem('userId', 'string')

    }, [])

    return (
        <>
            <StickyHeader />
            <StickyFooter />
        </>
    )
}

export default LogedPageTemplate

/* eslint-enable */
