import angryJenkins from "../../assets/images/angryJenkins.png"
import BoardListItem from "./BoardListItem"

function BoardList() {

    const lorem = "Lorem ipsuetur adipiscing elit. Vestibulum porta odio eros, eget dignissim felis egestas vitae. Mauris sit amet est nec eros accumsan eleifend. Etia".slice(0, 100)

    const articles = [
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
    ]

    return (
            articles.map((article, index) => {
                return (
                    <BoardListItem article={article} index={index} key={index} />
                )
            })
    )
}



export default BoardList