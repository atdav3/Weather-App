package com.example.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.app.admin.OverviewFragment;
import com.example.app.admin.UserActivityFragment;
import com.example.app.admin.PerformanceFragment;
import com.example.app.admin.UsersFragment;

public class AdminPagerAdapter extends FragmentStateAdapter {
    public AdminPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new OverviewFragment();
        if (position == 1) return new UserActivityFragment();
        if (position == 2) return new PerformanceFragment();
        return new UsersFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


