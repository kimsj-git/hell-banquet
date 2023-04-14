import React, { useState } from "react";
import styled from "styled-components";

interface Problem {
  id: number;
  items: Array<Array<number>>;
  answer: number;
}

interface DestinationProps {
  attribute: number;
  xIndex: number;
  yIndex: number;
}

const GameBoard: React.FC = () => {
  const problem: Problem = {
    id: 1,
    items: [
      [-1, 1, -1, 3, 2, -1],
      [0, 0, 0, 0, 0, 1],
      [0, 0, 0, 0, 0, -1],
      [0, 0, 0, 0, 0, 2],
      [0, 0, 0, 0, 0, -1],
      [0, 0, 0, 0, 0, 3],
    ],
    answer: 3,
  };
  const [boardState, setBoardState] = useState(problem);

  const onClickHandler = (
    event: MouseEvent,
    xIndex: number,
    yIndex: number,
    rotate: number
  ) => {
    event.preventDefault();
    const itemValue = boardState.items[yIndex][xIndex];
    const newBoardState = boardState.items.map((row, rowIndex) =>
      rowIndex === yIndex
        ? row.map((value, columnIndex) =>
            columnIndex === xIndex ? (itemValue === 0 ? rotate : 0) : value
          )
        : row
    );
    setBoardState({ ...boardState, items: newBoardState });
  };

  return (
    <React.Fragment>
      {boardState.items.map((item, yIndex) => {
        return (
          <RowFlexBox key={yIndex}>
            {item.map((rowValue, xIndex) => {
              if (rowValue === 0 || rowValue === 4 || rowValue === 5) {
                return (
                  <EmptyBox attribute={rowValue} key={xIndex}>
                    <LeftTopDiagonal
                      attribute={rowValue}
                      xIndex={xIndex}
                      yIndex={yIndex}
                      onClick={(event: MouseEvent) =>
                        onClickHandler(event, xIndex, yIndex, 4)
                      }
                    />
                    <RightTopDiagonal
                      attribute={rowValue}
                      xIndex={xIndex}
                      yIndex={yIndex}
                      onClick={(event: MouseEvent) =>
                        onClickHandler(event, xIndex, yIndex, 5)
                      }
                    />
                  </EmptyBox>
                );
              } else {
                return (
                  <EmptyBox
                    attribute={rowValue}
                    xIndex={xIndex}
                    yIndex={yIndex}
                    key={xIndex}
                  />
                );
              }
            })}
          </RowFlexBox>
        );
      })}
    </React.Fragment>
  );
};

// function painterFunc(params:number) {
//     if (params === 1) return "red";
//     else if (params === 2) return "blue";
//     else if (params === 3) return "gray";
//     else if (params === -1) return "white";
//     else return "black";
// }

const EmptyBox = styled.div`
  position: relative;
  width: 100px;
  height: 100px;

  background: ${(props: DestinationProps) => {
    const { attribute } = props;
    if (attribute === 1) return "red";
    else if (attribute === 2) return "blue";
    else if (attribute === 3) return "gray";
  }};

  border: solid 1px
    ${(props: DestinationProps) => {
      const { attribute } = props;
      if (attribute === 1) return "red";
      else if (attribute === 2) return "blue";
      else if (attribute === 3) return "gray";
      else if (attribute === -1) return "white";
      else return "black";
    }};
`;

const styleForDiagonal = `
    position: absolute;
    top: 50%;
    left: 0;
    right: 0;
    bottom: 0;

    height: 10px;
    background: black;
    `;

const LeftTopDiagonal = styled.div`
  ${styleForDiagonal}
  transform: rotate(45deg);
  background: ${(props: DestinationProps) => {
    const { attribute } = props;
    if (attribute === 4) return "green";
    else if (attribute === 5) return "white";
    else return "black";
  }};

  z-index: ${(props: DestinationProps) => {
    const { attribute } = props;
    if (attribute === 4) return 1;
    else return 0;
  }};
`;

const RightTopDiagonal = styled.div`
  ${styleForDiagonal}
  transform: rotate(-45deg);
  background: ${(props: DestinationProps) => {
    const { attribute } = props;
    if (attribute === 5) return "green";
    else if (attribute === 4) return "white";
    else return "black";
  }};

  z-index: ${(props: DestinationProps) => {
    const { attribute } = props;
    if (attribute === 5) return 1;
    else return 0;
  }};
`;

const RowFlexBox = styled.div`
  display: flex;
  flex-direction: row;
`;

export default GameBoard;
