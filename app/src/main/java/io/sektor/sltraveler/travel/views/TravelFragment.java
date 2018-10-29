package io.sektor.sltraveler.travel.views;

import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import io.sektor.sltraveler.R;

public class TravelFragment extends Fragment {

    private static TravelFragment instance;
    private NowFragment nowFragment = new NowFragment();
    private Location location;

    public static TravelFragment getInstance() {
        if (instance == null)
            instance = new TravelFragment();

        return instance;
    }

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
        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        // Set up ViewPager
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        pager.setAdapter(new TravelPagerAdapter(getChildFragmentManager()));

        // Fix icon colors, because Android
        for (int i = 0; i < tabs.getTabCount(); i++)
            tabs.getTabAt(i).getIcon().setColorFilter(getResources().getColor(R.color.white_75), PorterDuff.Mode.SRC_IN);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                toolbar.setTitle(getPageTitle(tab.getPosition()));
                tab.getIcon().setColorFilter(getResources().getColor(R.color.md_white), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                tab.getIcon().setColorFilter(getResources().getColor(R.color.white_75), PorterDuff.Mode.SRC_IN);
            }
        });

        // If we don't have data from Bundle, select the Here & Now tab
        if (savedInstanceState == null) {
            tabs.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.md_white), PorterDuff.Mode.SRC_IN);
            toolbar.setTitle(getPageTitle(0));
        }
    }

    public void setLocation(Location location) {
        this.location = location;
        nowFragment.setLocation(location);
    }

    private class TravelPagerAdapter extends FragmentPagerAdapter {

        TravelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return nowFragment;
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

    }

    public String getPageTitle(int position) {
        switch (position) {
            case 0: return getResources().getString(R.string.here_now);
            case 1: return getResources().getString(R.string.favorites);
            case 2: return getResources().getString(R.string.journey_planner);
            case 3: return getResources().getString(R.string.disruptions);
        }
        throw new IllegalArgumentException("Unknown fragment at index " + position);
    }
}
