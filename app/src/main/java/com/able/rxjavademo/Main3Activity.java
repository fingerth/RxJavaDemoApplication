package com.able.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class Main3Activity extends AppCompatActivity {

    private Disposable d;
    @BindView(R.id.bt1)
    Button bt1;
    @BindView(R.id.bt2)
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        ButterKnife.bind(this);


        RxView.clicks(bt2).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                Main3Activity.this.d = d;
            }

            @Override
            public void onNext(Object value) {
                Main3Activity.this.d.dispose();
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(500);
                        startActivity(new Intent(Main3Activity.this, Main2Activity.class));
                    }
                }.start();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @OnClick({R.id.bt1, R.id.bt2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                new Thread() {
                    @Override
                    public void run() {
                        SystemClock.sleep(500);
                        startActivity(new Intent(Main3Activity.this, Main2Activity.class));
                    }
                }.start();

                break;
            case R.id.bt2:
                break;
        }
    }
}
