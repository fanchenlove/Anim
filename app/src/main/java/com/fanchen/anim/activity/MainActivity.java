package com.fanchen.anim.activity;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanchen.anim.R;
import com.fanchen.anim.base.BaseActivity;
import com.fanchen.anim.fragment.HomePagerFragment;
import com.fanchen.anim.util.AnimConstant;
import com.fanchen.anim.util.PreferenceUtil;
import com.fanchen.anim.view.CircleImageView;


/**
 *
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView mUserName;
    private TextView mUserSign;
    private ImageView mSwitchMode;
    private CircleImageView mUserAvatarView;

    private long lastTime = System.currentTimeMillis();

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void findView(View view) {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        View headerView = mNavigationView.getHeaderView(0);
        mUserAvatarView = (CircleImageView) headerView.findViewById(R.id.user_avatar_view);
        mUserName = (TextView) headerView.findViewById(R.id.user_name);
        mUserSign = (TextView) headerView.findViewById(R.id.user_other_info);
        mSwitchMode = (ImageView) headerView.findViewById(R.id.iv_head_switch_mode);
    }

    @Override
    protected void initViewData(Bundle savedState, LayoutInflater inflater) {
        super.initViewData(savedState, inflater);

        disableNavigationViewScrollbars();

        //设置头像
//        mUserAvatarView.setImageResource(R.drawable.ic_hotbitmapgg_avatar);
        //设置用户名 签名
//        mUserName.setText(getResources().getText(R.string.hotbitmapgg));
//        mUserSign.setText(getResources().getText(R.string.about_user_head_layout));

        if (PreferenceUtil.getBoolean(AnimConstant.SWITCH_MODE_KEY, false)) {
            mSwitchMode.setImageResource(R.drawable.ic_switch_daily);
        } else {
            mSwitchMode.setImageResource(R.drawable.ic_switch_night);
        }
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, homePagerFragment).show(homePagerFragment).commit();
    }

    @Override
    protected void setListener() {
        mNavigationView.setNavigationItemSelectedListener(this);
        //设置日夜间模式切换
        mSwitchMode.setOnClickListener(this);
    }

    @Override
    protected boolean isSwipeActivity() {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                toggleDrawer();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long time = System.currentTimeMillis();
            if (time - lastTime < 3000) {
                super.onBackPressed();
            } else {
                lastTime = time;
                showSnackbar("在按一次退出程序");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    /**
     * DrawerLayout侧滑菜单开关
     */
    public void toggleDrawer() {
        if (mDrawerLayout == null) return;
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void disableNavigationViewScrollbars() {
        if (mNavigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) mNavigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    /**
     * 日夜间模式切换
     */
    private void togoNightMode() {
        if (PreferenceUtil.getBoolean(AnimConstant.SWITCH_MODE_KEY, false)) {
            // 日间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            PreferenceUtil.putBoolean(AnimConstant.SWITCH_MODE_KEY, false);
        } else {
            // 夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            PreferenceUtil.putBoolean(AnimConstant.SWITCH_MODE_KEY, true);
        }
        recreate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_switch_mode:
                togoNightMode();
                break;
        }
    }
}
