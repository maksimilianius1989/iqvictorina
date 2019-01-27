package blutbad.iqquiz.classes;

import android.content.Context;
import android.media.SoundPool;
import blutbad.iqquiz.R;

public class GameSound {
    static int key_press;
    static Context mContext;
    static SoundPool soundPool;
    static boolean sound_state = true;
    static int win_end;

    /* renamed from: blutbad.iqquiz.classes.GameSound$1 */
    static class C02291 implements Runnable {
        C02291() {
        }

        public void run() {
            GameSound.soundPool = new SoundPool(5, 3, 0);
            GameSound.key_press = GameSound.soundPool.load(GameSound.mContext, R.raw.key_press, 1);
            GameSound.win_end = GameSound.soundPool.load(GameSound.mContext, R.raw.win_end, 1);
        }
    }

    /* renamed from: blutbad.iqquiz.classes.GameSound$2 */
    static class C02302 implements Runnable {
        C02302() {
        }

        public void run() {
            GameSound.soundPool.stop(GameSound.key_press);
            GameSound.soundPool.stop(GameSound.win_end);
        }
    }

    /* renamed from: blutbad.iqquiz.classes.GameSound$3 */
    static class C02313 implements Runnable {
        C02313() {
        }

        public void run() {
            GameSound.soundPool.play(GameSound.key_press, Float.valueOf("0.3").floatValue(), Float.valueOf("0.3").floatValue(), 0, 0, 1.0f);
        }
    }

    /* renamed from: blutbad.iqquiz.classes.GameSound$4 */
    static class C02324 implements Runnable {
        C02324() {
        }

        public void run() {
            GameSound.soundPool.play(GameSound.win_end, Float.valueOf("0.3").floatValue(), Float.valueOf("0.3").floatValue(), 0, 0, 1.0f);
        }
    }

    public static void Create(Context nContext) {
        mContext = nContext;
        new Thread(new C02291()).start();
    }

    public static void STOP_ALL() {
        if (sound_state) {
            new Thread(new C02302()).start();
        }
    }

    public static void Sound_State(boolean ss) {
        sound_state = ss;
    }

    public static void PLAY_key_press() {
        if (sound_state) {
            new Thread(new C02313()).start();
        }
    }

    public static void PLAY_win_end() {
        if (sound_state) {
            new Thread(new C02324()).start();
        }
    }
}
