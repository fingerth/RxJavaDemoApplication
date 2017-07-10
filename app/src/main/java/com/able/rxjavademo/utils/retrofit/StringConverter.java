package com.able.rxjavademo.utils.retrofit;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/6/30.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public class StringConverter implements Converter<ResponseBody, String> {

    public static final StringConverter INSTANCE = new StringConverter();

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }


}