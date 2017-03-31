package com.example.customviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
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

    private int sepcpading=20;


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
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextView, defStyleAttr, 0);
        text = typedArray.getString(R.styleable.MyTextView_mText);
        textColor = typedArray.getColor(R.styleable.MyTextView_mTextColor, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.MyTextView_mTextSize, 100);
        typedArray.recycle();
        initData();
    }

    //初始化数据
    private void initData() {
        textList = new ArrayList<>();
        paint = new Paint();
        rect = new Rect();
        //获取
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        //将字符串的填充到矩形中
        paint.getTextBounds(text, 0, text.length(), rect);
    }

    //一般重写ondraew方法，
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //String text, float x, float y, Paint paint
        //1.第一个参数是要写的字符串，第二个参数是字符串开始的X轴上的位置，第三个参数是Y轴的坐标，第三个参数是画笔
        //2.计算字符串要显示到画布上的位置，如果要居中显示，控件大小的一半减去字符串一半的位置作为起始点即X轴坐标,高度也是一样的
        //3.将设置了颜色的画笔设置好
        //        canvas.drawText(text, getWidth() / 2 - rect.width() / 2, getHeight() / 2 - rect.height() / 2, paint);

        //换行，满一行换一行，动态的设置起始点的Y轴的位置，
        for (int i = 0; i < textList.size(); i++) {
            paint.getTextBounds(textList.get(i), 0, textList.get(i).length(), rect);
            if (i==0){
                canvas.drawText(textList.get(i), (getWidth() / 2 - rect.width() / 2), (getPaddingTop() + rect.height() * i), paint);
            }else {
                canvas.drawText(textList.get(i), (getWidth() / 2 - rect.width() / 2), (getPaddingTop() +sepcpading*i+ rect.height() * i), paint);
            }
        }

    }

    boolean isOneLines = true;
    float lineNum;
    float spLineNum;

    //当View的宽高设置为wrap_content时，父控件的宽高为match_parent，子控件将会全屏显示将会全屏展示
    //MeasureSpec.AT_MOST和MeasureSpec.EXACTLY返回的都是父控件所剩余的空间，
    // 在子控件为wrap_content和match_parent下都会返回父控件的剩余空间造成上面的现象出现
    //解决方法：重写测量方法当模式为MeasureSpec.AT_MOST时将子空间的大小返回给父类即可。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取宽高模式,模式为数字
        int widthModle = MeasureSpec.getMode(widthMeasureSpec);
        int heightModle = MeasureSpec.getMode(heightMeasureSpec);

        //获取父类的约数条件下的宽高
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Log.v("custom", "宽的模式:"+widthModle);
        Log.v("custom", "高的模式:"+heightModle);
        Log.v("custom", "宽的尺寸:"+widthSize);
        Log.v("custom", "高的尺寸:"+heightSize);

        int textWidth = rect.width();

        Log.v("custom", "文所需宽度:"+textWidth);

        if (textList.size() == 0) {
            //没有将总的字符串分割，判断一行最多能够放多长的字符串，即父类所给的剩余空间的宽度减去控件内部
            // 的pading即为一行的最大长度。
            int pading = getPaddingLeft() + getPaddingRight();
            int speaceWidth = widthSize - pading;

            Log.v("custom", "除去pading的的宽度:"+speaceWidth);
            //字符串的长度小于减去pading的长度，一行可以放得下
            if (textWidth < speaceWidth) {
                textList.add(text);
                lineNum = 1;
            } else {
                isOneLines = false;
                spLineNum = textWidth / speaceWidth;
                Log.v("custom", "没有去除小数的行数:"+spLineNum);
                //除不清，说明多一行需额外加1行
                if ((spLineNum + "").contains(".")) {
                    lineNum = Integer.parseInt((spLineNum + "").substring(0, (spLineNum + "").indexOf("."))) + 1;
                    Log.v("custom", "去除小数之后的行数"+lineNum);
                } else {
                    lineNum = spLineNum;
                }
                //字符串的总长度/行数，即每一行的字符串的长度
                float nLine = (text.length() / lineNum);
                int lineLength;
                if((nLine + "").contains(".")){
                    lineLength = Integer.parseInt((nLine + "").substring(0, (nLine + "").indexOf("."))) + 1;
                }else {
                    lineLength=(int)nLine;
                }
                Log.v("custom", "文本内容:"+text);
                Log.v("custom", "文本总长度:"+text.length());
                Log.v("custom", "能绘制文本每一行的字符串的长度:"+lineLength);
                Log.v("custom", "需要绘制:"+lineNum+"行");
                Log.v("custom", "lineLength:"+lineLength);

                //分割字符串，将字符串分割成
                for (int i = 0; i < lineNum; i++) {
                    String lineStr;

                    if (text.length() < lineLength) {
                        lineStr = text.substring(0, text.length());
                    } else {
                        lineStr = text.substring(0, lineLength);
                    }

                    textList.add(lineStr);
                    //每次重新给text辅助覆盖，使得text的值越来越少，指导空为止
                    if (!TextUtils.isEmpty(text)) {
                        if (text.length() < lineLength) {
                            text = text.substring(0, text.length());
                        } else {
                            text = text.substring(lineLength, text.length());
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        int width;
        int height;
        if (widthModle == MeasureSpec.EXACTLY) {
            //如果是精确数据直接保存父类给予的精确空间，否则就返回字符串的宽度
            width = widthSize;
        } else {
            if (isOneLines) {
                //padding+文本的宽度就是整个子控件的宽度
                width = getPaddingLeft() + textWidth + getPaddingRight();
            } else {
                //多行处理，返回父控件的宽度，即
                width = widthSize;
            }
        }
        if (heightModle == MeasureSpec.EXACTLY) {
            //如果是精确数据直接保存父类给予的精确空间，否则就返回字符串的宽度
            height = heightSize;
        } else {
            float textHeight = rect.height();
            if(isOneLines){
                height = (int)(getPaddingTop() + textHeight + getPaddingBottom());
            }else {
                //多行根据行高设置
                height=(int)(getPaddingTop() + textHeight*lineNum + getPaddingBottom());
            }

        }
        setMeasuredDimension(width, height);
    }
}
