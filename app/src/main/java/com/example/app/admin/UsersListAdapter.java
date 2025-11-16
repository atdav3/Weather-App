package com.example.app.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.R;
import com.example.app.models.User;

import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.VH> {
    interface Listener {
        void onEdit(User u);
        void onDelete(User u);
    }

    private List<User> users;
    private final Listener listener;

    public UsersListAdapter(List<User> users, Listener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_row, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        User u = users.get(position);
        holder.tvUsername.setText(u.getUsername());
        holder.tvEmail.setText(u.getEmail());
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(u));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(u));
    }

    @Override
    public int getItemCount() { return users != null ? users.size() : 0; }

    public void update(List<User> data) { this.users = data; notifyDataSetChanged(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvUsername, tvEmail; ImageButton btnEdit, btnDelete;
        VH(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}


