package com.zl.rxbindingexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    @BindView(R.id.bt_1)
    Button mBt1;
    @BindView(R.id.bt_2)
    Button mBt2;
    @BindView(R.id.bt_3)
    Button mBt3;
    @BindView(R.id.bt_4)
    Button mBt4;

    private Subscription mSubscription;
    private int i = 0; //记录点击次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_1, R.id.bt_2, R.id.bt_3, R.id.bt_4})
    public void onClick(final View view) {
        //多控件组合防抖

        unSubscribe();
        i++;

        mSubscription = Observable.create(new Observable.OnSubscribe<View>(){
            @Override
            public void call(Subscriber<? super View> subscriber) {
                if (subscriber.isUnsubscribed())
                    return;
                Log.i(TAG, "按钮点击" + i + "次");
                subscriber.onNext(view);
            }
        })  .buffer(500, TimeUnit.MILLISECONDS) // 缓存0.5s内的点击
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<List<View>>() {
                @Override
                public void call(List<View> list) {
                    if(list.size() == 0){
                        return;
                    }
                    View view = list.get(list.size() - 1);// 只响应0.5秒内按的最后一次点击事件
                    i = 0;

                    switch (view.getId()) {
                        case R.id.bt_1:
                            Log.i(TAG, "1");
                            Intent intent1 = new Intent(MainActivity.this, ButtonClicksActivity.class);
                            startActivity(intent1);
                            break;
                        case R.id.bt_2:
                            Log.i(TAG, "2");
                            Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.bt_3:
                            Log.i(TAG, "3");
                            Intent intent3 = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent3);
                            break;
                        case R.id.bt_4:
                            Log.i(TAG, "4");
                            Intent intent4 = new Intent(MainActivity.this, ListActivity.class);
                            startActivity(intent4);
                            break;
                        default:
                            break;
                    }
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    private void unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

}
