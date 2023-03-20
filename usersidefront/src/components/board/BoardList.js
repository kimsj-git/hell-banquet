import { Card, Grid } from "@mui/material"
import styled from "styled-components"
import angryJenkins from "../../assets/images/angryJenkins.png"

function BoardList(params) {

    const lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum porta odio eros, eget dignissim felis egestas vitae. Mauris sit amet est nec eros accumsan eleifend. Etia"

    const articles = [
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},

    ]

    return (
        <>
            {articles.map(article => {
                return (
                    <Card>
                        <Grid container>
                            <Grid item xs={4}>
                                <JanvanFace src={article.src} alt={article?.name} />
                            </Grid>
                            <Grid item xs={8}>
                                {article.content}
                            </Grid>

                        </Grid>
                    </Card>
                )
            })}
        </>
    )
}

const JanvanFace = styled.img`
    width: 100%
`


export default BoardList