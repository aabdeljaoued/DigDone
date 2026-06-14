# Play Store Checklist

## App Verification

- Run unit tests: `./gradlew testDebugUnitTest`
- Build debug APK: `./gradlew assembleDebug`
- Build release AAB: `./gradlew bundleRelease`
- Test create, delete, and recurring memo flows on a real device
- Verify notification permission flow on Android 13+
- Verify reminder behavior under Android power management on Android 12+
- Verify boot rescheduling after device reboot
- Verify rescheduling after app update
- Verify phrase-based notification disable flow
- Verify empty title and invalid date/time validation

## Release Build

- Create a private upload keystore outside the repository
- Copy `signing.properties.example` to `signing.properties` and fill in local signing values
- Configure CI signing through secrets if publishing from CI
- Increment `versionCode` for every Play Store upload
- Confirm `versionName` matches the release being published
- Generate a signed Android App Bundle (`.aab`)
- Keep the generated keystore and passwords out of Git

Local signed release command:

```sh
./gradlew clean testDebugUnitTest bundleRelease
```

Release AAB output:

```text
app/build/outputs/bundle/release/app-release.aab
```

Current local signing files are intentionally ignored by Git:

- `signing.properties`
- `release/digdone-upload-keystore.p12`

## Store Listing

- Finalize app name: DigDone
- Prepare short description
- Prepare full description
- Prepare app icon and feature graphic
- Capture phone screenshots
- Capture tablet screenshots if targeting tablets
- Choose app category
- Add support email
- Add privacy policy URL

Required graphics before production:

- App icon: 512 x 512 PNG
- Feature graphic: 1024 x 500 PNG or JPG
- Phone screenshots: at least 2
- Optional tablet screenshots if tablet distribution is enabled

## Privacy And Policy

- Publish the privacy policy from `docs/PRIVACY_POLICY.md` on a public URL
- Complete the Play Console data safety form
- Declare local-only data storage
- Declare that the app does not collect or share user data, if that remains true
- Review notification policy requirements
- Confirm the release does not request restricted exact-alarm permissions

## Alarm Policy Note

DigDone uses Android alarm scheduling without requesting the restricted `SCHEDULE_EXACT_ALARM` permission. This reduces Play policy risk, but Android may delay reminders slightly for battery optimization.

## Final Pre-Upload

- Run `./gradlew clean testDebugUnitTest bundleRelease`
- Install and smoke-test a release build locally
- Check Android vitals after internal testing
- Use Play Console internal testing before production rollout

## Internal Testing Steps

- Create the app in Play Console
- Upload `app-release.aab` to Internal testing
- Add tester email addresses or a Google Group
- Complete required app content forms
- Complete Data safety
- Add privacy policy URL
- Submit the internal testing release for review if required
- Install from the Play Store internal testing link on at least one Android 13+ device
- Verify notification permission, reminder scheduling, delete flow, and reboot rescheduling
