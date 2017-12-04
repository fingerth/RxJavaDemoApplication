package com.able.rxjavademo.utils.retrofitutils;

import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * ======================================================
 * Created by Administrator able_fingerth on 2017/12/4.
 * <p/>
 * compile 'io.reactivex.rxjava2:rxjava:2.1.7'
 * compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
 * compile 'com.squareup.retrofit2:retrofit:2.3.0'
 * compile 'com.squareup.retrofit2:converter-gson:2.3.0'
 * compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
 * compile 'com.squareup.okhttp3:logging-interceptor:3.9.0'
 *
 * If you are using ProGuard you might need to add the following options:
 * -dontwarn okio.**
 * -dontwarn javax.annotation.**
 *
 */
public class RetrofitUtils {
    private final static String TAG = "RetrofitUtils";
    private final static String BASE_URL = "http://tabtabmeal.com/";
    private boolean hasTag = true;

    public Retrofit retrofit;

    private RetrofitUtils(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                // 如是有Gson这类的Converter 一定要放在其它前面
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                // 针对rxjava2.x
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //使用自己创建的OkHttp
                .client(getOkHttpClient())
                .build();
    }

    private static RetrofitUtils instances;

    public static RetrofitUtils getInstances() {
        if (instances == null) {
            instances = new RetrofitUtils(BASE_URL);
        }
        return instances;
    }

    public static RetrofitUtils getInstances(String baseUrl) {
        return new RetrofitUtils(baseUrl);
    }
//1.請求
//               RetrofitUtils.getInstances().retrofit.create(BlogService.class).getBlog3(map).enqueue(new Callback<String>() {
//                   @Override
//                   public void onResponse(Call<String> call, Response<String> response) {
//                       LogUtils.setTag("RetrofitUtils", "Retrofit2數據返回1：" + response.body());
//                   }
//
//                   @Override
//                   public void onFailure(Call<String> call, Throwable t) {
//
//                   }
//               });
//2.Rx請求
//                RetrofitUtils.getInstances().retrofit.create(BlogService.class).getBlog7(map)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<String>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(String value) {
//                                LogUtils.setLog(TAG, "value :  " + value.toString());
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });




    private OkHttpClient getOkHttpClient() {

        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (!hasTag) return;
                Log.d(RetrofitUtils.TAG, "OkHttp====Message: " + message);
            }
        });
        //日志显示级别
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }


    private interface DemoService {
        //        @Path:路径参数
        //        @Query:?后面的参数，例如：?expand="dddddd"
        @GET("api/order/cart.ashx")
        Call<ResponseBody> getBlog(@Query("status") String status, @Query("id") int id, @Query("action") String action, @Query("memberId") String memberId);

        @GET("api/order/cart.ashx")
        Call<String> getBlog3(@QueryMap Map<String, String> map);


        //        @Field:Post传递的参数
        //        @FormUrlEncoded：如果POST请求，传递数据，必须要有
        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<ResponseBody> getBlog2(@Field("status") String status, @Field("id") int id, @Field("action") String action, @Field("memberId") String memberId);

        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<ResponseBody> getBlog4(@FieldMap Map<String, String> map);

        //TODO json

        @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
        @POST("api/UrlPrefix")
        Call<ResponseBody> getBlog5(@Body DemoBody body);


        //TODO RxJava

        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Observable<String> getBlog6(@FieldMap Map<String, String> map);

        @GET("api/order/cart.ashx")
        Observable<String> getBlog7(@QueryMap Map<String, String> map);

    }

    private class DemoBody{
        //{"AppID":"cfsapp!#*2468","OS":"Android","ClientVersion":"0.7"}
        public String AppID;
        public String OS;
        public String ClientVersion;

        public DemoBody() {
            AppID = "cfsapp!#*2468";
            OS = "Android";
            ClientVersion = "0.7";
        }

    }

    private static class StringConverter implements Converter<ResponseBody, String> {

        static final StringConverter INSTANCE = new StringConverter();

        @Override
        public String convert(ResponseBody value) throws IOException {
            return value.string();
        }


    }

    private static class StringConverterFactory extends Converter.Factory {

        static final StringConverterFactory INSTANCE = new StringConverterFactory();

        static StringConverterFactory create() {
            return INSTANCE;
        }

        // 我们只关实现从ResponseBody 到 String 的转换，所以其它方法可不覆盖
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            if (type == String.class) {
                return StringConverter.INSTANCE;
            }
            //其它类型我们不处理，返回null就行
            return null;
        }
    }


}
