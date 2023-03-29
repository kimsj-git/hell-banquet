from fastapi import FastAPI, File, Form, UploadFile

from fastapi.responses import JSONResponse

from dotenv import load_dotenv
import os
import boto3

import uuid

import shutil

app = FastAPI()

load_dotenv()
ACCESS_KEY = os.environ.get("ACCESS_KEY")
SECRET_KEY = os.environ.get("SECRET_KEY")
BUCKET = os.environ.get("BUCKET")
REGION = os.environ.get("REGION")

client_s3 = boto3.client('s3',
    aws_access_key_id=ACCESS_KEY,
    aws_secret_access_key=SECRET_KEY
)

@app.post("/ai/draw/")
async def draw_is_correct(image: UploadFile = File(), category: str = Form()):
    try:
        file_path = f'{category}.png'
        with open(file_path, 'wb') as buffer:
            shutil.copyfileobj(image.file, buffer)

        result = True
    except:
        result = False

    return JSONResponse(content={"success": result})


@app.post("/ai/janban/")
async def check_janban(image: UploadFile = File()):

    # 아래에 코드 작성

    file_name = str(uuid.uuid4()) + ".png"
    contents = await image.read()

    try:
        client_s3.put_object(
            Body=contents,
            Bucket=BUCKET,
            Key=file_name,
            ContentType='image/png'
        )
        s3_file_path = f'https://leftover-bucket.s3.ap-northeast-2.amazonaws.com/{file_name}'

        return JSONResponse(content={"s3_file_path": s3_file_path})

    except:
        return JSONResponse(content={"s3_file_path": "failed"})