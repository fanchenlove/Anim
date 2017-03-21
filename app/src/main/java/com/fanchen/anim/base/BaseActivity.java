package com.fanchen.anim.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.fanchen.anim.AnimAppliction;
import com.fanchen.anim.R;
import com.fanchen.anim.fragment.ThemeFragment;
import com.fanchen.anim.util.ThemeHelper;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.swipbackhelper.SwipeBackPage;

import java.util.List;

/**
 * 应用中所有activity应该继承该类
 * Created by fanchen on 2017/1/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements ThemeFragment.OnThemeChangeListener {

    /**
     *
     */
    public AnimAppliction appliction;
    /**
     *
     */
    private View mMainView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appliction = (AnimAppliction) getApplication();
        if (appliction != null)
            appliction.addActivity(this);
        SwipeBackHelper.onCreate(this);
        SwipeBackPage backPage = SwipeBackHelper.getCurrentPage(this);// 获取当前页面
        backPage.setSwipeBackEnable(isSwipeActivity());// 设置是否可滑动
        backPage.setSwipeEdgePercent(getEdgePercent());// 可滑动的范围。百分比。0.2表示为左边20%的屏幕
        backPage.setSwipeSensitivity(getSensitivity());// 对横向滑动手势的敏感程度。0为迟钝 1为敏感
        backPage.setClosePercent(getClosePercent());// 触发关闭Activity百分比
        backPage.setSwipeRelateEnable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);// 是否与下一级activity联动(微信效果)仅限5.0以上机器
        backPage.setDisallowInterceptTouchEvent(false);
        init(savedInstanceState, null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 使用滑动关闭功能
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 使用滑动关闭功能
        if (appliction != null)
            appliction.removeActivity(this);
        SwipeBackHelper.onDestroy(this);
    }

    /**
     * 设置是否可滑动
     *
     * @return
     */
    protected boolean isSwipeActivity() {
        return true;
    }

    /**
     * 可滑动的范围。百分比。0.2表示为左边20%的屏幕
     *
     * @return
     */
    protected float getEdgePercent() {
        return 0.15f;
    }

    /**
     * 对横向滑动手势的敏感程度。0为迟钝 1为敏感
     *
     * @return
     */
    protected float getSensitivity() {
        return 0.45f;
    }

    /**
     * 触发关闭Activity百分比
     *
     * @return
     */
    protected float getClosePercent() {
        return 0.4f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        init(savedInstanceState, persistentState);
    }

    /**
     * @param savedInstanceState
     * @param persistentState
     */
    private void init(Bundle savedInstanceState, PersistableBundle persistentState) {
        int layout = getLayout();
        if (layout <= 0)
            return;
        LayoutInflater inflater = getLayoutInflater();
        mMainView = inflater.inflate(layout, null, false);
        setContentView(mMainView);
        //等所有的view初始化完成之后，
        //再来进行界面数据的初始化
        mMainView.post(new InflaterRunnable(mMainView, savedInstanceState, inflater));
    }

    @Override
    public void onConfirm(int currentTheme) {
        if (ThemeHelper.getTheme(this) != currentTheme) {
            ThemeHelper.setTheme(this, currentTheme);
            ThemeUtils.refreshUI(this, extraRefreshable);
        }
    }

    /**
     *
     * @param attr
     * @return
     */
    public int getAttributeValue(int attr){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

//    @Override
//    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onPostCreate(savedInstanceState);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(ThemeUtils.getColorById(this, R.color.theme_color_primary_dark));
//            ActivityManager.TaskDescription description = new ActivityManager.TaskDescription(null, null,ThemeUtils.getThemeAttrColor(this, android.R.attr.colorPrimary));
//            setTaskDescription(description);
//        }
//    }

    /**
     * @return
     */
    protected abstract int getLayout();

    /**
     *
     */
    protected void setListener() {
    }

    /**
     * @param view
     */
    protected void findView(View view) {
    }

    /**
     * @param savedState
     * @param inflater
     */
    protected void initViewData(Bundle savedState,LayoutInflater inflater) {
    }

    /**
     * @param clazz
     */
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    /**
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * @param clazz
     */
    public void startService(Class<?> clazz) {
        startService(new Intent(this, clazz));
    }

    /**
     * @param clazz
     * @param bundle
     */
    public void startService(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startService(intent);
    }

    /**
     * @param clazz
     * @param code
     */
    public void startActivityForResult(Class<?> clazz, int code) {
        startActivityForResult(new Intent(this, clazz), code);
    }

    /**
     * @param clazz
     * @param bundle
     * @param code
     */
    public void startActivityForResult(Class<?> clazz, Bundle bundle, int code) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivityForResult(intent, code);
    }

    /**
     * @param editText
     * @return
     */
    public String getEditTextString(EditText editText) {
        if (editText == null) return "";
        return editText.getText().toString().trim();
    }

    /**
     * 获取当前用户可见Fragment
     *
     * @return 可能为空
     */
    public Fragment getVisibleFragment() {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        for (Fragment f : fragments) {
            if (f.getUserVisibleHint())
                return f;
        }
        return null;
    }

    /**
     * @param id
     * @param name
     * @param f
     */
    public void changeFragment(int id, String name, Fragment f) {
        if (isFinishing()) return;
        FragmentManager fm = getSupportFragmentManager();
        if (fm == null) return;
        // 切换动画
        FragmentTransaction ft = fm.beginTransaction();
        // 替换布局为fragment
        ft.replace(id, f);
        // 将当前fragment添加到Application列表里面
        if (!TextUtils.isEmpty(name)) ft.addToBackStack(name);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commitAllowingStateLoss();
    }

    /**
     * @param id
     * @param f
     */
    public void changeFragment(int id, Fragment f) {
        changeFragment(id, null, f);
    }

    /**
     * @param id
     */
    public void showToast(int id) {
        String string = getResources().getString(id);
        showToast(string);
    }

    /**
     * @param c
     */
    public void showToast(CharSequence c) {
        showToast(c, Toast.LENGTH_SHORT);
    }

    /**
     * @param id
     * @param len
     */
    public void showToast(int id, int len) {
        String string = getResources().getString(id);
        showToast(string, len);
    }

    /**
     * @param c
     * @param len
     */
    public void showToast(final CharSequence c, final int len) {
        if ("main".equals(Thread.currentThread().getName())) {
            makeToast(c, len);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeToast(c, len);
                }
            });
        }
    }

    /**
     * @param c
     * @param len
     */
    private void makeToast(CharSequence c, int len) {
        Toast.makeText(this, c, len);
    }

    /**
     * @param view
     * @param c
     * @param len
     */
    public void showSnackbar(View view, CharSequence c, int len) {
        showSnackbar(view, c, len, null, null);
    }

    /**
     * @param view
     * @param c
     */
    public void showSnackbar(View view, CharSequence c) {
        showSnackbar(view, c, Snackbar.LENGTH_SHORT, null, null);
    }

    /**
     * @param c
     */
    public void showSnackbar(CharSequence c) {
        if (mMainView != null)
            showSnackbar(mMainView, c, Snackbar.LENGTH_SHORT, null, null);
    }

    /**
     * @param view
     * @param c
     * @param title
     * @param l
     */
    public void showSnackbar(View view, CharSequence c, CharSequence title, View.OnClickListener l) {
        showSnackbar(view, c, Snackbar.LENGTH_SHORT, title, l);
    }

    /**
     * @param view
     * @param c
     * @param len
     * @param title
     * @param l
     */
    public void showSnackbar(final View view, final CharSequence c, final int len, final CharSequence title, final View.OnClickListener l) {
        if ("main".equals(Thread.currentThread().getName())) {
            makeSnackbar(view, c, len, title, l);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeSnackbar(view, c, len, title, l);
                }
            });
        }
    }

    /**
     * @param view
     * @param c
     * @param len
     * @param title
     * @param l
     */
    private void makeSnackbar(View view, CharSequence c, int len, CharSequence title, View.OnClickListener l) {
        Snackbar.make(view, c, len).setAction(title, l).show();
    }

    /**
     *
     */
    private ThemeUtils.ExtraRefreshable extraRefreshable = new ThemeUtils.ExtraRefreshable(){

        @Override
        public void refreshGlobal(Activity activity) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null, ThemeUtils.getThemeAttrColor(BaseActivity.this, android.R.attr.colorPrimary));
                setTaskDescription(taskDescription);
                getWindow().setStatusBarColor(ThemeUtils.getColorById(BaseActivity.this, R.color.theme_color_primary_dark));
            }
        }

        @Override
        public void refreshSpecificView(View view) {

        }
    };

    /**
     *
     */
    private class InflaterRunnable implements Runnable {
        private View view;
        private Bundle savedState;
        private LayoutInflater inflater;

        public InflaterRunnable(View view, Bundle savedState, LayoutInflater inflater) {
            this.view = view;
            this.savedState = savedState;
            this.inflater = inflater;
        }

        @Override
        public void run() {
            findView(view);
            initViewData(savedState, inflater);
            setListener();
        }
    }
}
