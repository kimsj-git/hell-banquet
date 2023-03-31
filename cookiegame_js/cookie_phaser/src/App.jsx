import "./assets/DotFont.css";
import styled from "styled-components";
import GameMain from "./assets/game_main.gif";
import { useState } from "react";
import "./component/cookiegame/CookieGame";

function App() {
  const [isGameStart, setIsGameStart] = useState(false);

  const handlesetIsGameStart = () => {
    setIsGameStart(true);
  };

  return (
    <ContainerGame>
      {!isGameStart && <MainImage src={GameMain} />}
      {!isGameStart && (
        <TitleContainer>
          <div>즐거운</div>
          <div>쿠키게임^^</div>
        </TitleContainer>
      )}
      {isGameStart ? (
        <div id="phaser-container"></div>
      ) : (
        <ContainerButton>
          <GameButton onClick={handlesetIsGameStart}>게임 시작</GameButton>
        </ContainerButton>
      )}
    </ContainerGame>
  );
}

// function App() {
//   const [isGameStart, setIsGameStart] = useState(false);

//   const handlesetIsGameStart = () => {
//     setIsGameStart(true);
//   };

//   return (
//     <ContainerGame>
//       {!isGameStart && <MainImage src={GameMain} />}
//       {!isGameStart && (
//         <TitleContainer>
//           <div>즐거운</div>
//           <div>쿠키게임^^</div>
//         </TitleContainer>
//       )}
//       {isGameStart ? (
//         <div id="phaser-container"></div>
//       ) : (
//         <ContainerButton>
//           <GameButton onClick={handlesetIsGameStart}>게임 시작</GameButton>
//         </ContainerButton>
//       )}
//     </ContainerGame>
//   );
// }

const ContainerGame = styled.div`
  display: flex;
  align-items: center;
  height: 100vh;
`;

const MainImage = styled.img`
  width: 100%;
`;

const ContainerButton = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  top: 65%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const TitleContainer = styled.div`
  font-family: "DotFont";
  font-size: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  top: 18%;
  left: 52%;
  transform: translate(-50%, -50%);
  background: linear-gradient(
    to right,
    red,
    orange,
    yellow,
    green,
    blue,
    indigo,
    violet
  );
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  -webkit-text-stroke-width: 3px;
  -webkit-text-stroke-color: black;
`;

const GameButton = styled.button`
  font-family: "DotFont";
  font-size: 50px;
  background-color: red;
`;

export default App;
