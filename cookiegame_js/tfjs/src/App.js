import React, { useRef, useEffect } from "react";
import * as tf from "@tensorflow/tfjs";
import * as facemesh from "@tensorflow-models/facemesh";

const App = () => {
  const videoRef = useRef(null);
  const canvasRef = useRef(null);

  useEffect(() => {
    const loadModel = async () => {
      const model = await facemesh.load();

      const detectFaces = async () => {
        const predictions = await model.estimateFaces(videoRef.current, false);

        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        predictions.forEach((prediction) => {
          prediction.scaledMesh.forEach((point) => {
            const [x, y] = point;
            ctx.beginPath();
            ctx.arc(x, y, 1, 0, 2 * Math.PI);
            ctx.fillStyle = "red";
            ctx.fill();
          });
        });

        requestAnimationFrame(detectFaces);
      };

      detectFaces();
    };

    if (navigator.mediaDevices.getUserMedia) {
      navigator.mediaDevices
        .getUserMedia({ video: true })
        .then((stream) => {
          videoRef.current.srcObject = stream;
          videoRef.current.play();
          loadModel();
        })
        .catch((error) => {
          console.error("Error accessing media devices.", error);
        });
    }
  }, []);

  return (
    <div>
      <video ref={videoRef} width="640" height="480" />
      <canvas ref={canvasRef} width="640" height="480" />
    </div>
  );
};

export default App;
