package blutbad.iqquiz.classes.layouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import blutbad.iqquiz.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ScrollingImageView extends View {
    public static ScrollingImageViewBitmapLoader BITMAP_LOADER = new C05821();
    private int arrayIndex = 0;
    private List<Bitmap> bitmaps;
    private Rect clipBounds = new Rect();
    private boolean isStarted;
    private int maxBitmapHeight = 0;
    private float offset = 0.0f;
    private int[] scene;
    private float speed;

    /* renamed from: blutbad.iqquiz.classes.layouts.ScrollingImageView$1 */
    static class C05821 implements ScrollingImageViewBitmapLoader {
        C05821() {
        }

        public Bitmap loadBitmap(Context context, int resourceId) {
            return BitmapFactory.decodeResource(context.getResources(), resourceId);
        }
    }

    public ScrollingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ParallaxView, 0, 0);
        int initialState = 0;
        TypedArray typedArray;
        try {
            initialState = ta.getInt(R.styleable.ParallaxView_initialState, 0);
            this.speed = ta.getDimension(R.styleable.ParallaxView_speedd, 10.0f);
            int sceneLength = ta.getInt(R.styleable.ParallaxView_sceneLength, 1000);
            int randomnessResourceId = ta.getResourceId(R.styleable.ParallaxView_randomnessResourceId, 0);
            int[] randomness = new int[0];
            if (randomnessResourceId > 0) {
                randomness = getResources().getIntArray(randomnessResourceId);
            }
            int type = isInEditMode() ? 3 : ta.peekValue(1).type;
            Bitmap bitmap;
            if (type == 1) {
                typedArray = getResources().obtainTypedArray(ta.getResourceId(R.styleable.ParallaxView_typedArray, 0));
                int bitmapsSize = 0;
                for (int r : randomness) {
                    bitmapsSize += r;
                }
                this.bitmaps = new ArrayList(Math.max(typedArray.length(), bitmapsSize));
                int i = 0;
                while (i < typedArray.length()) {
                    int multiplier = 1;
                    if (randomness.length > 0 && i < randomness.length) {
                        multiplier = Math.max(1, randomness[i]);
                    }
                    bitmap = BITMAP_LOADER.loadBitmap(getContext(), typedArray.getResourceId(i, 0));
                    for (int m = 0; m < multiplier; m++) {
                        this.bitmaps.add(bitmap);
                    }
                    this.maxBitmapHeight = Math.max(bitmap.getHeight(), this.maxBitmapHeight);
                    i++;
                }
                Random random = new Random();
                this.scene = new int[sceneLength];
                for (i = 0; i < this.scene.length; i++) {
                    this.scene[i] = random.nextInt(this.bitmaps.size());
                }
                try {
                    ta.recycle();
                } catch (Exception e) {
                }
                if (initialState == 0) {
                    start();
                }
            }
            if (type == 3) {
                bitmap = BITMAP_LOADER.loadBitmap(getContext(), ta.getResourceId(R.styleable.ParallaxView_srcc, 0));
                if (bitmap != null) {
                    this.bitmaps = Collections.singletonList(bitmap);
                    this.scene = new int[]{0};
                    this.maxBitmapHeight = ((Bitmap) this.bitmaps.get(0)).getHeight();
                } else {
                    this.bitmaps = Collections.emptyList();
                }
            }
            ta.recycle();
            if (initialState == 0) {
                start();
            }
        } catch (Throwable th) {
        } finally {

        }
    }
/*
Method generation error in method: blutbad.iqquiz.classes.layouts.ScrollingImageView.<init>(android.content.Context, android.util.AttributeSet):void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x018e: INVOKE  (wrap: android.content.res.TypedArray
  ?: MERGE  (r19_2 'typedArray' android.content.res.TypedArray) = (r19_1 'typedArray' android.content.res.TypedArray), (r17_0 'ta' android.content.res.TypedArray)) android.content.res.TypedArray.recycle():void type: VIRTUAL in method: blutbad.iqquiz.classes.layouts.ScrollingImageView.<init>(android.content.Context, android.util.AttributeSet):void, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:203)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:100)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:50)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:299)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:187)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:320)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:257)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:220)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:75)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:12)
	at jadx.core.ProcessClass.process(ProcessClass.java:40)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
	at jadx.api.JadxDecompiler$$Lambda$8/1840976765.run(Unknown Source)
Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r19_2 'typedArray' android.content.res.TypedArray) = (r19_1 'typedArray' android.content.res.TypedArray), (r17_0 'ta' android.content.res.TypedArray) in method: blutbad.iqquiz.classes.layouts.ScrollingImageView.<init>(android.content.Context, android.util.AttributeSet):void, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:226)
	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:101)
	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:84)
	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:632)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:338)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:220)
	... 22 more
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:537)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:509)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:211)
	... 27 more

*/

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), this.maxBitmapHeight);
            }

            public void onDraw(Canvas canvas) {
                super.onDraw(canvas);
   /*             if (canvas != null && !this.bitmaps.isEmpty()) {
                    canvas.getClipBounds(this.clipBounds);
                    while (this.offset <= ((float) (-getBitmap(this.arrayIndex).getWidth()))) {
                        this.offset += (float) getBitmap(this.arrayIndex).getWidth();
                        this.arrayIndex = (this.arrayIndex + 1) % this.scene.length;
                    }
                    float left = this.offset;
                    int i = 0;
                    while (left < ((float) this.clipBounds.width())) {
                        Bitmap bitmap = getBitmap((this.arrayIndex + i) % this.scene.length);
                        int width = bitmap.getWidth();
                        canvas.drawBitmap(bitmap, getBitmapLeft((float) width, left), 0.0f, null);
                        left += (float) width;
                        i++;
                    }
                    if (this.isStarted && this.speed != 0.0f) {
                        this.offset -= Math.abs(this.speed);
                        postInvalidateOnAnimation();
                    }
                }*/
            }

            private Bitmap getBitmap(int sceneIndex) {
                return (Bitmap) this.bitmaps.get(this.scene[sceneIndex]);
            }

            private float getBitmapLeft(float layerWidth, float left) {
                if (this.speed < 0.0f) {
                    return (((float) this.clipBounds.width()) - layerWidth) - left;
                }
                return left;
            }

            public void start() {
                if (!this.isStarted) {
                    this.isStarted = true;
                    postInvalidateOnAnimation();
                }
            }

            public void stop() {
                if (this.isStarted) {
                    this.isStarted = false;
                    invalidate();
                }
            }

            public void setSpeed(float speed) {
                this.speed = speed;
                if (this.isStarted) {
                    postInvalidateOnAnimation();
                }
            }
        }
