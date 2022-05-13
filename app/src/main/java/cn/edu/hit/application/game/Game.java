package cn.edu.hit.application.game;


import android.app.Activity;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.aircraft.AbstractAircraft;
import cn.edu.hit.aircraft.HeroAircraft;
import cn.edu.hit.aircraft.MobEnemy;
import cn.edu.hit.application.ImageManager;
import cn.edu.hit.basic.FlyingObject;
import cn.edu.hit.basic.GameBackground;
import cn.edu.hit.bullet.AbstractBullet;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends FrameLayout {
    private Activity context;

    private GameBackground background;

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<AbstractBullet> heroBullets;
    private final List<AbstractBullet> enemyBullets;

    private int enemyMaxNumber = 5;

    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;

    private TextView scoreAndHp;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;


    public Game(Activity context) {
        super(context);
        this.context = context;
        heroAircraft = new HeroAircraft(
                context
                , GameActivity.WINDOW_WIDTH / 2,
                GameActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 100);
        /*heroAircraft.setOnTouchListener((v, event) -> {
            heroAircraft.setLocation(event.getX(), event.getY());
            return true;
        });*/
        background = new GameBackground(context);
        scoreAndHp = new TextView(context);
        scoreAndHp.setTextColor(Color.RED);

        this.addView(background);
        this.addView(heroAircraft);
        this.addView(scoreAndHp);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(1);

        //启动英雄机鼠标监听
        //new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    MobEnemy mobEnemy = new MobEnemy(
                            context,
                            (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                            (int) (Math.random() * GameActivity.WINDOW_HEIGHT * 0.2),
                            0,
                            10,
                            30
                    );
                    enemyAircrafts.add(mobEnemy);
                    context.runOnUiThread(() -> this.addView(mobEnemy));
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();
            //每个时刻重绘界面
            paint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // TODO 敌机射击

        // 英雄射击
        for (AbstractBullet bullet : heroAircraft.shoot()) {
            heroBullets.add(bullet);
            context.runOnUiThread(() -> this.addView(bullet));
            //this.addView(bullet);
        }
    }

    private void bulletsMoveAction() {
        for (AbstractBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (AbstractBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄


        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            // 英雄子弹攻击敌机
            for (AbstractBullet bullet : heroBullets) {
                if (bullet.notValid()) {
                    continue;
                }
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        score += 10;
                    }
                }
            }

            // 英雄机 与 敌机 相撞，均损毁
            if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                enemyAircraft.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }


        // Todo: 我方获得道具，道具生效

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            AbstractBullet enemyBullet = enemyBullets.get(i);
            if (enemyBullet.notValid()) {
                context.runOnUiThread(() -> this.removeView(enemyBullet));
                enemyBullets.remove(i--);
            }
        }
        for (int i = 0; i < heroBullets.size(); i++) {
            AbstractBullet heroBullet = heroBullets.get(i);
            if (heroBullet.notValid()) {
                context.runOnUiThread(() -> this.removeView(heroBullet));
                heroBullets.remove(i--);
            }
        }
        for (int i = 0; i < enemyAircrafts.size(); i++) {
            AbstractAircraft enemy = enemyAircrafts.get(i);
            if (enemy.notValid()) {
                context.runOnUiThread(() -> this.removeView(enemy));
                enemyAircrafts.remove(i--);
            }
        }

    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    public void paint() {

        // 绘制背景,图片滚动
        background.setBackGroundTop(backGroundTop);
        context.runOnUiThread(background::invalidate);
        this.backGroundTop += 1;
        if (this.backGroundTop == GameActivity.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(enemyBullets);
        paintImageWithPositionRevised(heroBullets);

        paintImageWithPositionRevised(enemyAircrafts);
        context.runOnUiThread(heroAircraft::invalidate);

        //绘制得分和生命值
        paintScoreAndLife();

    }

    private void paintImageWithPositionRevised(List<? extends FlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (FlyingObject object : objects) {
            context.runOnUiThread(object::invalidate);
        }
    }

    private void paintScoreAndLife() {
        /*int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);*/
        context.runOnUiThread(()-> scoreAndHp.setText("SCORE:" + this.score + '\n' + "LIFE:" + this.heroAircraft.getHp()));
    }


}
