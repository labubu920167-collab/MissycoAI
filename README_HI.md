# Missyco AI — Online AI starter (Vivo Y35 / Android 14)

## इस ZIP में क्या है
- Purple-black Android chat UI
- Internet permission
- OpenAI-compatible Chat Completions API का basic sample
- GitHub Actions cloud build workflow, ताकि phone से source upload करके APK artifact बनवा सकें

## जरूरी सीमाएँ
- यह ZIP source project है, **APK नहीं**। इस environment में Android SDK/Gradle उपलब्ध नहीं था, इसलिए APK compile/test नहीं हुई।
- API URL/key/model अभी sample values हैं। `MainActivity.java` में `API_URL`, `API_KEY`, `MODEL` देखें।
- API key को APK में रखना असुरक्षित है। केवल निजी local testing के लिए भी सावधानी रखें; public app के लिए backend proxy बनाएं।
- Provider का API format/model अलग हो सकता है; compatible endpoint जरूरी है।
- Voice input, text-to-speech, persistent chat storage और production backend इस starter में अभी शामिल नहीं हैं।

## Phone-only cloud build (GitHub)
1. ZIP extract करें।
2. GitHub पर नया repository बनाएं और extracted files/folders upload करें (folder structure जस का तस रखें)।
3. Repository में Actions tab खोलें। `Build Missyco AI APK` workflow चुनें और `Run workflow` दबाएं; या `main` branch में push/upload करने पर workflow अपने-आप चलेगा।
4. Workflow सफल होने पर run के नीचे `Artifacts` में `MissycoAI-debug-apk` डाउनलोड करें।
5. ZIP को extract करके `app-debug.apk` फोन पर खोलें और Android की install prompt follow करें।

## Build errors
- अगर workflow fail हो, Actions run का error screenshot/text भेजें।
- APK install होने का अर्थ online AI ready होना नहीं है; API endpoint/model/key configure करने होंगे।
