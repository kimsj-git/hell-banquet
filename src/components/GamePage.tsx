import React from "react";
import GameBoard from "./GameBoard";
import styled from "styled-components";

const GamePage: React.FC = () => {
  return (
    <GameBox>
      <GameBoard />
    </GameBox>
  );
};

const GameBox = styled.div`
  margin: 2rem;
`;

export default GamePage;
