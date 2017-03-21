package com.fanchen.anim;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.fanchen.anim.retrofit.manager.RetrofitManager;
import com.fanchen.anim.util.LogUtil;
import com.fanchen.anim.util.ZXingLibrary;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 整个应用上下文
 * Created by fanchen on 2017/1/18.
 */
public class AnimAppliction extends Application{
    // 用来管理activity的列表,实现对程序整体异常的捕获
    private List<Activity> mActivitys = new ArrayList<>();
    // 当前应用程序fragment队列，主要是用来处理activity中的onBackPresseds事件
    private List<Fragment> mFragments = new ArrayList<>();

    public static AnimAppliction app = null;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        /*** 初始化尺寸工具类*/
        AppCrashHandler.shareInstance(this);
        ZXingLibrary.initDisplayOpinion(this);
        RetrofitManager.init(this);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this); // App的策略Bean
        strategy.setAppChannel(getPackageName()); // 设置渠道
        strategy.setAppVersion(getVersion()); // App的版本
        strategy.setAppReportDelay(100); // 设置SDK处理延时，毫秒
        strategy.setCrashHandleCallback(new AppCrashHandleCallback());
        CrashReport.initCrashReport(this,"", false, strategy); // 自定义策略生效，必须在初始化SDK前调用
        CrashReport.setUserId("BBDTEK");
    }

    /**
     * 向fragment队列添加一个
     */
    public void addFragment(Fragment f) {
        if(mFragments != null)
            mFragments.add(f);
    }

    /**
     * 弹出最上层的fragment
     */
    public Fragment popuFragment() {
        if (mFragments != null && mFragments.size() > 0)
            return mFragments.remove(mFragments.size() - 1);
        return null;
    }

    /**
     * 获取最上层的fragment
     */
    public Fragment getTopFragment() {
        if (mFragments != null && mFragments.size() > 0)
            return mFragments.get(mFragments.size() - 1);
        return null;
    }

    /**
     * 清空fragment队列
     */
    public void clearFragment() {
        if(mFragments != null)
            mFragments.clear();
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        if(mActivitys != null)
            mActivitys.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        if(mActivitys != null)
            mActivitys.add(a);
    }

    /**
     * 获取最上层的Activity
     */
    public Activity getTopActivity() {
        if (mActivitys != null && mActivitys.size() >= 1)
            return mActivitys.get(mActivitys.size() - 1);
        return null;
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        while (mActivitys != null && mActivitys.size() > 0) {
            mActivitys.remove(0).finish();
        }
        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "version:" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "version:";
        }
    }

    // bugly回调
    private class AppCrashHandleCallback extends CrashReport.CrashHandleCallback {
        @Override
        public synchronized Map<String, String> onCrashHandleStart(
                int crashType, String errorType, String errorMessage,
                String errorStack) {
            String crashTypeName = null;
            switch (crashType) {
                case CrashReport.CrashHandleCallback.CRASHTYPE_JAVA_CATCH:
                    crashTypeName = "JAVA_CATCH";
                    break;
                case CrashReport.CrashHandleCallback.CRASHTYPE_JAVA_CRASH:
                    crashTypeName = "JAVA_CRASH";
                    break;
                case CrashReport.CrashHandleCallback.CRASHTYPE_NATIVE:
                    crashTypeName = "JAVA_NATIVE";
                    break;
                case CrashReport.CrashHandleCallback.CRASHTYPE_U3D:
                    crashTypeName = "JAVA_U3D";
                    break;
                default:
                    crashTypeName = "unknown";
            }
            LogUtil.e(getClass(), "Crash Happen Type:" + crashType + " TypeName:" + crashTypeName);
            LogUtil.e(getClass(), "errorType:" + errorType);
            LogUtil.e(getClass(), "errorMessage:" + errorMessage);
            LogUtil.e(getClass(), "errorStack:" + errorStack);
            Map<String, String> userDatas = super.onCrashHandleStart(crashType,errorType, errorMessage, errorStack);
            if (userDatas == null) {
                userDatas = new HashMap<String, String>();
            }
            userDatas.put("DEBUG", "TRUE");
            return userDatas;
        }
    }
}
