package com.yhongm.mark_core;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wwt.mark_core.R;


public class ScaleView extends View {
    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private int radius;
    private int padding = 10;
    private float currentAngle = 0;
    private double initAngle;
    private Path mArcPath;
    private Paint mArcPaint;// 弧线画笔
    private Paint mScaleLinePaint;//刻度线画笔
    private TextPaint mScaleTextPaint;//刻度值画笔
    private TextPaint mSelectedTextPaint;//选中刻度线画笔
    private Paint mCurrentSelectValuePaint;//当前值画笔
    private Paint mCircleValuePaint;//当前值画笔

    private Paint mIndicatorPaint;//指针画笔
    private Paint mMaskPaint;//画遮罩层画笔
    private String mScaleUnit = "单位";//刻度单位
    private float mEvenyScaleValue = 1;//每滑动多少度,值得增加
    private int mScaleNumber = 30;//刻度数量
    private int mScaleSpace = 1;//刻度间距
    private int mScaleMin = 200;//刻度最小值
    private int mDrawLineSpace = 1;//画线间距
    private int mDrawTextSpace = 5;//画刻度值的间隔

    private int mArcLineColor = Color.RED;//弧线颜色
    private int mScaleLineColor = Color.BLUE;//刻度线颜色
    private int mIndicatorColor = Color.GREEN;
    private int mScaleTextColor = Color.BLACK;//刻度文字颜色
    private int mSelectTextColor = Color.BLACK;//选中刻度文字颜色

    private int mScaleMaxLength = 100;//刻度尺的长度

    private int eachScalePix = 15;//每个刻度值的像素
    private int mSlidingMoveX = 0;//滑动的差值
    private int totalX = 0;//滑动总距离
    private int mDownX;
    private int currentValue = 0;// 当前刻度值


    public ScaleView(Context context) {
        this(context, null);

    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        initAttr(context, attrs);
        initPath();
        initPaint();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
        int shape = typedArray.getInt(R.styleable.ScaleView_shape, 0);

        mScaleUnit = typedArray.getString(R.styleable.ScaleView_scaleUnit);
        if (TextUtils.isEmpty(mScaleUnit)) {
            mScaleUnit = "";
        }
        mEvenyScaleValue = typedArray.getFloat(R.styleable.ScaleView_everyScaleValue, 1);
        mScaleNumber = typedArray.getInt(R.styleable.ScaleView_scaleNum, 30);
        mScaleSpace = typedArray.getInt(R.styleable.ScaleView_scaleSpace, 1);
        mScaleMin = typedArray.getInt(R.styleable.ScaleView_scaleMin, 30);
        mDrawLineSpace = typedArray.getInt(R.styleable.ScaleView_drawLineSpace, 1);
        mDrawTextSpace = typedArray.getInt(R.styleable.ScaleView_drawTextSpace, 5);

        mArcLineColor = typedArray.getColor(R.styleable.ScaleView_arcLineColor, Color.RED);
        mScaleLineColor = typedArray.getColor(R.styleable.ScaleView_scaleLineColor, Color.RED);
        mIndicatorColor = typedArray.getColor(R.styleable.ScaleView_indicatorColor, Color.GREEN);
        mScaleTextColor = typedArray.getColor(R.styleable.ScaleView_scaleTextColor, Color.BLACK);
        mSelectTextColor = typedArray.getColor(R.styleable.ScaleView_selectTextColor, Color.BLACK);

        mScaleLineColor = typedArray.getColor(R.styleable.ScaleView_scaleLineColor, Color.parseColor("#666666"));

        mScaleTextColor = typedArray.getColor(R.styleable.ScaleView_scaleTextColor, Color.parseColor("#666666"));

        mIndicatorColor = typedArray.getColor(R.styleable.ScaleView_indicatorColor, Color.parseColor("#ff9933"));
        mScaleMaxLength = typedArray.getInt(R.styleable.ScaleView_scaleMaxLength, 100);

    }

    private void initPaint() {
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(mArcLineColor);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(18);

        mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScaleLinePaint.setColor(mScaleLineColor);
        mScaleLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);

        mScaleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mScaleTextPaint.setColor(mScaleTextColor);
        mScaleTextPaint.setTypeface(Typeface.SANS_SERIF);
        mScaleTextPaint.setTextSize(20);


        mSelectedTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mSelectedTextPaint.setTypeface(Typeface.SERIF);
        mSelectedTextPaint.setColor(mSelectTextColor);
        mSelectedTextPaint.setTextSize(50);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(18);
        mIndicatorPaint.setStyle(Paint.Style.FILL);

        mMaskPaint = new Paint();
        mMaskPaint.setFlags(Paint.ANTI_ALIAS_FLAG);


        mScaleLinePaint = new Paint();
        mScaleLinePaint.setAntiAlias(true);
        mScaleLinePaint.setStyle(Paint.Style.STROKE);
        mScaleLinePaint.setStrokeWidth(2);
        mScaleLinePaint.setColor(mScaleLineColor);


        mCurrentSelectValuePaint = new Paint();
        mCurrentSelectValuePaint.setAntiAlias(true);
        mCurrentSelectValuePaint.setTextSize(60);
        mCurrentSelectValuePaint.setColor(mSelectTextColor);
        mCurrentSelectValuePaint.setTextAlign(Paint.Align.CENTER);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setColor(mIndicatorColor);

        mCircleValuePaint = new Paint();
        mCircleValuePaint.setAntiAlias(true);
        mCircleValuePaint.setColor(Color.parseColor("#ECF4FB"));

    }

    private void initPath() {
        mArcPath = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();


            /**
             * 获取自定义View的测量后的宽和高，计算得出中心点坐标。
             */
            mCenterY = getMeasuredHeight() / 2;
            mCenterX = getMeasuredWidth() / 2;

            Log.e("TAG","==mCenter==="+mCenterX+":"+mCenterY);


    }

    @Override
    protected void onDraw(Canvas canvas) {


            //画出圆形
            drawTextCircle(canvas);

            drawNum(canvas);

            drawCurrentScale(canvas);

    }

    private void drawTextCircle(Canvas canvas) {
        currentValue = (-totalX + mCenterX) / eachScalePix + mScaleMin;

        Log.e("TAG","==currentValue==="+currentValue+"===mScaleMin==="+mScaleMin);

        canvas.drawCircle(mCenterX, mCenterY-150, 100, mCircleValuePaint);
    }


    /**
     * 绘画数字
     * 总共宽度为自定义View的测量宽度，每次增加一个刻度。
     * @param canvas
     */
    private void drawNum(Canvas canvas) {
        mScaleTextPaint.setStrokeWidth(2);
        mScaleTextPaint.setColor(mScaleTextColor);
        mScaleTextPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < mWidth; i++) {
            int top = mCenterY + 10;
            if ((-totalX + i) % (eachScalePix * mDrawTextSpace) == 0) {
//                top = top /*+ 10*/;
                if ((-totalX + i) >= 0 && (-totalX + i) <= mScaleMaxLength * eachScalePix)
                    canvas.drawText((-totalX + i) / eachScalePix + mScaleMin + "", i, top + 40, mScaleTextPaint);
            }


                if ((-totalX + i) % eachScalePix == 0) {
                if ((-totalX + i) >= 0 && (-totalX + i) <= mScaleMaxLength * eachScalePix) {
                    if(((-totalX + i) % (eachScalePix * mDrawTextSpace) == 0)){
                        canvas.drawLine(i, mCenterY-10, i, top+3, mScaleLinePaint);
                    }else{
                        canvas.drawLine(i, mCenterY, i, top, mScaleLinePaint);
                    }


                }
            }

        }

        canvas.drawLine(0, mCenterY+13, this.getMeasuredWidth(), mCenterY+13, mScaleLinePaint);


    }

    /**
     * 绘画当前刻度
     *
     * @param canvas
     */
    private void drawCurrentScale(Canvas canvas) {
        currentValue = (-totalX + mCenterX) / eachScalePix + mScaleMin;
        RectF roundRectF = new RectF();
        roundRectF.left = mCenterX - 3;
        roundRectF.right = mCenterX + 3;
        roundRectF.top = mCenterY-25;
        roundRectF.bottom = mCenterY +50 ;
        canvas.drawRoundRect(roundRectF, 6, 6, mIndicatorPaint);

        String currentScaleText = currentValue + "";

        canvas.drawText(currentScaleText /*+ mScaleUnit*/, mCenterX, mCenterY - 130, mCurrentSelectValuePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                    mDownX = (int) event.getX();
                 break;
            case MotionEvent.ACTION_MOVE:

                    mSlidingMoveX = (int) (event.getX() - mDownX);//滑动距离
                    totalX = totalX + mSlidingMoveX;
                    Log.e("TAG","==mSlidingMoveX=="+mSlidingMoveX+"==totalX=="+totalX);

                    if (mSlidingMoveX < 0) {
                        //向左滑动,刻度值增大
                        if (-totalX + mCenterX > mScaleMaxLength * eachScalePix) {
                            //向左滑动如果刻度值大于最大值，则不能滑动了
                            totalX = totalX - mSlidingMoveX;
                            return true;
                        } else {
                            invalidate();
                        }
                    } else {
                        //向右滑动，刻度值减小
                        //                    向右滑动刻度值小于最小值则不能滑动了
                        if (totalX - mCenterX > 0) {
                            totalX = totalX - mSlidingMoveX;
                            return true;
                        } else {
                            invalidate();

                        }
                    }
                    // fix g19980115建议解决滑动边界问题  see issue https://github.com/yhongm/ScaleView/issues/1
                    //向左滑动,刻度值增大
                    if (mSlidingMoveX < 0) {

                        if (-totalX + mCenterX > mScaleMaxLength * eachScalePix) {
                            //向左滑动如果刻度值大于最大值，则不能滑动了
                            totalX = -mScaleMaxLength * eachScalePix + mCenterX;
                            invalidate();
                            return true;
                        }
//                        else {
//                            invalidate();
//
//                        }
                        else{
                            //向右滑动，刻度值减小
                            //向右滑动刻度值小于最小值则不能滑动了
                            if (totalX - mCenterX > 0) {
                                totalX = mCenterX;
                                invalidate();
                                return true;
                            } else {
                                invalidate();
                            }
                        }
                        mDownX = (int) event.getX();

                }
                break;
                    case MotionEvent.ACTION_UP:
                break;
        }

        return super.onTouchEvent(event);
    }


    private double calcArcAngle(double arc) {
        double angle = arc * 180.0 / Math.PI;
        return angle;
    }




}
