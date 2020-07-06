package app.com.yangquan.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import app.com.yangquan.R;
import app.com.yangquan.base.BaseFragment;
import app.com.yangquan.view.DragLayout;
import butterknife.BindView;


/**
 * Created by xmuSistone
 */
public class CommonFragment extends BaseFragment implements DragLayout.GotoDetailListener {
    @BindView(R.id.address4)
    TextView address4;
    @BindView(R.id.address5)
    TextView address5;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.head1)
    ImageView head1;
    @BindView(R.id.head2)
    ImageView head2;
    @BindView(R.id.head3)
    ImageView head3;
    @BindView(R.id.head4)
    ImageView head4;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.address1)
    TextView address1;
    @BindView(R.id.address2)
    ImageView address2;
    @BindView(R.id.address3)
    TextView address3;
    @BindView(R.id.drag_layout)
    DragLayout dragLayout;
    private Drawable imageUrl;

    @Override
    protected int getLayout() {
        return R.layout.item_test2;
    }

    @Override
    protected void init() {
//        Glide.with(mContext).load(imageUrl).into(image);
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_test2, null);
        DragLayout dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);
        dragLayout.setGotoDetailListener(this);
        return rootView;
    }

    @Override
    public void gotoDetail() {
        Activity activity = (Activity) getContext();
       /* ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                new Pair(image, DetailActivity.IMAGE_TRANSITION_NAME),
                new Pair(address1, DetailActivity.ADDRESS1_TRANSITION_NAME),
                new Pair(address2, DetailActivity.ADDRESS2_TRANSITION_NAME),
                new Pair(address3, DetailActivity.ADDRESS3_TRANSITION_NAME),
                new Pair(address4, DetailActivity.ADDRESS4_TRANSITION_NAME),
                new Pair(address5, DetailActivity.ADDRESS5_TRANSITION_NAME),
                new Pair(rating, DetailActivity.RATINGBAR_TRANSITION_NAME),
                new Pair(head1, DetailActivity.HEAD1_TRANSITION_NAME),
                new Pair(head2, DetailActivity.HEAD2_TRANSITION_NAME),
                new Pair(head3, DetailActivity.HEAD3_TRANSITION_NAME),
                new Pair(head4, DetailActivity.HEAD4_TRANSITION_NAME)
        );
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_IMAGE_URL, imageUrl);
        ActivityCompat.startActivity(activity, intent, options.toBundle());*/
    }

    public void bindData(Drawable imageUrl) {
        this.imageUrl = imageUrl;
    }
}
