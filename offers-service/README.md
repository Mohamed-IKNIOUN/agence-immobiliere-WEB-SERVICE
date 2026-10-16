# Offers Service

The Offers service manages user bids, price negotiations, and offer status updates for the real estate platform.

## Tech Stack
- **Language:** Python
- **Framework:** FastAPI
- **Server:** Uvicorn

## Getting Started Locally

1. Open your terminal and navigate to this folder:
   ```bash
   cd offers-service

2. Create and activate a virtual environment:

    ```bash
    python -m venv venv
    
    # On Windows:
    venv\Scripts\activate
    
    # On macOS/Linux:
    source venv/bin/activate

3. Install dependencies:

    ```bash
    pip install -r requirements.txt

4. Run the development server:

    ```bash
    uvicorn app.main:app --reload --port 8000

5. Open your browser and visit http://localhost:8000/docs to test the interactive Swagger UI.


## Docker Instructions

To build and run this service inside a Docker container:

    ```bash
    docker build -t offers-service .
    docker run -p 8000:8000 offers-service