# AI Design Canvas Prompt — AlignerCare App (Final)

Use this document as a copy-paste prompt for AI-powered design tools (Figma AI, Galileo, etc.) to redesign the AlignerCare patient app.

---

## App overview

Design a mobile app called **AlignerCare** for clear aligner patients.

**Tagline:** *Your smile, reimagined.*

**What it does:** Helps patients track aligner wear, log time when aligners are out, view treatment progress, manage their tray schedule, do weekly teeth scans, and manage their profile.

**Tone:** Clean, clinical, calm, trustworthy — modern dental clinic feel.

**Visual style:**
- Primary: cyan-teal `#0F7BA6`
- Light slate backgrounds, white cards, soft blue-gray borders
- Gradient cards for tips and promos
- Amber for caution / plan-complete states
- Rounded corners 12–16px, bold headings, small uppercase labels
- Use **AlignerCare** everywhere (not "Clinical Sanctuary")

---

## User journey (simple flow)

```
App opens → Splash
  → Not logged in → Login
      → Existing patient → Login → Home
      → New patient → Sign Up → Home (with "Create Plan" card if no plan)
  → Logged in + no plan → Home (shows "Create Plan" card)
  → Logged in + has plan → Home (full dashboard)
```

**Bottom navigation (most main screens):**
`PROGRESS` | `SCHEDULE` | `SCAN` | `PROFILE`

---

## SCREEN 1 — Splash

**Purpose:** Brand loading screen (~1 second)

**Content:**
- Logo in rounded square
- **AlignerCare**
- *Your smile, reimagined.*
- Loading dots
- Footer: **CLINICAL EXCELLENCE** + version label

**Auto-navigates to:** Login, or Home, or Home with Create Plan card

---

## SCREEN 2 — Login (Returning patients)

**Purpose:** Existing patients sign in

**Hero:**
- **AlignerCare**
- *Your smile, reimagined.*
- Short welcome text about starting their alignment journey

**Section label:** WELCOME BACK  
**Title:** Sign In

**Fields:**
1. Phone Number (10 digits)
2. Email Address
3. OTP — 6 digit boxes + **Resend Code** link  
   Helper: *Enter your credentials and verification code sent via SMS.*

**Button flow:**
- First tap: **Request OTP**
- After OTP sent: **Sign In**

**Footer link:** *New patient? **Start Consultation*** → goes to **Sign Up**

**Pro tip card** about keeping a backup aligner set

**Footer:** Privacy | Support (links to future screens or web)

**Error:** Dialog with message + OK

**Success → Home**

---

## SCREEN 3 — Sign Up (New patients)

**Purpose:** New patient registration before first use

**Hero:** Same AlignerCare branding as Login

**Section label:** NEW PATIENT  
**Title:** Start Your Consultation

**Subtext:** *Create your account to begin tracking your aligner treatment with your clinic.*

**Fields:**
1. First Name
2. Last Name
3. Phone Number (10 digits)
4. Email Address
5. Date of Birth (date picker)
6. OTP — 6 digit boxes + **Resend Code**

**Button flow:**
- First tap: **Request OTP**
- After OTP sent: **Create Account**

**Footer link:** *Already a patient? **Sign In*** → back to Login

**Pro tip card** (same backup aligner tip or onboarding tip)

**Success → Home** (will show **Create Plan** card if no plan yet)

> **Implementation note:** Today the app uses one OTP flow for both login and register. Design Sign Up as a separate screen; fields align with Edit Profile (name, phone, email, DOB).

---

## SCREEN 4 — Home / Progress (Dashboard)

**Purpose:** Main hub

**Header:**
- Menu (hamburger) → drawer with **Logout**
- Title: **AlignerCare**
- Notification bell (future inbox — design as tappable)
- Profile photo circle

**Greeting:**
- *{Morning/Afternoon/Evening}, {First Name}*
- *Your smile transformation is {X}% complete.*

**Pull down to refresh**

### Home — State A: No plan yet

**Card:**
- **No active plan yet**
- Short text: *Set up your treatment to start tracking progress.*
- Button: **Create Plan** → Create Plan screen

### Home — State B: Plan finished

**Card:**
- **Plan finished**
- *You completed all {N} aligners. Contact your clinic if you need a refinement or retainer.*
- Button: **Start a new plan**

### Home — State C: Active treatment

**Card 1 — Current phase**
- **Aligner #{current} of {total}**
- Motivational message (varies by progress %)
- **START DATE** | **EST. FINISH** (two date boxes)
- Circular ring: **{X}% COMPLETED**

**Card 2 — Upcoming tray**
- **Upcoming Tray** + date e.g. *Friday, Mar 14*

**Card 3 — Average daily wear**
- Big stat e.g. **21h 30m**
- Label: **AVG. DAILY HOURS**
- Tap → Daily Wear Detail

**Card 4 — Non-wear timer** *(primary action)*
- **Current Break**
- *Tracking time while aligners are out for meals or cleaning.*
- Live timer e.g. `1h 23m 45s`
- Big circle button: **START TIMER** / **STOP TIMER**
- Tap card → Non-Wear Details

**Card 5 — Weekly scan promo (gradient)**
- **Capture your weekly scan**
- *Our AI analyzes your tooth movement to ensure your treatment is on track.*
- Button: **Track Progress** → Scan intro

**Two info cards:**
1. **Check-up** — *{Date & time from clinic}*  
   *(Design for real appointments later; use placeholder e.g. "Oct 24 • 10:30 AM" for now)*
2. **{N} Day Streak** — *Keep it up, {Name}!*

**Pro tip card**

**Bottom nav:** PROGRESS (active) | SCHEDULE | SCAN | PROFILE

---

## SCREEN 5 — Create Plan

**Purpose:** First-time treatment setup

**Header:** **AlignerCare** + profile avatar

**Fields:**
1. Total number of aligners (default 14, placeholder `e.g. 20`)
2. Days per aligner (default 7, placeholder `e.g. 7`)
3. Treatment start date (date picker, `dd/MM/yyyy`)

**Schedule Forecast card** with image + estimated end text

**Button:** **Create My Schedule**

**Helper:** *You can adjust these settings later.*

**Two info cards:** Smart Monitoring | Clinical Precision

**Success → Home (full dashboard)**

---

## SCREEN 6 — Aligner Schedule

**Purpose:** View and adjust all trays

**Header:** **Aligner Schedule**

**Intro:** *Customize your journey. Adjusting durations will recalibrate your entire schedule.*

**Tray cards (list):**
- PHASE 01, Aligner 1
- Badge: COMPLETED | CURRENT | UPCOMING
- Date range
- For active/upcoming: duration stepper **− {N}d +**

**Gradient info card:** Dynamic Recalibration explanation

**Sticky button when edited:** **Update Schedule**

**Bottom nav:** PROGRESS | SCHEDULE (active) | SCAN | PROFILE

---

## SCREEN 7 — Non-Wear Details

**Purpose:** Today's aligner-out sessions

**Header:** **Non-Wear Details** | back | calendar icon | profile

**Date:** e.g. *Thursday, Jun 25*  
**Subtitle:** *Summary of periods when aligners were removed.*

**Table:** START TIME | END TIME | DURATION  
*(Active session shows "Ongoing")*

**Total card:** **TOTAL NON-WEAR TIME** + large time

**Pro tip** about 22 hours daily wear

**Bottom nav:** PROGRESS (active) | SCHEDULE | SCAN | PROFILE

---

## SCREEN 8 — Daily Wear Detail

**Purpose:** Any past day — wear vs time off

**Header:** **DAILY WEAR DETAIL** + month/year

**Horizontal day picker** (only days with logged data)

**Total time off card** + **On Track** / *Under daily limit of 2h*

**Time Off Log table** (empty: *No sessions recorded*)

**Daily Compliance Summary:**
- Circular wear % chart
- Wear Time vs Time Off

**Pro tip**

**Back only — no bottom nav**

---

## SCREEN 9 — Weekly Scan (intro)

**Purpose:** Prep before camera

**Header:** **AlignerCare** | back | profile

**Title:** **Time for your scan!**  
**Body:** *Weekly scans help our AI track your progress…*

**3 guidelines:** Good Lighting | Clear View | Steady Hands

**Tip card**

**Button:** **Start AI Scan** → Camera (after permission)

**Bottom nav:** PROGRESS | SCHEDULE | SCAN (active) | PROFILE

---

## SCREEN 10 — Camera Scan

**Purpose:** 3-step photo capture (camera only — no results screen)

**Full-screen camera, dark overlay**

**Steps:** Front View → Left View → Right View  
**Top:** Close | Step 1/3  
**Center:** Mouth/teeth guide frame  
**Button:** Capture / **Finish Scan**

**On finish → back to Scan intro** *(no analysis/results screen for now)*

---

## SCREEN 11 — Profile

**Purpose:** Account hub

**Header:** **Profile**

**Large photo** + verified badge

**ACCOUNT OVERVIEW menu:**
1. **Aligner Schedule** → Schedule
2. **Profile** → Edit Profile
3. **Account Settings** → Account Settings
4. **Help & Support** → Help & Support

**Pro tip card**

**Log Out** (red)

**Footer:** version label

**Bottom nav:** PROGRESS | SCHEDULE | SCAN | PROFILE (active)

---

## SCREEN 12 — Edit Profile

**Title:** Personal Information  
*Update your personal details and how we contact you.*

**Photo** with edit button

**Fields:**
- First Name *(editable)*
- Last Name *(editable)*
- Email *(read-only)*
- Phone *(read-only)*
- Date of Birth *(date picker)*

**Button:** **Update Profile**

**Bottom nav:** PROFILE active

---

## SCREEN 13 — Account Settings

**Purpose:** App and account preferences *(new screen for redesign)*

**Header:** **Account Settings** | back

**Notifications**
- Toggle: Timer reminders
- Toggle: Daily summary reminder
- Toggle: Check-up reminders *(for future clinic appointments)*

**Account**
- **Log Out**
- **Delete Account** *(optional — future)*

**Legal**
- Privacy Policy
- Terms of Service

**Bottom nav:** PROFILE active

---

## SCREEN 14 — Help & Support

**Purpose:** Patient help center *(new screen for redesign)*

**Header:** **Help & Support** | back

**Quick help (FAQ)**
- How do I use the non-wear timer?
- How is my streak calculated?
- When should I switch aligners?
- How do I do a weekly scan?

**Contact**
- **Contact my clinic** *(future — placeholder button)*
- **Email support** *(placeholder)*

**Pro tip or FAQ accordion layout**

**Bottom nav:** PROFILE active

---

## Key user actions

| Action | Where | Result |
|--------|-------|--------|
| Request OTP → Sign In | Login | Goes to Home |
| Start Consultation | Login | Goes to Sign Up |
| Create Account | Sign Up | Goes to Home |
| Create Plan | Home (no plan) | Goes to Create Plan |
| Start / Stop Timer | Home | Tracks aligner-out time |
| Pull to refresh | Home | Refreshes data |
| Tap timer card | Home | Non-Wear Details |
| Tap avg hours card | Home | Daily Wear Detail |
| Track Progress | Home scan banner | Scan intro |
| Update Schedule | Schedule | Saves tray changes |
| Start AI Scan | Scan | 3-step camera |
| Update Profile | Edit Profile | Saves name, DOB, photo |
| Log Out | Home drawer / Account Settings | Returns to Login |

---

## Design decisions (locked in)

| # | Decision |
|---|----------|
| 1 | Brand name: **AlignerCare** everywhere |
| 2 | No plan after login → **Home with Create Plan card** |
| 3 | Check-up card stays — **real clinic appointments later** |
| 4 | Scan stays **camera-only** — no results screen |
| 5 | **Account Settings** and **Help & Support** as separate screens |
| 6 | **Sign Up flow** for new patients via "Start Consultation" |

---

## Screen list for design canvas

| # | Screen | Notes |
|---|--------|-------|
| 1 | Splash | |
| 2 | Login | |
| 3 | Sign Up | New |
| 4 | Home — no plan | |
| 5 | Home — active treatment | |
| 6 | Home — plan finished | |
| 7 | Create Plan | |
| 8 | Aligner Schedule | |
| 9 | Non-Wear Details | |
| 10 | Daily Wear Detail | |
| 11 | Scan intro | |
| 12 | Camera scan (3 steps) | |
| 13 | Profile | |
| 14 | Edit Profile | |
| 15 | Account Settings | New |
| 16 | Help & Support | New |

**Optional variants:** Login/Sign Up loading states, empty timer table, error dialogs, pull-to-refresh on Home.

---

## Related docs

- [DESIGN.md](./DESIGN.md) — Technical architecture and business rules
- [DEVELOPER_GUIDE.md](./DEVELOPER_GUIDE.md) — Developer walkthrough
- [APP_FLOW_AND_API_MAP.md](./APP_FLOW_AND_API_MAP.md) — Navigation and API map
