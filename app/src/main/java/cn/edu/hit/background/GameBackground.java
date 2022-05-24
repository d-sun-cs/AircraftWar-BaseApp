package cn.edu.hit.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.application.ImageManager;

public abstract class GameBackground extends View {
    Context context;

    /**
     * 图片,
     * null 表示未设置
     */
    protected Bitmap image = null;

    /**
     * x 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    protected int width = -1;

    /**
     * y 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    protected int height = -1;

    public void setBackGroundTop(int backGroundTop) {
        this.backGroundTop = backGroundTop;
    }

    private int backGroundTop;

    public GameBackground(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap background = getImage();
        canvas.drawBitmap(background, 0, backGroundTop, new Paint());
        if (backGroundTop == 0) {
            return;
        }
        Bitmap background0 = Bitmap.createBitmap(background,
                0,
                GameActivity.WINDOW_HEIGHT - backGroundTop,
                GameActivity.WINDOW_WIDTH,
                backGroundTop);

        canvas.drawBitmap(background0, 0, 0, new Paint());

    }

    public Bitmap getImage() {
        if (image == null) {
            image = ImageManager.get(this);
        }
        return ImageManager.imageScale(image, GameActivity.WINDOW_WIDTH, GameActivity.WINDOW_HEIGHT);
    }

    public int getImageWidth() {
        if (width == -1) {
            // 若未设置，则查询图片宽度并设置
            width = ImageManager.get(this).getWidth();
        }
        return width;
    }

    public int getImageHeight() {
        if (height == -1) {
            // 若未设置，则查询图片高度并设置
            height = ImageManager.get(this).getHeight();
        }
        return height;
    }

}
