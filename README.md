# Vivitek IR Remote — Android project

This project includes a GitHub Actions workflow to build the APK online without Android Studio.

## Build free using GitHub in your phone browser

1. Sign in or create a free GitHub account at https://github.com.
2. Create a new repository. You can name it `VivitekIRRemote`.
3. On the repository page, choose **Add file → Upload files** and upload the contents of this project folder, including the hidden `.github/workflows/build.yml` file. If uploading from mobile is awkward, use GitHub's web upload page in your browser's desktop-site mode.
4. Commit the uploaded files to the repository's default branch.
5. Open the repository's **Actions** tab. If asked, enable Actions.
6. Select **Build Vivitek IR Remote APK** on the left, then tap **Run workflow**.
7. Wait for the workflow to finish successfully. Open the completed run and download the `VivitekIRRemote-debug-apk` artifact.
8. Extract the downloaded artifact ZIP to get `app-debug.apk`, then install it on your Android phone.

GitHub Actions usage limits and availability can change; a free account is usually sufficient for a small public repository. Keep in mind the source repository will be public if you choose public.

## App behavior and compatibility

- The app uses Android `ConsumerIrManager` to send NEC-style pulses at 38 kHz.
- The device must have a supported built-in consumer IR emitter.
- The supplied file contains parsed protocol/address/command values rather than raw pulse timings. The app builds a standard NEC frame from address `0x31` and each command byte. If the projector does not respond, the protocol variant/carrier may need to be checked against a known-working capture.
- This is a debug APK intended for direct testing, not a Google Play release build.
