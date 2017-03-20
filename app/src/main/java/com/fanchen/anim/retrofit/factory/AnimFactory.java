package com.fanchen.anim.retrofit.factory;


import com.fanchen.anim.parser.annotation.AnimMethodParser;
import com.fanchen.anim.parser.annotation.AnimMethodType;
import com.fanchen.anim.parser.annotation.AnimParser;
import com.fanchen.anim.parser.annotation.AnimType;
import com.fanchen.anim.retrofit.converter.BumimiConverter;
import com.fanchen.anim.retrofit.converter.DiliConverter;
import com.fanchen.anim.retrofit.converter.FengcheConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 *
 * Created by fanchen on 2017/1/10.
 */
public class AnimFactory extends Converter.Factory {
    public static AnimFactory create() {
        return new AnimFactory();
    }
    private AnimFactory() {
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return fromResponseBody(type, annotations);
    }

    public Converter<ResponseBody, ?> fromResponseBody(Type type, Annotation[] annotations) {
        if(annotations == null)throw new NullPointerException("annotations == null");
        AnimParser parser = null;
        for (Annotation a : annotations){
            if(a instanceof AnimParser){
                parser = (AnimParser) a;
                break;
            }
        }
        if(parser == null) throw new NullPointerException("AnimParser == null");
        AnimMethodParser method = null;
        for (Annotation a : annotations){
            if(a instanceof AnimMethodParser){
                method = (AnimMethodParser) a;
                break;
            }
        }
        if(method == null) throw new NullPointerException("AnimMethodParser == null");
        AnimMethodType methodValue = method.value();
        Converter<ResponseBody, Object> converter = null;
        AnimType animValue = parser.value();
        if(animValue == AnimType.DILIDILI){
            converter = new DiliConverter(methodValue);
        }else if(animValue == AnimType.BUMIMI){
            converter = new BumimiConverter(methodValue);
        }else if(animValue == AnimType.FENGCHE_COMIC){
            converter = new FengcheConverter(methodValue);
        }
        if(converter == null) throw new NullPointerException("AnimType  is inexistence");
        return converter;
    }
}
