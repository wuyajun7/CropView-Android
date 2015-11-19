package cropimage.cropview.crop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wuyajun on 15/11/18.
 * Detail: 存储图片
 * <p/>
 * 生成该类的对象，并调用execute方法之后
 * 首先执行的是onProExecute方法
 * 其次执行doInBackgroup方法
 */
public class AsyncTaskSaveImage extends AsyncTask<Integer, Integer, String> {

    private final String SAVE_SUCCESS = "SAVE_SUCCESS";
    private final String SAVE_FAILURE = "SAVE_FAILURE";

    private ISaveImgListener iSaveImgListener;
    private Bitmap bitmap;
    private String imgName;
    private int imgMass;

    public AsyncTaskSaveImage(Bitmap _bitmap, String _imgName, int _imgMass, ISaveImgListener _iSaveImgListener) {
        super();
        this.iSaveImgListener = _iSaveImgListener;
        this.bitmap = _bitmap;
        this.imgName = _imgName;
        this.imgMass = _imgMass;
    }


    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    @Override
    protected String doInBackground(Integer... params) {
        String result = SAVE_FAILURE;

        String parentPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/niuniu/";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File f = new File(parentPath + imgName + ".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            result = SAVE_FAILURE;
            Log.i("saveImg", "在保存图片时出错 " + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = SAVE_FAILURE;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, imgMass, fOut);

        //计算压缩多少合适 -----------------
//        int quality = 100;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        while (baos.toByteArray().length / 1024f > 100) {
//            quality -= 20;
//            baos.reset();
//            if (quality <= 0) {
//                break;
//            }
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//        }
//        //计算出后使用结果比压缩
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);

        //--------------------------------

        try {
            fOut.flush();
            result = SAVE_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            result = SAVE_FAILURE;
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    @Override
    protected void onPostExecute(String result) {
        if (result.equals(SAVE_SUCCESS)) {
            iSaveImgListener.onSuccess(100);
        } else {
            iSaveImgListener.onFailure(0, SAVE_FAILURE);
        }
    }

    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPreExecute() {
    }

    /**
     * 这里的Intege参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        int vlaue = values[0];
        iSaveImgListener.progress(vlaue);
    }

}
