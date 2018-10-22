package io.sektor.sltraveler.travel.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.sektor.sltraveler.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class TravelFragment extends Fragment {

    public TravelFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager pager = view.findViewById(R.id.travel_pager);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);

        pager.setAdapter(new TravelPagerAdapter(getChildFragmentManager()));
    }

    private class TravelPagerAdapter extends FragmentPagerAdapter {

        TravelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new NowFragment();
                case 1: return new NowFragment(); // FavoritesFragment();
                case 2: return new NowFragment(); // PlannerFragment();
                case 3: return new NowFragment(); // DisruptionsFragment();
            }
            throw new IllegalArgumentException("Unknown fragment at index " + position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getResources().getString(R.string.here_now);
                case 1: return getResources().getString(R.string.favorites);
                case 2: return getResources().getString(R.string.journey_planner);
                case 3: return getResources().getString(R.string.disruptions);
            }
            throw new IllegalArgumentException("Unknown fragment at index " + position);
        }

    }
}
