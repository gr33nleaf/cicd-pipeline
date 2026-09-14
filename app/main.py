from fastapi import FastAPI
from fastapi.responses import HTMLResponse
import random

APP_VERSION = "1.0.0"

app = FastAPI()

@app.get("/", response_class=HTMLResponse)
def home():
    return f"""
    <html>
        <body>
            <h1>Random Number App</h1>
            <p>Version: {APP_VERSION}</p>
            <p><a href="/random">Get random number</a></p>
            <p><a href="/random?count=5">Get 5 random numbers</a></p>
        <body>
    </html>
    """

@app.get("/random")
def get_random(count: int = 1):
    if count ==1:
        return {"version": APP_VERSION, "number": random.randint(1, 100)}
    return {"version": APP_VERSION, "numbers": [random.randint(1, 100) for _ in range(count)]}