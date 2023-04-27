package com.example.myapplication010;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication010.databinding.ActivityMainBinding;

import java.io.InputStream;

public class MainActivity extends Activity implements View.OnDragListener {

    private View myHand1View;
    private ActivityMainBinding binding;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.test1.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                // 押下時に動作
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    // Viewをドラッグ状態にする。
                    v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
                    v.setAlpha(0);
                }
                return true;
            }

        });

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
            // 手を放し、ドラッグが終了した時の処理

            case DragEvent.ACTION_DRAG_ENDED:
                // ドラッグしているViewを表示させる。

                break;

        }
        return true;
    }

}