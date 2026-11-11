package com.example.byyourside.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;

import com.example.byyourside.ui.MotionHorizonView;

import ai.pleos.connect.sdk.vehicle.VehicleManager;
import ai.pleos.connect.sdk.navi.NaviHelperManager;

public class MotionHorizonService extends Service {

    private WindowManager windowManager;
    private MotionHorizonView horizonView;
    private VehicleManager vehicleManager;
    private NaviHelperManager naviHelperManager;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        horizonView = new MotionHorizonView(this);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
        );

        windowManager.addView(horizonView, params);

        initPleosConnectSDK();
    }

    private void initPleosConnectSDK() {
        // Vehicle SDK: 실시간 핸들 각도 반응
        vehicleManager = VehicleManager.getInstance(this);
        vehicleManager.registerSteeringListener(angle -> {
            if (horizonView != null) {
                horizonView.updateTargetAngle(angle * -0.5f);
            }
        });

        // NaviHelper SDK: 300m 앞 1.5초 선제 예지 틸팅
        naviHelperManager = NaviHelperManager.getInstance(this);
        naviHelperManager.registerTurnPredictionListener((distanceMeter, curveDegree) -> {
            if (distanceMeter <= 300 && horizonView != null) {
                horizonView.updateTargetAngle(curveDegree);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && horizonView != null) {
            windowManager.removeView(horizonView);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}