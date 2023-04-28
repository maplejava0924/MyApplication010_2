package com.example.myapplication010;

import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//MainActivity.javaはAndroidManifest.xmlのactivityタグの記載の通りAndroidOSによってインスタンス化される
public class MainActivity extends Activity{

    //インスタンス変数として定義する
    //MainActivity.javaのインスタンスは1つしかできないので、クラス変数でもインスタンス変数でも挙動は変わらない
    private int[] myHandIds = new int[9];
    private Guideline myHandLineTop;
    private Context context;

    //ImageViewに対してsetOnTouchListenerを使用すると出てくる警告に従って付けたアノテーション。
    //ImageViewを"Clickable"にするために必要らしい
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ActivityクラスのonCreate()を呼び出す。Bundleオブジェクトは前回のアクティビティが維持していた状態を表す
        super.onCreate(savedInstanceState);
        //activity_main.xmlのレイアウトを適用する
        setContentView(R.layout.activity_main);

        //自分の手札のView(myHand)のIDを取得し、配列に格納する
        for (int i = 0; i < 9; i++) {
            this.myHandIds[i] = getResources().getIdentifier("myHand" + (i + 1), "id", getPackageName());
        }

        //myHandに対してイベントを設定するための処理
        for (int myHandId:this.myHandIds) {
            //IDからViewをインスタンス化
            ImageView myHandImageView = findViewById(myHandId);
            //該当のImageViewがTouchされた時に呼ばれるリスナー
            //vはmyHandImageViewのこと
            myHandImageView.setOnTouchListener((v, event) -> {
                    //指が画面に触れた瞬間に発生するイベントを定義する
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        myHandOnTouchDownEvent(v);
                    }
                    return true;
            });
        }
        //背景画像をIDから検索しViewを変数に格納する
        ImageView background = findViewById(R.id.background);

        //GuidelineをIDから検索しViewを変数に格納する。
        //AndroidOSによって生成されたMainActivity.javaインスタンスの、Guideline myHandLineTop（インスタンス変数）に対してViewを格納する。
        //myHandLineTopはクラス変数ではないのでインスタンス間で値が共有されないが、そもそもMainActivity.javaインスタンスは1つしか生成されない。
        //当然MainActivity.javaインスタンス自体のGuideline myHandLineTopの値は書き換わっているのでメソッド内からthisで参照可能である。
        //そこまで複雑なことをやりたいわけではないので、getterやsetterは不要でthisで参照する
        this.myHandLineTop = findViewById(R.id.myHandLineTop);

        //OnDragListenerインターフェースを実装した匿名クラス内で、Contextを使用するために、
        //匿名クラスの外でContextを定義しておく
        this.context = this;

        //該当のImageViewに対してDragイベントが発生した時に呼ばれるリスナー
        //Dropされるのはbackgroundの画像なので、backgroundに対してリスナーを定義する
        //vはbackgroundのこと
        background.setOnDragListener((v,event) -> {
                switch (event.getAction()) {
                    // Dragが終了して、ViewにDropされたときの処理
                    case DragEvent.ACTION_DROP:

                        //Dropしたときに持っていたViewの情報を取得したいときはgetLocalState()を使用する
                        ImageView droppedImageView = (ImageView) event.getLocalState();
                        backgroundDropEvent(droppedImageView,event);
                        break;
                    default:
                        break;
                }
                return true;
        });

    }

    private void myHandOnTouchDownEvent(View v){
        //該当のImageViewの画像を取得し、nullでないならDrag状態にする
        //ImageViewに何も画像がセットされていない（＝@null）ということは、そこにカードが存在していないということを表すため
        Drawable myHandDrawable =  ((ImageView) v).getDrawable();
        if (myHandDrawable != null) {
            // ViewをDrag状態にする
            v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
            //ポップアップを出す
            Toast.makeText(getApplicationContext(), "DragStart!", Toast.LENGTH_SHORT).show();
            //logcatに情報を出力する
            Log.i("debugMessage","dragstart!");
        }
    }

    private void backgroundDropEvent(ImageView droppedImageView,DragEvent event){
        //DropしたViewのIDを格納する
        int droppedImageId = droppedImageView.getId();
        //Dropしたときに持っていたViewがmyHandであったか否か
        boolean isMyHand = false ;

        for(int myHandId:this.myHandIds){
            //Dropしたときに持っていたViewのIDがmyHandのIDとひとつでも一致したときのみ処理を行う
            if(droppedImageId == myHandId){
                isMyHand =true;
                break;
            }
        }
        //手を離した位置を格納する
        float dropY = event.getY();

        //DropしたViewのIDがmyHandのいずれかであり、手を離した位置がmyHandLineTopより下であるときのみ処理を行う
        if (isMyHand && dropY < this.myHandLineTop.getY()) {

            ImageView myField1ImageView= findViewById(R.id.myField1);

            Drawable fairyDrawable = ContextCompat.getDrawable(this.context, R.drawable.fairy_nonevolve);
            myField1ImageView.setImageDrawable(fairyDrawable);

            //ポップアップを表示する
            Toast.makeText(getApplicationContext(), "Droped!", Toast.LENGTH_SHORT).show();
            //logcatに情報を出力する
            Log.i("debugMessage","dropped!");
        }
    }

}