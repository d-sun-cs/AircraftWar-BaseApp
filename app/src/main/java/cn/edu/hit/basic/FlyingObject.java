package cn.edu.hit.basic;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.aircraft.AbstractAircraft;
import cn.edu.hit.application.ImageManager;

/**
 * 可飞行对象的父类
 *
 * @author hitsz
 */
public abstract class FlyingObject extends View {

    //locationX、locationY为图片中心位置坐标
    /**
     * x 轴坐标
     */
    protected int locationX;

    /**
     * y 轴坐标
     */
    protected int locationY;


    /**
     * x 轴移动速度
     */
    protected int speedX;

    /**
     * y 轴移动速度
     */
    protected int speedY;

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

    /**
     * 应用上下文
     */
    protected Context context;

    /**
     * 图片放大的倍数
     */
    protected final float zoomFactor = 1.4f;

    /**
     * 有效（生存）标记，
     * 通常标记为 false的对象会再下次刷新时清除
     */
    protected boolean isValid = true;

    public FlyingObject(Context context) {
        super(context);
        this.context = context;
    }

    public FlyingObject(Context context, int locationX, int locationY, int speedX, int speedY) {
        super(context);
        this.context = context;
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     */
    public void forward() {
        locationX += speedX;
        locationY += speedY;
        if (locationX <= 0 || locationX >= GameActivity.WINDOW_WIDTH) {
            // 横向超出边界后反向
            speedX = -speedX;
        }
    }

    /**
     * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br>
     * 对方与我方覆盖区域有交叉即判定撞击。
     * <br>
     * 非飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/2, y + height/2]
     * <br>
     * 飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/4, y + height/4]
     *
     * @param flyingObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    public boolean crash(FlyingObject flyingObject) {
        // 缩放因子，用于控制 y轴方向区域范围
        float factor = this instanceof AbstractAircraft ? 2 : 1;
        float fFactor = flyingObject instanceof AbstractAircraft ? 2 : 1;

        int x = flyingObject.getLocationX();
        int y = flyingObject.getLocationY();
        float fWidth = flyingObject.getImageWidth() * zoomFactor;
        float fHeight = flyingObject.getImageHeight() * zoomFactor;

        return x + (fWidth + this.getImageWidth() * zoomFactor) / 2 > locationX
                && x - (fWidth + this.getImageWidth() * zoomFactor) / 2 < locationX
                && y + (fHeight / fFactor + this.getImageHeight() * zoomFactor / factor) / 2 > locationY
                && y - (fHeight / fFactor + this.getImageHeight() * zoomFactor / factor) / 2 < locationY;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocation(double locationX, double locationY) {
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    public int getSpeedY() {
        return speedY;
    }

    public Bitmap getImage() {
        if (image == null) {
            image = ImageManager.get(this);
        }
        return image;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float longerWidth = getImageWidth() * zoomFactor;
        float longerHeight = getImageHeight() * zoomFactor;
        Bitmap biggerImage = ImageManager.imageScale(getImage(), longerWidth, longerHeight);
        canvas.drawBitmap(biggerImage,
                locationX - longerWidth / 2.0f,
                locationY - longerHeight / 2.0f,
                new Paint());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public boolean notValid() {
        return !this.isValid;
    }

    /**
     * 标记消失，
     * isValid = false.
     * notValid() => true.
     */
    public void vanish() {
        isValid = false;
    }

}

