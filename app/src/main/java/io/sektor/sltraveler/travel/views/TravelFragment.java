package io.sektor.sltraveler.travel.views;

import android.graphics.PorterDuff;
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
    private NowFragment nowFragment;
    private final static String SELECTED_TAB_KEY = "selected_tab";
    private ViewPager pager;

    public static TravelFragment getInstance() {
        if (instance == null)
            instance = new TravelFragment();

        return instance;
    }

    public TravelFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nowFragment = (NowFragment) NowFragment.instantiate(getContext(), NowFragment.class.getName());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = view.findViewById(R.id.travel_pager);
        TabLayout tabs = view.findViewById(R.id.tabs);
        final Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        // Set up ViewPager
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        pager.setAdapter(new TravelPagerAdapter(getChildFragmentManager()));
        pager.setOffscreenPageLimit(4);

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

        updateFromBundle(savedInstanceState, tabs, toolbar);
    }

    private void updateFromBundle(Bundle savedInstanceState, TabLayout tabs, Toolbar toolbar) {
        // If we don't have data from Bundle, select the Here & Now tab
        int selectedTab = 0;

        if (savedInstanceState != null && savedInstanceState.keySet().contains(SELECTED_TAB_KEY))
            selectedTab = savedInstanceState.getInt(SELECTED_TAB_KEY);

        pager.setCurrentItem(selectedTab);
        tabs.getTabAt(selectedTab).getIcon().setColorFilter(getResources().getColor(R.color.md_white), PorterDuff.Mode.SRC_IN);
        toolbar.setTitle(getPageTitle(selectedTab));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SELECTED_TAB_KEY, pager.getCurrentItem());

        super.onSaveInstanceState(outState);
    }

    private class TravelPagerAdapter extends FragmentPagerAdapter {

        TravelPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return nowFragment;
                case 1: return new FavoritesFragment();
                case 2: return new PlannerFragment();
                case 3: return new DisruptionsFragment();
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
