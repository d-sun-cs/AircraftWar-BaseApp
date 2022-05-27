package cn.edu.hit.application.game;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.edu.hit.R;
import cn.edu.hit.activity.GameActivity;
import cn.edu.hit.activity.RankActivity;
import cn.edu.hit.aircraft.AbstractAircraft;
import cn.edu.hit.aircraft.EliteEnemy;
import cn.edu.hit.aircraft.HeroAircraft;
import cn.edu.hit.aircraft.MobEnemy;
import cn.edu.hit.application.ImageManager;
import cn.edu.hit.application.MusicService;
import cn.edu.hit.background.GameBackground;
import cn.edu.hit.basic.FlyingObject;
import cn.edu.hit.bullet.BaseBullet;
import cn.edu.hit.prop.AbstractProp;
import cn.edu.hit.prop.BloodProp;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends FrameLayout {
    protected Activity context;

    private GameBackground background;

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    protected AbstractAircraft boss;

    private final List<AbstractProp> props;

    private int enemyMaxNumber = 5;

    private boolean gameOverFlag = false;
    protected int score = 0;
    private int time = 0;



    private TextView scoreAndHp;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    /**
     * 单机游戏难度
     */
    private String difficulty;

    /**
     * 背景音乐相关
     */
    private boolean musicEnable;
    private Intent musicIntent;

    public Game(Activity context, String difficulty, boolean musicEnable) {
        super(context);
        this.context = context;
        this.difficulty = difficulty;
        this.musicEnable = musicEnable;

        heroAircraft = new HeroAircraft(
                context
                , GameActivity.WINDOW_WIDTH / 2,
                GameActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                0, 0, 10000);
        //交给简单工厂去做
        background = createBackground();
        scoreAndHp = new TextView(context);
        scoreAndHp.setTextColor(Color.RED);

        this.addView(background);
        this.addView(heroAircraft);
        this.addView(scoreAndHp);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();

        props = new LinkedList<>();


        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(1);

        if (musicEnable) {
            musicIntent = new Intent(context, MusicService.class);
            context.startService(musicIntent);
        }
    }

    protected abstract GameBackground createBackground();

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
                    MobEnemy enemy ;
                    if (System.currentTimeMillis() % 3 == 1) {
                        enemy = new EliteEnemy(
                                context,
                                (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * GameActivity.WINDOW_HEIGHT * 0.2),
                                0,
                                10,
                                30
                        );
                    } else {
                        enemy = new MobEnemy(
                                context,
                                (int) (Math.random() * (GameActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * GameActivity.WINDOW_HEIGHT * 0.2),
                                0,
                                10,
                                30
                        );
                    }
                    enemyAircrafts.add(enemy);
                    context.runOnUiThread(() -> this.addView(enemy));
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            //道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 撞击检测
            scoreCheck();

            // 后处理
            postProcessAction();
            //每个时刻重绘界面
            paint();

            // 游戏结束检查
            gameOverCheck();
        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);


    }


    protected abstract void scoreCheck();

    //***********************
    //      Action 各部分
    //***********************

    private void gameOverCheck() {
        if (heroAircraft.getHp() <= 0) {
            // 游戏结束
            executorService.shutdown();
            gameOverFlag = true;
            MusicService.playSound(musicEnable, R.raw.game_over);
            System.out.println("Game Over!");

            if (musicEnable) {
                context.stopService(musicIntent);
            }

            Intent intentToRank = new Intent(context, RankActivity.class);
            intentToRank.putExtra("difficulty", difficulty);
            intentToRank.putExtra("score", score);
            context.startActivity(intentToRank);

            context.finish();
        }
    }

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
        for (AbstractAircraft enemy : enemiesAddBoss()) {
            for (BaseBullet bullet : enemy.shoot()) {
                enemyBullets.add(bullet);
                context.runOnUiThread(() -> this.addView(bullet));
            }
        }

        // 英雄射击
        for (BaseBullet bullet : heroAircraft.shoot()) {
            heroBullets.add(bullet);
            context.runOnUiThread(() -> this.addView(bullet));
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
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
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 敌机撞击到英雄机子弹
                //播放音频
                MusicService.playSound(musicEnable, R.raw.bullet_hit);
                // 敌机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        for (AbstractAircraft enemyAircraft : enemiesAddBoss()) {
            // 英雄子弹攻击敌机
            for (BaseBullet bullet : heroBullets) {
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
                    //播放音频
                    MusicService.playSound(musicEnable, R.raw.bullet_hit);
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        score += 10;
                        AbstractProp prop = enemyAircraft.produceProp();
                        if (Objects.nonNull(prop)) {
                            context.runOnUiThread(() -> this.addView(prop));
                            props.add(prop);
                        }
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
        for (AbstractProp prop : props) {
            if (prop.crash(heroAircraft) || heroAircraft.crash(prop)) {
                prop.subscribe(heroAircraft);
                prop.subscribe(enemiesAddBoss());
                prop.vanish();
                prop.notifyObservers(musicEnable);
                prop.unsubscribe(heroAircraft);
                prop.unsubscribe(enemiesAddBoss());
            }
        }

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
            BaseBullet enemyBullet = enemyBullets.get(i);
            if (enemyBullet.notValid()) {
                context.runOnUiThread(() -> this.removeView(enemyBullet));
                enemyBullets.remove(i--);
            }
        }
        for (int i = 0; i < heroBullets.size(); i++) {
            BaseBullet heroBullet = heroBullets.get(i);
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

        for (int i = 0; i < props.size(); i++) {
            AbstractProp prop = props.get(i);
            if (prop.notValid()) {
                context.runOnUiThread(() -> this.removeView(prop));
                props.remove(i--);
            }
        }

        if (Objects.nonNull(boss) && boss.notValid()) {
            context.runOnUiThread(()-> this.removeView(boss));
            MusicService.isBossAlive = false;
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

        paintImageWithPositionRevised(enemiesAddBoss());
        context.runOnUiThread(heroAircraft::invalidate);

        paintImageWithPositionRevised(props);

        //绘制得分和生命值
        paintScoreAndLife();

    }

    private List<AbstractAircraft> enemiesAddBoss() {
        List<AbstractAircraft> enemyAircrafts0 = new ArrayList<>(enemyAircrafts);
        if (Objects.nonNull(boss) && !boss.notValid()) {
            enemyAircrafts0.add(boss);
        }
        return enemyAircrafts0;
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
        context.runOnUiThread(() -> scoreAndHp.setText("SCORE:" + this.score + '\n' + "LIFE:" + this.heroAircraft.getHp()));
    }


}
