import React, { PropsWithChildren } from "react";

interface RoadSingleBoxProps {
  // props의 타입 정의
}

const RoadSingleBox: React.FC<RoadSingleBoxProps> = (
  props: PropsWithChildren<RoadSingleBoxProps>
) => {
  // 컴포넌트 내용 작성
  return <div>Hello TypeScript</div>;
};

export default RoadSingleBox;
