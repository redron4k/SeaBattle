package com.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DialogActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.buttonYes:
                startActivity(new Intent(DialogActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.buttonNo:
                finish();
                break;
        }
    }
}
