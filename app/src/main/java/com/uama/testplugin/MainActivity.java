package com.uama.testplugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("--->", "===================");
        new Test();
        Log.e("--->", "===================");
//        MyClassLoader classLoader = new MyClassLoader();
//        Class cls = classLoader.defineClass("pkg.Comparable", Test.createClass());
//        String name=cls.getName();
        setOnClickListener();
    }


    private void setOnClickListener() {
        Log.i("hello", "hi");
        findViewById(R.id.tx_hello).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }
}
