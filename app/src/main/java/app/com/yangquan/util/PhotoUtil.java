package app.com.yangquan.util;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import app.com.yangquan.R;

/*
 * 选择图片库工具类
 * */
public class PhotoUtil{
    //相册
    public static void picture(Activity activity,int max) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                //.openCamera()//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
                .theme(R.style.picture_WeChat_style)// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.MULTIPLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(true)//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回
                .isWeChatStyle(false)//开启R.style.picture_WeChat_style样式
//                .setPictureStyle()//动态自定义相册主题
//                .setPictureCropStyle()//动态自定义裁剪主题
//                .setPictureWindowAnimationStyle()//相册启动退出动画
                .isCamera(false)//列表是否显示拍照按钮
                .isZoomAnim(true)//图片选择缩放效果
//                .imageFormat()//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
                .maxSelectNum(max)//最大选择数量,默认9张
//                .minSelectNum()// 最小选择数量
//                .maxVideoSelectNum()//视频最大选择数量
//                .minVideoSelectNum()//视频最小选择数量
//                .videoMaxSecond()// 查询多少秒以内的视频
//                .videoMinSecond()// 查询多少秒以内的视频
                .imageSpanCount(4)//列表每行显示个数
//                .openClickSound()//是否开启点击声音
//                .selectionMedia()//是否传入已选图片
//                .recordVideoSecond()//录制视频秒数 默认60s
//                .previewEggs()//预览图片时是否增强左右滑动图片体验
//                .cropCompressQuality()// 注：已废弃 改用cutOutQuality()
                .isGif(true)//是否显示gif
                .previewImage(true)//是否预览图片
//                .previewVideo()//是否预览视频
//                .enablePreviewAudio()//是否预览音频
                .enableCrop(false)//是否开启裁剪
//                .cropWH()// 裁剪宽高比,已废弃，改用. cropImageWideHigh()方法
//                .cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                .withAspectRatio()//裁剪比例
//                .cutOutQuality()// 裁剪输出质量 默认100
                .freeStyleCropEnabled(true)//裁剪框是否可拖拽
                .circleDimmedLayer(false)// 是否开启圆形裁剪
//                .setCircleDimmedColor()//设置圆形裁剪背景色值
//                .setCircleDimmedBorderColor()//设置圆形裁剪边框色值
//                .setCircleStrokeWidth()//设置圆形裁剪边框粗细
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(true)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .isDragFrame(true)//是否可拖动裁剪框(固定)
                .hideBottomControls(true)//显示底部uCrop工具栏
//                .basicUCropConfig()//对外提供ucrop所有的配制项
                .compress(true)//是否压缩
                .compressFocusAlpha(true)//压缩后是否保持图片的透明通道
                .minimumCompressSize(50)// 小于多少kb的图片不压缩
//                .videoQuality()//视频录制质量 0 or 1
//                .compressQuality()//图片压缩后输出质量
//                .synOrAsy()//开启同步or异步压缩
//                .queryMaxFileSize()//查询指定大小内的图片、视频、音频大小，单位M
//                .compressSavePath()//自定义压缩图片保存地址，注意Q版本下的适配
//                .sizeMultiplier()//glide加载大小，已废弃
//                .glideOverride()//glide加载宽高，已废弃
                .isMultipleSkipCrop(true)//多图裁剪是否支持跳过
                .isMultipleRecyclerAnimation(true)// 多图裁剪底部列表显示动画效果
//                .querySpecifiedFormatSuffix()//只查询指定后缀的资源，PictureMimeType.ofJPEG() ...
                .isReturnEmpty(true)//未选择数据时按确定是否可以退出
//                .isAndroidQTransform()//Android Q版本下是否需要拷贝文件至应用沙盒内
//                .setRequestedOrientation()//屏幕旋转方向 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED ...
                .isOriginalImageControl(true)//开启原图选项
//                .bindCustomPlayVideoCallback()//自定义视频播放拦截
//                .bindCustomCameraInterfaceListener()//自定义拍照回调接口
//                .bindCustomPreviewCallback()// 自定义图片预览回调接口
//                .cameraFileName()//自定义拍照文件名，如果是相册内拍照则内部会自动拼上当前时间戳防止重复
//                .renameCompressFile()//自定义压缩文件名，多张压缩情况下内部会自动拼上当前时间戳防止重复
//                .renameCropFileName()//自定义裁剪文件名，多张裁剪情况下内部会自动拼上当前时间戳防止重复
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)//列表动画效果,AnimationType.ALPHA_IN_ANIMATION、SLIDE_IN_BOTTOM_ANIMATION
//                .isUseCustomCamera()// 开启自定义相机
//                .setButtonFeatures()// 自定义相机按钮状态,CustomCameraView.BUTTON_STATE_BOTH
//                .setLanguage()//国际化语言 LanguageConfig.CHINESE、ENGLISH、JAPAN等
//                .isWithVideoImage()//图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(true)//选择条件达到阀时列表是否启用蒙层效果
                .isAutomaticTitleRecyclerTop(true)//图片列表超过一屏连续点击顶部标题栏快速回滚至顶部
//                .loadCacheResourcesCallback()//获取ImageEngine缓存图片，参考Demo
//                .setOutputCameraPath()// 自定义相机输出目录只针对Android Q以下版本，具体参考Demo
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
    }
    //拍照
    public static void pictureCamera(Activity activity,int max) {
        PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofImage())//相册 媒体类型 PictureMimeType.ofAll()、ofImage()、ofVideo()、ofAudio()
                .openCamera(PictureMimeType.ofImage())//单独使用相机 媒体类型 PictureMimeType.ofImage()、ofVideo()
                .theme(R.style.picture_default_style)// xml样式配制 R.style.picture_default_style、picture_WeChat_style or 更多参考Demo
                .loadImageEngine(GlideEngine.createGlideEngine())
                .selectionMode(PictureConfig.SINGLE)//单选or多选 PictureConfig.SINGLE PictureConfig.MULTIPLE
                .isPageStrategy(true)//开启分页模式，默认开启另提供两个参数；pageSize每页总数；isFilterInvalidFile是否过滤损坏图片
                .isSingleDirectReturn(true)//PictureConfig.SINGLE模式下是否直接返回
                .isWeChatStyle(false)//开启R.style.picture_WeChat_style样式
//                .setPictureStyle()//动态自定义相册主题
//                .setPictureCropStyle()//动态自定义裁剪主题
//                .setPictureWindowAnimationStyle()//相册启动退出动画
                .isCamera(false)//列表是否显示拍照按钮
                .isZoomAnim(true)//图片选择缩放效果
//                .imageFormat()//拍照图片格式后缀,默认jpeg, PictureMimeType.PNG，Android Q使用PictureMimeType.PNG_Q
                .maxSelectNum(max)//最大选择数量,默认9张
//                .minSelectNum()// 最小选择数量
//                .maxVideoSelectNum()//视频最大选择数量
//                .minVideoSelectNum()//视频最小选择数量
//                .videoMaxSecond()// 查询多少秒以内的视频
//                .videoMinSecond()// 查询多少秒以内的视频
                .imageSpanCount(4)//列表每行显示个数
//                .openClickSound()//是否开启点击声音
//                .selectionMedia()//是否传入已选图片
//                .recordVideoSecond()//录制视频秒数 默认60s
//                .previewEggs()//预览图片时是否增强左右滑动图片体验
//                .cropCompressQuality()// 注：已废弃 改用cutOutQuality()
                .isGif(true)//是否显示gif
                .previewImage(true)//是否预览图片
//                .previewVideo()//是否预览视频
//                .enablePreviewAudio()//是否预览音频
                .enableCrop(true)//是否开启裁剪
//                .cropWH()// 裁剪宽高比,已废弃，改用. cropImageWideHigh()方法
//                .cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                .withAspectRatio()//裁剪比例
//                .cutOutQuality()// 裁剪输出质量 默认100
                .freeStyleCropEnabled(true)//裁剪框是否可拖拽
                .circleDimmedLayer(true)// 是否开启圆形裁剪
//                .setCircleDimmedColor()//设置圆形裁剪背景色值
//                .setCircleDimmedBorderColor()//设置圆形裁剪边框色值
//                .setCircleStrokeWidth()//设置圆形裁剪边框粗细
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)//是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .rotateEnabled(true)//裁剪是否可旋转图片
                .scaleEnabled(true)//裁剪是否可放大缩小图片
                .isDragFrame(true)//是否可拖动裁剪框(固定)
                .hideBottomControls(true)//显示底部uCrop工具栏
//                .basicUCropConfig()//对外提供ucrop所有的配制项
                .compress(true)//是否压缩
                .compressFocusAlpha(true)//压缩后是否保持图片的透明通道
                .minimumCompressSize(100)// 小于多少kb的图片不压缩
//                .videoQuality()//视频录制质量 0 or 1
//                .compressQuality()//图片压缩后输出质量
//                .synOrAsy()//开启同步or异步压缩
//                .queryMaxFileSize()//查询指定大小内的图片、视频、音频大小，单位M
//                .compressSavePath()//自定义压缩图片保存地址，注意Q版本下的适配
//                .sizeMultiplier()//glide加载大小，已废弃
//                .glideOverride()//glide加载宽高，已废弃
                .isMultipleSkipCrop(true)//多图裁剪是否支持跳过
                .isMultipleRecyclerAnimation(true)// 多图裁剪底部列表显示动画效果
//                .querySpecifiedFormatSuffix()//只查询指定后缀的资源，PictureMimeType.ofJPEG() ...
                .isReturnEmpty(true)//未选择数据时按确定是否可以退出
//                .isAndroidQTransform()//Android Q版本下是否需要拷贝文件至应用沙盒内
//                .setRequestedOrientation()//屏幕旋转方向 ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED ...
                .isOriginalImageControl(true)//开启原图选项
//                .bindCustomPlayVideoCallback()//自定义视频播放拦截
//                .bindCustomCameraInterfaceListener()//自定义拍照回调接口
//                .bindCustomPreviewCallback()// 自定义图片预览回调接口
//                .cameraFileName()//自定义拍照文件名，如果是相册内拍照则内部会自动拼上当前时间戳防止重复
//                .renameCompressFile()//自定义压缩文件名，多张压缩情况下内部会自动拼上当前时间戳防止重复
//                .renameCropFileName()//自定义裁剪文件名，多张裁剪情况下内部会自动拼上当前时间戳防止重复
                .setRecyclerAnimationMode(AnimationType.ALPHA_IN_ANIMATION)//列表动画效果,AnimationType.ALPHA_IN_ANIMATION、SLIDE_IN_BOTTOM_ANIMATION
//                .isUseCustomCamera()// 开启自定义相机
//                .setButtonFeatures()// 自定义相机按钮状态,CustomCameraView.BUTTON_STATE_BOTH
//                .setLanguage()//国际化语言 LanguageConfig.CHINESE、ENGLISH、JAPAN等
//                .isWithVideoImage()//图片和视频是否可以同选,只在ofAll模式下有效
                .isMaxSelectEnabledMask(true)//选择条件达到阀时列表是否启用蒙层效果
                .isAutomaticTitleRecyclerTop(true)//图片列表超过一屏连续点击顶部标题栏快速回滚至顶部
//                .loadCacheResourcesCallback()//获取ImageEngine缓存图片，参考Demo
//                .setOutputCameraPath()// 自定义相机输出目录只针对Android Q以下版本，具体参考Demo
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调分两种方式onActivityResult()和OnResultCallbackListener方式
    }
}
