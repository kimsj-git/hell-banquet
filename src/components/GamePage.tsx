import React, { PropsWithChildren } from "react";

interface GamePageProps {
  // props의 타입 정의
}

const GamePage: React.FC<GamePageProps> = (
  props: PropsWithChildren<GamePageProps>
) => {
  // 컴포넌트 내용 작성
  return <div>Hello TypeScript</div>;
};

export default GamePage;
