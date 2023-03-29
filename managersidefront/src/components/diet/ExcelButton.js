import { Button } from "@mui/material"

function ExcelButton(params) {
    const styleForButton = {
        color: '#000000',
        fontWeight: 1000,
        border: '2px solid black',
        background: '#e5e5e5'
    }
    
    return (
        <>
        <Button style={styleForButton} variant="outlined" size="large">
            식단 일괄 등록
        </Button>
        <br />
        <br />
        </>
    )
}

export default ExcelButton