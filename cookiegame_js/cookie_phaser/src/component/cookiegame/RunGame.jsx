// import "./component/cookiegame/CookieGame";

// const RunGame = () => {
//   return <div id="phaser-container"></div>;
// };

// export default RunGame;

import Phaser from "phaser";
import { Scene1 } from "./Scene1";
import { Scene2 } from "./Scene2";
import { Scene3 } from "./Scene3";
import { Scene4 } from "./Scene4";
// import { useEffect } from "react";
import { useEffect, useRef } from "react";
import styled from "styled-components";

const RunGame = () => {
  const videoRef = useRef(null);

  useEffect(() => {
    const config = {
      type: Phaser.AUTO,
      parent: "phaser-container",
      scale: {
        mode: Phaser.Scale.HEIGHT_CONTROLS_HEIGHT,
        width: 600,
        height: 600,
      },
      backgroundColor: "#ffffff",
      scene: [Scene1, Scene2, Scene3, Scene4],
    };

    new Phaser.Game(config);

    navigator.mediaDevices
      .getUserMedia({ video: true, audio: false })
      .then((stream) => {
        // 스트림을 비디오에 할당
        videoRef.current.srcObject = stream;
      })
      .catch((error) => {
        console.error("Error accessing media devices.", error);
      });
  }, []);

  return (
    <Container>
      <WebCamContainer>
        <Video ref={videoRef} autoPlay playsInline muted />
      </WebCamContainer>
      <PhaserContainer id="phaser-container" />
    </Container>
  );
};

const Container = styled.div`
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  height: 100%;
`;

const WebCamContainer = styled.div`
  height: 50%;
  width: 100%;
`;

const PhaserContainer = styled.div`
  height: 50%;
`;

const Video = styled.video`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

export default RunGame;
