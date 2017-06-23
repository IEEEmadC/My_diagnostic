package org.dev4u.hv.my_diagnostic;

import android.os.Handler;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.Fragments.DiseaseFragment;
import org.dev4u.hv.my_diagnostic.Fragments.HistoryFragment;
import org.dev4u.hv.my_diagnostic.Fragments.SearchFragment;

import java.util.ArrayList;
import java.util.List;

import utils.AutoCompleteAdapter;
import utils.DiseaseUtilitesSingleton;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MenuItem prevMenuItem;
    private Handler handler = new Handler();
    private SearchFragment frmSearch;
    private HistoryFragment frmHistory;
    private DiseaseFragment frmDisease;
    private static boolean threadFinish=false;
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
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                viewPager.setCurrentItem(0);
                                return true;
                            case R.id.action_history:
                                viewPager.setCurrentItem(1);
                                return true;
                            case R.id.action_diseases:
                                viewPager.setCurrentItem(2);
                                return true;
                        }
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
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                if(position==0 && threadFinish){
                    updateFragments();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        Init();

    }//end onCreate



    public void Init(){
        DiseaseUtilitesSingleton.getInstance().init(this);
        new Thread(new Runnable() {
            public void run() {
                DiseaseUtilitesSingleton.getInstance().fillData();
                handler.post(new Runnable() {
                    public void run() {
                        threadFinish=true;
                        updateFragments();
                        //llenarCompleter();
                    }
                });
            }
        }).start();
    }

    private void updateFragments(){
        frmSearch.updateFragment();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        frmSearch = new SearchFragment();
        frmHistory = new HistoryFragment();
        frmDisease = new DiseaseFragment();
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

}
