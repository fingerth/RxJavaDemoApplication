package com.able.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.able.rxjavademo.utils.LogUtils;
import com.able.rxjavademo.utils.retrofit.StringConverterFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

public class Main2Activity extends AppCompatActivity {
    private final static String TAG = "Main2Activity";
    @BindView(R.id.et)
    EditText et;
    //http://tabtabmeal.com/api/order/cart.ashx?status=1&id=235&action=check&memberId=1592
    //{"status":100,"msg":"success","result":{"TotalPrice":0.0}}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Toast.makeText(Main2Activity.this, "text2", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    @OnClick({R.id.button1, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                b2();
                break;
            case R.id.button2:
                break;
        }
    }

    private void b2() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "1");
        map.put("id", "235");
        map.put("action", "check");
        map.put("memberId", "1592");
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://www.baidu.com")
                .baseUrl("http://tabtabmeal.com/")
                //.baseUrl("https://online.centawealth.com/MobileWebAPI/")
                .addConverterFactory(GsonConverterFactory.create())
                // 针对rxjava2.x
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        BlogService service = retrofit.create(BlogService.class);



//        Call<TabTabMealBean> call = service.getBlog21("1", 235, "check", "1592");
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
//        call.enqueue(new Callback<TabTabMealBean>() {
//            @Override
//            public void onResponse(Call<TabTabMealBean> call, Response<TabTabMealBean> response) {
//                try {
//                    LogUtils.setTag(TAG, "Retrofit2數據返回1：" + response.body().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TabTabMealBean> call, Throwable t) {
//                t.printStackTrace();
//                LogUtils.setTag(TAG, "Retrofit2數據返回：error!" + t.toString());
//            }
//        });

        service.getBlog6(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TabTabMealBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.setLog(TAG, "Disposable 11111111111  ");
                    }

                    @Override
                    public void onNext(TabTabMealBean value) {
                        LogUtils.setLog(TAG, "TabTabMealBean value 2222222:  " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.setLog(TAG, "Throwable e 333333333  ");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.setLog(TAG, "onComplete 4444444444444  ");
                    }
                });

    }

    private void b1() {
        Map<String, String> map = new HashMap<>();
        map.put("status", "1");
        map.put("id", "235");
        map.put("action", "check");
        map.put("memberId", "1592");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://tabtabmeal.com/")
                //.baseUrl("https://online.centawealth.com/MobileWebAPI/")
                // 如是有Gson这类的Converter 一定要放在其它前面
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                // 针对rxjava2.x
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        BlogService service = retrofit.create(BlogService.class);

//        Call<ResponseBody> call = service.getBlog(new BodyXxx());
        Call<String> call = service.getBlog22("1", 235, "check", "1592");
        // 用法和OkHttp的call如出一辙,
        // 不同的是如果是Android系统回调方法执行在主线程
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    LogUtils.setTag(TAG, "Retrofit2數據返回1：" + response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                LogUtils.setTag(TAG, "Retrofit2數據返回：error!" + t.toString());
            }
        });
    }

    public interface BlogService2 {
        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<ResponseBody> getBlog(@Field("status") String status, @Field("id") int id, @Field("action") String action, @Field("memberId") String memberId);

//        @Field:Post传递的参数
//        @FormUrlEncoded：如果POST请求，传递数据，必须要有
    }

    public interface BlogService {
        @GET("api/order/cart.ashx")
        Call<ResponseBody> getBlog(@Query("status") String status, @Query("id") int id, @Query("action") String action, @Query("memberId") String memberId);
//        @Path:路径参数
//        @Query:?后面的参数，例如：?expand="dddddd"


        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<ResponseBody> getBlog2(@Field("status") String status, @Field("id") int id, @Field("action") String action, @Field("memberId") String memberId);

        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<String> getBlog22(@Field("status") String status, @Field("id") int id, @Field("action") String action, @Field("memberId") String memberId);

        //        @Field:Post传递的参数
//        @FormUrlEncoded：如果POST请求，传递数据，必须要有
        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<TabTabMealBean> getBlog21(@Field("status") String status, @Field("id") int id, @Field("action") String action, @Field("memberId") String memberId);


        @GET("api/order/cart.ashx")
        Call<ResponseBody> getBlog3(@QueryMap Map<String, String> map);
//        @Path:路径参数
//        @Query:?后面的参数，例如：?expand="dddddd"


        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Call<ResponseBody> getBlog4(@FieldMap Map<String, String> map);
//        @Field:Post传递的参数
//        @FormUrlEncoded：如果POST请求，传递数据，必须要有


        @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
        @POST("api/UrlPrefix")
        Call<ResponseBody> getBlog5(@Body BodyXxx body);

        @FormUrlEncoded
        @POST("api/order/cart.ashx")
        Observable<TabTabMealBean> getBlog6(@FieldMap Map<String, String> map);

    }


    class BodyXxx {
        //{"AppID":"cfsapp!#*2468","OS":"Android","ClientVersion":"0.7"}
        public String AppID;
        public String OS;
        public String ClientVersion;

        public BodyXxx() {
            AppID = "cfsapp!#*2468";
            OS = "Android";
            ClientVersion = "0.7";
        }

    }

    class TabTabMealBean {
        //{"status":100,"msg":"success","result":{"TotalPrice":0.0}}
        public String status;
        public String msg;
        public Result result;

        public class Result {
            public String TotalPrice;

            @Override
            public String toString() {
                return "Result{" +
                        "TotalPrice='" + TotalPrice + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "22TabTabMealBean{" +
                    "status='" + status + '\'' +
                    ", msg='" + msg + '\'' +
                    ", result=" + result.toString() +
                    '}';
        }
    }
}
