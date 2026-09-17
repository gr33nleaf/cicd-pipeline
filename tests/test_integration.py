import httpx

BASE_URL = "http://localhost:8000"

def test_root_endpoint():
    response = httpx.get(f"{BASE_URL}/")
    assert response.status_code == 200

def test_random_endpoint_single():
    response = httpx.get(f"{BASE_URL}/random")
    assert response.status_code == 200
    data = response.json()
    assert "number" in data or "numbers" in data

def test_random_endpoint_with_count():
    response = httpx.get(f"{BASE_URL}/random?count=5")
    assert response.status_code == 200
    data = response.json()
    assert "numbers" in data
    assert len(data["numbers"]) == 5

def test_random_numbers_in_range():
    response = httpx.get(f"{BASE_URL}/random?count=10")
    assert response.status_code == 200
    data = response.json()
    for num in data["numbers"]:
        assert 1 <= num <= 100