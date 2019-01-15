package mathis.haloy62.ball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {

    // BALL
    private Ball[] balls;
    private int n_ball = 0;
    private final int MAX_BALL = 500;
    private float size = 150;

    private Paint fgPaintSel;
    private float downX;
    private float downY;
    private float touchX;
    private float touchY;
    private float upX;
    private float upY;

    private Random rand = new Random();

    // TOUCH EVENT
    private boolean touch_ball_flg = false;
    private int touch_ball_index;

    public GameView(Context context) {
        super(context);

        fgPaintSel = new Paint();
        fgPaintSel.setAlpha(255);
        fgPaintSel.setStrokeWidth(10);
        fgPaintSel.setColor(Color.CYAN);
        fgPaintSel.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void initBall() {

        balls = new Ball[MAX_BALL];

        for(int i=0; i<10; i++) {
            boolean intersect = true;
            balls[i] = new Ball(new Vector(200+rand.nextDouble()*(this.getWidth()-400),
                    200+rand.nextDouble()*(this.getHeight()-400)),
                    size,  Color.argb(255, rand.nextInt(256), rand.nextInt(156), 156));
            while(intersect) {
                intersect = false;
                for(int j=0; j<i; j++) {
                    if(balls[i].intersect(balls[j])) {
                        intersect = true;
                        balls[i].setPosition(200+rand.nextDouble()*(this.getWidth()-400),
                                200+rand.nextDouble()*(this.getHeight()-400));
                        break;
                    }
                }
            }
            n_ball++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for(int i=0; i<n_ball; i++) {
            balls[i].increment();
            balls[i].collisionWall(this.getWidth(), this.getHeight());
            for(int j=0; j<n_ball; j++) {
                if (i != j && balls[i].collides(balls[j])) {
                    balls[i].decrement();
                    balls[i].transferEnergy(balls[j]);
                }
            }
            balls[i].applyFriction();
            @SuppressLint("DrawAllocation") Paint paint = new Paint();
            paint.setColor(balls[i].getColor());
            canvas.drawOval(balls[i], paint);
        }
        if(touch_ball_flg) {
            canvas.drawLine(balls[touch_ball_index].centerX(),balls[touch_ball_index].centerY(),touchX,touchY,fgPaintSel);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            touchX = event.getX();
            touchY = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            for(int i=0; i<n_ball; i++) {
                if (balls[i].contains(event.getX(), event.getY())) {
                    touch_ball_index = i;
                    downX = event.getX();
                    downY = event.getY();
                    touch_ball_flg = true;
                }
            }
            // NEW BALL ON TOUCH
            boolean intersect = false;
            Ball tmp = new Ball(new Vector(event.getX(),event.getY()),size,  Color.argb(255, rand.nextInt(256), rand.nextInt(156), 156));
            for(int j=0; j<n_ball; j++) {
                if(tmp.intersect(balls[j])) {
                    intersect = true;
                }
            }
            if(!touch_ball_flg && !intersect && n_ball<=MAX_BALL) {
                balls[n_ball] = tmp;
                n_ball++;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(touch_ball_flg) {
                upX = event.getX();
                upY = event.getY();
                balls[touch_ball_index].setVelocity((downX-upX)/10, (downY-upY)/10);
                touch_ball_flg = false;
            }
        }
        return true;
    }
}
