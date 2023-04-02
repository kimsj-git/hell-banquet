import Phaser from "phaser";
import { Scene1 } from "./Scene1";
import { Scene2 } from "./Scene2";
const config = {
  type: Phaser.AUTO,
  parent: "phaser-container",
  scale: {
    mode: Phaser.Scale.HEIGHT_CONTROLS_HEIGHT,
    width: 300,
    height: 700,
  },
  scene: [Scene1, Scene2],
};

export default new Phaser.Game(config);
