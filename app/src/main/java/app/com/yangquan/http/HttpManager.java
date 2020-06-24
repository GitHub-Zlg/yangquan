package app.com.yangquan.http;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.viewpager.widget.ViewPager;
import app.com.yangquan.R;
import app.com.yangquan.adapter.ImChatFacePagerAdapter;
import app.com.yangquan.listener.OnFaceClickListener;
import app.com.yangquan.util.ChatUiHelper;
import app.com.yangquan.util.TextRender;
import app.com.yangquan.util.ToastUtil;
import app.com.yangquan.view.WrapContentHeightViewPager;
import okhttp3.Call;
import okhttp3.MediaType;

public class HttpManager {

    /**
     * post请求
     *
     * @param url      请求链接
     * @param tag      请求的tag
     * @param parms    请求参数
     * @param flag     区分多个请求返回的数据
     * @param callBack 回调
     */
    public static void post(final Context context, String tag, String url, final int flag, Map<String, Object> parms, final HttpCallBack callBack) {
        String content = new JSONObject(parms).toString();
        Log.e("zlg", "url====" + url);
        Log.e("zlg", "content=====" + content);
        com.zhy.http.okhttp.OkHttpUtils.postString()
                .tag(tag)
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(content)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("zlg", "ssss=====" + e.toString());
                        if (context != null) {
                            if (callBack != null) {
                                callBack.onHttpFail(flag, "网络异常,请检查网络");
                            }
                        }
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        if (context != null) {
                            if (callBack != null) {
                                try {
                                    String data = null;
                                    try {
                                        data = new String(s.getBytes(), "utf-8");
//                                            s= Emoji.decode(data);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
                                    Matcher matcher = pattern.matcher(s);
                                    char ch;
                                    while (matcher.find()) {
                                        ch = (char) Integer.parseInt(matcher.group(2), 16);
                                        s = s.replace(matcher.group(1), ch + "");
                                    }
                                    Log.e("zlg", "ssss=====" + s);
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getInt("code") == 0) {
                                        callBack.onHttpSuccess(flag, s);
//                                        callBack.onHttpSuccess(flag,utf8ToString(s));
                                    } else {
                                        callBack.onHttpFail(flag, jsonObject.getString("msg") != null ?
                                                jsonObject.getString("msg") : "网络异常,请检查网络");
                                        ToastUtil.show(jsonObject.getString("msg"));
                                        Log.e("zlg", "msg====" + s);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    /**
     * get请求
     *
     * @param url      请求链接
     * @param tag      请求的tag
     * @param parms    请求参数
     * @param flag     区分多个请求返回的数据
     * @param callBack 回调
     */
    public static void get(final Context context, String tag, String url, final int flag,
                           Map<String, String> parms, final HttpCallBack callBack) {
        String content = new JSONObject(parms).toString();
        Log.e("zlg", "content" + content);
        Log.e("zlg", "url====" + url);
        com.zhy.http.okhttp.OkHttpUtils.get()
                .tag(tag)
                .url(url)
                .params(parms)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (context != null) {
                            if (callBack != null) {
                                callBack.onHttpFail(flag, "网络异常,请检查网络");
                            }
                        }
                        Log.e("zlg", "ssss=====" + e.toString());
                        Log.e("zlg", "ssss=====" + id);
                    }

                    @Override
                    public void onResponse(String s, int id) {
                        if (context != null) {
                            if (callBack != null) {
                                try {
                                    String data = null;
                                    try {
                                        data = new String(s.getBytes(), "utf-8");
//                                            s= Emoji.decode(data);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
                                    Matcher matcher = pattern.matcher(s);
                                    Log.e("zlg", "ssss=====" + s);
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getInt("code") == 0) {
                                        callBack.onHttpSuccess(flag, s);
//                                        callBack.onHttpSuccess(flag,utf8ToString(s));
                                    } else {
                                        callBack.onHttpFail(flag, jsonObject.getString("msg") != null ?
                                                jsonObject.getString("msg") : "网络异常,请检查网络");
                                        if (!jsonObject.getString("msg").equals("宁还没有关注过别人呢！")) {
                                            ToastUtil.show(jsonObject.getString("msg"));
                                        }
                                        Log.e("zlg", "msg====" + jsonObject.getString("msg"));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    public static void up(final Context context, final String tag, String url, final int flag, String key,Map<String, File> map, Map<String, String> params, final HttpCallBack callBack) {
        String content = new JSONObject(map).toString();
        String param = new JSONObject(params).toString();
        Log.e("zlg", "content====" + content);
        Log.e("zlg", "param====" + param);
        Log.e("zlg", "key====" + key);
        com.zhy.http.okhttp.OkHttpUtils.post().
                tag(tag)
                .addHeader("Content-Type", "multipart/form-data")
                .files(key, map)
                .params(params)
                .url(url)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        callBack.onHttpFail(flag, "");
                        Log.e("zlg", "ssss=====" + e.toString());
                        Log.e("zlg", "ssss=====" + i);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        if (context != null) {
                            if (callBack != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getInt("code") == 0) {
                                        callBack.onHttpSuccess(flag, s);
//                                        LogUtil.e(s);
                                        Log.e("zlg", "ssss=====" + s);
                                    } else {
                                        callBack.onHttpFail(flag, jsonObject.getString("msg") != null ?
                                                jsonObject.getString("msg") : "网络异常,请检查网络");
                                        ToastUtil.show(jsonObject.getString("msg"));
                                        Log.e("zlg", "msg====" + jsonObject.getString("msg"));
                                        Log.e("zlg", "code====" + jsonObject.getString("code"));
                                        Log.e("zlg", "data====" + jsonObject.getString("data"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }


//    public static void upFile(final Context context, final String tag, String url, final int flag,File file1,File file2, Map<String, String> params, final HttpCallBack callBack) {
////        String content = new JSONObject(map).toString();
//        String param = new JSONObject(params).toString();
//        Log.e("zlg", "content====" + content);
//        Log.e("zlg", "param====" + param);
//        com.zhy.http.okhttp.OkHttpUtils.post().
//                tag(tag)
//                .addHeader("Content-Type", "multipart/form-data")
////                .files("list", map)
//                .addFile("list1",f)
//                .params(params)
//                .url(url)
//                .build()
//                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//                        callBack.onHttpFail(flag, "");
//                        Log.e("zlg", "ssss=====" + e.toString());
//                        Log.e("zlg", "ssss=====" + i);
//                    }
//
//                    @Override
//                    public void onResponse(String s, int i) {
//                        if (context != null) {
//                            if (callBack != null) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(s);
//                                    if (jsonObject.getInt("code") == 0) {
//                                        callBack.onHttpSuccess(flag, s);
////                                        LogUtil.e(s);
//                                        Log.e("zlg", "ssss=====" + s);
//                                    } else {
//                                        callBack.onHttpFail(flag, jsonObject.getString("msg") != null ?
//                                                jsonObject.getString("msg") : "网络异常,请检查网络");
//                                        ToastUtil.show(jsonObject.getString("msg"));
//                                        Log.e("zlg", "msg====" + jsonObject.getString("msg"));
//                                        Log.e("zlg", "code====" + jsonObject.getString("code"));
//                                        Log.e("zlg", "data====" + jsonObject.getString("data"));
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                });
//    }

}
