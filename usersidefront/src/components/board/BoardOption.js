import React, { useState } from "react"
import { useNavigate } from "react-router-dom";
import BoardOptionButton from "./BoardOptionButton"
import ArticleCreateModal from "./ArticleCreateModal";

import styled from "styled-components"
import { Create, Search, ArrowBack } from "@mui/icons-material"
import BoardSearchModal from "./BoardSearchModal";


function BoardOption() {
    // 마찬가지로 위치가 fix되어 있는 버튼을 만들어야 한다
    // 여기서 만든 버튼과 코드를 그대로 검색에서 사용할 것
    const navigate = useNavigate()

    function onClickHandler(targetId) {
        setBoardOptions((prevOptions) =>
            prevOptions.map((option) =>
                option.id === targetId ? { ...option, visible: false } : { ...option, visible: true }
            )
        );
    };

    function onClose() {
        setBoardOptions((prevOptions) =>
            prevOptions.map((option) => ({
                ...option,
                visible: true
            }))
        );
    };

    const [boardOptions, setBoardOptions] = useState([
        {id: 2, component: ArrowBack, onClick: () => navigate(-1), visible: true, },
        {id: 0, component: Search, onClick: onClickHandler, visible: true, modal: <BoardSearchModal/>},
        {id: 1, component: Create, onClick: onClickHandler, visible: true, modal: <ArticleCreateModal/>},
    ])

    return(
        <PositionProvider >
            {boardOptions.map(option => {
                if (option.visible) {
                    return (
                        <BoardOptionButton key={option.id} option={option} />
                    )
                } else {
                    return (
                        <React.Fragment key={option.id}  >
                            <ArticleCreateModal isOpen={!option.visible && boardOptions[0].visible} onClose={onClose} />
                            <BoardSearchModal isOpen={!option.visible && boardOptions[1].visible} onClose={onClose} />
                        </React.Fragment>
                    )
                }
            })}
        </PositionProvider>
    )
} 

const PositionProvider = styled.div`
    position: fixed;
    bottom: 80px;
    right: 20px;

    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;


    border-radius: 50px;

`


export default BoardOption