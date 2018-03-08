package com.boatchina.imerit.app.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.LatLng;
import com.boatchina.imerit.app.view.activity.FenceActivity;
import com.boatchina.imerit.data.fence.FenceEntity;

import java.util.Map;

/**
 * Created by fflamingogo on 2016/9/13.
 */
public class MyView extends View{
    Paint paint = new Paint();
    float mx;
    float my;
    double sqrt;
    Data data;
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    FenceEntity fend;
    AMap aMap;
    Map<Integer,FenceActivity.MyCircle> hash;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            mx = event.getX();
            my = event.getY();

            invalidate();
        }
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            float x = event.getX() - mx;
            float y = event.getY() - my;

            sqrt = Math.sqrt(x * x + y * y);
            invalidate();

        }
        if(event.getAction()==MotionEvent.ACTION_UP){


            data = new Data(mx,my,sqrt, (float) (mx-sqrt), (float) (my-sqrt), (float) (mx+sqrt), (float) (my+sqrt));
            sqrt=0;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(mx,my, (float) sqrt,paint);
//        if(!fends.isEmpty()) {
//            for (Fend fend : fends) {
//                Point p1 = aMap.getProjection().toScreenLocation(new LatLng(fend.getLat1(), fend.getLng1()));
//                Point p2 = aMap.getProjection().toScreenLocation(new LatLng(fend.getLat2(), fend.getLng2()));
//                showOnView(p1,p2,canvas);
//            }
////            invalidate();
//        }
        if(fend!=null) {
            Point p1 = aMap.getProjection().toScreenLocation(new LatLng(fend.getLat1(), fend.getLng1()));
            Point p2 = aMap.getProjection().toScreenLocation(new LatLng(fend.getLat2(), fend.getLng2()));
            showOnView(p1,p2,canvas);
        }

//        if(hash.)
//        Iterator<Integer> iterator = hash.keySet().iterator();
//        while(iterator.hasNext()) {
//            final int next = iterator.next();
//            Circle circle = hash.get(next).getCircle();
//            Point center = aMap.getProjection().toScreenLocation(circle.getCenter());
//            canvas.drawCircle(center.x,center.y,5000,paint);
//        }
    }

    public void showOnView(Point p1, Point p2,Canvas canvas) {
        int ox = (p1.x + p2.x)/2;
        int oy = (p1.y + p2.y)/2;
        int r = p2.x - ox;
        System.out.println("..."+mx+","+my+","+sqrt);
        canvas.drawCircle(ox,oy, (float) r,paint);

//        sqrt=0;

    }

//    public void showOnView(AMap aMap, Map<Integer,FenceActivity.MyCircle> hash) {
//        this.fends.addAll(fends);
//        this.aMap = aMap;
//    }
    public void showOnView(AMap aMap, FenceEntity fend) {
        this.fend=fend;
        this.aMap = aMap;
    }

    public class Data{
    float mx;
    float my;
    double sqrt;
    float xx;
    float yy;
    float XX;
    float YY;

    public Data(float mx, float my, double sqrt, float xx, float yy, float XX, float YY) {
        this.mx = mx;
        this.my = my;
        this.sqrt = sqrt;
        this.xx = xx;
        this.yy = yy;
        this.XX = XX;
        this.YY = YY;
    }

    public float getXX() {
        return XX;
    }

    public void setXX(float XX) {
        this.XX = XX;
    }

    public float getYY() {
        return YY;
    }

    public void setYY(float YY) {
        this.YY = YY;
    }

    public float getXx() {
        return xx;
    }

    public void setXx(float xx) {
        this.xx = xx;
    }

    public float getYy() {
        return yy;
    }

    public void setYy(float yy) {
        this.yy = yy;
    }



    public float getMx() {
        return mx;
    }

    public void setMx(float mx) {
        this.mx = mx;
    }

    public float getMy() {
        return my;
    }

    public void setMy(float my) {
        this.my = my;
    }

    public double getSqrt() {
        return sqrt;
    }

    public void setSqrt(double sqrt) {
        this.sqrt = sqrt;
    }
}

    public Data getData() {
        return data;
    }



}
