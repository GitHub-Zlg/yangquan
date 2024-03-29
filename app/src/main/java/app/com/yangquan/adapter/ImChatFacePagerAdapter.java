package app.com.yangquan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import app.com.yangquan.R;
import app.com.yangquan.listener.OnFaceClickListener;
import app.com.yangquan.util.FaceUtil;

/**
 * Created by cxf on 2018/7/11.
 * 聊天表情的ViewPager Adapter
 */

public class ImChatFacePagerAdapter extends PagerAdapter {

    private List<View> mViewList;
    private static final int FACE_COUNT = 34;//每页20个表情

    public ImChatFacePagerAdapter(Context context, OnFaceClickListener onFaceClickListener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mViewList = new ArrayList<>();
        List<String> faceList = FaceUtil.getFaceList();
        int fromIndex = 0;
        int size = faceList.size();
        int pageCount = size / FACE_COUNT;
        if (size % FACE_COUNT > 0) {
            pageCount++;
            for (int i = 0, count = pageCount * FACE_COUNT - size; i < count; i++) {
                faceList.add("");
            }
        }
        for (int i = 0; i < pageCount; i++) {
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.view_chat_face_page, null, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 7, GridLayoutManager.VERTICAL, false));
            int endIndex = fromIndex + FACE_COUNT;
            List<String> list = new ArrayList<>();
            for (int j = fromIndex; j < endIndex; j++) {
                list.add(faceList.get(j));
            }
            list.add("<");
            recyclerView.setAdapter(new ImChatFaceAdapter(list, inflater, onFaceClickListener));
            mViewList.add(recyclerView);
            fromIndex = endIndex;
        }
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@Nullable View view, @Nullable Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@Nullable ViewGroup container, int position) {
        View view = mViewList.get(position);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@Nullable ViewGroup container, int position, @Nullable Object object) {
        container.removeView(mViewList.get(position));
    }
}
