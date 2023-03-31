import { useState } from "react"
import { useEffect } from "react"
import { getTodayArticle } from "../../api/board"

function RecommendAritcle() {
    const dummy = {writer: '관리자', content: "아직 오늘 작성된 게시글이 없어요"}
    const [todayArticle, setTodayArticle] = useState(dummy)

    const date = new Date().toISOString().split('T')[0]

    useEffect(() =>{
        getTodayArticle(
            date,
            (data) => {
                return data.data
            },
            (err) => console.log(err)
        ).then((data) => setTodayArticle(data))
    }, [date])

    return (
        <div>
            {todayArticle?.content}
        </div>
    )
}

export default RecommendAritcle