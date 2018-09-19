package com.liuh.learn.andserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop_server)
    Button btnStop;
    @BindView(R.id.btn_browser)
    Button btnBrowser;
    @BindView(R.id.tv_message)
    TextView tvMsg;

    private ServerManager mServerManager;

    private ProgressDialog mDialog;

    private String mRootUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDialog = new ProgressDialog(this);

        // 注册广播接收者
        mServerManager = new ServerManager(this);
        mServerManager.register();

        // start server
        btnStart.performClick();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServerManager.unRegister();
    }

    @OnClick({R.id.btn_start, R.id.btn_stop_server, R.id.btn_browser})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                showDialog(null, "请稍后...");
                mServerManager.startService();
                break;
            case R.id.btn_stop_server:
                showDialog(null, "请稍后...");
                mServerManager.stopService();
                break;
            case R.id.btn_browser:
                if (!TextUtils.isEmpty(mRootUrl)) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setData(Uri.parse(mRootUrl));
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 接收到 server 已开启的的广播
     *
     * @param ip
     */
    public void serverStart(String ip) {
        Log.e("-----", "serverStart");
        dismissDialog();
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        btnBrowser.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(ip)) {
            List<String> addressList = new LinkedList<>();
            mRootUrl = "http://" + ip + ":8090/";
            addressList.add(mRootUrl);
            addressList.add("http://" + ip + ":8090/login.html");
            addressList.add("http://" + ip + ":8090/image");
            addressList.add("http://" + ip + ":8090/download");
            addressList.add("http://" + ip + ":8090/upload");
            tvMsg.setText(TextUtils.join("\n", addressList));
        } else {
            mRootUrl = null;
            tvMsg.setText(R.string.server_ip_error);
        }
    }

    public void serverError(String message) {
        Log.e("-----", "serverError");
        dismissDialog();
        mRootUrl = null;
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        btnBrowser.setVisibility(View.GONE);
        tvMsg.setText(message);
    }

    public void serverStop() {
        Log.e("-----", "serverStop");
        dismissDialog();
        mRootUrl = null;
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        btnBrowser.setVisibility(View.GONE);
        tvMsg.setText(R.string.server_stop_succeed);
    }

    protected void showDialog(String title, String message) {
        if (mDialog == null || !mDialog.isShowing()) {// 如果不存在，或者没有正在显示，则创建新的dialog并显示
            mDialog = ProgressDialog.show(this, title, message);
        } else {// 否者直接修改现有dialog的title与message
            mDialog.setTitle(title);
            mDialog.setMessage(message);
        }
    }

    protected void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
