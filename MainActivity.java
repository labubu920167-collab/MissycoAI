package com.missyco.ai;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    // Private testing only. Do not ship a real API key inside a public APK.
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String MODEL = "gpt-4o-mini";

    private LinearLayout messages;
    private EditText input;
    private Button send;
    private ScrollView scroll;
    private final JSONArray history = new JSONArray();
    private int purple = Color.rgb(192,132,252);
    private int bg = Color.rgb(16,9,28);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(12), dp(16), dp(10));
        root.setBackgroundColor(bg);

        TextView title = new TextView(this);
        title.setText("✦ MISSYCO AI");
        title.setTextColor(purple);
        title.setTextSize(24);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER_VERTICAL);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(44)));

        TextView subtitle = new TextView(this);
        subtitle.setText("Your futuristic AI companion • Online");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(12);
        root.addView(subtitle);

        scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        messages = new LinearLayout(this);
        messages.setOrientation(LinearLayout.VERTICAL);
        messages.setPadding(0, dp(12), 0, dp(12));
        scroll.addView(messages);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, 0, 1);
        sp.topMargin = dp(8); sp.bottomMargin = dp(8);
        root.addView(scroll, sp);

        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        input = new EditText(this);
        input.setSingleLine(false);
        input.setMaxLines(3);
        input.setHint("Missyco se kuch poochho…");
        input.setHintTextColor(Color.GRAY);
        input.setTextColor(Color.WHITE);
        input.setTextSize(15);
        input.setPadding(dp(12), dp(8), dp(12), dp(8));
        input.setBackground(round(Color.rgb(36,24,52), dp(18), Color.rgb(77,48,104)));
        bottom.addView(input, new LinearLayout.LayoutParams(0, dp(52), 1));
        send = new Button(this);
        send.setText("SEND");
        send.setTextColor(Color.WHITE);
        send.setTextSize(12);
        send.setAllCaps(false);
        send.setBackground(round(Color.rgb(126,34,206), dp(16), Color.TRANSPARENT));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(dp(82), dp(52));
        bp.leftMargin = dp(8);
        bottom.addView(send, bp);
        root.addView(bottom);
        setContentView(root);

        addBubble("Namaste! 💜 Main Missyco AI hoon. Apni API key set karke mujhse online chat kar sakte ho.", false);
        send.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = input.getText().toString().trim();
        if (text.isEmpty()) return;
        input.setText("");
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
            .hideSoftInputFromWindow(input.getWindowToken(), 0);
        addBubble(text, true);
        if (API_KEY.equals("YOUR_API_KEY_HERE") || API_KEY.trim().isEmpty()) {
            addBubble("Setup baaki hai: MainActivity.java mein API_URL, API_KEY aur MODEL ko apne AI provider ke values se set karo. Public app mein API key APK ke andar mat rakhna.", false);
            return;
        }
        send.setEnabled(false);
        addBubble("Soch rahi hoon…", false);
        new Thread(() -> {
            String reply = callApi(text);
            runOnUiThread(() -> {
                if (messages.getChildCount() > 0) {
                    View last = messages.getChildAt(messages.getChildCount()-1);
                    if (last instanceof TextView && ((TextView)last).getText().toString().equals("Soch rahi hoon…"))
                        messages.removeView(last);
                }
                addBubble(reply, false);
                send.setEnabled(true);
            });
        }).start();
    }

    private String callApi(String prompt) {
        HttpURLConnection c = null;
        try {
            JSONObject user = new JSONObject();
            user.put("role", "user"); user.put("content", prompt);
            history.put(user);
            JSONObject body = new JSONObject();
            body.put("model", MODEL);
            body.put("messages", history);
            body.put("temperature", 0.7);
            c = (HttpURLConnection)new URL(API_URL).openConnection();
            c.setRequestMethod("POST");
            c.setConnectTimeout(20000); c.setReadTimeout(45000);
            c.setDoOutput(true);
            c.setRequestProperty("Content-Type", "application/json");
            c.setRequestProperty("Authorization", "Bearer " + API_KEY);
            try (OutputStream os = c.getOutputStream()) {
                os.write(body.toString().getBytes("UTF-8"));
            }
            int status = c.getResponseCode();
            InputStream stream = status >= 200 && status < 300 ? c.getInputStream() : c.getErrorStream();
            StringBuilder out = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"))) {
                String line; while ((line = br.readLine()) != null) out.append(line);
            }
            JSONObject response = new JSONObject(out.toString());
            if (status < 200 || status >= 300) return "AI service error (" + status + "): " + response.optString("error", out.toString());
            String answer = response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            JSONObject assistant = new JSONObject();
            assistant.put("role", "assistant"); assistant.put("content", answer);
            history.put(assistant);
            return answer;
        } catch (Exception e) {
            return "Connection error: " + e.getMessage() + "\nInternet, API URL, API key aur model check karo.";
        } finally {
            if (c != null) c.disconnect();
        }
    }

    private void addBubble(String text, boolean user) {
        TextView bubble = new TextView(this);
        bubble.setText(text);
        bubble.setTextColor(Color.WHITE);
        bubble.setTextSize(15);
        bubble.setPadding(dp(14), dp(10), dp(14), dp(10));
        bubble.setMaxWidth((int)(getResources().getDisplayMetrics().widthPixels * 0.82f));
        bubble.setBackground(round(user ? Color.rgb(91,33,182) : Color.rgb(36,24,52), dp(18),
                user ? Color.rgb(147,51,234) : Color.rgb(77,48,104)));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-2, -2);
        p.gravity = user ? Gravity.RIGHT : Gravity.LEFT;
        p.topMargin = dp(5); p.bottomMargin = dp(5);
        messages.addView(bubble, p);
        scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
    }

    private GradientDrawable round(int color, int radius, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(radius);
        if (stroke != Color.TRANSPARENT) d.setStroke(dp(1), stroke);
        return d;
    }
    private int dp(int x) { return (int)(x * getResources().getDisplayMetrics().density + 0.5f); }
}