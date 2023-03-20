import { useNavigate } from "react-router-dom"

import styled from "styled-components"
import NotFoundImage  from "../assets/images/404.png"

function Error404Page() {
    const navigate = useNavigate()

    return (
        <The404Img onClick={() => navigate(-1)} src={NotFoundImage}/>
    )
}

const The404Img = styled.img`
    width: 100%;
    height: 100%;
`

export default Error404Page