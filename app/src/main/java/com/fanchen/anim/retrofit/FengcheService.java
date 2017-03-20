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
 * Created by fanchen on 2017/1/19.
 */
public interface FengcheService {

    @GET("/list/{cid}-{page}.html")
    @AnimMethodParser(AnimMethodType.ITEMLIST)
    @AnimParser(AnimType.FENGCHE_COMIC)
    Call<List<Anim>> loadAnimList(@Path("cid")String cid,@Path("page")int page);

    @GET("/content/{catid}.html")
    @AnimMethodParser(AnimMethodType.DETAILS)
    @AnimParser(AnimType.FENGCHE_COMIC)
    Call<AnimDetails> loadAnimDetails(@Path("catid")String catid);


}
