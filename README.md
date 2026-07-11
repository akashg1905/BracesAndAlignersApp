# SmyloApp (MVP)

**New to Kotlin?** Read the full walkthrough: [docs/DEVELOPER_GUIDE.md](docs/DEVELOPER_GUIDE.md) (libraries, package map, screen flows, diagrams).

## Tech Stack
- Kotlin + Jetpack Compose (Material 3)
- MVVM + Hilt
- Retrofit + OkHttp
- Room + DataStore
- WorkManager + Firebase Cloud Messaging

## Project Structure
- `app/src/main/java/com/example/smylo/core`: common, database, network, preferences
- `app/src/main/java/com/example/smylo/feature/auth`: OTP auth flow
- `app/src/main/java/com/example/smylo/feature/plan`: aligner plan + schedule
- `app/src/main/java/com/example/smylo/feature/timer`: non-wear timer + weekly summary
- `app/src/main/java/com/example/smylo/feature/dashboard`: aggregated home state
- `app/src/main/java/com/example/smylo/feature/notifications`: local notifications + FCM service + worker
- `app/src/main/java/com/example/smylo/navigation`: routes + nav host

## API Design (High Level)
- `POST /auth/register` -> send OTP (request body: `email`, `phone`)
- `POST /auth/verify-otp` -> verify OTP and return token (see guide for JSON shape)
- `POST /api/plan` -> create aligner plan
- `GET /api/plan/active` -> fetch active plan: **404** = no plan row (app clears local plan); **200** with body including `plan_status` / `planStatus` = `"expired"` when the plan exists but today is after the last day (app shows “Plan finished”, not “no plan”).
- `POST /timer/session` -> sync non-wear session start/stop
- `POST /timer/daily-summary` -> sync daily non-wear totals

## Data Models
- Auth: `OtpSendRequest`, `OtpVerifyRequest`, `AuthTokenResponse`
- Plan: `AlignerPlanEntity`, `AlignerScheduleItemEntity`
- Timer: `NonWearSessionEntity`, `DailyNonWearSummaryEntity`, `TimerState`

## Setup Guide
1. Install Android Studio with Android SDK 34 and JDK 17.
2. Place `google-services.json` under `app/`.
3. Set base API URL in `app/build.gradle.kts` via `API_BASE_URL`.
4. Sync Gradle and run `:app` on device/emulator.

## Core Flows
- Splash checks auth and routes to `Auth` or `Dashboard`.
- OTP login persists session in Room + DataStore.
- Plan setup generates full schedule from aligner count and duration.
- Timer start/stop persists active session and daily totals.
- Dashboard shows active aligner, progress %, timer state.
- Periodic worker posts daily summary reminder.
