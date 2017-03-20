package com.fanchen.anim.retrofit.converter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fanchen.anim.R;
import com.fanchen.anim.entity.Anim;
import com.fanchen.anim.entity.AnimBangumi;
import com.fanchen.anim.entity.AnimBanner;
import com.fanchen.anim.entity.AnimClass;
import com.fanchen.anim.entity.AnimDetails;
import com.fanchen.anim.entity.AnimEpisode;
import com.fanchen.anim.entity.AnimBangumiHome;
import com.fanchen.anim.entity.AnimItem;
import com.fanchen.anim.entity.AnimRecom;
import com.fanchen.anim.entity.AnimRecomHome;
import com.fanchen.anim.entity.inter.IAmin;
import com.fanchen.anim.jni.BumimiDecode;
import com.fanchen.anim.parser.annotation.AnimMethodType;
import com.fanchen.anim.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org .json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * Created by fanchen on 2017/1/18.
 */
public class BumimiConverter extends BaseStringConverter {

    public static final int[] DRAWABLE = new int[]{R.drawable.ic_avatar1,R.drawable.ic_avatar2,R.drawable.ic_avatar3,R.drawable.ic_avatar4,
            R.drawable.ic_avatar5,R.drawable.ic_avatar6,R.drawable.ic_avatar7,R.drawable.ic_avatar1,R.drawable.ic_avatar8,
            R.drawable.ic_avatar9,R.drawable.ic_avatar10,R.drawable.ic_avatar11};

        public static final int[] BANGUMI_DRAWABLE = new int[]{R.drawable.bangumi_home_ic_season_1,
            R.drawable.bangumi_home_ic_season_2, R.drawable.bangumi_home_ic_season_3,
            R.drawable.bangumi_home_ic_season_4};

    public BumimiConverter(AnimMethodType animMethodType) {
        super(animMethodType);
    }

    @Override
    protected Object convert(String str, AnimMethodType methodType) throws IOException {
        if(methodType == AnimMethodType.PLAYURL){
            String url = BumimiDecode.decodeUrl(str);
        }else{
            String json = BumimiDecode.decodeJson(str);
            LogUtil.e(getClass(),json);
            if(!TextUtils.isEmpty(json)){
                try{
                    if(methodType == AnimMethodType.HOME){
                        return getHome(json);
                    }else if(methodType == AnimMethodType.SEARCH){

                    }else  if(methodType == AnimMethodType.ITEMLIST){
                        return getItems(json);
                    }else  if(methodType == AnimMethodType.DETAILS){
                        return getDetails(json);
                    }else if(methodType == AnimMethodType.REGION){
                       return getAnimClasss(json);
                    }else if(methodType == AnimMethodType.RECOM){
                        return getRecom(json);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    throw new IOException("parser data error");
                }
            }
        }
        return null;
    }

    private AnimRecomHome getRecom(String json) throws JSONException{
        AnimRecomHome home = new AnimRecomHome();
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.has("hdp")){
            ArrayList<AnimBanner> banners = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("hdp");
            for (int i = 0 ; i < jsonArray.length() ; i ++){
                JSONObject o = jsonArray.getJSONObject(i);
                AnimBanner banner = new AnimBanner();
                banner.setTitle(o.has("title") ? o.getString("title") : "");
                banner.setCover(o.has("thumb") ? o.getString("thumb") : "");
                banner.setCid(o.has("cat_id") ? o.getString("cat_id") : "");
                banner.setId(o.has("link_url") ? o.getString("link_url") : "");
                banner.setSource(IAmin.BUMIMI);
                banners.add(banner);
            }
            home.setBanners(banners);
        }
        ArrayList<AnimRecom> items = new ArrayList<>();
        if(jsonObject.has("hot_tj")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("热门推荐");
            animBangumi.setType(AnimRecom.TYPE_VERTICAL);
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("hot_tj")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[2]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("hot_ysj")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("热门影视剧");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("hot_ysj")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[1]));
            animBangumi.setIsResource(true);
            animBangumi.setType(AnimRecom.TYPE_VERTICAL);
            items.add(animBangumi);
        }
        if(jsonObject.has("hot_zy")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("热门综艺");
            animBangumi.setType(AnimRecom.TYPE_VERTICAL);
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("hot_zy")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[0]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("recommend")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("推荐观看");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("recommend")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[0]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("new_dm")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("最新动漫");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("new_dm")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[3]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("donghua")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("动画");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("donghua")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[1]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("music")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("音乐");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("music")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[2]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("yuleshishang")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("娱乐时尚");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("yuleshishang")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[3]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("game")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("游戏");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("game")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[0]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("gaoxiao")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("搞笑");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("gaoxiao")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[1]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        if(jsonObject.has("kexue")){
            AnimRecom animBangumi = new AnimRecom();
            animBangumi.setTitle("科学");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("kexue")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[2]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        home.setItems(items);
        return home;
    }

    private List<AnimClass<Serializable>> getAnimClasss(String json)  throws JSONException {
        List<AnimClass<Serializable>> animClasses = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.has("data")){
            JSONArray data = jsonObject.getJSONArray("data");
            for (int i = 0 ; i < data.length() ; i++){
                JSONObject jobj = data.getJSONObject(i);
                AnimClass<Serializable> animClass = new AnimClass<>();
                animClass.setTitle(jobj.has("name") ? jobj.getString("name") : "");
                animClass.setData(jobj.has("typeid") ? jobj.getString("typeid") : "");
                animClass.setDrawable(DRAWABLE[i % DRAWABLE.length]);
                animClass.setSource(IAmin.BUMIMI);
                animClasses.add(animClass);
            }
        }
        return animClasses;
    }

    @NonNull
    private AnimDetails getDetails(String json) throws JSONException {
        AnimDetails details = new AnimDetails();
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.has("data")){
            JSONObject data = jsonObject.getJSONObject("data");
            details.setCover(data.has("thumb") ? data.getString("thumb") : "");
            details.setTitle(data.has("title") ? data.getString("title") : "");
            details.setTags(data.has("sort") ? data.getJSONArray("sort") != null ? data.getJSONArray("sort").toString() : "" : "");
            details.setAuthor(data.has("area") ? data.getString("area") : "");
            details.setSource(IAmin.BUMIMI);
            details.setCatid(data.has("catid") ? data.getString("catid") : "");
            details.setId(data.has("id") ? data.getString("id") : "");
            details.setIntro(data.has("description") ? data.getString("description") : "");
            details.setState(data.has("beizhu") ? data.getString("beizhu") : "");
            details.setTime(data.has("time") ? data.getString("time") : "");
        }
        List<AnimEpisode> episodes = new ArrayList<>();
        List<AnimItem> items = new ArrayList<>();
        details.setRecommends(items);
        details.setEpisodes(episodes);
        if(jsonObject.has("playlists")){
            JSONArray playlists = jsonObject.getJSONArray("playlists");
            for (int i = 0 ; i < playlists.length() ; i++){
                JSONObject jobj = new JSONObject(playlists.get(i).toString());
                AnimEpisode ep = new AnimEpisode();
                ep.setSource(IAmin.BUMIMI);
                ep.setTitle(jobj.has("t") ? jobj.getString("t") : "");
                ep.setCatid(jobj.has("tnum") ? jobj.getString("tnum") : "");
                ep.setId(jobj.has("vid") ? jobj.getString("vid") : "");
                episodes.add(ep);
            }
        }
        if(jsonObject.has("serie")){
            JSONArray serie = jsonObject.getJSONArray("serie");
            for (int i = 0 ; i < serie.length() ; i++){
                JSONObject jobj = new JSONObject(serie.get(i).toString());
                AnimItem item = new AnimItem();
                item.setCover(jobj.has("thumb") ? jobj.getString("thumb") : "");
                item.setTitle(jobj.has("title") ? jobj.getString("title") : "");
                item.setSource(IAmin.BUMIMI);
                item.setLastEpisode(jobj.has("beizhu") ? jobj.getString("beizhu") : "");
                item.setCatid(jobj.has("catid") ? jobj.getString("catid") : "");
                item.setId(jobj.has("id") ? jobj.getString("id") : "");
                items.add(item);
            }
        }
        return details;
    }

    @NonNull
    private  List<Anim> getItems(String json) throws JSONException {
        List<Anim> animList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONArray news_lianzai = jsonObject.getJSONArray("news_lianzai");
        for (int i = 0; i < news_lianzai.length(); i ++ ){
            String jStr = news_lianzai.get(i).toString();
            JSONObject obj = new JSONObject(jStr);
            Anim anim = new Anim();
            anim.setCover(obj.has("thumb") ? obj.getString("thumb") : "");
            anim.setTitle(obj.has("title") ? obj.getString("title") : "");
            anim.setLastEpisode(obj.has("add_vtitle") ? obj.getString("add_vtitle") : "");
            anim.setState(obj.has("beizhu") ? obj.getString("beizhu") : "");
            anim.setTime(obj.has("time") ? obj.getString("time") : "");
            anim.setCatid(obj.has("catid") ? obj.getString("catid") : "");
            anim.setId(obj.has("id") ? obj.getString("id") : "");
            anim.setSource(IAmin.BUMIMI);
            animList.add(anim);
        }
        return animList;
    }

    private AnimBangumiHome getHome(String json) throws JSONException{
        AnimBangumiHome home = new AnimBangumiHome();
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.has("hdp")){
            ArrayList<AnimBanner> banners = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("hdp");
            for (int i = 0 ; i < jsonArray.length() ; i ++){
                JSONObject o = jsonArray.getJSONObject(i);
                AnimBanner banner = new AnimBanner();
                banner.setTitle(o.has("title") ? o.getString("title") : "");
                banner.setCover(o.has("thumb") ? o.getString("thumb") : "");
                banner.setCid(o.has("cat_id") ? o.getString("cat_id") : "");
                banner.setId(o.has("link_url") ? o.getString("link_url") : "");
                banner.setSource(IAmin.BUMIMI);
                banners.add(banner);
            }
            home.setBanners(banners);
        }
        ArrayList<AnimBangumi> items = new ArrayList<>();
        //最新更新
        if(jsonObject.has("news")){
            AnimBangumi animBangumi = new AnimBangumi();
            animBangumi.setTitle("最新更新");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("news")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[0]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        //新番连载
        if(jsonObject.has("news_lianzai")){
            AnimBangumi animBangumi = new AnimBangumi();
            animBangumi.setTitle("新番连载");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("news_lianzai")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[1]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        //国产动漫
        if(jsonObject.has("guoc")){
            AnimBangumi animBangumi = new AnimBangumi();
            animBangumi.setTitle("国产动漫");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("guoc")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[2]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        //日韩动漫
        if(jsonObject.has("rihan")){
            AnimBangumi animBangumi = new AnimBangumi();
            animBangumi.setTitle("日韩动漫");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("rihan")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[3]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        //欧美动漫
        if(jsonObject.has("guowai")){
            AnimBangumi animBangumi = new AnimBangumi();
            animBangumi.setTitle("欧美动漫");
            animBangumi.setItems(parserItem(jsonObject.getJSONArray("guowai")));
            animBangumi.setCover(String.valueOf(BANGUMI_DRAWABLE[0]));
            animBangumi.setIsResource(true);
            items.add(animBangumi);
        }
        home.setItems(items);
        return home;
    }

    @NonNull
    private List<AnimItem> parserItem(JSONArray jsonArray) throws JSONException {
        List<AnimItem> news = new ArrayList<>();
        for (int i = 0 ; i < jsonArray.length() ; i ++){
            JSONObject o = jsonArray.getJSONObject(i);
            AnimItem item = new AnimItem();
            item.setId(o.has("id") ? o.getString("id") : "");
            item.setCatid(o.has("catid") ? o.getString("catid") : "");
            item.setTitle(o.has("title") ? o.getString("title") : "");
            item.setCover(o.has("thumb") ? o.getString("thumb") : "");
            item.setLastEpisode(o.has("add_vtitle") ? o.getString("add_vtitle") : "");
            item.setTime(o.has("time") ? o.getString("time") : "");
            item.setSource(IAmin.BUMIMI);
            news.add(item);
        }
        return news;
    }
}
