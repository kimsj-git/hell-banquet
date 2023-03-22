import styled from "styled-components"

import { ArticleDetail } from "../components/board"
import BoardOption from "../components/board/BoardOption"
import { LogedPageTemplate } from "../components/common"

function BoardDetailPage() {
    return (
        <>
            <Vr />
            <LogedPageTemplate />
            <ArticleDetail />
            <BoardOption />
        </>
    )
}

const Vr = styled.div`
    position: absolute;
    top: 0;
    left: 20%;

    width: 10px;
    height: 100%;
    background: black;

    z-index: 0;
    parent: relative;

`

export default BoardDetailPage