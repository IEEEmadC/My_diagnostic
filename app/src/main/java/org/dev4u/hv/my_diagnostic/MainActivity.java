package org.dev4u.hv.my_diagnostic;

import android.support.annotation.NonNull;
import android.support.design.internal.TextScale;
import android.support.design.widget.BottomNavigationView;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.dev4u.hv.my_diagnostic.Fragments.DiseaseFragment;
import org.dev4u.hv.my_diagnostic.Fragments.HistoryFragment;
import org.dev4u.hv.my_diagnostic.Fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    DashboardNavigationController dashboardNavigationController;
    MenuItem prevMenuItem;
    boolean clic = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager_main);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        clic=true;
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                viewPager.setCurrentItem(0);
                                return true;
                                //break;
                            case R.id.action_history:
                                viewPager.setCurrentItem(1);
                                return true;
                                //break;
                            case R.id.action_diseases:
                                viewPager.setCurrentItem(2);
                                return true;
                                //break;
                        }
                        clic=false;
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(position).setChecked(false);
                }
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                //if(!clic) dashboardNavigationController.startAnimation();
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                clic=false;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        dashboardNavigationController = new DashboardNavigationController(bottomNavigationView);

    }//end onCreate

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        SearchFragment frmSearch = new SearchFragment();
        HistoryFragment frmHistory = new HistoryFragment();
        DiseaseFragment frmDisease = new DiseaseFragment();
        adapter.addFragment(frmSearch);
        adapter.addFragment(frmHistory);
        adapter.addFragment(frmDisease);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    public class DashboardNavigationController {
        private BottomNavigationView bottomNavigationView;
        private final TransitionSet transitionSet;
        private static final long ACTIVE_ANIMATION_DURATION_MS = 115L;
        public DashboardNavigationController(BottomNavigationView bottomNavigationView) {
            this.bottomNavigationView = bottomNavigationView;
            transitionSet = new AutoTransition();
            transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
            transitionSet.setDuration(ACTIVE_ANIMATION_DURATION_MS);
            transitionSet.setInterpolator(new FastOutSlowInInterpolator());
            TextScale textScale = new TextScale();
            transitionSet.addTransition(textScale);
        }
        public void startAnimation() {
            TransitionManager.beginDelayedTransition(bottomNavigationView, transitionSet);
        }
    }
}
