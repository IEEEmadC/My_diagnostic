package org.dev4u.hv.my_diagnostic.FragmentsIntro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.dev4u.hv.my_diagnostic.MiAnimacion;
import org.dev4u.hv.my_diagnostic.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class LauncherActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private CircleIndicator indicator;
    RelativeLayout fondoApp;
    MiAnimacion manejadorAnim;
    TextView lblSkip;

    int pages=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        viewPager = (ViewPager) findViewById(R.id.pager_introduction);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        fondoApp = (RelativeLayout)findViewById(R.id.launcher_background);
        lblSkip = (TextView)findViewById(R.id.lblSkip);
        setupViewPager(viewPager);
        indicator.setViewPager(viewPager);
        manejadorAnim = new MiAnimacion(getBaseContext(),fondoApp);
        manejadorAnim.start();

        lblSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()!=(pages-1)){
                    viewPager.setCurrentItem( viewPager.getCurrentItem()+1 );
                }else{
                    LauncherActivity.this.finish();
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        FirstIntroductionFragment frmFirst = new FirstIntroductionFragment();
        SecondIntroductionFragment frmSecond = new SecondIntroductionFragment();
        ThirdIntroductionFragment frmThird = new ThirdIntroductionFragment();
        FourIntroductionFragment frmFour = new FourIntroductionFragment();
        Fragment_disclaimer frmfive = new Fragment_disclaimer();
        adapter.addFragment(frmFirst);
        adapter.addFragment(frmSecond);
        adapter.addFragment(frmThird);
        adapter.addFragment(frmFour);
        adapter.addFragment(frmfive);
        pages=5;
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
