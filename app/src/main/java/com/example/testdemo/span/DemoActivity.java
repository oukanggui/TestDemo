package com.example.testdemo.span;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.testdemo.R;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        TextView tvText = findViewById(R.id.tv_text);
        String subContent = "喂，您好！";
        String content = subContent + "欧先生在忙，您找TA有什么事？";
        SpannableString spanString = new SpannableString(content);

        int start = content.indexOf(subContent);

        int end = start + subContent.length();

        spanString.setSpan(new RoundStrokeTextSpan(10, ContextCompat.getColor(this, R.color.colorAccent), 3
                        , 36, Color.parseColor("#000000")),
                start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvText.setText(spanString);

        tvText.setMovementMethod(LinkMovementMethod.getInstance());
    }
}