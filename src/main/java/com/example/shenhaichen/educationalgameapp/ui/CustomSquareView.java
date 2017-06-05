package com.example.shenhaichen.educationalgameapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.shenhaichen.educationalgameapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * the height of this view in game.xml must at least more than the height
 * of screen, if not, it will cause an error.
 * btw, all view is square
 * Created by shenhaichen on 10/05/2017.
 */
public class CustomSquareView extends View {
    // the array stores detail value
    private int[][] array_sudoku = null;
    private int width;
    private int height;
    private Context context;
    private Point centerPoint;
    //start point, at the left and top of panel
    private Point left_top_Point;
    private Point right_top_Point;
    private Point left_bottom_Point;
    private Point right_bottom_Point;
    private RectF puzzleRectF;
    // the width of every square
    private float square_width;
    // the number of every line
    private int square_num = 9;
    // the ratio of the cell, could adjust
    private static final float RATIO = 16 / 17F;

    private List<Rect> falseRects;
    private Paint bg_Paint;
    private Paint normalLinePaint;
    private Paint linePaint;
    private Paint selectPaint;
    private Rect selectRect;
    private Paint foregroundPaint;
    private Paint checkPaint;
    private Paint.FontMetrics fm;

    private int selectI = -1;
    private int selectJ = -1;

    // the main function of the touchArray is to control onTouch event
    // which means if there is a value at a point, it could not be chosen.
    private int[][] touchArray;
    //-1:there is value; 1:true; 0:false
    private int[][] checkArray;

    public CustomSquareView(Context context) {
        super(context);
        initPaint();
    }

    public CustomSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomSquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * init all paint, at the function of onDraw only drawing
     */
    private void initPaint() {
        bg_Paint = new Paint();
        bg_Paint.setColor(getResources().getColor(R.color.gainsboro));
        bg_Paint.setAntiAlias(true);

        normalLinePaint = new Paint();
        normalLinePaint.setColor(getResources().getColor(R.color.darkgray));
        normalLinePaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.DimGray));
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1.5f);

        selectPaint = new Paint();
        selectPaint.setColor(getResources().getColor(R.color.SkyBlue));
        selectPaint.setAntiAlias(true);

        foregroundPaint = new Paint();
        foregroundPaint.setColor(Color.BLACK);
        foregroundPaint.setStyle(Paint.Style.FILL);
        foregroundPaint.setTextSize(20);
        foregroundPaint.setTextScaleX(1);
        foregroundPaint.setTextAlign(Paint.Align.CENTER);
        fm = foregroundPaint.getFontMetrics();

        checkPaint = new Paint();
        checkPaint.setColor(getResources().getColor(R.color.HotPink));
        checkPaint.setAntiAlias(true);
    }

    /**
     * when the size of the view change, the view also have to change
     *
     * @param w    new width
     * @param h    new height
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
        // every line of the width have to change with the size change
        square_width = w * RATIO / square_num;
        // the content in the cell
        foregroundPaint.setTextSize(square_width * 0.65F);
        centerPoint = new Point(w / 2, h / 2);
        // find four key point of the view, to draw a rectangle of puzzle,
        left_top_Point = new Point((int) (w / 2 - w / 2 * RATIO), (int) (h / 2 - w / 2 * RATIO));
        right_top_Point = new Point((int) (w / 2 + w / 2 * RATIO), (int) (h / 2 - w / 2 * RATIO));
        left_bottom_Point = new Point((int) (w / 2 - w / 2 * RATIO), (int) (h / 2 + w / 2 * RATIO));
        right_bottom_Point = new Point((int) (w / 2 + w / 2 * RATIO), (int) (h / 2 + w / 2 * RATIO));
        puzzleRectF = new RectF(left_top_Point.x, left_top_Point.y, right_bottom_Point.x, right_bottom_Point.y);
        selectRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw background and square first
        canvas.drawRect(puzzleRectF, bg_Paint);
        // the detail square of horizontal and vertical line;
        for (int i = 0; i < 10; i++) {
            if (i % 3 == 0) {
                canvas.drawLine(left_top_Point.x + i * square_width, left_top_Point.y,
                        left_bottom_Point.x + i * square_width, left_bottom_Point.y, linePaint);
                canvas.drawLine(left_top_Point.x, left_top_Point.y + i * square_width,
                        right_top_Point.x, right_top_Point.y + i * square_width, linePaint);
            } else {
                canvas.drawLine(left_top_Point.x + i * square_width, left_top_Point.y,
                        left_bottom_Point.x + i * square_width, left_bottom_Point.y, normalLinePaint);
                canvas.drawLine(left_top_Point.x, left_top_Point.y + i * square_width,
                        right_top_Point.x, right_top_Point.y + i * square_width, normalLinePaint);
            }
        }
        if (selectRect != null) {
            canvas.drawRect(selectRect, selectPaint);
        }
        if (falseRects != null && falseRects.size() > 0) {
            for (Rect falseRect : falseRects) {
                canvas.drawRect(falseRect, checkPaint);
            }
        }

        if (array_sudoku != null) {
            for (int i = 0; i < array_sudoku.length; i++) {
                int[] row = array_sudoku[i];
                for (int j = 0; j < row.length; j++) {
                    int value = row[j];
                    if (value != 0) {
                        if (touchArray[i][j] == 1) {
                            foregroundPaint.setColor(Color.BLACK);
                            canvas.drawText(value + "", j * square_width + square_width / 2 + left_top_Point.x,
                                    i * square_width + square_width / 2 - (fm.ascent + fm.descent) + left_top_Point.y,
                                    foregroundPaint);
                        } else {
                            foregroundPaint.setColor(getResources().getColor(R.color.DoderBlue));
                            canvas.drawText(value + "", j * square_width + square_width / 2 + left_top_Point.x,
                                    i * square_width + square_width / 2 - (fm.ascent + fm.descent) + left_top_Point.y,
                                    foregroundPaint);
                        }
                    }
                }
            }
        }
    }

    /**
     *  this function to get the coordinate of the user touch
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        // the position of user touch, can not beyond the boundary of screen, which means cant over
        // four key point
        if (x < width / 2 - width / 2 * RATIO
                || x > width / 2 + width / 2 * RATIO
                || y < height / 2 - width / 2 * RATIO
                || y > height / 2 + width / 2 * RATIO) {
            return false;
        } else {
            int selX = (int) ((x - left_top_Point.x) / square_width);
            int selY = (int) ((y - left_top_Point.y) / square_width);
            selectI = selY;
            selectJ = selX;
//            System.out.println(selX + ":" + selY + "#" + touchArray[selY][selX]);
            // if the square did not be chosen
            if (touchArray[selY][selX] == 0) {
                // refresh the view of square
                invalidate(selectRect);
                falseRects = null;
                checkArray = null;
                // the square of select rectangle
                selectRect.set((int) (left_top_Point.x + selX * square_width), (int) (left_top_Point.y + selY * square_width),
                        (int) (left_top_Point.x + selX * square_width + square_width), (int) (left_top_Point.y + selY * square_width + square_width));
                invalidate(selectRect);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * setting the initial array
     * @param sudokuArray
     * @param isFirst is first time setting or not
     */
    public void setSudokuArray(int[][] sudokuArray, boolean isFirst) {
        this.array_sudoku = sudokuArray;
        if (isFirst) {
            touchArray = new int[square_num][square_num];
            for (int i = 0; i < sudokuArray.length; i++) {
                int[] row = sudokuArray[i];
                for (int j = 0; j < row.length; j++) {
                    if (row[j] != 0) {
                        touchArray[i][j] = 1;
                    } else {
                        touchArray[i][j] = 0;
                    }
                }
            }
        }
        invalidate();
    }

    /**
     *  after user click button of check, then will run this function to compare with the original array
     * @param checkArray
     */
    public void setCheckArray(int[][] checkArray) {
//        this.checkArray = checkArray;
        falseRects = new ArrayList<>();
        if (checkArray != null) {
            for (int i = 0; i < checkArray.length; i++) {
                int[] row = checkArray[i];
                for (int j = 0; j < row.length; j++) {
                    int value = row[j];
                    if (value == 0) {
                        Rect falseRect = new Rect((int)(left_top_Point.x + j * square_width), (int) (left_top_Point.y + i * square_width),
                                (int)(left_top_Point.x + j * square_num + square_width), (int) (left_top_Point.y + i * square_width + square_width));
                        falseRects.add(falseRect);
                    }
                }
            }
            invalidate();
        }
    }

    public int getSelectI() {
        return selectI;
    }

    public int getSelectJ() {
        return selectJ;
    }

    /**
     * set the amount of the column and row
     *
     * @param num
     */
    public void setNum(int num) {
        this.square_num = num;
        invalidate();
    }
}
