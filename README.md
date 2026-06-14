# DigDone

Recurring memo reminders for Android.

DigDone lets you create local memo reminders that repeat hourly, daily, weekly, monthly, or yearly. Memos are stored on-device and scheduled with Android alarms so they can continue after app restarts and device reboots.

## Stack

- Kotlin
- Jetpack Compose
- DataStore Preferences
- Kotlinx Serialization
- AlarmManager reminders
- Broadcast receivers for alarm delivery and reboot/package replacement
- Gradle wrapper

## Features

- Create recurring memos: hourly, daily, weekly, monthly, yearly
- Trigger notifications when a memo is due
- Calendar-aware monthly and yearly recurrence
- Disable notifications with a phrase-based setting
- Persist memo data locally
- Reschedule after reboot
- Request notification permission when reminders are created or re-enabled
- Cancel scheduled alarms when memos are deleted

## Requirements

- JDK 17 or newer
- Android SDK with API 35 installed
- Android SDK Build Tools

If the Android SDK is not in the default location, create a local `local.properties` file:

```properties
sdk.dir=/path/to/android-sdk
```

This file is intentionally ignored by Git.

## Build And Test

Run unit tests:

```sh
./gradlew testDebugUnitTest
```

Build a debug APK:

```sh
./gradlew assembleDebug
```

Build a release Android App Bundle for Play Store upload:

```sh
./gradlew bundleRelease
```

The debug APK is generated under:

```text
app/build/outputs/apk/debug/
```

## Project Structure

- `app/src/main/java/com/aabdeljaoued/digdone/model/` - memo and recurrence models
- `app/src/main/java/com/aabdeljaoued/digdone/data/` - DataStore repositories and UI state
- `app/src/main/java/com/aabdeljaoued/digdone/schedule/` - alarm scheduling and cancellation
- `app/src/main/java/com/aabdeljaoued/digdone/receiver/` - alarm and boot receivers
- `app/src/main/java/com/aabdeljaoued/digdone/notify/` - notification channel and notification display
- `app/src/main/java/com/aabdeljaoued/digdone/ui/` - Jetpack Compose UI
- `app/src/test/` - unit tests

## Android Permissions

DigDone uses these permissions:

- `POST_NOTIFICATIONS` for Android 13+ reminder notifications
- `RECEIVE_BOOT_COMPLETED` to reschedule active memos after reboot

DigDone avoids restricted exact-alarm permissions for easier Play Store review. Reminder delivery may be adjusted by Android power management.

## Notes

- Memos are stored locally only.
- Monthly and yearly reminders use calendar math, so edge cases like January 31 and February 29 roll to the nearest valid calendar date.
- Notifications can be disabled without stopping memo recurrence advancement.
- This project currently has no backend service or cloud sync.

## Verified Commands

```sh
./gradlew testDebugUnitTest
./gradlew assembleDebug
```
