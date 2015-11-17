package cropimage.cropview.crop;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.Date;

import cropimage.cropview.R;

/**
 * Created by wuyajun on 15/11/17.
 * Detail:
 */
public class MainActivity extends Activity {

    private final int minification = 2;//图片压缩倍数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.select_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
    }

    private File mCaptureFile = null;
    private static final int REQUEST_CAPTURE_IMAGE = 0;

    /**
     * 显示图片选择 Dialog
     */
    private void showPhotoDialog() {

        new AlertDialog.Builder(this)
                .setTitle("头像上传")
                .setItems(new String[]{"拍照", "从相册中选择"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mCaptureFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),
                                    "" + new Date().getTime() + ".jpg");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCaptureFile));
                            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
                        } else if (which == 1) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
                            intent.setType("image/*");//从所有图片中进行选择
                            startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAPTURE_IMAGE) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cr = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cr.moveToFirst()) {
                        File file = new File(cr.getString(cr.getColumnIndex(MediaStore.Images.Media.DATA)));
                        Bitmap bitmap = ImageUtil.loadBitmap(file, minification);

                        setBitmapJumpActivity(bitmap);
                        Log.i("resulteeeee", "1");
                        Log.i("resulteeeee", "1 " + FileSizeUtil.getFileOrFilesSize(file.getAbsolutePath(), FileSizeUtil.SIZETYPE_KB));
                    }
                    cr.close();
                } else {
                    Log.i("resulteeeee", "2");
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Log.i("resulteeeee", "2 " + FileSizeUtil.getBitmapsize(bitmap, FileSizeUtil.SIZETYPE_KB));
                    setBitmapJumpActivity(ImageUtil.changeBitmap(bitmap,
                            bitmap.getWidth() / minification, bitmap.getHeight() / minification));
                }

            } catch (Exception e) {
                if (mCaptureFile != null && mCaptureFile.exists()) {
                    Log.i("resulteeeee", "3");
                    Log.i("resulteeeee", "1 " + FileSizeUtil.getFileOrFilesSize(mCaptureFile.getAbsolutePath(), FileSizeUtil.SIZETYPE_KB));

                    Bitmap bitmap = ImageUtil.loadBitmap(mCaptureFile, minification);
                    setBitmapJumpActivity(bitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBitmapJumpActivity(Bitmap bitmap) {
        ((AppController) getApplication()).cropped = bitmap;
        startActivity(new Intent(MainActivity.this, CorpActivity.class));
    }

}
