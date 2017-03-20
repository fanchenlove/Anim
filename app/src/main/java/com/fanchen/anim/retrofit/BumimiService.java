package com.fanchen.anim.retrofit;

import com.fanchen.anim.entity.Anim;
import com.fanchen.anim.entity.AnimClass;
import com.fanchen.anim.entity.AnimDetails;
import com.fanchen.anim.entity.AnimBangumiHome;
import com.fanchen.anim.entity.AnimRecomHome;
import com.fanchen.anim.parser.annotation.AnimMethodParser;
import com.fanchen.anim.parser.annotation.AnimMethodType;
import com.fanchen.anim.parser.annotation.AnimParser;
import com.fanchen.anim.parser.annotation.AnimType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by fanchen on 2017/1/18.
 */
@AnimParser(AnimType.BUMIMI)
public interface BumimiService {
    @GET("/api.php")
    @AnimParser(AnimType.BUMIMI)
    @AnimMethodParser(AnimMethodType.ITEMLIST)
    Call<ArrayList<Anim>> loadAnimList(@Query("page") int page, @Query("op") String op, @Query("action") String action,  @Query("uuid") String uuid, @Query("appkey") String appkey);

    @GET("/index.php")
    @AnimParser(AnimType.BUMIMI)
    @AnimMethodParser(AnimMethodType.DETAILS)
    Call<AnimDetails> loadAnimDetails( @Query("page") int page,@Query("catid") String catid,@Query("id") String id, @Query("uuid") String uuid, @Query("appkey") String appkey);

    @GET("/index.php?newc=1&m=content&c=index&a=lists&in=app")
    @AnimParser(AnimType.BUMIMI)
    @AnimMethodParser(AnimMethodType.HOME)
    Call<AnimBangumiHome> loadBangumiHome(@Query("page") int page,@Query("catid") String catid,@Query("uuid") String uuid,@Query("appkey") String appkey);

    @GET("/api.php?newc=1&op=app_new&a=fenlei&action=comic")
    @AnimParser(AnimType.BUMIMI)
    @AnimMethodParser(AnimMethodType.REGION)
    Call<ArrayList<AnimClass<String>>> loadRegionList(@Query("uuid") String uuid,@Query("appkey") String appkey);

    @GET("/app_cache/default.php")
    @AnimParser(AnimType.BUMIMI)
    @AnimMethodParser(AnimMethodType.RECOM)
    Call<AnimRecomHome> loadRecomHome();
}
