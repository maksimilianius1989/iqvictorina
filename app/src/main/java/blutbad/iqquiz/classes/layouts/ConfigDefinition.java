package blutbad.iqquiz.classes.layouts;

public class ConfigDefinition {
    private boolean checkCanFit;
    private boolean debugDraw = false;
    private int gravity = 51;
    private int heightMode;
    private int layoutDirection = 0;
    private int maxHeight;
    private int maxWidth;
    private int orientation = 0;
    private float weightDefault = 0.0f;
    private int widthMode;

    public ConfigDefinition() {
        setOrientation(0);
        setDebugDraw(false);
        setWeightDefault(0.0f);
        setGravity(0);
        setLayoutDirection(0);
        setCheckCanFit(true);
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        if (orientation == 1) {
            this.orientation = orientation;
        } else {
            this.orientation = 0;
        }
    }

    public boolean isDebugDraw() {
        return this.debugDraw;
    }

    public void setDebugDraw(boolean debugDraw) {
        this.debugDraw = debugDraw;
    }

    public float getWeightDefault() {
        return this.weightDefault;
    }

    public void setWeightDefault(float weightDefault) {
        this.weightDefault = Math.max(0.0f, weightDefault);
    }

    public int getGravity() {
        return this.gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getLayoutDirection() {
        return this.layoutDirection;
    }

    public void setLayoutDirection(int layoutDirection) {
        if (layoutDirection == 1) {
            this.layoutDirection = layoutDirection;
        } else {
            this.layoutDirection = 0;
        }
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxLength() {
        return this.orientation == 0 ? this.maxWidth : this.maxHeight;
    }

    public int getMaxThickness() {
        return this.orientation == 0 ? this.maxHeight : this.maxWidth;
    }

    public void setCheckCanFit(boolean checkCanFit) {
        this.checkCanFit = checkCanFit;
    }

    public boolean isCheckCanFit() {
        return this.checkCanFit;
    }

    public void setWidthMode(int widthMode) {
        this.widthMode = widthMode;
    }

    public void setHeightMode(int heightMode) {
        this.heightMode = heightMode;
    }

    public int getLengthMode() {
        return this.orientation == 0 ? this.widthMode : this.heightMode;
    }

    public int getThicknessMode() {
        return this.orientation == 0 ? this.heightMode : this.widthMode;
    }
}
