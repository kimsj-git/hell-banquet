// import { useState, useEffect } from "react"

import { Icon } from "@mui/material"

function ArticleOptionItem(params) {
    const { iconName, num } = params
    // const [ count, setCount ] = useState(num)
    // const [ isClicked, setIsClicked ] = useState(false)

    
    // function onOptionClick() {
    //     if (isClicked) {
    //         setCount(num)
    //         setIsClicked(false)
    //     } else {
    //         setCount(num + 1)
    //         setIsClicked(true)
    //     }
    // }
    
    // useEffect(() => {
        
    //     return (
    //         console.log(num)
    //     )
    // }, [num])

    return (
        <span >
            <Icon component={iconName} />
            {num}
        </span>
    )
}

export default ArticleOptionItem