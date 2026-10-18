# Missyco AI — Phone Build Pack

यह Android project आपके Vivo Y35 / Android 14 के लिए एक शुरुआती purple-black online AI chat app है।

## जरूरी सच
- यह **source project** है, APK नहीं।
- इस environment में Android SDK/Gradle build नहीं चला, इसलिए APK बनने की पुष्टि नहीं है।
- Online AI के लिए OpenAI-compatible API endpoint और API key चाहिए। API key को APK में hardcode करना सुरक्षित नहीं है; बेहतर production setup में अपनी backend proxy इस्तेमाल करें।
- नीचे demo endpoint/key fields हैं। इन्हें अपने provider के वास्तविक values से बदलना होगा।

## फोन से build
1. ZIP extract करें।
2. AndroidIDE जैसे Android Gradle IDE में project folder import/open करें।
3. AndroidIDE में SDK/Gradle setup और project sync करें।
4. `app/src/main/java/com/missyco/ai/MainActivity.java` में `API_URL` और `API_KEY` को अपने provider के values से बदलें (केवल निजी testing के लिए; सार्वजनिक app में key hardcode न करें)।
5. Build > Assemble/Build APK चलाएँ।
6. सफल होने पर `app/build/outputs/apk/debug/app-debug.apk` खोजें और install करें।

यदि AndroidIDE में Gradle/SDK error आता है, उसका screenshot भेजें। फोन पर build की सफलता AndroidIDE और compatible Gradle/SDK setup पर निर्भर है।
