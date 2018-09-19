package com.liuh.learn.andserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.liuh.learn.andserver.handler.FileHandler;
import com.liuh.learn.andserver.handler.ImageHandler;
import com.liuh.learn.andserver.handler.LoginHandler;
import com.liuh.learn.andserver.handler.UploadHandler;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.filter.HttpCacheFilter;
import com.yanzhenjie.andserver.website.AssetsWebsite;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2018/9/19 15:02
 * Description:Server Service
 */
public class CoreService extends Service {

    /**
     * AndServer
     */
    private Server mServer;


    @Override
    public void onCreate() {
        super.onCreate();

        mServer = AndServer.serverBuilder()
                .inetAddress(NetUtils.getLocalIPAddress())
                .port(8090)
                .timeout(10, TimeUnit.SECONDS)
                .website(new AssetsWebsite(getAssets(), "web"))
                .registerHandler("/download", new FileHandler())
                .registerHandler("/login", new LoginHandler())
                .registerHandler("/upload", new UploadHandler())
                .registerHandler("/image", new ImageHandler())
                .filter(new HttpCacheFilter())
                .listener(mListener)
                .build();
    }

    /**
     * Server listener
     */
    private Server.ServerListener mListener = new Server.ServerListener() {
        @Override
        public void onStarted() {
            String hostAddress = mServer.getInetAddress().getHostAddress();
            ServerManager.serverStart(CoreService.this, hostAddress);
        }

        @Override
        public void onStopped() {
            ServerManager.serverStop(CoreService.this);
        }

        @Override
        public void onError(Exception e) {
            ServerManager.serverError(CoreService.this, e.getMessage());
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }

    /**
     * Start server
     */
    private void startServer() {
        if (mServer != null) {
            if (mServer.isRunning()) {
                String hostAddress = mServer.getInetAddress().getHostAddress();
                ServerManager.serverStart(CoreService.this, hostAddress);
            } else {
                mServer.startup();
            }
        }
    }

    /**
     * Stop server
     */
    private void stopServer() {
        if (mServer != null && mServer.isRunning()) {
            mServer.shutdown();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
