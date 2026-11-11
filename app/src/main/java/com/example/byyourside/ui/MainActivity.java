package com.example.byyourside.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.byyourside.service.MotionHorizonService;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndRequestOverlayPermission();
    }

    private void checkAndRequestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "'다른 앱 위에 표시' 권한 승인이 필요합니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
            );
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            startMotionService();
        }
    }

    private void startMotionService() {
        Intent serviceIntent = new Intent(this, MotionHorizonService.class);
        startService(serviceIntent);
        Toast.makeText(this, "옆에서 (Yeop-eoseo) 멀미 방지 엔진 가동 시작!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startMotionService();
            } else {
                Toast.makeText(this, "권한이 거부되어 앱을 가동할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}