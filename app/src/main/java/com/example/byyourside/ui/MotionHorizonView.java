package com.example.byyourside.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MotionHorizonView extends View {

    private Paint borderPaint;
    private float currentAngle = 0.0f;
    private float targetAngle = 0.0f;

    public MotionHorizonView(Context context) {
        super(context);
        init();
    }

    private void init() {
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(Color.parseColor("#00F2FE")); // 반투명 민트 글로우
        borderPaint.setStrokeWidth(5f);                     // 5px 테두리
        borderPaint.setAlpha(120);                          // 비침습적 은은한 투명도
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void updateTargetAngle(float angle) {
        this.targetAngle = angle;
        postInvalidateOnAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 60fps 프레임 보간
        currentAngle = currentAngle + (targetAngle - currentAngle) * 0.15f;

        canvas.save();

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        canvas.rotate(currentAngle, centerX, centerY);

        // 상·하단 최외곽 5px 선 그리기
        canvas.drawLine(0, 0, getWidth(), 0, borderPaint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), borderPaint);

        canvas.restore();

        if (Math.abs(targetAngle - currentAngle) > 0.01f) {
            postInvalidateOnAnimation();
        }
    }
}