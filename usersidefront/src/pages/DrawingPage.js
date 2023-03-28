import React, { useRef, useState, useEffect } from 'react';

function DrawingPage() {
  const canvasRef = useRef(null);
  const [isDrawing, setIsDrawing] = useState(false);

  // 캔버스 context 가져오기
  const getCanvasContext = () => {
    const canvas = canvasRef.current;
    return canvas.getContext('2d');
  };

  // 마우스 좌표 계산 함수
  const getMousePosition = (event) => {
    const canvas = canvasRef.current;
    const { left, top } = canvas.getBoundingClientRect();
    const x = event.clientX - left;
    const y = event.clientY - top;
    return { x, y };
  };

  // 그리기 시작
  const startDrawing = (event) => {
    const context = getCanvasContext();
    const { x, y } = getMousePosition(event);
    context.beginPath();
    context.moveTo(x, y);
    setIsDrawing(true);
  };

  // 그리기 중
  const draw = (event) => {
    if (!isDrawing) return;
    const context = getCanvasContext();
    const { x, y } = getMousePosition(event);
    context.lineTo(x, y);
    context.stroke();
  };

  // 그리기 종료
  const stopDrawing = () => {
    setIsDrawing(false);
  };

  useEffect(() => {
    const canvas = canvasRef.current;
    const context = canvas.getContext('2d');

    // 선 스타일 설정
    context.strokeStyle = '#000000'; // 검은색
    context.lineWidth = 2;
    context.lineJoin = 'round';
    context.lineCap = 'round';
  }, []);

  return (
    <canvas
      ref={canvasRef}
      width={430}
      height={932}
      onMouseDown={startDrawing}
      onMouseMove={draw}
      onMouseUp={stopDrawing}
      onMouseOut={stopDrawing}
    />
  );
}

export default DrawingPage;