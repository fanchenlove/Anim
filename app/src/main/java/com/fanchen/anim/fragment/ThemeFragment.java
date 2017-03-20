package com.fanchen.anim.fragment;

import com.fanchen.anim.base.BaseFragment;

/**
 * Created by fanchen on 2017/2/26.
 */
public class ThemeFragment extends BaseFragment{

    @Override
    protected int getLayout() {
        return 0;
    }

    /**
     *
     */
    public interface OnThemeChangeListener{
        /**
         *
         * @param currentTheme
         */
        void onConfirm(int currentTheme);
    }
}
