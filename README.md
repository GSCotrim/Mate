<img src="./assets/smallerLogo.png" width="120" alt="mate" />

[![CI](https://github.com/GSCotrim/Mate/actions/workflows/ci.yml/badge.svg)](https://github.com/GSCotrim/Mate/actions/workflows/ci.yml)

Plataforma de estudos de matemática desenvolvida para o **Cursinho Popular de Paraisópolis** — um curso preparatório gratuito em São Paulo.

Os alunos percorrem trilhas estruturadas de aulas e exercícios, com o progresso salvo entre as sessões.

## Stack

- **Backend:** Kotlin, Spring Boot 3.5, PostgreSQL 16
- **Frontend:** React + TypeScript *(Fase 2)*
- **Deploy:** Render.com

## Estrutura do repositório

```
Mate/
  backend/     API Spring Boot
  content/     Conteúdo das aulas em Markdown
  k8s/         Manifestos Kubernetes (portfólio)
  assets/      Imagens e recursos visuais
```

## Rodando localmente

```bash
# Subir o banco de dados
docker compose up -d

# Rodar a aplicação
./gradlew bootRun

# Rodar os testes
./gradlew test
```

Requer Docker e JDK 21.

## Roadmap

- [ ] Fase 1 — Backend API *(em andamento)*
- [ ] Fase 2 — Frontend web
- [ ] Fase 3 — Android
- [ ] Fase 4 — iOS

---

A mathematics learning platform built for the **Cursinho Popular de Paraisópolis** — a free college-prep course in São Paulo.

Students work through structured tracks of lessons and exercises, with their progress tracked across sessions.

## Stack

- **Backend:** Kotlin, Spring Boot 3.5, PostgreSQL 16
- **Frontend:** React + TypeScript *(Phase 2)*
- **Deploy:** Render.com

## Repository Structure

```
Mate/
  backend/     Spring Boot API
  content/     Lesson content in Markdown
  k8s/         Kubernetes manifests (portfolio)
  assets/      Images and visual assets
```

## Running locally

```bash
# Start the database
docker compose up -d

# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

Requires Docker and JDK 21.

## Roadmap

- [ ] Phase 1 — Backend API *(in progress)*
- [ ] Phase 2 — Web frontend
- [ ] Phase 3 — Android
- [ ] Phase 4 — iOS