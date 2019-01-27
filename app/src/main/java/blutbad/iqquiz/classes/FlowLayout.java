package blutbad.iqquiz.classes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import blutbad.iqquiz.R;
import blutbad.iqquiz.classes.layouts.CommonLogic;
import blutbad.iqquiz.classes.layouts.ConfigDefinition;
import blutbad.iqquiz.classes.layouts.LineDefinition;
import blutbad.iqquiz.classes.layouts.ViewDefinition;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private final ConfigDefinition config = new ConfigDefinition();
    List<LineDefinition> lines = new ArrayList();
    List<ViewDefinition> views = new ArrayList();

    public static class LayoutParams extends MarginLayoutParams {
        private int gravity = 0;
        @ExportedProperty(mapping = {@IntToString(from = 0, to = "NONE"), @IntToString(from = 48, to = "TOP"), @IntToString(from = 80, to = "BOTTOM"), @IntToString(from = 3, to = "LEFT"), @IntToString(from = 5, to = "RIGHT"), @IntToString(from = 16, to = "CENTER_VERTICAL"), @IntToString(from = 112, to = "FILL_VERTICAL"), @IntToString(from = 1, to = "CENTER_HORIZONTAL"), @IntToString(from = 7, to = "FILL_HORIZONTAL"), @IntToString(from = 17, to = "CENTER"), @IntToString(from = 119, to = "FILL")})
        private boolean newLine = false;
        private float weight = -1.0f;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            readStyleParameters(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        private void readStyleParameters(Context context, AttributeSet attributeSet) {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout_LayoutParams);
            try {
                this.newLine = a.getBoolean(1, false);
                this.gravity = a.getInt(0, 0);
                this.weight = a.getFloat(2, -1.0f);
            } finally {
                a.recycle();
            }
        }

        public int getGravity() {
            return this.gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        public float getWeight() {
            return this.weight;
        }

        public void setWeight(float weight) {
            this.weight = weight;
        }

        public boolean isNewLine() {
            return this.newLine;
        }

        public void setNewLine(boolean newLine) {
            this.newLine = newLine;
        }
    }

    public FlowLayout(Context context) {
        super(context);
        readStyleParameters(context, null);
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        readStyleParameters(context, attributeSet);
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        readStyleParameters(context, attributeSet);
    }

    private void readStyleParameters(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.FlowLayout);
        try {
            this.config.setOrientation(a.getInteger(1, 0));
            this.config.setDebugDraw(a.getBoolean(3, false));
            this.config.setWeightDefault(a.getFloat(4, 0.0f));
            this.config.setGravity(a.getInteger(0, 0));
            this.config.setLayoutDirection(a.getInteger(2, 0));
        } finally {
            a.recycle();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int count = getChildCount();
        this.views.clear();
        this.lines.clear();
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), lp.width), getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), lp.height));
                ViewDefinition viewDefinition = new ViewDefinition(this.config, child);
                viewDefinition.setWidth(child.getMeasuredWidth());
                viewDefinition.setHeight(child.getMeasuredHeight());
                viewDefinition.setNewLine(lp.isNewLine());
                viewDefinition.setGravity(lp.getGravity());
                viewDefinition.setWeight(lp.getWeight());
                viewDefinition.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
                this.views.add(viewDefinition);
            }
        }
        this.config.setMaxWidth((MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight()) - getPaddingLeft());
        this.config.setMaxHeight((MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()) - getPaddingBottom());
        this.config.setWidthMode(MeasureSpec.getMode(widthMeasureSpec));
        this.config.setHeightMode(MeasureSpec.getMode(heightMeasureSpec));
        this.config.setCheckCanFit(this.config.getLengthMode() != 0);
        CommonLogic.fillLines(this.views, this.lines, this.config);
        CommonLogic.calculateLinesAndChildPosition(this.lines);
        int contentLength = 0;
        int linesCount = this.lines.size();
        for (i = 0; i < linesCount; i++) {
            contentLength = Math.max(contentLength, ((LineDefinition) this.lines.get(i)).getLineLength());
        }
        LineDefinition currentLine = (LineDefinition) this.lines.get(this.lines.size() - 1);
        int contentThickness = currentLine.getLineStartThickness() + currentLine.getLineThickness();
        CommonLogic.applyGravityToLines(this.lines, CommonLogic.findSize(this.config.getLengthMode(), this.config.getMaxLength(), contentLength), CommonLogic.findSize(this.config.getThicknessMode(), this.config.getMaxThickness(), contentThickness), this.config);
        for (i = 0; i < linesCount; i++) {
            applyPositionsToViews((LineDefinition) this.lines.get(i));
        }
        int totalControlWidth = getPaddingLeft() + getPaddingRight();
        int totalControlHeight = getPaddingBottom() + getPaddingTop();
        if (this.config.getOrientation() == 0) {
            totalControlWidth += contentLength;
            totalControlHeight += contentThickness;
        } else {
            totalControlWidth += contentThickness;
            totalControlHeight += contentLength;
        }
        setMeasuredDimension(resolveSize(totalControlWidth, widthMeasureSpec), resolveSize(totalControlHeight, heightMeasureSpec));
    }

    private void applyPositionsToViews(LineDefinition line) {
        List<ViewDefinition> childViews = line.getViews();
        int childCount = childViews.size();
        for (int i = 0; i < childCount; i++) {
            ViewDefinition child = (ViewDefinition) childViews.get(i);
            child.getView().measure(MeasureSpec.makeMeasureSpec(child.getWidth(), 1073741824), MeasureSpec.makeMeasureSpec(child.getHeight(), 1073741824));
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int linesCount = this.lines.size();
        for (int i = 0; i < linesCount; i++) {
            LineDefinition line = (LineDefinition) this.lines.get(i);
            int count = line.getViews().size();
            for (int j = 0; j < count; j++) {
                ViewDefinition child = (ViewDefinition) line.getViews().get(j);
                View view = child.getView();
                LayoutParams lp = (LayoutParams) view.getLayoutParams();
                view.layout(((getPaddingLeft() + line.getX()) + child.getInlineX()) + lp.leftMargin, ((getPaddingTop() + line.getY()) + child.getInlineY()) + lp.topMargin, (((getPaddingLeft() + line.getX()) + child.getInlineX()) + lp.leftMargin) + child.getWidth(), (((getPaddingTop() + line.getY()) + child.getInlineY()) + lp.topMargin) + child.getHeight());
            }
        }
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        drawDebugInfo(canvas, child);
        return more;
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    protected LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    private void drawDebugInfo(Canvas canvas, View child) {
        if (isDebugDraw()) {
            float x;
            float y;
            Paint childPaint = createPaint(InputDeviceCompat.SOURCE_ANY);
            Paint newLinePaint = createPaint(SupportMenu.CATEGORY_MASK);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.rightMargin > 0) {
                x = (float) child.getRight();
                y = ((float) child.getTop()) + (((float) child.getHeight()) / 2.0f);
                canvas.drawLine(x, y, x + ((float) lp.rightMargin), y, childPaint);
                canvas.drawLine((((float) lp.rightMargin) + x) - 4.0f, y - 4.0f, x + ((float) lp.rightMargin), y, childPaint);
                canvas.drawLine((((float) lp.rightMargin) + x) - 4.0f, y + 4.0f, x + ((float) lp.rightMargin), y, childPaint);
            }
            if (lp.leftMargin > 0) {
                x = (float) child.getLeft();
                y = ((float) child.getTop()) + (((float) child.getHeight()) / 2.0f);
                canvas.drawLine(x, y, x - ((float) lp.leftMargin), y, childPaint);
                canvas.drawLine((x - ((float) lp.leftMargin)) + 4.0f, y - 4.0f, x - ((float) lp.leftMargin), y, childPaint);
                canvas.drawLine((x - ((float) lp.leftMargin)) + 4.0f, y + 4.0f, x - ((float) lp.leftMargin), y, childPaint);
            }
            if (lp.bottomMargin > 0) {
                x = ((float) child.getLeft()) + (((float) child.getWidth()) / 2.0f);
                y = (float) child.getBottom();
                canvas.drawLine(x, y, x, y + ((float) lp.bottomMargin), childPaint);
                canvas.drawLine(x - 4.0f, (((float) lp.bottomMargin) + y) - 4.0f, x, y + ((float) lp.bottomMargin), childPaint);
                canvas.drawLine(x + 4.0f, (((float) lp.bottomMargin) + y) - 4.0f, x, y + ((float) lp.bottomMargin), childPaint);
            }
            if (lp.topMargin > 0) {
                x = ((float) child.getLeft()) + (((float) child.getWidth()) / 2.0f);
                y = (float) child.getTop();
                canvas.drawLine(x, y, x, y - ((float) lp.topMargin), childPaint);
                canvas.drawLine(x - 4.0f, (y - ((float) lp.topMargin)) + 4.0f, x, y - ((float) lp.topMargin), childPaint);
                canvas.drawLine(x + 4.0f, (y - ((float) lp.topMargin)) + 4.0f, x, y - ((float) lp.topMargin), childPaint);
            }
            if (!lp.isNewLine()) {
                return;
            }
            if (this.config.getOrientation() == 0) {
                x = (float) child.getLeft();
                y = ((float) child.getTop()) + (((float) child.getHeight()) / 2.0f);
                canvas.drawLine(x, y - 6.0f, x, y + 6.0f, newLinePaint);
                return;
            }
            x = ((float) child.getLeft()) + (((float) child.getWidth()) / 2.0f);
            y = (float) child.getTop();
            canvas.drawLine(x - 6.0f, y, x + 6.0f, y, newLinePaint);
        }
    }

    private Paint createPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(2.0f);
        return paint;
    }

    public int getOrientation() {
        return this.config.getOrientation();
    }

    public void setOrientation(int orientation) {
        this.config.setOrientation(orientation);
        requestLayout();
    }

    public boolean isDebugDraw() {
        return this.config.isDebugDraw() || debugDraw();
    }

    public void setDebugDraw(boolean debugDraw) {
        this.config.setDebugDraw(debugDraw);
        invalidate();
    }

    private boolean debugDraw() {
        try {
            Method m = ViewGroup.class.getDeclaredMethod("debugDraw", (Class[]) null);
            m.setAccessible(true);
            return ((Boolean) m.invoke(this, new Object[]{null})).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public float getWeightDefault() {
        return this.config.getWeightDefault();
    }

    public void setWeightDefault(float weightDefault) {
        this.config.setWeightDefault(weightDefault);
        requestLayout();
    }

    public int getGravity() {
        return this.config.getGravity();
    }

    public void setGravity(int gravity) {
        this.config.setGravity(gravity);
        requestLayout();
    }

    public int getLayoutDirection() {
        if (this.config == null) {
            return 0;
        }
        return this.config.getLayoutDirection();
    }

    public void setLayoutDirection(int layoutDirection) {
        this.config.setLayoutDirection(layoutDirection);
        requestLayout();
    }
}
