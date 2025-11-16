package com.example.app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.app.utils.AuthManager;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etPasswordConfirm;
    private Button btnRegister;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đăng ký");
        }

        authManager = new AuthManager(this);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirm = etPasswordConfirm.getText().toString().trim();

            if (username.isEmpty()) { etUsername.setError("Vui lòng nhập tên đăng nhập"); etUsername.requestFocus(); return; }
            if (email.isEmpty()) { etEmail.setError("Vui lòng nhập email"); etEmail.requestFocus(); return; }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { etEmail.setError("Email không hợp lệ"); etEmail.requestFocus(); return; }
            if (password.length() < 6) { etPassword.setError("Mật khẩu tối thiểu 6 ký tự"); etPassword.requestFocus(); return; }
            if (!password.equals(confirm)) { etPasswordConfirm.setError("Mật khẩu nhập lại không khớp"); etPasswordConfirm.requestFocus(); return; }

            if (authManager.register(username, email, password)) {
                Toast.makeText(this, "Đăng ký thành công, vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại hoặc dữ liệu không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


