from minio import Minio
from minio.error import S3Error
import config

def get_minio_client():
    return Minio(
        config.MINIO_ENDPOINT,
        access_key=config.MINIO_ROOT_USER,
        secret_key=config.MINIO_ROOT_PASSWORD,
        secure=False
    )

def ensure_bucket_exists(client: Minio, bucket_name: str):
    try:
        if not client.bucket_exists(bucket_name):
            client.make_bucket(bucket_name)
    except S3Error as e:
        raise Exception(f"Error creating bucket: {str(e)}")
