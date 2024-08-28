package com.main;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView versionName = findViewById(R.id.version);
        versionName.append(BuildConfig.VERSION_NAME);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
            finish();
            break;
            case R.id.imageView:
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.swing_anim));
        }
    }
}
