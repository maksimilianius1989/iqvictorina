package blutbad.iqquiz.classes;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.apache.http.HttpStatus;

public class USER_CONTROL {
    public static int count_question = 0;
    public static int count_question_true = 0;
    public static int level_curque = 0;
    public static int[] level_exp = new int[]{0, 35, 70, 110, 150, 200, 260, 320, HttpStatus.SC_BAD_REQUEST, HttpStatus.SC_INTERNAL_SERVER_ERROR};
    public static int level_nextup = 100;
    public static int level_up = 0;
    public static int money = 100;
    public static boolean sound_state = true;
    public static int user_id = 0;
    SharedPreferences mSharedPref;

    public static void setData(SharedPreferences mSettings) {
        Editor saveSettings = mSettings.edit();
        saveSettings.putInt("count_question", count_question);
        saveSettings.putInt("count_question_true", count_question_true);
        saveSettings.putInt("money", money);
        saveSettings.putBoolean("sound_state", sound_state);
        saveSettings.commit();
    }

    public static void getSettingsData(SharedPreferences mSettings) {
        count_question = mSettings.getInt("count_question", count_question);
        count_question_true = mSettings.getInt("count_question_true", count_question_true);
        money = mSettings.getInt("money", money);
        sound_state = mSettings.getBoolean("sound_state", sound_state);
    }

    public static int getUser_Level() {
        int level = 0;
        int tmp_level_up = count_question_true;
        for (int i = 0; i < level_exp.length; i++) {
            tmp_level_up -= level_exp[i];
            if (tmp_level_up <= 0) {
                break;
            }
            level_nextup = level_exp[i + 1];
            level_up = level_exp[i];
            level_curque = tmp_level_up;
            level = i;
        }
        return level + 1;
    }

    public static int getUser_CurQuestion() {
        return level_curque;
    }

    public static int getUser_NextUp() {
        return level_nextup;
    }

    public static int getUser_Up() {
        return level_up;
    }

    public static int getUserID() {
        return user_id;
    }

    public static void setUserID(int id) {
        user_id = id;
    }
}
