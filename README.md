# Phase 1: Skills refresh (4 weeks)

Goal: be fluent in the 2026 versions of your stack by building one small full-stack app, **Clipboard**:
a place to save and rank Twitch clips. It also becomes the starting point for the Phase 2 portfolio project.

```
phase1/
  java21-katas/   Java 17–21 language exercises (tests fail until you implement them)
  api/            Spring Boot 4.1, Java 21, Postgres 17, Flyway, Testcontainers
  web/            React 19 + TypeScript + Vite
```

## How to run things

| What | Command (from that folder) |
|---|---|
| Kata tests | `.\mvnw test` (or run them in IntelliJ) |
| API | `.\mvnw spring-boot:run`. Starts Postgres via `compose.yaml` automatically; Docker Desktop must be running |
| API tests | `.\mvnw test`. Starts a throwaway Postgres with Testcontainers |
| Web | `npm run dev` → http://localhost:5173 (`/api/*` is proxied to :8080) |
| DB in DBeaver | localhost:5432, db `clips`, user `clips`, password `secret` |

---

## Week 1: Modern Java + Spring Boot 4

- [ ] **Katas**: make all tests in `java21-katas` pass (records, sealed types + pattern switch, streams, virtual threads).
- [ ] Skim "What's new" for Java 17→21 (records, sealed, `switch` patterns, text blocks, `var`, SequencedCollection, virtual threads).
- [ ] Skim the Spring Boot 4 / Spring Framework 7 release notes. Key changes since you last worked:
      `javax.*` → `jakarta.*`, Jackson 3, `RestClient` (replaces `RestTemplate`), `ProblemDetail` errors, built-in API versioning, JSpecify null annotations.
- [ ] **API: Clip CRUD** in `api/` (the table already exists: `db/migration/V1__create_clip.sql`)
  - `Clip` JPA entity + `ClipRepository extends JpaRepository`
  - Request/response DTOs as **records** with Bean Validation (`@NotBlank`, `@URL`, `@Size`)
  - `ClipController` under `/api/clips`: list (paginated, filter by `channel`), get, create, update views, delete
  - 404 → `ProblemDetail` via `@RestControllerAdvice`
  - Tests: one `@SpringBootTest` + `MockMvcTester` test per endpoint (Testcontainers is already wired up)

## Week 2: TypeScript + React 19

- [ ] TypeScript Handbook: "Everyday Types", "Narrowing", "Generics", "Utility Types".
- [ ] react.dev "Learn" section, especially "You Might Not Need an Effect" (the biggest mindset change since class components).
- [ ] **Web: Clipboard UI** in `web/`
  - `npm i @tanstack/react-query`: `useQuery` to list clips, `useMutation` to create/delete
  - A `Clip` type that matches the API DTO; no `any`
  - Create form using a React 19 form action (`<form action={...}>` + `useActionState`)
  - Filter by channel, sorted by views
  - Tests: `npm i -D vitest @testing-library/react jsdom`, then test the form

## Week 3: SQL, Postgres, Docker

- [ ] **Migration V2**: `tag` table + `clip_tag` join table (many-to-many), with indexes.
- [ ] **Leaderboard endpoint** `/api/clips/top`: top 3 clips per channel using a window function
      (`ROW_NUMBER() OVER (PARTITION BY channel ORDER BY views DESC)`) in a native query or `JdbcClient`.
- [ ] Run `EXPLAIN ANALYZE` on it in DBeaver; seed ~100k rows (`generate_series`) and see what the index changes.
- [ ] Write a `Dockerfile` for the API (multi-stage, JRE 21 base) or try `.\mvnw spring-boot:build-image`.
- [ ] Top-level `compose.yaml` that runs postgres + api together.

## Week 4: CI, cloud basics, AI tooling

- [ ] `gh auth login`, create a GitHub repo, push this folder.
- [ ] GitHub Actions workflow: run `api` tests (Docker is available on `ubuntu-latest`) and `web` build + tests on every push.
- [ ] AWS fundamentals: IAM, S3, RDS, ECS Fargate / App Runner, CloudWatch. The free AWS Skill Builder
      "Cloud Practitioner Essentials" course covers it; the cert itself is optional.
- [ ] Optional: deploy the API to AWS App Runner or Fly.io + a managed Postgres (watch costs; tear it down after).

## Throughout: AI-assisted development

Hiring teams now assume you can use AI coding tools well. Practise the workflow on this project:
write the spec or tests yourself, let Claude Code draft the implementation, then **review every line**.
Do the katas by hand first, since that's the part you'll be tested on in interviews.

## Done when

You can explain and demo: a paginated, validated REST API with migrations and integration tests; a typed React
UI using server-state caching; a window-function query you've profiled; and a green CI pipeline.
