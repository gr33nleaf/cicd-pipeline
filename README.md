# cicd-pipeline

[![CI/CD Pipeline](https://github.com/gr33nleaf/cicd-pipeline/actions/workflows/main.yml/badge.svg)](https://github.com/gr33nleaf/cicd-pipeline/actions/workflows/main.yml)

Proof of Concept: Automated CI/CD pipeline built with GitHub Actions, Kubernetes (Kind), and industry-standard security and quality tools.

---

## Pipeline Overview

### CI – build-and-test (all branches)

| Step | Tool |
|---|---|
| Secret Scan | Gitleaks |
| Unit Tests + Coverage | pytest + pytest-cov |
| Static Analysis (SAST) | SonarCloud |
| Vulnerability Scan | Trivy |
| Container Build | Docker |

### CD – deploy (main branch only)

| Step | Tool |
|---|---|
| Container Registry | GHCR |
| Orchestration | Kubernetes / Kind |
| Integration Tests | pytest + httpx |
| Dynamic Analysis (DAST) | OWASP ZAP |
| Performance / Load Test | k6 |
| Automatic Rollback | kubectl rollout undo |
| DORA Metrics Summary | Shell / git |

---

## Tech Stack

- **CI/CD**: GitHub Actions
- **Language**: Python 3.12 / FastAPI
- **Containerisation**: Docker
- **Orchestration**: Kubernetes (Kind) + HPA
- **Registry**: GitHub Container Registry (GHCR)
- **Testing**: pytest, httpx, k6
- **Security**: Gitleaks, SonarCloud, Trivy, OWASP ZAP

---

## DORA Metrics

| Metric | Value |
|---|---|
| Lead Time for Changes | ~8 min |
| Deployment Frequency | 1× per push to main |
| Change Failure Rate | 0% |
| Time to Restore | ~1–2 min (automatic rollback) |

---

## Setup

1. Fork or clone this repository
2. Create a [SonarCloud](https://sonarcloud.io) project and generate a `SONAR_TOKEN`
3. Add `SONAR_TOKEN` under *Settings → Secrets and variables → Actions*
4. Push to any branch to trigger CI, push to `main` to trigger CI + CD