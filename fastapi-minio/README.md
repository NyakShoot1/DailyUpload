# FastAPI MinIO Photo Upload Service

This service provides API endpoints for uploading and managing photos using MinIO as storage. It supports both single and multiple photo uploads with detailed response handling.

## Features

- Single and multiple photo upload support
- Automatic file type validation (images only)
- Unique filename generation with UUID
- Detailed upload status reporting
- File listing and deletion capabilities
- Request logging middleware
- Error handling with detailed responses

## Prerequisites

- Python 3.8+
- MinIO Server running locally or remotely
- Poetry or pip for dependency management
- Docker and Docker Compose (optional, for containerized setup)

## Setup

1. Install dependencies:
```bash
pip install -r requirements.txt
```

2. Configure environment variables in `.env` file:
```env
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin
MINIO_ENDPOINT=localhost:9000
MINIO_BUCKET=photos
```

3. Start MinIO server (if using Docker):
```bash
docker-compose up -d
```

4. Run the application:
```bash
# From the app directory
uvicorn main:app --reload --port 8000
```

## API Endpoints

### POST /upload
Upload a single photo to MinIO storage.
- Request: Multipart form data with 'file' field containing the image
- Response: JSON with upload details
```json
{
    "message": "File uploaded successfully",
    "filename": "uuid-filename.ext",
    "bucket": "photos"
}
```

### POST /upload/multiple
Upload multiple photos simultaneously.
- Request: Multipart form data with multiple 'files' fields
- Response: Detailed JSON with upload status for each file
```json
{
    "uploaded_files": [
        {
            "original_name": "photo1.png",
            "stored_name": "uuid-filename.png",
            "content_type": "image/png",
            "size": 12345
        }
    ],
    "errors": [],
    "total_uploaded": 1,
    "total_failed": 0,
    "bucket": "photos"
}
```

### GET /files
List all files in the storage bucket.
- Response: JSON array of filenames
```json
{
    "files": ["file1.jpg", "file2.png"]
}
```

### DELETE /files/{filename}
Delete a specific file from storage.
- Response: Success message
```json
{
    "message": "File deleted successfully"
}
```

## Error Handling

The API uses standard HTTP status codes:
- 200: Successful operation
- 207: Partial success (for multiple uploads where some files succeeded and others failed)
- 400: Bad request (invalid file type, no files provided)
- 404: File not found
- 500: Server error (MinIO operation errors)

Error responses include detailed messages to help identify the issue.

## Development

The application includes request logging middleware that logs:
- HTTP method
- Request path
- Response status code
- Request processing time

Logs are formatted as:
```
YYYY-MM-DD HH:MM:SS,ms - INFO - Method: POST Path: /upload Status: 200 Duration: 0.15s
```

## Docker Support

The project includes Docker support through docker-compose:
1. MinIO server runs on port 9000
2. MinIO console available on port 9001
3. FastAPI application runs on port 8000

## Security Notes

- The service validates file types to ensure only images are uploaded
- Each file is stored with a unique UUID-based filename
- Sensitive configuration is handled through environment variables
- MinIO credentials should be properly secured in production

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request
