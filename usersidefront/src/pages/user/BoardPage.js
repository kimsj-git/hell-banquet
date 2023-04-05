import BoardList from "../../components/board/BoardList";
import BoardOption from "../../components/board/BoardOption";
import { LogedPageTemplate } from "../../components/common";

function BoardPage() {
  return (
    <>
      <LogedPageTemplate />
      <BoardList />
      <BoardOption />
    </>
  );
}

export default BoardPage;
