package com.fanchen.anim.util;

import com.fanchen.anim.R;
import com.fanchen.anim.entity.Anim;
import com.fanchen.anim.entity.AnimClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanchen on 2017/3/15.
 */
public class BumimiUtil {

    public static ArrayList<AnimClass> getAnimClasss(){
        ArrayList<AnimClass> classes = new ArrayList<>();
        classes.add(new AnimClass(R.drawable.ic_category_live,"番剧"));
        classes.add(new AnimClass(R.drawable.ic_category_t11,"电视"));
        classes.add(new AnimClass(R.drawable.ic_category_t1,"电影"));
        classes.add(new AnimClass(R.drawable.ic_category_promo,"综艺"));
        classes.add(new AnimClass(R.drawable.ic_category_t119,"动画"));
        classes.add(new AnimClass(R.drawable.ic_category_t129,"娱乐"));
        classes.add(new AnimClass(R.drawable.ic_category_t13,"游戏"));
        classes.add(new AnimClass(R.drawable.ic_category_t155,"搞笑"));
        classes.add(new AnimClass(R.drawable.ic_category_t3,"音乐"));
        classes.add(new AnimClass(R.drawable.ic_category_t4,"舞蹈"));
        classes.add(new AnimClass(R.drawable.ic_category_t160,"科技"));
        return classes;
    }

}
