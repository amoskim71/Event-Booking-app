package in.brainboxmedia;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.brainboxmedia.fragment.AboutUsFragment;
import in.brainboxmedia.fragment.HomeFragment;

/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        View view1 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_home);
        tabLayout.getTabAt(0).setCustomView(view1);

        View view3 = getLayoutInflater().inflate(R.layout.custom_tab_icon, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_about_us);
        tabLayout.getTabAt(1).setCustomView(view3);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        HomeFragment h = new HomeFragment();
        if (data != null) {
            h.setArguments(data);
        }
        adapter.addFrag(h);
        adapter.addFrag(new AboutUsFragment());
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

        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
