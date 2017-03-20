package com.fanchen.anim.retrofit.converter;

import com.fanchen.anim.parser.annotation.AnimMethodType;
import com.fanchen.anim.util.LogUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * Created by fanchen on 2017/1/18.
 */
public abstract class BaseStringConverter implements Converter<ResponseBody, Object> {
    private AnimMethodType animMethodType;

    public BaseStringConverter(AnimMethodType animMethodType) {
        this.animMethodType = animMethodType;
    }

    @Override
    public Object convert(ResponseBody responseBody) throws IOException {
        byte[] bs = null;
        if (responseBody == null) {
            throw new IOException("response body is empty");
        }
        bs = responseBody.bytes();
        if (bs == null || bs.length == 0) {
            throw new IOException("response body is empty");
        }
        String str = null;
        try {
            str = new String(bs,getCharset());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            throw new IOException("charset is not supported.");
        }
        return convert(str, animMethodType);
    }

    /**
     * @param str
     * @param methodType
     * @return
     * @throws IOException
     */
    protected abstract Object convert(String str, AnimMethodType methodType) throws IOException;

    /**
     *
     * @return
     */
    protected String getCharset(){
        return "UTF-8";
    }
}
