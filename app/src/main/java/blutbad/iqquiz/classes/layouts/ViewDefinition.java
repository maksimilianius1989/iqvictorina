package blutbad.iqquiz.classes.layouts;

import android.view.View;

public class ViewDefinition {
    private int bottomMargin;
    private final ConfigDefinition config;
    private int gravity;
    private int height;
    private int inlineStartLength;
    private int inlineStartThickness;
    private int leftMargin;
    private boolean newLine;
    private int rightMargin;
    private int topMargin;
    private final View view;
    private float weight;
    private int width;

    public ViewDefinition(ConfigDefinition config, View child) {
        this.config = config;
        this.view = child;
    }

    public int getLength() {
        return this.config.getOrientation() == 0 ? this.width : this.height;
    }

    public void setLength(int length) {
        if (this.config.getOrientation() == 0) {
            this.width = length;
        } else {
            this.height = length;
        }
    }

    public int getSpacingLength() {
        return this.config.getOrientation() == 0 ? this.leftMargin + this.rightMargin : this.topMargin + this.bottomMargin;
    }

    public int getThickness() {
        return this.config.getOrientation() == 0 ? this.height : this.width;
    }

    public void setThickness(int thickness) {
        if (this.config.getOrientation() == 0) {
            this.height = thickness;
        } else {
            this.width = thickness;
        }
    }

    public int getSpacingThickness() {
        return this.config.getOrientation() == 0 ? this.topMargin + this.bottomMargin : this.leftMargin + this.rightMargin;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean weightSpecified() {
        return this.weight >= 0.0f;
    }

    public int getInlineStartLength() {
        return this.inlineStartLength;
    }

    public void setInlineStartLength(int inlineStartLength) {
        this.inlineStartLength = inlineStartLength;
    }

    public boolean gravitySpecified() {
        return this.gravity != 0;
    }

    public int getGravity() {
        return this.gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public boolean isNewLine() {
        return this.newLine;
    }

    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
    }

    public View getView() {
        return this.view;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getInlineStartThickness() {
        return this.inlineStartThickness;
    }

    public void setInlineStartThickness(int inlineStartThickness) {
        this.inlineStartThickness = inlineStartThickness;
    }

    public void setMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        this.leftMargin = leftMargin;
        this.topMargin = topMargin;
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    public int getInlineX() {
        return this.config.getOrientation() == 0 ? this.inlineStartLength : this.inlineStartThickness;
    }

    public int getInlineY() {
        return this.config.getOrientation() == 0 ? this.inlineStartThickness : this.inlineStartLength;
    }
}
