package com.pipertts.studio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private EditText scriptBox;
    private TextView counter;
    private TextView status;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, float size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.WHITE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248, 250, 252));

        // Header
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(22), dp(22), dp(22), dp(22));
        header.setBackgroundColor(Color.rgb(17, 24, 39));

        TextView title = text(
                "🎙️ Piper TTS Studio",
                26,
                Color.WHITE
        );
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        TextView subtitle = text(
                "Professional voice generation for YouTube",
                14,
                Color.rgb(203, 213, 225)
        );

        header.addView(title);
        header.addView(subtitle);

        TextView voice = text(
                "\n🎙️ en_US-ryan-high    •    🖥️ CPU    •    🎧 MP3 192 kbps",
                12,
                Color.rgb(226, 232, 240)
        );

        header.addView(voice);

        root.addView(header);

        // Scroll area
        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(20), dp(18), dp(25));

        TextView label = text(
                "Tutorial Script",
                15,
                Color.rgb(17, 24, 39)
        );
        label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        content.addView(label);

        // Script box
        scriptBox = new EditText(this);
        scriptBox.setHint("Paste your complete YouTube tutorial script here...");
        scriptBox.setTextSize(16);
        scriptBox.setGravity(Gravity.TOP | Gravity.START);
        scriptBox.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );
        scriptBox.setMinHeight(dp(280));
        scriptBox.setBackgroundColor(Color.WHITE);
        scriptBox.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT |
                android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );

        LinearLayout.LayoutParams scriptParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(300)
                );

        scriptParams.topMargin = dp(10);

        content.addView(scriptBox, scriptParams);

        // Counter
        counter = text(
                "0 characters • 0 words",
                12,
                Color.rgb(107, 114, 128)
        );
        counter.setGravity(Gravity.RIGHT);

        LinearLayout.LayoutParams counterParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        counterParams.topMargin = dp(7);

        content.addView(counter, counterParams);

        scriptBox.addTextChangedListener(
                new android.text.TextWatcher() {

                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        String value = s.toString().trim();

                        int characters = s.length();

                        int words = value.isEmpty()
                                ? 0
                                : value.split("\\s+").length;

                        counter.setText(
                                String.format(
                                        "%,d characters • %,d words",
                                        characters,
                                        words
                                )
                        );
                    }

                    public void afterTextChanged(
                            android.text.Editable s) {
                    }
                }
        );

        // Buttons
        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setGravity(Gravity.CENTER_VERTICAL);

        Button generate = new Button(this);
        generate.setText("🎙️  Generate MP3");
        generate.setTextSize(15);
        generate.setTextColor(Color.WHITE);
        generate.setAllCaps(false);
        generate.setBackgroundColor(Color.rgb(17, 24, 39));

        LinearLayout.LayoutParams generateParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(55),
                        1
                );

        generateParams.topMargin = dp(18);
        generateParams.rightMargin = dp(6);

        buttons.addView(generate, generateParams);

        Button clear = new Button(this);
        clear.setText("Clear");
        clear.setTextSize(15);
        clear.setTextColor(Color.rgb(55, 65, 81));
        clear.setAllCaps(false);
        clear.setBackgroundColor(Color.rgb(229, 231, 235));

        LinearLayout.LayoutParams clearParams =
                new LinearLayout.LayoutParams(
                        dp(100),
                        dp(55)
                );

        clearParams.topMargin = dp(18);

        buttons.addView(clear, clearParams);

        content.addView(buttons);

        // Status
        status = text(
                "Ready — paste your tutorial script and click Generate MP3.",
                14,
                Color.rgb(71, 85, 105)
        );

        status.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(15)
        );
        status.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams statusParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

        statusParams.topMargin = dp(18);

        content.addView(status, statusParams);

        // Generate button for now
        generate.setOnClickListener(v -> {

            String script = scriptBox.getText().toString().trim();

            if (script.isEmpty()) {
                status.setText(
                        "⚠️ Please enter your tutorial script."
                );
                return;
            }

            status.setText(
                    "⏳ Piper TTS engine will generate your narration here."
            );
        });

        // Clear
        clear.setOnClickListener(v -> {
            scriptBox.setText("");
            status.setText(
                    "Ready — paste your tutorial script and click Generate MP3."
            );
        });

        TextView footer = text(
                "\nPiper TTS • Local CPU processing • No paid service",
                11,
                Color.rgb(156, 163, 175)
        );
        footer.setGravity(Gravity.CENTER);

        content.addView(footer);

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        setContentView(root);
    }
}
