package com.able.rxjavademo.utils.retrofit;

import java.io.IOException;

import retrofit2.Call;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/7/3.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public  class CustomCall<R> {

    public final Call<R> call;

    public CustomCall(Call<R> call) {
        this.call = call;
    }

    public R get() throws IOException {
        return call.execute().body();
    }
}