package com.fanchen.anim.retrofit;

import com.fanchen.anim.entity.Anim;
import com.fanchen.anim.entity.AnimDetails;
import com.fanchen.anim.parser.annotation.AnimMethodParser;
import com.fanchen.anim.parser.annotation.AnimMethodType;
import com.fanchen.anim.parser.annotation.AnimParser;
import com.fanchen.anim.parser.annotation.AnimType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by fanchen on 2017/1/10.
 */
@AnimParser(AnimType.DILIDILI)
public interface DiliService {

    @GET("anime/{time}/")
    @AnimParser(AnimType.DILIDILI)
    @AnimMethodParser(AnimMethodType.ITEMLIST)
    Call<List<Anim>> loadAnimList(@Path("time") String time);

    @GET("anime/{name}/")
    @AnimParser(AnimType.DILIDILI)
    @AnimMethodParser(AnimMethodType.DETAILS)
    Call<AnimDetails> loadAnimDetails(@Path("name") String name);


}
