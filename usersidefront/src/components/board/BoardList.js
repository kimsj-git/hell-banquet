import angryJenkins from "../../assets/images/angryJenkins.png"
import BoardListItem from "./BoardListItem"

import { useRef, useEffect, useState } from "react"

function BoardList() {

    const lorem = "Lorem ipsuetur adipiscing elit. Vestibulum porta odio eros, eget dignissim felis egestas vitae. Mauris sit amet est nec eros accumsan eleifend. Etia".slice(0, 100)

    const [articles, setArticles] = useState([
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
        {content: lorem, src: angryJenkins},
    ])
    
    const articleListRef = useRef(null);
    useEffect(() => {
        const observerOptions = {
            root: null,
            rootMargin: '100px',
            threshold: 0.5
        };
        
        const observer = new IntersectionObserver(entries => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    // article이 2배씩 증식함
                    console.log('Article is now visible!', entry.target);
                    setArticles(articles.flatMap(article => [article, article]))
                }
            });
        }, observerOptions);
        
        observer.observe(articleListRef.current.lastChild);

        return () => observer.disconnect();
    }, [articles]);

    return (
        <div ref={articleListRef} >
            {articles.map((article, index) => {
                return (
                    <BoardListItem article={article} index={index} key={index} />
                )
            })}
        </div>

    )
}



export default BoardList