import { useState } from "react"

// import ArticleOptionItem from "./ArticleOptionItem"

import { Icon, IconButton } from "@mui/material"
import { ThumbDown, ThumbUp, Comment,  } from "@mui/icons-material"

function ArticleOption(params) {
    const { article } = params
    // const { likeCount, dislikeCount, commentCount } = article
    // 차후에 props에서 넘어온 값으로 변경할 것
    // 0 좋아요 1 싫어요 -1 몰라유
    const [isLiked, setIsLiked] = useState(-1)
    const [likeCount, setLikeCount] = useState(article.likeCount)
    const [dislikeCount, setDislikeCount] = useState(article.dislikeCount)
    const [commentCount, setCommentCount] = useState(article.commentCount)

    const handleClickCount = (event) => {
        event.preventDefault()
        const target = event.target?.id
        if (! target || isLiked === target) {
            return
        }
        const {onClick, num, otherHand } = articleOptions[target]
        const otherHadnOption = articleOptions[otherHand]
        if (isLiked !== -1) {
            otherHadnOption.onClick(otherHadnOption.num - 1)
        } 
        setIsLiked(target)
        onClick(num + 1)
    }

    const articleOptions = [
        {id: "like", iconName: ThumbUp, num: likeCount, onClick: setLikeCount, otherHand: 1},
        {id: "hate", iconName: ThumbDown, num: dislikeCount, onClick: setDislikeCount, otherHand: 0},
        {id: "comments", iconName: Comment, num: commentCount, onClick: setCommentCount}
    ]



    return (
        articleOptions.map((option, index) => {
            const {iconName, num, } = option
            const styleForIcon = {
                marginRight: 10,
                color: isLiked === index.toString() ? '#990000' : '#000000',
            }
            return (
                <IconButton onClick={handleClickCount} key={index} id={index} >
                    <Icon component={iconName} style={styleForIcon} />
                    {num}
                </IconButton>
                )
        })
    )
            
}


export default ArticleOption