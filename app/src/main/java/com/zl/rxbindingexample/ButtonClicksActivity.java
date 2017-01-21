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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class ButtonClicksActivity extends AppCompatActivity {

    private static String TAG = "ButtonClicksActivity";

    @BindView(R.id.bt)
    Button mBt;
    @BindView(R.id.bt1)
    Button mBt1;

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_click);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        RxTextView.text(mBt).call("猛戳按钮");

        RxView.clicks(mBt)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.i(TAG, "---------");
                    }
                });

//        RxView.clicks(mBt)
//                .throttleLast(1, TimeUnit.SECONDS) //取时间间隔内最后一次事件
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        Log.i(TAG, "---------");
//                    }
//                });


        Observable<Void> observable= RxView.clicks(mBt1).share();

        Subscription s = observable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i(TAG, "第一次");
            }
        });

        mCompositeSubscription.add(s);

        Subscription s1 = observable.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i(TAG, "第二次");
            }
        });

        mCompositeSubscription.add(s1);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

}
