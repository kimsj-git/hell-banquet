FROM python:3.9.7

WORKDIR /app

COPY requirements.txt /app

RUN apt-get update && apt-get install -y libgl1-mesa-glx
RUN pip3 install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cpu
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

COPY . /app

EXPOSE 5000

CMD ["uvicorn", "main:app", "--proxy-headers", "--host", "0.0.0.0", "--port", "5000", ssl_keyfile="/etc/letsencrypt/live/j8a802.p.ssafy.io/privkey.pem", ssl_certfile="/etc/letsencrypt/live/j8a802.p.ssafy.io/fullchain.pem"]
