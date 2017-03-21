package com.fanchen.anim.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fanchen.anim.AnimAppliction;
import com.fanchen.anim.util.LogUtil;


/**
 * 应用中所有的fragment基础该类
 *
 * 提供一些基本方法的封装
 * Created by fanchen on 2017/1/21.
 */
public abstract class BaseFragment extends Fragment {
    /**
     *该Fragment的主视图
     */
    private View mMainView;
    /**
     *该Fragment绑定的activity
     */
    public BaseActivity activity;
    /**
     *appliction
     */
    public AnimAppliction appliction;

    // 标志位 标志已经初始化完成。
    protected volatile boolean isPrepared = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (BaseActivity) getActivity();
        appliction = activity.appliction;
        int layout = getLayout();
        if(layout > 0){
            mMainView = inflater.inflate(layout, container, false);
            return mMainView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //用户可见，并且没有初始化
        if(!isPrepared && getUserVisibleHint() && view != null){
            LogUtil.e(getClass(),"onViewCreated => " + toString());
            mMainView.post(new InitRunnable(view, savedInstanceState));
        }
    }

    private  void initFragment(View view, @Nullable Bundle savedInstanceState){
        synchronized(BaseFragment.class){
            findView(view);
            initViewData(savedInstanceState, getArguments());
            setListener();
            isPrepared = true;
        }
    }

    /**
     * Fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && mMainView != null && !isPrepared) {
            LogUtil.e(getClass(),"setUserVisibleHint => " + toString());
            mMainView.post(new InitRunnable(mMainView,null));
        }
    }

    /**
     * 应用布局文件id
     * @return
     */
    protected abstract int getLayout();

    /**
     * 查找必要控件
     * @param v
     */
    protected void findView(View v){}

    /**
     * 初始化视图控件及数据
     * @param savedInstanceState
     * @param args
     */
    protected void initViewData(@Nullable Bundle savedInstanceState, Bundle args){}
    /**
     *设置监听器
     */
    protected void setListener(){}

    /**
     *
     * @param attr
     * @return
     */
    public int getAttributeValue(int attr){
        if(isAdded() && ! isDetached() && activity != null){
            return activity.getAttributeValue(attr);
        }
        return -1;
    }

    /**
     * 通过id查找控件，在mMainView上
     * @param id 控件id
     * @param <T>
     * @return
     */
    public <T extends View> T findViewById(int id) {
        return mMainView == null ? null : (T) mMainView.findViewById(id);
    }

    /**
     * 通过Child position 查找控件。在mMainView上
     * @param position 下标
     * @param <T>
     * @return
     */
    public <T extends View> T getChild(int position){
        return mMainView == null ? null : (mMainView instanceof ViewGroup) ? (T)((ViewGroup)mMainView).getChildAt(position) : null;
    }

    /**
     * 打开一个Activity
     * @param clazz
     */
    public void startActivity(Class<?> clazz) {
        if(isAdded() && ! isDetached() && activity != null){
            startActivity(new Intent(activity, clazz));
        }
    }

    /**
     * 打开一个Activity
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz, Bundle bundle) {
        if(isAdded() && ! isDetached() && activity != null){
            Intent intent = new Intent(activity, clazz);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * 打开一个Service
     * @param clazz
     */
    public void startService(Class<?> clazz) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.startService(clazz);
        }
    }

    /**
     * 打开一个Service
     * @param clazz
     * @param bundle
     */
    public void startService(Class<?> clazz, Bundle bundle) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.startService(clazz, bundle);
        }
    }

    /**
     * 开启activity并在返回时获取返回值
     * @param clazz
     * @param code
     */
    public void startActivityForResult(Class<?> clazz, int code) {
        if(isAdded() && ! isDetached() && activity != null){
            startActivityForResult(new Intent(activity, clazz), code);
        }
    }

    /**
     * 开启activity并在返回时获取返回值
     * @param clazz
     * @param bundle
     * @param code
     */
    public void startActivityForResult(Class<?> clazz, Bundle bundle, int code) {
        if(isAdded() && ! isDetached() && activity != null){
            Intent intent = new Intent(activity, clazz);
            intent.putExtras(bundle);
            startActivityForResult(intent, code);
        }
    }

    /**
     *
     * @return
     */
    public LayoutInflater getLayoutInflater(){
        if(isAdded() && ! isDetached() && activity != null){
            return activity.getLayoutInflater();
        }
        return null;
    }

    /**
     * 获取EditText 的字符串
     * @param editText
     * @return
     */
    public String getEditTextString(EditText editText) {
        if(isAdded() && ! isDetached() && activity != null){
            return activity.getEditTextString(editText);
        }
        return "";
    }

    /**
     * 显示一个Toast
     * @param id
     */
    public void showToast(int id) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showToast(id);
        }
    }

    /**
     * 显示一个Toast
     * @param c
     */
    public void showToast(CharSequence c) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showToast(c);
        }
    }

    /**
     * 显示一个Toast
     * @param id
     * @param len
     */
    public void showToast(int id, int len) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showToast(id, len);
        }
    }

    /**
     * 显示一个Snackbar
     * @param view
     * @param c
     * @param len
     */
    public void showSnackbar(View view, CharSequence c, int len) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showSnackbar(view, c, len);
        }
    }

    /**
     * 显示一个Snackbar
     * @param c
     */
    public void showSnackbar(CharSequence c) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showSnackbar( c);
        }
    }

    /**
     * 显示一个Snackbar
     * @param view
     * @param c
     */
    public void showSnackbar(View view, CharSequence c) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showSnackbar(view,c);
        }
    }

    /**
     * 显示一个Snackbar
     * @param view
     * @param c
     * @param title
     * @param l
     */
    public void showSnackbar(View view, CharSequence c, CharSequence title, View.OnClickListener l) {
        if(isAdded() && ! isDetached() && activity != null){
            activity.showSnackbar(view,c,title,l);
        }
    }

    private class InitRunnable implements Runnable{

        private View view;
        private @Nullable Bundle savedInstanceState;

        public InitRunnable(View view, @Nullable Bundle savedInstanceState){
            this.view = view;
            this.savedInstanceState = savedInstanceState;
        }

        @Override
        public void run() {
            initFragment(view,savedInstanceState);
        }
    }

}
