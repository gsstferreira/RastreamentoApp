package br.usp.pcs2018.rastreamentopacotesapp.Adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public TabLayoutAdapter(FragmentManager manager) {
        super(manager);
        this.fragments = new ArrayList<>();
    }

    public void addFragment(Fragment f) {
        fragments.add(f);
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return fragments.get(position);
        }
        catch (Exception e) {
            return  null;
        }
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
