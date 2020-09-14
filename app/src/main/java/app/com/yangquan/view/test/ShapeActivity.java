package app.com.yangquan.view.test;

import app.com.yangquan.R;
import app.com.yangquan.base.BaseActivity;
import butterknife.BindView;

public class ShapeActivity extends BaseActivity {
    @BindView(R.id.shape)
    ShapeView shape;

    @Override
    protected int getLayout() {
        return R.layout.activity_shape;
    }

    @Override
    protected void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(shape!=null){
                                shape.change();
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onSuccess(int flag, String message) {

    }

    @Override
    protected void onFailure(int flag, String error) {

    }
}
