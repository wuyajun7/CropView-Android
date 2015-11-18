package cropimage.cropview.crop;

/**
 * Created by wuyajun on 15/11/18.
 * Detail:
 */
public interface ISaveImgListener {
    void onSuccess(int statusCode);

    void onFailure(int statusCode, String msg);

    void progress(int pro);
}
