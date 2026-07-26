# Play Store release & closed beta

Guide for building a signed release App Bundle and publishing Smylo to **Google Play Closed testing**.

Package name: `com.smylo`  
Current version: `0.1.0-beta.1` (`versionCode` 1)  
Production API: `https://bracesandalignersbackend.onrender.com/` (from `gradle.properties`)

---

## 1. Create the upload keystore (once)

Run from the **project root** (`SmyloApp/`):

```bash
keytool -genkey -v -keystore smylo-upload.jks -keyalg RSA -keysize 2048 -validity 10000 -alias smylo-upload
```

Back up `smylo-upload.jks` and both passwords in a password manager. Losing the upload key blocks future updates until you reset the upload key in Play Console.

Copy the example properties file and fill in real values:

```bash
cp keystore.properties.example keystore.properties
```

`keystore.properties` (gitignored):

```properties
storeFile=smylo-upload.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=smylo-upload
keyPassword=YOUR_KEY_PASSWORD
```

`*.jks`, `*.keystore`, and `keystore.properties` must never be committed.

---

## 2. Build the release App Bundle

```bash
./gradlew :app:bundleRelease
```

On Windows (PowerShell):

```powershell
.\gradlew.bat :app:bundleRelease
```

Output:

```
app/build/outputs/bundle/release/app-release.aab
```

### Signing notes

- If `keystore.properties` exists, the release bundle is signed with your **upload key**.
- If it is missing, Gradle still builds a release AAB (for local minify smoke tests) but it is **not** suitable for Play upload until you add the keystore. AGP may sign with the debug key in that case — do not upload that artifact to Play.
- Release builds force an `https://` API base URL (see `app/build.gradle.kts`). Debug may still use LAN/emulator HTTP via `src/debug`.
- Release minify uses R8; the project uses AGP **8.6.1+** so R8 can read Kotlin **2.1** metadata. If minify OOMs, keep `org.gradle.jvmargs=-Xmx4096m` in `gradle.properties`.

### Install a release APK locally (optional smoke test)

```bash
./gradlew :app:assembleRelease
adb install -r app/build/outputs/apk/release/app-release.apk
```

Smoke-test on a physical device:

- OTP login
- Plan create / active plan
- Non-wear timer + notifications
- Camera scan
- Account settings + Contact Support
- Confirm traffic goes to the Render HTTPS backend (no cleartext)

---

## 3. Play Console — create the app

1. Open [Google Play Console](https://play.google.com/console) (developer account, one-time $25 fee).
2. **Create app** → name **Smylo**, default language, app or game = App, free/paid.
3. Complete the dashboard checklist items below before (or while) setting up closed testing.

### Store listing (minimum for closed test)

| Asset | Spec |
|-------|------|
| App name | Smylo |
| Short description | ≤ 80 characters |
| Full description | Feature overview for testers |
| App icon | 512 × 512 PNG |
| Feature graphic | 1024 × 500 PNG |
| Phone screenshots | At least 2 |

Also set a public **support email** and a **privacy policy URL** (required: account data, camera, notifications).

### Declarations

1. **Privacy policy** — host a public page (GitHub Pages, Notion, or your site).
2. **Data safety** — declare data you collect (e.g. email/phone, photos if uploaded, app activity, device IDs / FCM tokens), purposes, and whether data is shared with third parties.
3. **Content rating** — complete the IARC questionnaire.
4. **Target audience** — age groups; confirm not primarily children if applicable.
5. **Ads** — declare whether the app contains ads.
6. **News app** — usually No.

### Foreground service

The timer uses `foregroundServiceType="specialUse"` with a justification in `AndroidManifest.xml`. Keep that wording accurate; Play may review it.

---

## 4. Closed testing track

1. Play Console → **Testing** → **Closed testing**.
2. Create a track (e.g. “Internal beta”) if prompted.
3. **Create new release** → upload `app-release.aab`.
4. Confirm **Play App Signing** (recommended): Google holds the app signing key; you keep the upload key.
5. Add release notes (e.g. “First closed beta — OTP login, plan, timer, weekly scan”).
6. **Save** → **Review release** → **Start rollout to Closed testing**.

### Add testers

1. Create an email list or Google Group under **Testers**.
2. Copy the **opt-in URL** and send it to testers.
3. Testers must:
   - Open the opt-in link while signed into the Google account you invited
   - Accept becoming a tester
   - Install **Smylo** from the Play Store (not a sideloaded APK)

First closed-test review can take hours to a few days.

---

## 5. After first upload — version bumps

For each new Play upload, increment in `app/build.gradle.kts`:

```kotlin
versionCode = 2        // must always increase
versionName = "0.1.0-beta.2"
```

Rebuild the AAB and create a new closed-testing release.

---

## 6. Open testing / production (later)

When closed beta is stable:

1. Promote the release to **Open testing** (public opt-in link) or **Production**.
2. Complete any remaining store listing / countries / pricing steps.
3. Production may require a longer review.

---

## Troubleshooting

| Issue | What to check |
|-------|----------------|
| `keystore.properties` / store file not found | File at project root; `storeFile` path relative to project root |
| Cleartext / cleartext blocked | Release disables cleartext; use debug for LAN HTTP, or HTTPS for release |
| R8 / missing class at runtime | Add keep rules in `app/proguard-rules.pro` for the failing type |
| FCM not received on release | Confirm `google-services.json` package `com.smylo`; SHA-1 of **upload** and **app signing** certs in Firebase (Play Console → App integrity → App signing key certificate) |
| Tester cannot see the app | Opt-in accepted; correct Google account; release fully rolled out; wait for Play propagation |
