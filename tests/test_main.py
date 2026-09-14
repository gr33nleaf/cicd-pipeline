from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_home():
    response = client.get("/")
    assert response.status_code == 200
    assert "Random Number App" in response.text

def test_random_single():
    response = client.get("/random")
    assert response.status_code == 200
    data = response.json()
    assert "number" in data
    assert 1 <= data["number"] <= 100

def test_random_multiple():
    response = client.get("/random?count=5")
    assert response.status_code == 200
    data = response.json()
    assert "numbers" in data
    assert len(data["numbers"]) == 5