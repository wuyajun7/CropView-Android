package cropimage.cropview.crop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wuyajun on 15/11/17.
 * Detail:
 */
public class ImageUtil {

    /**
     * 不要透明度的将图片转换成Bitmap
     *
     * @param localFile
     * @param inSampleSize 压缩大小， 默认是10
     * @return
     */
    public static Bitmap loadBitmap(File localFile, int inSampleSize) {
        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;  //不要透明度，降低内存消耗
        decodeOptions.inJustDecodeBounds = false;
        if (inSampleSize <= 0) {
            decodeOptions.inSampleSize = 3;
        } else {
            decodeOptions.inSampleSize = inSampleSize;
        }

        Bitmap tBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath(), decodeOptions);
        return tBitmap;
    }

    /**
     * bitmap 压缩
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap changeBitmap(Bitmap bitmap, int w, int h) {
        Bitmap localBitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h);
        bitmap.recycle();
        return localBitmap;
    }

    /**
     * 回收 bitmap 所占内存
     *
     * @param bitmap
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();   //回收图片所占的内存
            System.gc();  //提醒系统及时回收
        }
    }

    /**
     * 保存图片
     *
     * @param bitmap  图片
     * @param imgName 图片名称
     * @param imgMass 图片质量
     */
    public static void saveBitmap(Bitmap bitmap, String imgName, int imgMass) {
        String parentPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/niuniu/";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File f = new File(parentPath + imgName + ".jpg");
        try {
            f.createNewFile();
        } catch (IOException e) {
            Log.i("saveImg", "在保存图片时出错 " + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, imgMass, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
