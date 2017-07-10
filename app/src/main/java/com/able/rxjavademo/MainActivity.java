package com.able.rxjavademo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.able.rxjavademo.adapter.ListViewAdapter;
import com.able.rxjavademo.myutils.LocalSaveFileUtils;
import com.able.rxjavademo.myutils.StaticUtils;
import com.able.rxjavademo.myutils.ToastUtils;
import com.able.rxjavademo.myutils.premission.PermissionUtils;
import com.able.rxjavademo.utils.LogUtils;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    @BindView(R.id.lv)
    ListView lv;
    private ListViewAdapter adapter;

    /**
     * map                  一對一
     * flatMap  concatMap   一對多
     * zip                  二對一
     * <p>
     * <p>
     * filter  過濾器，返回true， false的拋棄
     * sample  定時 發送一條，其他的丟掉
     */
//    Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
//    Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
//    Schedulers.newThread() 代表一个常规的新线程
//    AndroidSchedulers.mainThread() 代表Android的主线程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StaticUtils.getScreen(this);
        ButterKnife.bind(this);
        PermissionUtils.getPersimmions(this);
    }

    @OnClick({R.id.button, R.id.button2,R.id.button3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
//                doRxJava6();
                doRxJavaDemoMothed();
                break;
            case R.id.button2:
                ToastUtils.showToast(this, "555");
                se.request(96);
                break;
            case R.id.button3:
                initSetAdapter();
                break;
        }
    }

    private List<String> listStr = new ArrayList<>();

    private ObservableEmitter<File> emitter;
    private FlowableEmitter<File> e;

    private void viewFiles(File file) {
        if (emitter != null) {
            emitter.onNext(file);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                LogUtils.setLog("LocalSaveFileUtils", "圖片路徑 == " + (i + 1));
                if (files[i].isDirectory()) {
                    viewFiles(files[i]);
                }
            }
        }
    }

    private void viewFlowableFiles(File file) {
        if (e != null) {
            boolean flag = false;
            while (MainActivity.this.e.requested() == 0) {
                if (!flag) {
                    Log.d("LocalSaveFileUtils", "Oh no! I can't emit value!");
                    flag = true;
                }
            }
            e.onNext(file);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                LogUtils.setLog("LocalSaveFileUtils", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx == " + (i + 1));
                if (files[i].isDirectory()) {
                    viewFlowableFiles(files[i]);
                }
            }
        }
    }


    private int i = 1;

    private void doRxJavaDemoMothed() {
        Flowable
                .create(new FlowableOnSubscribe<File>() {
                    @Override
                    public void subscribe(FlowableEmitter<File> e) throws Exception {
                        MainActivity.this.e = e;
                        //viewFlowableFiles(LocalSaveFileUtils.getImgFiles());
                        viewFlowableFiles(Environment.getExternalStorageDirectory());
                    }
                }, BackpressureStrategy.ERROR)
                .flatMap(new Function<File, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(File file) throws Exception {
                        return PublishProcessor.fromArray(file.listFiles());
                    }
                })
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) throws Exception {
                        return file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png");
                    }
                })
                .map(new Function<File, String>() {
                    @Override
                    public String apply(File file) throws Exception {
                        LogUtils.setLog("LocalSaveFileUtils", "圖片路徑 == " + i++ + " ;file.getAbsolutePath():" + file.getAbsolutePath());
                        return file.getAbsolutePath();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        LogUtils.setLog("LocalSaveFileUtils", "Subscription");
                        se = s;
                        se.request(5);
                    }

                    @Override
                    public void onNext(String s) {
                        listStr.add(s);
                        LogUtils.setLog("LocalSaveFileUtils", "listStr.add(s); == " + listStr.size() + ";圖片路徑" + s);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        //创建一个上游 Observable：
//        Observable<File> observable = Observable.create(new ObservableOnSubscribe<File>() {
//            @Override
//            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
//                MainActivity.this.emitter = emitter;
//                viewFiles(LocalSaveFileUtils.getImgFiles());
//            }
//        });
//
//        //建立连接
//        observable
//                .flatMap(new Function<File, ObservableSource<File>>() {
//                    @Override
//                    public ObservableSource<File> apply(File file) throws Exception {
//                        return Observable.fromArray(file.listFiles());
//                    }
//                })
//                .filter(new Predicate<File>() {
//                    @Override
//                    public boolean test(File file) throws Exception {
//                        return file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png");
//                    }
//                })
//                .map(new Function<File, String>() {
//                    @Override
//                    public String apply(File file) throws Exception {
//                        return file.getAbsolutePath();
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        LogUtils.setLog("LocalSaveFileUtils", "圖片路徑 == " + s);
//                        listStr.add(s);
//                    }
//                });

//        initSetAdapter();

    }

    private void initSetAdapter() {
        if (adapter == null) {
            adapter = new ListViewAdapter(this, listStr);
            lv.setAdapter(adapter);
        } else {
            adapter.setListStr(listStr);
        }
    }

    //    private void doRxJava() {
//        //创建一个上游 Observable：
//        //创建一个下游 Observer
//        //建立连接
//    }

    private void doRxJava6() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "current requested: " + emitter.requested());
                boolean flag;
                for (int i = 0; ; i++) {
                    flag = false;
                    while (emitter.requested() == 0) {
                        if (!flag) {
                            Log.d(TAG, "Oh no! I can't emit value!");
                            flag = true;
                        }
                    }
                    emitter.onNext(i);
                    Log.d(TAG, "emit " + i + " , requested = " + emitter.requested());
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private static Subscription mSubscription;
    private static Subscription se;

    public static void request(long n) {
        if (mSubscription != null) {
            mSubscription.request(n); //在外部调用request请求上游
        }
    }

    /**
     * 创建一个上游 Flowable
     * 创建一个下游 Subscriber
     * 连接还是通过subscribe()
     * 解決Backpressure（背壓）
     */
    private void doRxJava5() {
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "上游  haha current requested: " + emitter.requested());
//                Log.d(TAG, "上游 emit 1");
//                emitter.onNext(1);
//                Log.d(TAG, "上游 emit 2");
//                emitter.onNext(2);
//                Log.d(TAG, "上游 emit 3");
//                emitter.onNext(3);
//                Log.d(TAG, "上游 emit complete");
//                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); //增加了一个参数

        Subscriber<Integer> downstream = new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "下游 onSubscribe");
                //s.request(Long.MAX_VALUE);  //注意这句代码
                mSubscription = s;
                s.request(10);

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "下游 onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                Log.w(TAG, "下游 onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "下游 onComplete");
            }
        };

        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(downstream);
    }

    private void doRxJava4() {
        //创建一个上游 Observable：
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<Integer, Integer, String>() {
            @Override
            public String apply(Integer integer, Integer integer2) throws Exception {
                int count = integer + integer2;
                return "這：總和為 " + count + " 哦。";
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "" + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.w(TAG, throwable);
            }
        });
        //创建一个下游 Observer
        //建立连接
    }

    private void doRxJava3() {
        //创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });
        //创建一个下游 Observer
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "" + s);
            }
        };
        //建立连接   無序 flatMap   有序 concatMap
        observable.flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("這是：第 " + integer + " 列，裡面的第 " + i + " 個。");
                }
                int random = 1 + (int) (Math.random() * 10);
                return Observable.fromIterable(list).delay(random, TimeUnit.MILLISECONDS);
            }
        }).subscribe(consumer);
    }

    private void doRxJava2() {
        //创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onComplete();
            }
        });

        //创建一个下游 Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        //建立连接
        observable
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "這是：第 " + integer + " 個。";
                    }
                })
                .subscribe(observer);

    }

    private void doRxJava1() {
        //创建一个上游 Observable：
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                Log.d(TAG, "Observable thread is : " + Thread.currentThread().getName());
                Log.d(TAG, "emit 1");

                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "subscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "Observer thread is :" + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + integer);

                Log.d(TAG, "" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete");
            }
        };
        //建立连接
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


}
