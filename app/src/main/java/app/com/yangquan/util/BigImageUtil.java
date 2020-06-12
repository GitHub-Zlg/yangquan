package app.com.yangquan.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.example.zhouwei.library.CustomPopWindow;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.List;

import app.com.yangquan.R;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.view.listener.OnBigImageLongClickListener;

public class BigImageUtil{
    public static void single(Context context,String path){
        ImagePreview.getInstance()
                .setContext(context)
                .setIndex(0)
                .setImage(path)
                .setEnableDragClose(true)
                .setEnableUpDragClose(true)
                .setErrorPlaceHolder(R.drawable.bg_photo_num)
                .setShowDownButton(false)
                .setZoomTransitionDuration(500)
                .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                    @Override
                    public boolean onLongClick(Activity activity, View view, int position) {
                        savePopWin(context,view, path);
                        return false;
                    }
                })
                .start();
    }

    public static void more(Context context, List<String> list,int postion){
        ImagePreview.getInstance()
                .setContext(context)
                .setIndex(postion)
                .setImageList(list)
                .setEnableDragClose(true)
                .setEnableUpDragClose(true)
                .setErrorPlaceHolder(R.drawable.bg_photo_num)
                .setShowDownButton(false)
                .setZoomTransitionDuration(500)
                .setBigImageLongClickListener(new OnBigImageLongClickListener() {
                    @Override
                    public boolean onLongClick(Activity activity, View view, int position) {
                        savePopWin(context,view, list.get(position));
                        return false;
                    }
                })
                .start();
    }



    private static void savePopWin(Context context,View view,String path){
        View saveView = LayoutInflater.from(context).inflate(R.layout.pop_save, null);
        CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(context)
                .setView(saveView)//显示的布局，还可以通过设置一个View
                .enableBackgroundDark(true)
                // .size(600,400) //设置显示的大小，不设置就默认包裹内容
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAtLocation(view, Gravity.BOTTOM, 0, 50);
        saveView.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show("暂未实现该功能，敬请期待");
                popWindow.dissmiss();
            }
        });
        saveView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dissmiss();
            }
        });

    }
}
