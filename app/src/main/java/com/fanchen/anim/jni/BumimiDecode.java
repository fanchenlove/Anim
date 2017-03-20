package com.fanchen.anim.jni;

import com.fanchen.anim.util.SecurityUtil;

/**
 *
 * Created by fanchen on 2017/1/18.
 */
public class BumimiDecode {

    public static boolean isloadLibrary = true;

    static {
        try {
            System.loadLibrary("anim_decode");
        } catch (UnsatisfiedLinkError e) {
            isloadLibrary = false;
            e.printStackTrace();
        }
    }

    /**
     * 解密实体Json数据
     *
     * @param str
     * @return
     */
    private static native String decodeInfoJson(String str);

    /**
     * 解密播放URLJson数据
     *
     * @param str
     * @return
     */
    private static native String decodeUrlJson(String str);


    /**
     * @param strParameter
     * @return
     */
    private static native String signBiliParameter(String strParameter);

    /**
     * 解密实体Json数据
     *
     * @param str
     * @return
     */
    public static String decodeJson(String str) {
        if (isloadLibrary) {
            byte[] decode = SecurityUtil.decode(str);
            return decodeInfoJson(new String(decode));
        }
        return "";
    }

    /**
     * 对bili请求参数进行签名
     *
     * @param str
     * @return
     */
    public static String biliParameter(String str) {
        if (isloadLibrary) {
            return signBiliParameter(str);
        }
        return "";
    }

    /**
     * 解密播放URLJson数据
     *
     * @param str
     * @return
     */
    public static String decodeUrl(String str) {
        if (isloadLibrary) {
            String decodeUrlJson = decodeUrlJson(str);
            return decodeUrlJson;
        }
        return "";
    }
}
