package e_business_projekt.e_business_projekt;

import android.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    // Viewpager that will display the several sections of the app, one at a time
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionbar = getActionBar();

        //deactivate home button since there is no parent
        actionbar.setHomeButtonEnabled(false);

        //display tabs in the actionbar
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position){
                actionbar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionbar.addTab(
                    actionbar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this)
            );
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter{

        public AppSectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i){
                case 0:
                    return new FirstSectionFragment();

                default:
                    Fragment fragment = new OtherSection();
                    Bundle args = new Bundle();
                    args.putInt(OtherSection.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position){
            return "Section " + (position + 1);
        }

        public static class FirstSectionFragment extends Fragment {

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){

                View rootView = inflater.inflate(R.layout.fragment_listView_section, container, false);
                return rootView;
            }
        }

        public static class OtherSection extends Fragment {

            public static final String ARG_SECTION_NUMBER = "section number";

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
                View rootView = inflater.inflate(R.layout.fragment_scan_section, container, false);
                //Bundle args = getArguments();
                return rootView;
            }


        }
    }
}
