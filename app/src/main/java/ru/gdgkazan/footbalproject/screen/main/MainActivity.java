package ru.gdgkazan.footbalproject.screen.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.gdgkazan.footbalproject.R;
import ru.gdgkazan.footbalproject.screen.fixtures.FixturesFragment;
import ru.gdgkazan.footbalproject.screen.results.ResultsFragment;

/**
 * Created by Alexey Antonchik on 18.09.16.
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.viewpager)
    ViewPager mPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        setupViewPager(mPager);
        mTabLayout.setupWithViewPager(mPager);
    }

    private void setupViewPager(@NonNull ViewPager viewPager) {
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FixturesFragment(), getResources().getString(R.string.fixtures));
        adapter.addFragment(new ResultsFragment(), getResources().getString(R.string.results));
        viewPager.setAdapter(adapter);
    }

}