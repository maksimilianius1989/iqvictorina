package blutbad.iqquiz.classes;

import android.os.CountDownTimer;
import android.util.Log;

public class GameAddMoney {
    static int CUR_SEC_END = 600;
    static CountDownTimer MoneyTimer = null;
    public static String curEndTimer = "";
    static int curSecEndTimer = CUR_SEC_END;
    public static boolean isEndTimer = false;
    static int pauseSecEndTimer = 0;

    public static void create() {
        stop();
        isEndTimer = false;
        if (pauseSecEndTimer > 0) {
            curSecEndTimer = pauseSecEndTimer;
            Log.d("GameAddMoney", "Resume");
        } else {
            curSecEndTimer = CUR_SEC_END;
            Log.d("GameAddMoney", "create");
        }
        MoneyTimer = new CountDownTimer((long) (curSecEndTimer * 1000), 1000) {
            public void onTick(long millisUntilFinished) {
                GameAddMoney.curSecEndTimer--;
                if (GameAddMoney.curSecEndTimer <= 0) {
                    GameAddMoney.curSecEndTimer = 0;
                }
                GameAddMoney.curEndTimer = GameAddMoney.getTime(GameAddMoney.curSecEndTimer);
            }

            public void onFinish() {
                GameAddMoney.curSecEndTimer--;
                GameAddMoney.curEndTimer = GameAddMoney.getTime(GameAddMoney.curSecEndTimer);
                GameAddMoney.isEndTimer = true;
                GameAddMoney.curSecEndTimer = GameAddMoney.CUR_SEC_END;
                GameAddMoney.stop();
                Log.d("GameAddMoney", "onFinish");
            }
        }.start();
    }

    public static void Resume() {
        create();
        pauseSecEndTimer = 0;
        Log.d("GameAddMoney", "Resume");
    }

    public static void Pause() {
        pauseSecEndTimer = curSecEndTimer;
        stop();
        Log.d("GameAddMoney", "Pause");
    }

    public static void stop() {
        if (MoneyTimer != null) {
            MoneyTimer.cancel();
            MoneyTimer = null;
            Log.d("GameAddMoney", "stop");
        }
    }

    private static String getTime(int time) {
        String result = "";
        int hours = time / 3600;
        int minutes = (time - (hours * 3600)) / 60;
        int seconds = (time - (hours * 3600)) - (minutes * 60);
        if (hours > 0) {
            result = String.valueOf(hours) + ":";
        }
        if (minutes < 10) {
            result = result + "0" + String.valueOf(minutes) + ":";
        } else {
            result = result + String.valueOf(minutes) + ":";
        }
        if (seconds < 10) {
            return result + "0" + String.valueOf(seconds);
        }
        return result + String.valueOf(seconds);
    }
}
