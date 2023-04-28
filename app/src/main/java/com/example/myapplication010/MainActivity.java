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
    private int[] myFieldIds = new int[5];
    private int[] enemyFieldIds = new int[5];
    private Guideline myHandLineTop;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //ActivityクラスのonCreate()を呼び出す。Bundleオブジェクトは前回のアクティビティが維持していた状態を表す
        super.onCreate(savedInstanceState);
        //activity_main.xmlのレイアウトを適用する
        setContentView(R.layout.activity_main);

        //自分の手札のView(myHand)のIDを取得し、配列に格納する
        setIdArray(this.myHandIds,"myHand");
        //該当のidの配列を受け取り、すべてのViewに対してTouchDownしたときのリスナーを設定する
        setTouchDownListener(this.myHandIds);

        //自分のフィールドのView(myField)のIDを取得し、配列に格納する
        setIdArray(this.myFieldIds,"myField");
        //該当のidの配列を受け取り、すべてのViewに対してTouchDownしたときのリスナーを設定する
        setTouchDownListener(this.myFieldIds);

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
                    View droppedImageView = (View) event.getLocalState();
                    backgroundDropEvent(droppedImageView,event);
                    break;
                default:
                    break;
            }
            return true;
        });

        //相手のフィールドのView(enemyField)のIDを取得し、配列に格納する
        setIdArray(this.enemyFieldIds,"enemyField");
        //myFieldに対してイベントを設定するための処理
        for (int enemyFieldId:this.enemyFieldIds) {
            //IDから検索しViewを変数に格納する。
            ImageView enemyFieldImageView = findViewById(enemyFieldId);
            //該当のImageViewがTouchされた時に呼ばれるリスナー
            //vはenemyFieldImageViewのこと
            enemyFieldImageView.setOnDragListener((v, event) -> {
                //指が画面に触れた瞬間に発生するイベントを定義する
                switch (event.getAction()) {
                    // Dragが終了して、ViewにDropされたときの処理
                    case DragEvent.ACTION_DROP:
                        //Dropしたときに持っていたViewの情報を取得したいときはgetLocalState()を使用する
                        View droppedImageView = (View) event.getLocalState();
                        enemyFieldDropEvent(v,droppedImageView, event);
                        break;
                    default:
                        break;
                }
                return true;
            });
        }
    }

    //ViewがTouchDownされたときのイベント
    //v:TouchDownされたView
    private void startDragView(View v){
        //該当のImageViewの画像を取得し、nullでないならDrag状態にする
        if (notNullImageViewSrc((ImageView) v)) {
            // ViewをDrag状態にする
            v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
            //ポップアップを出す
            Toast.makeText(getApplicationContext(), "DragStart!", Toast.LENGTH_SHORT).show();
            //logcatに情報を出力する
            Log.i("debugMessage","Dragstart!");
        }
    }

    //backgroundImageViewがDropされたときのイベント
    //droppedView:DropしたときにもっていたView
    private void backgroundDropEvent(View droppedView,DragEvent event){
        //Dropしたときに持っていたViewのIDを格納する
        int droppedImageId = droppedView.getId();
        //手を離した位置を格納する
        float dropY = event.getY();
        //Dropしたときに持っていたViewのIDがmyHandのいずれかであり、手を離した位置がmyHandLineTopより下であるときのみ処理を行う
        if (isInIDArray(droppedImageId,this.myHandIds) && dropY < this.myHandLineTop.getY()) {

            ImageView myField1ImageView= findViewById(R.id.myField1);

            Drawable fairyDrawable = ContextCompat.getDrawable(this.context, R.drawable.fairy_nonevolve);
            myField1ImageView.setImageDrawable(fairyDrawable);

            //ポップアップを表示する
            Toast.makeText(getApplicationContext(), "myHandDroped!", Toast.LENGTH_SHORT).show();
            //logcatに情報を出力する
            Log.i("debugMessage","myHandDropped!");

            //play処理

        }
    }

    // v:DropされたView
    // droppedView:Dropしたときに持っていたView
    private void enemyFieldDropEvent(View v,View droppedView,DragEvent event){
        //DropされたImageViewの画像を取得し、nullでないならイベントを発生させる
        if (notNullImageViewSrc((ImageView) v)) {
            //Dropしたときに持っていたViewのIDを格納する
            int droppedImageId = droppedView.getId();

            //Dropしたときに持っていたViewのIDがmyFieldのいずれかであるときのみ処理を行う
            if (isInIDArray(droppedImageId,this.myFieldIds)) {
                //ポップアップを表示する
                Toast.makeText(getApplicationContext(), "MyFieldDroped!", Toast.LENGTH_SHORT).show();
                //logcatに情報を出力する
                Log.i("debugMessage", "myFieldDropped!");

                //フォロワーへattack処理

            }
        }
    }

    //ImageViewのsrcが@nullでないならtrueを返す
    //ImageViewに何も画像がセットされていない（＝@null）ということは、そこにカードが存在していないということを表す
    private boolean notNullImageViewSrc(ImageView imageView){

        Drawable imageViewDrawable =  imageView.getDrawable();
        if(imageViewDrawable != null){
            return true;
        }
        else {
            return false;
        }

    }

    //IDを取得し、配列に格納する
    //指定するidはmyHand1,myHand2,myHand3のように連番である必要がある
    //また、格納先のidArrayの配列の長さとxmlに定義された該当のidのViewの個数が一致していないとNullpointする可能性があるので注意すること
    private void setIdArray(int[] idArray, String id){
        for (int i = 0; i < idArray.length; i++) {
            idArray[i] = getResources().getIdentifier(id + (i + 1), "id", getPackageName());
        }
    }

    //ImageViewに対してsetOnTouchListenerを使用すると出てくる警告に従って付けたアノテーション。
    //ImageViewを"Clickable"にするために必要らしい
    @SuppressLint("ClickableViewAccessibility")
    //該当のidの配列を受け取り、すべてのViewに対してTouchDownしたときのリスナーを設定する
    private void setTouchDownListener(int[] idArray){
        //Viewに対してイベントを設定するための処理
        for (int myViewId:idArray) {
            //IDから検索しViewを変数に格納する。
            ImageView myImageView = findViewById(myViewId);
            //該当のImageViewがTouchされた時に呼ばれるリスナー
            //vはmyImageViewのこと
            myImageView.setOnTouchListener((v, event) -> {
                //指が画面に触れた瞬間に発生するイベントを定義する
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //ViewにDragがスタートするように定義する
                    startDragView(v);
                }
                return true;
            });
        }
    }

    //idがIDArrayの中でひとつでも一致したときTrueを返す
    private boolean isInIDArray(int id,int[] idArray){
        for (int myId : idArray) {
            if (id == myId) {
                return true;
            }
        }
        return false;
    }



}