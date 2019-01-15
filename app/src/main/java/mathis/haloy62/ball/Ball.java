package mathis.haloy62.ball;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

public class Ball extends RectF {

    private Vector velocity;
    private Vector position;
    private double gravity = 0.1;
    private float size;
    private int color;

    public Ball(float left, float top, float right, float bottom, int color) {
        super(left, top, right, bottom);
        velocity = new Vector(0, 0);
        position = new Vector(centerX(), centerY());
        this.color = color;
        size = right-left;
    }

    public Ball (Vector position, float size, int color) {
        super((float)position.getX()-size/2, (float)position.getY()-size/2,
                (float)position.getX()+size/2, (float)position.getY()+size/2);
        velocity = new Vector(0, 0);
        this.position = position;
        this.color = color;
        this.size = size;
    }

    public void setVelocity(double x, double y) {
        velocity.setX(x);
        velocity.setY(y);
    }

    public void setPosition(double x, double y) {
        position.setX(x);
        position.setY(y);
        left=(float)x-size/2;
        top=(float)y-size/2;
        right=(float)x+size/2;
        bottom=(float)y+size/2;
    }

    public int getColor() {
        return color;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void increment() {
        // GRAVITY
        velocity.setY(velocity.getY()+gravity);
        left += velocity.getX();
        right += velocity.getX();
        top += velocity.getY();
        bottom += velocity.getY();
        position.setX(centerX());
        position.setY(centerY());
    }

    public void decrement() {
        left -= velocity.getX();
        right -= velocity.getX();
        top -= velocity.getY();
        bottom -= velocity.getY();
        position.setX(centerX());
        position.setY(centerY());
    }

    public void applyFriction() {
        velocity.setX(velocity.getX() * 0.985);
        velocity.setY(velocity.getY() * 0.985);
    }

    public boolean collides(Ball ball) {
        return position.sub(ball.position).getSize() <= ((right-left)/2 + (ball.right-ball.left)/2);
    }

    public float distanceCarre(Ball ball) {
        return (this.centerX()-ball.centerX())*(this.centerX()-ball.centerX())+(this.centerY()-ball.centerY())*(this.centerY()-ball.centerY());
    }

    public void transferEnergy(Ball ball) {
        Vector newV2 = position.sub(ball.position);
        newV2.normalize();
        newV2.multiply(velocity.dot(newV2));

        Vector newV1 = velocity.sub(newV2);

        Vector newV2B = ball.position.sub(position);
        newV2B.normalize();
        newV2B.multiply(ball.velocity.dot(newV2B));

        Vector newV1B = ball.velocity.sub(newV2B);

        ball.velocity = newV2.add(newV1B);
        velocity = newV1.add(newV2B);

        // SWAP COLOR
        if(     Color.red(this.getColor()) > Color.red(ball.getColor())
                && (int)this.position.getY() > (int)ball.position.getY()
                || Color.red(this.getColor()) < Color.red(ball.getColor())
                && (int)this.position.getY() < (int)ball.position.getY()) {
            int tmp = this.getColor();
            this.setColor(ball.getColor());
            ball.setColor(tmp);
        }

    }


    public void collisionWall(int width, int height) {
        if(left<0 || left>width-(right-left)) {
            decrement();
            velocity.setX(-velocity.getX());
        }
        if(top<0 || top>height-(bottom-top)) {
            decrement();
            velocity.setY(-velocity.getY());
        }
    }
}
