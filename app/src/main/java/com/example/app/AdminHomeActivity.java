package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button btnOverview = findViewById(R.id.btnOverview);
        Button btnUserActivity = findViewById(R.id.btnUserActivity);
        Button btnPerformance = findViewById(R.id.btnPerformance);

        View.OnClickListener go = v -> startActivity(new Intent(this, AdminActivity.class));
        btnOverview.setOnClickListener(go);
        btnUserActivity.setOnClickListener(go);
        btnPerformance.setOnClickListener(go);
    }
}



