package cropimage.cropview.crop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import java.io.File;

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
            decodeOptions.inSampleSize = 2;
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

}
