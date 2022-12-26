package com.meraki.capstone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.meraki.capstone.Fragments.ButtonFragment;
import com.meraki.capstone.Fragments.UserListFragment;

public class MyViewPageAdapter  extends FragmentStateAdapter {
    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 :
                return new ButtonFragment();
            case 1 :
                return new UserListFragment();
            default :
                return new ButtonFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
