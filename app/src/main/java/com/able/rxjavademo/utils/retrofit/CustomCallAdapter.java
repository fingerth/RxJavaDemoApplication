package com.able.rxjavademo.utils.retrofit;

import com.able.rxjavademo.R;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/7/3.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public  class CustomCallAdapter implements CallAdapter<R,CustomCall<?>> {

    private final Type responseType;

    // 下面的 responseType 方法需要数据的类型
    CustomCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public CustomCall<R> adapt(Call<R> call) {
        return new CustomCall<>(call);
    }


//    @Override
//    public <R> CustomCall<R> adapt(Call<R> call) {
//        // 由 CustomCall 决定如何使用
//        return new CustomCall<>(call);
//    }
}