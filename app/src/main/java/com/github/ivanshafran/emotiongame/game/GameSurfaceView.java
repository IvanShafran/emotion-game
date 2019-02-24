package com.github.ivanshafran.emotiongame.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.github.ivanshafran.emotiongame.camera.FaceFeatures;
import com.github.ivanshafran.emotiongame.game.drawer.Drawer;
import com.github.ivanshafran.emotiongame.resource.ResourceProvider;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static volatile FaceFeatures faceFeatures = new FaceFeatures(0, 0, 0);

    public static void setFaceFeatures(FaceFeatures faceFeatures) {
        GameSurfaceView.faceFeatures = faceFeatures;
    }

    private final Drawer drawer;
    private final SurfaceHolder surfaceHolder;

    private volatile boolean isThreadRunning = false;

    private volatile StepProcessor stepProcessor;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context);

        drawer = new Drawer(context);
        // Get SurfaceHolder object.
        surfaceHolder = this.getHolder();
        // Add current object as the callback listener.
        surfaceHolder.addCallback(this);

        // Set the SurfaceView object at the top of View object.
        setZOrderOnTop(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        GameConfig config = new GameConfig(
                new CanvasConfig(getWidth(), getHeight()),
                DefaultGameConfigsKt.getDefaultSkyConfig(),
                DefaultGameConfigsKt.getDefaultSunConfig(),
                DefaultGameConfigsKt.getDefaultRoadConfig(),
                DefaultGameConfigsKt.getDefaultPlayerConfig(),
                DefaultGameConfigsKt.getDefaultEnemyConfig(),
                DefaultGameConfigsKt.getDefaultScoreConfig(),
                DefaultGameConfigsKt.getDefaultLifeConfig(),
                DefaultGameConfigsKt.getDefaultBonusConfig(),
                DefaultGameConfigsKt.getDefaultSpeedMultiplierConfig()
        );

        stepProcessor = new StepProcessor(config, new ResourceProvider(getContext()));

        // Create the child thread when SurfaceView is created.
        Thread thread = new Thread(this);
        // Start to run the child thread.
        thread.start();
        // Set thread running flag to true.
        isThreadRunning = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // Set thread running flag to false when Surface is destroyed.
        // Then the thread will jump out the while loop and complete.
        isThreadRunning = false;
    }

    @Override
    public void run() {
        while (isThreadRunning) {
            Canvas canvas = surfaceHolder.lockCanvas();

            GameState gameState = stepProcessor.doNextStep(
                    System.currentTimeMillis(),
                    faceFeatures
            );
            drawer.draw(canvas, gameState);

            surfaceHolder.unlockCanvasAndPost(canvas);

            fpsSleep();
        }
    }

    private void fpsSleep() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException ex) {
            isThreadRunning = false;
        }
    }

}
