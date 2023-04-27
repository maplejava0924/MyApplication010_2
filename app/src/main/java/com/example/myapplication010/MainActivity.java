package com.example.myapplication010;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication010.databinding.ActivityMainBinding;

import java.io.InputStream;
import java.sql.Array;

public class MainActivity extends Activity{

    //ImageViewに対してsetOnTouchListenerを使用すると出てくる警告に従って付けたアノテーション。
    //ImageViewを"Clickable"にするために必要らしい。
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //自分の手札のView(myHand)のIDを取得し、配列に格納する
        int[] myHandIds = new int[9];
        for (int i = 0; i < 9; i++) {
            myHandIds[i] = getResources().getIdentifier("myHand" + (i + 1), "id", getPackageName());
        }

        //myHandに対してイベントを設定するための処理
        for (int myHandId:myHandIds) {
            //IDからViewをインスタンス化
            ImageView myHandImageView = findViewById(myHandId);
            //該当のImageViewがTouchされた時に呼ばれるリスナー
            myHandImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //指が画面に触れた瞬間に発生するイベントを定義する
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //該当のImageViewの画像を取得し、nullでないならDrag状態にする
                        //ImageViewに何も画像がセットされていない（＝@null）ということは、そこにカードが存在していないということを表すため
                        Drawable myHandDrawable = myHandImageView.getDrawable();
                        if (myHandDrawable != null) {
                            // ViewをDrag状態にする
                            v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
                            //ポップアップを出す
                            Toast.makeText(getApplicationContext(), "DragStart!", Toast.LENGTH_SHORT).show();
                            //logcatに情報を出力する
                            Log.i("debugMessage","dragstart!");
                        }
                    }
                    return true;
                }
            });
        }
        //背景画像をインスタンス化
        ImageView background = findViewById(R.id.background);
        //ガイドラインをインスタンス化
        Guideline myFieldLineTop = findViewById(R.id.myFieldLineTop);
        Guideline myFieldLineBottom = findViewById(R.id.myFieldLineBottom);

        //該当のImageViewに対してDragイベントが発生した時に呼ばれるリスナー
        //Dropされるのはbackgroundの画像なので、backgroundに対してリスナーを定義する
        background.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    // Dragが終了して、ViewにDropされたときの処理
                    case DragEvent.ACTION_DROP:

                        //Dropしたときに持っていたViewの情報を取得したいときはgetLocalState()を使用する
                        ImageView droppedImageView = (ImageView) event.getLocalState();
                        //DropしたViewのIDを格納する
                        int droppedImageId = droppedImageView.getId();
                        //Dropしたときに持っていたViewがmyHandであったか否か
                        boolean isMyHand = false ;

                        for(int myHandId:myHandIds){
                            //Dropしたときに持っていたViewのIDがmyHandのIDとひとつでも一致したときのみ処理を行う
                            if(droppedImageId == myHandId){
                                isMyHand =true;
                                break;
                            }
                        }
                        //手を離した位置を格納する
                        float dropY = event.getY();

                        //DropしたViewのIDがmyHandのいずれかであり、手を離した位置が自分のフィールドのガイドライン内であるときのみ処理を行う
                        if (isMyHand && dropY > myFieldLineTop.getY() && dropY < myFieldLineBottom.getY()) {
                            //ポップアップを表示する
                            Toast.makeText(getApplicationContext(), "Droped!", Toast.LENGTH_SHORT).show();
                            //logcatに情報を出力する
                            Log.i("debugMessage","dropped!");
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

}