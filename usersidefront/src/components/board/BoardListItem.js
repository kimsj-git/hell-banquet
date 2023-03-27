import ArticleOption from "./ArticleOption"
import { LinkDecoNone } from "../common"

import styled from "styled-components"
import { Card, Container, Grid, } from "@mui/material"
import { ThumbDown, ThumbUp, Comment } from "@mui/icons-material"

function BoardListItem(params) {
    const { article } = params
    const { likeCount, dislikeCount, commentCount } = article

    const onLikeClickHandler = (event) => {
        console.log(event)
        event.preventDefault()
    }

    const articleOptions = [
        {id: "like", iconName: ThumbUp, num: likeCount, onClick: onLikeClickHandler},
        {id: "hate", iconName: ThumbDown, num: dislikeCount, onClick: onLikeClickHandler},
        {id: "comments", iconName: Comment, num: commentCount, }
    ]

    return (
        <LinkDecoNone to={`/board/${article.id}`} state={article} >
            <ArticleCard style={{display: 'flex', alignItems: 'center'}}>
                <Grid container style={{display: 'flex', alignItems: 'center'}}>
                    <Grid item xs={4}>
                        <JanvanFace src={article.src} alt={article?.id} />
                    </Grid>
                    {/* CSS 적용하셔야 해요 */}
                    <Grid item xs={8}>
                        <Container style={{height: 100}}>{article.content}</Container>
                        <Container style={{display: 'flex', justifyContent: 'space-around'}}>
                            {articleOptions.map(option => {
                                const {iconName, num, id, } = option
                                return (
                                    <span onClick={option?.onClick} key={id} >
                                        <ArticleOption iconName={iconName} num={num} />
                                    </span>
                                )
                            })}
                        </Container>
                    </Grid>
                </Grid>
            </ArticleCard>
        </LinkDecoNone>
    )
}

const ArticleCard = styled(Card)`
    margin: 10px 0px 10px 0px; 
    height: 160px;
`

const JanvanFace = styled.img`
    width: 140px;
    height: 140px;

    margin-left: 0px 10px 0px 10px;
`

export default BoardListItem