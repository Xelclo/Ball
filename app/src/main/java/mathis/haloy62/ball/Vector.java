package mathis.haloy62.ball;

import android.graphics.Color;

public class Vector {

    private double x;
    private double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void incX(double x) {
        this.x += x;
    }

    public void incY(double y) {
        this.y += y;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public void multiply(double f) {
        x *= f;
        y *= f;
    }

    public void setSize(double size) {
        normalize();
        multiply(size);
    }

    public double getSize() {
        return Math.sqrt(x * x + y * y);
    }

    public void normalize() {
        double intensity = getSize();
        x /= intensity;
        y /= intensity;
    }

    public double dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public void rotate(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double newX = x * c - y * s;
        double newY = x * s + y * c;

        x = newX;
        y = newY;
    }

    public double getAngle() {
        return Math.atan2(y, x);
    }

    public double getRelativeAngleBetween(Vector v) {
        return getSign(v) * Math.acos(dot(v) / (getSize() * v.getSize()));
    }

    // http://www.oocities.org/pcgpe/math2d.html
    // http://gamedev.stackexchange.com/questions/45412/understanding-math-used-to-determine-if-vector-is-clockwise-counterclockwise-f
    public int getSign(Vector v) {
        return (y * v.x > x * v.y) ? -1 : 1;
    }

    public Vector perp() {
        return new Vector(-getY(), getX());
    }

    // http://johnblackburne.blogspot.com.br/2012/02/perp-dot-product.html
    public double perpDot(Vector v) {
        return perp().dot(v);
    }
}