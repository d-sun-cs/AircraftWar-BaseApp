package cn.edu.hit.application;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import cn.edu.hit.R;
import cn.edu.hit.aircraft.HeroAircraft;
import cn.edu.hit.aircraft.MobEnemy;
import cn.edu.hit.background.EasyGameBackground;
import cn.edu.hit.bullet.EnemyBullet;
import cn.edu.hit.bullet.HeroBullet;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE_EASY;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    //TODO 精英敌机图片、道具图片
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BLOOD_PROP_IMAGE;
    public static Bitmap BOMB_PROP_IMAGE;
    public static Bitmap BULLET_PROP_IMAGE;
    //Boss图片
    public static Bitmap BOSS_IMAGE;


    public static void initImages(Context context) {
        BACKGROUND_IMAGE_EASY = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bg);

        HERO_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.hero);
        MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.mob);
        HERO_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet_hero);
        ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bullet_enemy);
        //TODO 精英敌机图片、道具图片
        ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.elite);
        BLOOD_PROP_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.prop_blood);
        BOMB_PROP_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.prop_bomb);
        BULLET_PROP_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.prop_bullet);
        //BOSS 图片
        BOSS_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boss);

        CLASSNAME_IMAGE_MAP.put(EasyGameBackground.class.getName(), BACKGROUND_IMAGE_EASY);
        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        //TODO 精英敌机图片、道具图片
        //CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
        //CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
        //CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);
        //CLASSNAME_IMAGE_MAP.put(BulletProp.class.getName(), BULLET_PROP_IMAGE);
        //Boss图片
        //CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_IMAGE);
    }

    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

    /**
     * 调整图片大小
     *
     * @param bitmap 源
     * @param dst_w  输出宽度
     * @param dst_h  输出高度
     * @return
     */
    public static Bitmap imageScale(Bitmap bitmap, float dst_w, float dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w =  dst_w / src_w;
        float scale_h =  dst_h / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix,
                true);
        return dstbmp;
    }

}
