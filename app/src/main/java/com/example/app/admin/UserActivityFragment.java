package com.example.app.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.app.R;
import com.example.app.adapters.SearchHistoryAdapter;
import com.example.app.models.SearchHistory;
import com.example.app.services.HistoryService;

import java.util.ArrayList;
import java.util.List;

public class UserActivityFragment extends Fragment {
    private RecyclerView rvSearchHistory;
    private SearchHistoryAdapter adapter;
    private HistoryService historyService;
    private View btnRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_user_activity, container, false);
        rvSearchHistory = root.findViewById(R.id.rvSearchHistory);
        rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SearchHistoryAdapter(requireContext(), new ArrayList<>());
        rvSearchHistory.setAdapter(adapter);
        historyService = new HistoryService(requireContext());
        btnRefresh = root.findViewById(R.id.btnRefreshHistory);
        btnRefresh.setOnClickListener(v -> loadHistory());
        loadHistory();
        return root;
    }

    private void loadHistory() {
        List<SearchHistory> list = historyService.getAllSearchHistory();
        if (list == null) list = new ArrayList<>();
        adapter.updateData(list);
    }
}


