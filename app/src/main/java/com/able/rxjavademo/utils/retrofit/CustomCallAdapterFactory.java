package com.able.rxjavademo.utils.retrofit;

import com.able.rxjavademo.R;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/7/3.
 * <p/>
 * 版权所有，违者必究！
 * <详情描述/>
 */
public  class CustomCallAdapterFactory extends CallAdapter.Factory {
    public static final CustomCallAdapterFactory INSTANCE = new CustomCallAdapterFactory();

    @Override
    public CallAdapter<R,CustomCall<?>> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        // 获取原始类型
        Class<?> rawType = getRawType(returnType);
        // 返回值必须是CustomCall并且带有泛型
        if (rawType == CustomCall.class && returnType instanceof ParameterizedType) {
            Type callReturnType = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new CustomCallAdapter(callReturnType);
        }
        return null;
    }
}