package in.brainboxmedia.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/*
        This program was written by Mayank khan singh dsouza
        contact at mayank0398@gmail.com
    Intended for the Brain Box Media commercial use
            */
public class TicketView extends LinearLayout {
    private Bitmap bm;
    private Canvas cv;
    private Paint eraser;
    private int holesBottomMargin = 60;
    private int holeRadius = 30;

    public TicketView(Context context) {
        super(context);
        Init();
    }

    public TicketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public TicketView(Context context, AttributeSet attrs,
                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    private void Init() {
        eraser = new Paint();
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraser.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            cv = new Canvas(bm);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();

        bm.eraseColor(Color.TRANSPARENT);

        // set the view background color
        cv.drawColor(Color.WHITE);

        // drawing footer square contains the buy now button
        Paint paint = new Paint();
        paint.setARGB(255, 250, 250, 250);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        cv.drawRect(0, h, w, h - pxFromDp(getContext(), holesBottomMargin), paint);

        // adding punching holes on the ticket by erasing them
        cv.drawCircle(0, 0, holeRadius, eraser); // top-left hole
        cv.drawCircle(w / 2, 0, holeRadius, eraser); // top-middle hole
        cv.drawCircle(w, 0, holeRadius, eraser); // top-right
        cv.drawCircle(w / 2, h, holeRadius, eraser);// bottom-middle hole
        cv.drawCircle(0, h, holeRadius, eraser); // bottom-left hole
        cv.drawCircle(w, h, holeRadius, eraser); // bottom right hole

        // drawing the image
        canvas.drawBitmap(bm, 0, 0, null);

        // drawing dashed lines at the bottom
        Path mPath = new Path();
        mPath.moveTo(holeRadius, h - pxFromDp(getContext(), holesBottomMargin));
        mPath.quadTo(w - holeRadius, h - pxFromDp(getContext(), holesBottomMargin), w - holeRadius, h - pxFromDp(getContext(), holesBottomMargin));

        super.onDraw(canvas);
    }
}
