package blutbad.iqquiz.classes.layouts;

import android.graphics.Rect;
import android.view.Gravity;
import java.util.List;

public class CommonLogic {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static void calculateLinesAndChildPosition(List<LineDefinition> lines) {
        int prevLinesThickness = 0;
        int linesCount = lines.size();
        for (int i = 0; i < linesCount; i++) {
            LineDefinition line = (LineDefinition) lines.get(i);
            line.setLineStartThickness(prevLinesThickness);
            prevLinesThickness += line.getLineThickness();
            int prevChildThickness = 0;
            List<ViewDefinition> childViews = line.getViews();
            int childCount = childViews.size();
            for (int j = 0; j < childCount; j++) {
                ViewDefinition child = (ViewDefinition) childViews.get(j);
                child.setInlineStartLength(prevChildThickness);
                prevChildThickness += child.getLength() + child.getSpacingLength();
            }
        }
    }

    public static void applyGravityToLines(List<LineDefinition> lines, int realControlLength, int realControlThickness, ConfigDefinition config) {
        int linesCount = lines.size();
        if (linesCount > 0) {
            int totalWeight = linesCount;
            LineDefinition lastLine = (LineDefinition) lines.get(linesCount - 1);
            int excessThickness = realControlThickness - (lastLine.getLineThickness() + lastLine.getLineStartThickness());
            if (excessThickness < 0) {
                excessThickness = 0;
            }
            int excessOffset = 0;
            for (int i = 0; i < linesCount; i++) {
                LineDefinition child = (LineDefinition) lines.get(i);
                int gravity = getGravity(null, config);
                int extraThickness = Math.round((float) ((excessThickness * 1) / totalWeight));
                int childLength = child.getLineLength();
                int childThickness = child.getLineThickness();
                Rect container = new Rect();
                container.top = excessOffset;
                container.left = 0;
                container.right = realControlLength;
                container.bottom = (childThickness + extraThickness) + excessOffset;
                Rect result = new Rect();
                Gravity.apply(gravity, childLength, childThickness, container, result);
                excessOffset += extraThickness;
                child.setLineStartLength(child.getLineStartLength() + result.left);
                child.setLineStartThickness(child.getLineStartThickness() + result.top);
                child.setLength(result.width());
                child.setThickness(result.height());
                applyGravityToLine(child, config);
            }
        }
    }

    public static void applyGravityToLine(LineDefinition line, ConfigDefinition config) {
        List<ViewDefinition> views = line.getViews();
        int viewCount = views.size();
        if (viewCount > 0) {
            int i;
            float totalWeight = 0.0f;
            for (i = 0; i < viewCount; i++) {
                totalWeight += getWeight((ViewDefinition) views.get(i), config);
            }
            ViewDefinition lastChild = (ViewDefinition) views.get(viewCount - 1);
            int excessLength = line.getLineLength() - ((lastChild.getLength() + lastChild.getSpacingLength()) + lastChild.getInlineStartLength());
            int excessOffset = 0;
            for (i = 0; i < viewCount; i++) {
                int extraLength;
                ViewDefinition child = (ViewDefinition) views.get(i);
                float weight = getWeight(child, config);
                int gravity = getGravity(child, config);
                if (totalWeight == 0.0f) {
                    extraLength = excessLength / viewCount;
                } else {
                    extraLength = Math.round((((float) excessLength) * weight) / totalWeight);
                }
                int childLength = child.getLength() + child.getSpacingLength();
                int childThickness = child.getThickness() + child.getSpacingThickness();
                Rect container = new Rect();
                container.top = 0;
                container.left = excessOffset;
                container.right = (childLength + extraLength) + excessOffset;
                container.bottom = line.getLineThickness();
                Rect result = new Rect();
                Gravity.apply(gravity, childLength, childThickness, container, result);
                excessOffset += extraLength;
                child.setInlineStartLength(result.left + child.getInlineStartLength());
                child.setInlineStartThickness(result.top);
                child.setLength(result.width() - child.getSpacingLength());
                child.setThickness(result.height() - child.getSpacingThickness());
            }
        }
    }

    public static int findSize(int modeSize, int controlMaxSize, int contentSize) {
        switch (modeSize) {
            case Integer.MIN_VALUE:
                return Math.min(contentSize, controlMaxSize);
            case 0:
                return contentSize;
            case 1073741824:
                return controlMaxSize;
            default:
                return contentSize;
        }
    }

    private static float getWeight(ViewDefinition child, ConfigDefinition config) {
        return child.weightSpecified() ? child.getWeight() : config.getWeightDefault();
    }

    private static int getGravity(ViewDefinition child, ConfigDefinition config) {
        int childGravity;
        int parentGravity = config.getGravity();
        if (child == null || !child.gravitySpecified()) {
            childGravity = parentGravity;
        } else {
            childGravity = child.getGravity();
        }
        childGravity = getGravityFromRelative(childGravity, config);
        parentGravity = getGravityFromRelative(parentGravity, config);
        if ((childGravity & 7) == 0) {
            childGravity |= parentGravity & 7;
        }
        if ((childGravity & 112) == 0) {
            childGravity |= parentGravity & 112;
        }
        if ((childGravity & 7) == 0) {
            childGravity |= 3;
        }
        if ((childGravity & 112) == 0) {
            return childGravity | 48;
        }
        return childGravity;
    }

    public static int getGravityFromRelative(int childGravity, ConfigDefinition config) {
        int i = 3;
        if (config.getOrientation() == 1 && (childGravity & 8388608) == 0) {
            int horizontalGravity = childGravity;
            childGravity = (0 | (((horizontalGravity & 7) >> 0) << 4)) | (((horizontalGravity & 112) >> 4) << 0);
        }
        if (config.getLayoutDirection() != 1 || (childGravity & 8388608) == 0) {
            return childGravity;
        }
        int i2;
        int ltrGravity = childGravity;
        if ((ltrGravity & 3) == 3) {
            i2 = 5;
        } else {
            i2 = 0;
        }
        childGravity = 0 | i2;
        if ((ltrGravity & 5) != 5) {
            i = 0;
        }
        return childGravity | i;
    }

    public static void fillLines(List<ViewDefinition> views, List<LineDefinition> lines, ConfigDefinition config) {
        LineDefinition currentLine = new LineDefinition(config);
        lines.add(currentLine);
        int count = views.size();
        for (int i = 0; i < count; i++) {
            boolean newLine;
            ViewDefinition child = (ViewDefinition) views.get(i);
            if (child.isNewLine() || (config.isCheckCanFit() && !currentLine.canFit(child))) {
                newLine = true;
            } else {
                newLine = false;
            }
            if (newLine) {
                currentLine = new LineDefinition(config);
                if (config.getOrientation() == 1 && config.getLayoutDirection() == 1) {
                    lines.add(0, currentLine);
                } else {
                    lines.add(currentLine);
                }
            }
            if (config.getOrientation() == 0 && config.getLayoutDirection() == 1) {
                currentLine.addView(0, child);
            } else {
                currentLine.addView(child);
            }
        }
    }
}
