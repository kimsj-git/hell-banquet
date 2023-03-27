import { useState } from 'react'

import { MoreHoriz } from "@mui/icons-material"
import { Icon } from "@mui/material"

function UpDelModal(params) {
    const { article } = params
    const [ isOpen, setIsOpen ] = useState(false)

    const onMoreClickHandler = (event) => {
        event.preventDefault()
        setIsOpen(!isOpen)
    }

    const moreButtonStyle = {
        position: 'absolute',
        top: '10px',
        right: '10px',
    }

    const dropBox = 
    <div style={moreButtonStyle}>
        <div onClick={onMoreClickHandler} >hello</div>
        <div onClick={onMoreClickHandler} >hello</div>
    </div>


    return (
        <>
        {localStorage.userId !== article?.author 
        ? isOpen 
            ? dropBox
            : <Icon onClick={onMoreClickHandler} component={MoreHoriz} style={moreButtonStyle} />
        : <></>}
        </>
    )
}

export default UpDelModal