package com.example.app.admin;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.database.UserDatabaseHelper;
import com.example.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {
    private RecyclerView recyclerView;
    private UsersListAdapter adapter;
    private Button btnAddUser;
    private UserDatabaseHelper db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_users, container, false);
        recyclerView = root.findViewById(R.id.rvUsers);
        btnAddUser = root.findViewById(R.id.btnAddUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        db = new UserDatabaseHelper(requireContext());
        adapter = new UsersListAdapter(new ArrayList<>(), new UsersListAdapter.Listener() {
            @Override public void onEdit(User u) { showEditDialog(u); }
            @Override public void onDelete(User u) { deleteUser(u); }
        });
        recyclerView.setAdapter(adapter);
        btnAddUser.setOnClickListener(v -> showCreateDialog());
        loadUsers();
        return root;
    }

    private void loadUsers() {
        List<User> users = db.getAllUsers();
        adapter.update(users);
    }

    private void showCreateDialog() {
        View form = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_user_form, null);
        EditText etUsername = form.findViewById(R.id.etUsername);
        EditText etEmail = form.findViewById(R.id.etEmail);
        EditText etPassword = form.findViewById(R.id.etPassword);
        new AlertDialog.Builder(requireContext())
                .setTitle("Thêm người dùng")
                .setView(form)
                .setPositiveButton("Lưu", (d, w) -> {
                    String u = etUsername.getText().toString().trim();
                    String e = etEmail.getText().toString().trim();
                    String p = etPassword.getText().toString().trim();
                    if (u.isEmpty() || p.isEmpty()) { Toast.makeText(requireContext(), "Thiếu thông tin", Toast.LENGTH_SHORT).show(); return; }
                    long id = db.insertUser(new User(u, e, p));
                    if (id > 0) { loadUsers(); Toast.makeText(requireContext(), "Đã thêm", Toast.LENGTH_SHORT).show(); }
                    else { Toast.makeText(requireContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show(); }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(User user) {
        View form = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_user_form, null);
        EditText etUsername = form.findViewById(R.id.etUsername);
        EditText etEmail = form.findViewById(R.id.etEmail);
        EditText etPassword = form.findViewById(R.id.etPassword);
        etUsername.setText(user.getUsername());
        etUsername.setEnabled(false);
        etEmail.setText(user.getEmail());
        etPassword.setText(user.getPassword());
        new AlertDialog.Builder(requireContext())
                .setTitle("Sửa người dùng")
                .setView(form)
                .setPositiveButton("Lưu", (d, w) -> {
                    user.setEmail(etEmail.getText().toString().trim());
                    user.setPassword(etPassword.getText().toString().trim());
                    int n = db.updateUser(user);
                    if (n > 0) { loadUsers(); Toast.makeText(requireContext(), "Đã cập nhật", Toast.LENGTH_SHORT).show(); }
                    else { Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show(); }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteUser(User user) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa người dùng")
                .setMessage("Bạn chắc chắn muốn xóa " + user.getUsername() + "?")
                .setPositiveButton("Xóa", (d, w) -> {
                    int n = db.deleteUser(user.getId());
                    if (n > 0) { loadUsers(); Toast.makeText(requireContext(), "Đã xóa", Toast.LENGTH_SHORT).show(); }
                    else { Toast.makeText(requireContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show(); }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}


