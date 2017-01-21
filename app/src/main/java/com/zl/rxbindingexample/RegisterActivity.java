package com.zl.rxbindingexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "RegisterActivity";

    @BindView(R.id.bt)
    Button mBt;

    private Observable<Void> verifyCodeObservable;

    private static int SECOND = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        verifyCodeObservable = RxView.clicks(mBt)
                .throttleFirst(SECOND, TimeUnit.SECONDS) //防止20秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        RxView.enabled(mBt).call(false);
                    }
                });

        verifyCodeObservable.subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                                .take(SECOND)
                                .subscribe(new Observer<Long>() {
                                    @Override
                                    public void onCompleted() {
                                        RxTextView.text(mBt).call("获取验证码");
                                        RxView.enabled(mBt).call(true);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e(TAG, e.toString());
                                    }

                                    @Override
                                    public void onNext(Long aLong) {
                                        RxTextView.text(mBt).call("剩余" + (SECOND - aLong) + "秒");
                                    }
                                });
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        verifyCodeObservable.unsubscribeOn(AndroidSchedulers.mainThread()); //防止泄露
    }

}
