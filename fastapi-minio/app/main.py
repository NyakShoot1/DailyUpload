from fastapi import FastAPI, UploadFile, HTTPException, File
from fastapi.responses import JSONResponse
from minio.error import S3Error
import uuid
import config
from minio_client import get_minio_client, ensure_bucket_exists
import logging
from fastapi.middleware.cors import CORSMiddleware
import time
from io import BytesIO
from typing import List

# Настройка логирования
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = FastAPI()
minio_client = get_minio_client()

# Добавляем middleware для логирования запросов
@app.middleware("http")
async def log_requests(request, call_next):
    start_time = time.time()
    response = await call_next(request)
    process_time = time.time() - start_time
    logger.info(
        f"Method: {request.method} Path: {request.url.path} "
        f"Status: {response.status_code} Duration: {process_time:.2f}s"
    )
    return response

@app.on_event("startup")
async def startup_event():
    ensure_bucket_exists(minio_client, config.MINIO_BUCKET)

@app.post("/upload")
async def upload_photo(file: UploadFile):
    if not file.content_type.startswith('image/'):
        raise HTTPException(status_code=400, detail="File must be an image")

    try:
        # Генерируем уникальное имя файла
        file_extension = file.filename.split('.')[-1]
        unique_filename = f"{uuid.uuid4()}.{file_extension}"

        # Читаем файл в BytesIO
        file_data = await file.read()
        file_stream = BytesIO(file_data)

        # Загружаем файл в MinIO
        minio_client.put_object(
            bucket_name=config.MINIO_BUCKET,
            object_name=unique_filename,
            data=file_stream,
            length=len(file_data),
            content_type=file.content_type
        )

        return JSONResponse(
            content={
                "message": "File uploaded successfully",
                "filename": unique_filename,
                "bucket": config.MINIO_BUCKET
            },
            status_code=200
        )
    except S3Error as e:
        raise HTTPException(status_code=500, detail=f"Error uploading file to MinIO: {str(e)}")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Unexpected error: {str(e)}")

@app.post("/upload/multiple")
async def upload_multiple_photos(files: List[UploadFile] = File(...)):
    if not files:
        raise HTTPException(status_code=400, detail="No files were uploaded")
    logger.info(f"Received {len(files)} files for upload.")  # Логирование

    uploaded_files = []
    errors = []

    for file in files:
        logger.info(f"Uploading file: {file.filename} of type: {file.content_type}")  # Логируем каждый файл
        if not file.content_type.startswith('image/'):
            errors.append(f"{file.filename} is not an image")
            continue


        try:
            # Генерируем уникальное имя файла
            file_extension = file.filename.split('.')[-1]
            unique_filename = f"{uuid.uuid4()}.{file_extension}"

            # Читаем файл в BytesIO
            file_data = await file.read()
            file_stream = BytesIO(file_data)

            # Загружаем файл в MinIO
            minio_client.put_object(
                bucket_name=config.MINIO_BUCKET,
                object_name=unique_filename,
                data=file_stream,
                length=len(file_data),
                content_type=file.content_type
            )

            uploaded_files.append({
                "original_name": file.filename,
                "stored_name": unique_filename,
                "content_type": file.content_type,
                "size": len(file_data)
            })

        except Exception as e:
            errors.append(f"{file.filename}")

    response = {
        "uploaded_files": uploaded_files,
        "errors": errors,
        "total_uploaded": len(uploaded_files),
        "total_failed": len(errors),
        "bucket": config.MINIO_BUCKET
    }

    # Если есть ошибки, но есть и успешные загрузки, вернём статус 207 (Multi-Status)
    if errors and uploaded_files:
        return JSONResponse(content=response, status_code=207)
    # Если все файлы загружены успешно
    elif uploaded_files:
        return JSONResponse(content=response, status_code=200)
    # Если все файлы вызвали ошибки
    else:
        raise HTTPException(status_code=400, detail=response)

@app.get("/files")
async def list_files():
    try:
        objects = minio_client.list_objects(config.MINIO_BUCKET)
        files = [obj.object_name for obj in objects]
        return {"files": files}
    except S3Error as e:
        raise HTTPException(status_code=500, detail=f"Error listing files: {str(e)}")

@app.delete("/files/{filename}")
async def delete_file(filename: str):
    try:
        minio_client.remove_object(config.MINIO_BUCKET, filename)
        return {"message": f"File {filename} deleted successfully"}
    except S3Error as e:
        raise HTTPException(status_code=500, detail=f"Error deleting file: {str(e)}")
