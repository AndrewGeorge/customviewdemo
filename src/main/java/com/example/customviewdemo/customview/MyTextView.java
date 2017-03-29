package com.example.customviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.customviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class MyTextView extends View {
//    public MyTextView(Context context) {
//        super(context);
//    }
//
//    public MyTextView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    //文本内容
    private String text;
    //文本颜色
    private int textColor;
    //文本字体大小
    private int textSize;
    private Paint paint;

    private Rect rect;

    private List<String> textList;


    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取view的xml的位置在圣什么地方如何获解析View的？？？？
        //获取指定View所拥有的属性的列表
        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView,defStyleAttr,0);
        text=typedArray.getString(R.styleable.MyTextView_mText);
        textColor=typedArray.getColor(R.styleable.MyTextView_mTextColor,Color.BLACK);
        textSize=typedArray.getDimensionPixelSize(R.styleable.MyTextView_mTextSize,100);
            typedArray.recycle();
        initData();
    }
    //初始化数据
    private void initData() {
//        text = "hello word";
//        textColor = Color.BLACK;
//        textSize = 100;
        textList=new ArrayList<>();
        paint = new Paint();
        rect = new Rect();
        //获取

        paint.setTextSize(textSize);
        paint.setColor(textColor);

        //奖字符串的填充到矩形中
        paint.getTextBounds(text, 0, text.length(), rect);
        getWidth();
    }

    //一般重写ondraew方法，
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //String text, float x, float y, Paint paint
        //1.第一个参数是要写的字符串，第二个参数是字符串开始的X轴上的位置，第三个参数是Y轴的坐标，第三个参数是画笔
        //2.计算字符串要显示到画布上的位置，如果要居中显示，控件大小的一半减去字符串一半的位置作为起始点即X轴坐标,高度也是一样的
        //3.将设置了颜色的画笔设置好
        canvas.drawText(text, getWidth() / 2 - rect.width() / 2, getHeight() / 2 - rect.height() / 2, paint);
    }

    //当View的宽高设置为wrap_content时，父控件的宽高为match_parent，子控件将会全屏显示将会全屏展示
    //MeasureSpec.AT_MOST和MeasureSpec.EXACTLY返回的都是父控件所剩余的空间，
    // 在子控件为wrap_content和match_parent下都会返回父控件的剩余空间造成上面的现象出现
    //解决方法：重写测量方法当模式为MeasureSpec.AT_MOST时将子空间的大小返回给父类即可。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高模式,模式为数字
        int widthModle=MeasureSpec.getMode(widthMeasureSpec);
        int heightModle=MeasureSpec.getMode(heightMeasureSpec);

        //获取父类的约数条件下的宽高
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int  heightSize=MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthModle==MeasureSpec.EXACTLY){
            //如果是精确数据直接保存父类给予的精确空间，否则就返回字符串的宽度
            width=widthSize;
        }else {
            //padding+文本的宽度就是整个子控件的宽度
            width=getPaddingLeft()+rect.width()+getPaddingRight();
        }
        
        if (heightModle==MeasureSpec.EXACTLY){
            //如果是精确数据直接保存父类给予的精确空间，否则就返回字符串的宽度
            height=heightSize;
        }else {
            height=getPaddingTop()+rect.height()+getPaddingBottom();
        }

        setMeasuredDimension(width,height);


    }
}
