package app.com.yangquan.jiguang.im;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.com.yangquan.App;
import app.com.yangquan.R;

/**
 * Created by cxf on 2017/8/9.
 */

public class ImgLoader {
    private static RequestManager sManager;

    static {
        sManager = Glide.with(App.getInstance());
    }

    public static void display(String url, ImageView imageView) {
        sManager.load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(imageView);
    }

    public static void displayWithError(String url, ImageView imageView, int errorRes) {
        sManager.load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).error(errorRes)).into(imageView);
    }

    public static void displayAvatar(String url, ImageView imageView) {
        displayWithError(url, imageView, R.mipmap.icon_avater);
    }

    public static void display(File file, ImageView imageViews) {
        ViewGroup.LayoutParams layoutParams = imageViews.getLayoutParams();
        sManager.load(file).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(imageViews);
    }

    public static void display(int res, ImageView imageView) {
        sManager.load(res).into(imageView);
    }

    /**
     * 显示视频封面缩略图
     */
    public static void displayVideoThumb(String videoPath, ImageView imageViews,ImageView imageViewo,ImageView imageViewh) {
        sManager.load(Uri.fromFile(new File(videoPath))).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                int intrinsicWidth = resource.getIntrinsicWidth();
                int intrinsicHeight = resource.getIntrinsicHeight();
                ViewGroup.LayoutParams layoutParams = imageViews.getLayoutParams();
                if (intrinsicWidth > intrinsicHeight) {
                    imageViewh.setImageDrawable(resource);
                } else if (intrinsicWidth < intrinsicHeight) {
                    imageViewo.setImageDrawable(resource);
                } else {
                    imageViews.setImageDrawable(resource);
                }
            }
        });
    }

    public static void displayDrawable(String url, final DrawableCallback callback) {
        sManager.load(url).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (callback != null) {
                    callback.callback(resource);
                }
            }
        });
    }

    public static void displayDrawable(File file, final DrawableCallback callback) {
        sManager.load(file).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (callback != null) {
                    callback.callback(resource);
                }
            }
        });
    }


    public static void display(String url, ImageView imageView, int placeholderRes) {
        sManager.load(url).apply(RequestOptions.diskCacheStrategyOf((DiskCacheStrategy.RESOURCE)).placeholder(placeholderRes)).into(imageView);
    }

    /**
     * 显示模糊的毛玻璃图片
     */
    /*public static void displayBlur(String url, ImageView imageView) {
        sManager.load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE).bitmapTransform(sBlurTransformation))
                .into(imageView);
    }*/

    public interface DrawableCallback {
        void callback(Drawable drawable);
    }
}
