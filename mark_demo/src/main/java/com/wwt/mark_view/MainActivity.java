package com.wwt.mark_view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.yhongm.mark_core.ScaleView;
import com.wwt.mark_view.StatusBarUtil;

public class MainActivity extends Activity {
    ScaleView arcScaleView;
    TextView outputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arcScaleView = (ScaleView) findViewById(R.id.arc_scaleview);
//        outputValue = (TextView) findViewById(R.id.tv_output_value);
//        arcScaleView.setSelectScaleListener(value -> outputValue.setText("通过接口获取到的当前选择值:" + value));


//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

//        View view = findViewById(R.id.fake_statusbar_view);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
//        layoutParams.height = StatusBarUtil.getStatusBarHeight(this);
//        view.setLayoutParams(layoutParams);
//
//        view.setBackgroundColor(Color.WHITE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            MainActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }

//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

        StatusBarUtil.setStatusViewAttrColor(findViewById(R.id.fake_statusbar_view), this, R.color.translant);
        StatusBarUtil.setLightMode(MainActivity.this);

    }





}
