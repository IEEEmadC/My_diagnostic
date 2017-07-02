package org.dev4u.hv.my_diagnostic;

import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.dev4u.hv.my_diagnostic.Fragments.BaseFragment;
import org.dev4u.hv.my_diagnostic.Fragments.DiseaseFragment;
import org.dev4u.hv.my_diagnostic.Fragments.HistoryFragment;
import org.dev4u.hv.my_diagnostic.Fragments.SearchFragment;

import utils.DiseaseUtilitesSingleton;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener{


    private final int INDEX_SEARCH  = FragNavController.TAB1;
    private final int INDEX_HISTORY = FragNavController.TAB2;
    private final int INDEX_DISEASE = FragNavController.TAB3;
    private static SearchFragment frm1   = new SearchFragment();
    private static HistoryFragment frm2  = new HistoryFragment();
    private static DiseaseFragment frm3  = new DiseaseFragment();

    private Handler handler = new Handler();

    private static boolean threadFinish=false;
    private BottomBar mBottomBar;
    private FragNavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        //animation
        //.defaultTransactionOptions(FragNavTransactionOptions.newBuilder().customAnimations
        // (R.anim.slide_int_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right).build())

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.selectTabAtPosition(0);
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, 3)
                .defaultTransactionOptions(FragNavTransactionOptions.newBuilder().customAnimations(R.anim.alpha_in, R.anim.alpha_out,R.anim.alpha_in,R.anim.alpha_out).build())
                .build();



        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bb_menu_search:
                        mNavController.switchTab(INDEX_SEARCH);
                        break;
                    case R.id.bb_menu_history:
                        mNavController.switchTab(INDEX_HISTORY);
                        break;
                    case R.id.bb_menu_diseases:
                        mNavController.switchTab(INDEX_DISEASE);
                        break;
                }
            }
        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                mNavController.clearStack();
            }
        });

    }//end onCreate

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            Log.d("Eliminado de ","Pila pos :"+mNavController.getCurrentStackIndex());
            mNavController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    public void Init(){
        DiseaseUtilitesSingleton.getInstance().init(this);
        new Thread(new Runnable() {
            public void run() {
                DiseaseUtilitesSingleton.getInstance().fillData();
                handler.post(new Runnable() {
                    public void run() {
                        threadFinish=true;
                        updateFragments();
                    }
                });
            }
        }).start();
    }

    private void updateFragments(){
        //frm1.updateFragment();
        //frm3.updateFragment();
    }
    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_SEARCH:
                return frm1;
            case INDEX_HISTORY:
                return frm2;
            case INDEX_DISEASE:
                return frm3;
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onTabTransaction(Fragment fragment, int i) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }
}
