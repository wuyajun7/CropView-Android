package cropimage.cropview.crop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Random;

import cropimage.cropview.R;


public class CorpActivity extends Activity {

    private CropImageView mCropView;
    private LinearLayout mRootLayout;
    private LinearLayout mTabBar;

    private DisplayMetrics metrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        initViews();
        FontUtils.setFont(mRootLayout);
        mCropView.setImageBitmap(((AppController) getApplication()).cropped);
    }

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCropView.setInitialFrameScale(0.75f);
            switch (v.getId()) {
                case R.id.buttonDone:
                    ((AppController) getApplication()).cropped = mCropView.getCroppedBitmap();

                    AsyncTaskSaveImage asyncTaskSaveImage = new AsyncTaskSaveImage(
                            mCropView.getCroppedBitmap(),
                            "niuniu" + System.currentTimeMillis(), 40,
                            new ISaveImgListener() {
                                @Override
                                public void onSuccess(int statusCode) {
                                    Intent intent = new Intent(CorpActivity.this, ResultActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(int statusCode, String msg) {

                                }

                                @Override
                                public void progress(int pro) {
                                    Log.i("propropro", "" + pro);
                                }
                            }
                    );
                    asyncTaskSaveImage.execute();

                    break;
                case R.id.buttonFitImage:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);
                    break;
                case R.id.button1_1:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_1_1);
                    break;
                case R.id.button3_4:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    break;
                case R.id.button4_3:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    break;
                case R.id.button9_16:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    break;
                case R.id.button16_9:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    break;
                case R.id.buttonCustom:
                    mCropView.setCustomRatio(7, 5);
                    break;
                case R.id.buttonFree:
                    mCropView.setCropMode(CropImageView.CropMode.RATIO_FREE);
                    break;
                case R.id.buttonCircle:
                    mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                    break;
                case R.id.buttonRotateImage:
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    break;
                case R.id.buttonSelectScale:
                    showOrHidLayout(mTabBar);
                    break;
                case R.id.close_tabbar:
                    showOrHidLayout(mTabBar);
                    break;
            }
        }
    };

    private void initViews() {
        mCropView = (CropImageView) findViewById(R.id.cropImageView);

        //自定义充满宽度 - 比例7：5
        mCropView.setInitialFrameScale(1.0f);
        mCropView.setCustomRatio(7, 5);

        findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        findViewById(R.id.button1_1).setOnClickListener(btnListener);
        findViewById(R.id.button3_4).setOnClickListener(btnListener);
        findViewById(R.id.button4_3).setOnClickListener(btnListener);
        findViewById(R.id.button9_16).setOnClickListener(btnListener);
        findViewById(R.id.button16_9).setOnClickListener(btnListener);
        findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        findViewById(R.id.buttonRotateImage).setOnClickListener(btnListener);
        findViewById(R.id.buttonSelectScale).setOnClickListener(btnListener);
        findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        mRootLayout = (LinearLayout) findViewById(R.id.layout_root);
        mTabBar = (LinearLayout) findViewById(R.id.tab_bar);
        findViewById(R.id.close_tabbar).setOnClickListener(btnListener);
    }

    //动画 时间
    private final int duration = 400;

    /**
     * 显示或隐藏layout
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showOrHidLayout(final View view) {
        if (View.GONE == view.getVisibility()) {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", metrics.widthPixels * 3 / 2, 0F);
            animator1.setDuration(duration);
            animator1.setInterpolator(new AccelerateInterpolator());
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });
            animator1.start();
        } else {
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "translationX", 0F, metrics.widthPixels * 3 / 2);
            animator1.setDuration(duration);
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            animator1.start();
        }
    }

}
