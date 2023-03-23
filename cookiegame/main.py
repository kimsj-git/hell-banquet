from fastapi import FastAPI, WebSocket
import cv2
from game.game import game  # game.py 파일에서 game_logic 함수를 불러옵니다.

app = FastAPI()


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()

    cap = cv2.VideoCapture(0)

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        # game_logic 함수를 호출하여 처리된 프레임과 점수를 얻습니다.
        processed_frame, score = game(frame, cap)

        # 프레임을 JPEG 형식으로 인코딩하고 이진 데이터로 변환
        ret, buffer = cv2.imencode('.jpg', processed_frame)
        frame_binary = buffer.tobytes()

        # 클라이언트에 이진 데이터 전송
        await websocket.send_bytes(frame_binary)

    cap.release()
    cv2.destroyAllWindows()