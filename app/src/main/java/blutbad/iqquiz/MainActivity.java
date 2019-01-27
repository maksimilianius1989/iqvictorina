package blutbad.iqquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import blutbad.iqquiz.classes.FlowLayout;
import blutbad.iqquiz.classes.GameAddMoney;
import blutbad.iqquiz.classes.GameSound;
import blutbad.iqquiz.classes.JSONParser;
import blutbad.iqquiz.classes.QDialog;
import blutbad.iqquiz.classes.USER_CONTROL;
import blutbad.iqquiz.classes.layouts.CircularProgressBar;
import blutbad.iqquiz.dialogs.DialogGameMoney;
import blutbad.iqquiz.dialogs.DialogGameStatistics;
import blutbad.iqquiz.dialogs.DialogMoreGames;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnClickListener, RewardedVideoAdListener {
    private static final String SERVER_SUCCESS = "success";
    private static Context instance = null;
    public static boolean isVideoAd = false;
    public static RewardedVideoAd mRewardedVideoAd = null;
    public static SharedPreferences mSettings = null;
    static final String url_iqquiz = "http://blutbad.ru/iqquiz/iqquiz.php";
    final String ALL_NUMS = "0123456789";
    int ANSWER_SECOND_TO_ELAPCE = 50;
    ArrayList<String> Answer_Keys = new ArrayList();
    ArrayList<String> Answer_OpenKeys = new ArrayList();
    CountDownTimer EndQuestionTimer = null;
    final String HIDE_CHAR = "?";
    final int MAX_COUNT_KEY = 14;
    CountDownTimer QuestionTimer;
    Button btn_answer_key;
    Button btn_answer_openkey;
    TextView btn_delete_key;
    Button btn_menu_exitgame;
    Button btn_menu_more;
    Button btn_menu_newgame;
    Button btn_menu_resumegame;
    Button btn_menu_sendgame;
    Button btn_menu_statgame;
    int count_draw_char_answer = 0;
    CircularProgressBar cpb_time_elapce;
    String cur_Answer = "";
    String cur_Id = "0";
    String cur_Online = "0";
    String cur_Question = "";
    int end_elapce_Answer = 0;
    FrameLayout fl_error_load;
    FrameLayout fl_game_timer;
    FlowLayout fl_keyboard_conteiner;
    FrameLayout fl_keyboard_list;
    FrameLayout fl_main_conteiner_game;
    FlowLayout fl_question_answer;
    FrameLayout fl_question_keyboard;
    ImageView iv_logo;
    JSONParser jsonParser_Answer = new JSONParser();
    LinearLayout ll_question_top;
    LinearLayout ll_start_game;
    boolean mGameStart = false;
    boolean mGameVisible = true;
    JSONArray mGetAnswer = null;
    Button old_sel_btnkey;
    ProgressBar pb_question_load;
    RelativeLayout rl_main_game;
    int select_pos_char = -1;
    String select_pos_str = "";
    TextView tv_menu_online;
    TextView tv_money_total;
    TextView tv_question_count;
    TextView tv_question_id;
    TextView tv_question_text;
    TextView tv_question_win_text;
    TextView tv_time_elapce;
    TextView tv_user_level;

    /* renamed from: blutbad.iqquiz.MainActivity$1 */
    class C02171 implements OnClickListener {
        C02171() {
        }

        public void onClick(View v) {
            MainActivity.this.open_GameStatistics();
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$2 */
    class C02182 implements OnClickListener {
        C02182() {
        }

        public void onClick(View v) {
            MainActivity.this.open_GameStatistics();
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$3 */
    class C02193 implements OnClickListener {
        C02193() {
        }

        public void onClick(View v) {
            MainActivity.this.open_GameMoney();
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$4 */
    class C02204 implements OnClickListener {
        C02204() {
        }

        public void onClick(View v) {
            MainActivity.this.Answer_dellast_Char();
            MainActivity.this.DrawAnswer();
            MainActivity.this.togele_btn_answer();
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$5 */
    class C02215 implements OnClickListener {
        C02215() {
        }

        public void onClick(View v) {
            if (MainActivity.this.select_pos_char < 0 || MainActivity.this.fl_question_keyboard.getVisibility() != 0) {
                v.setVisibility(8);
            } else if (USER_CONTROL.money > 0) {
                MainActivity.this.Answer_OpenKeys.set(MainActivity.this.select_pos_char, String.valueOf(MainActivity.this.cur_Answer.charAt(MainActivity.this.select_pos_char)));
                int old_select_pos_char = MainActivity.this.select_pos_char;
                MainActivity.this.DrawAnswer();
                MainActivity.this.togele_btn_answer();
                MainActivity.this.User_Money_Change(-1);
//                Button button = MainActivity.this.fl_question_answer.getChildAt(old_select_pos_char);
//                button.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.key_answer_addchar));
                MainActivity.this.Hide_Key_keyboard(String.valueOf(MainActivity.this.cur_Answer.charAt(old_select_pos_char)));
            } else {
                QDialog.noMoney(MainActivity.this);
            }
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$6 */
    class C02226 implements OnClickListener {
        C02226() {
        }

        public void onClick(View v) {
            MainActivity.this.btn_answer_key.setVisibility(8);
            MainActivity.this.questionFinish();
            Log.d("Ответ", "Завершение ответ на вопрос");
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$7 */
    class C02237 implements OnCheckedChangeListener {
        C02237() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            GameSound.Sound_State(isChecked);
            USER_CONTROL.sound_state = isChecked;
            USER_CONTROL.setData(MainActivity.mSettings);
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$8 */
    class C02248 implements OnClickListener {
        C02248() {
        }

        public void onClick(View view) {
            if (MainActivity.this.fl_question_keyboard.getVisibility() == 0) {
                Button sel_btn = (Button) view;
                Log.d("Ответ", "Нажата буква ответа " + view.getTag());
                MainActivity.this.select_pos_char = Integer.valueOf(view.getTag().toString()).intValue();
                if (((String) MainActivity.this.Answer_OpenKeys.get(MainActivity.this.select_pos_char)).equals("")) {
                    MainActivity.this.btn_answer_openkey.setText("Открыть букву №" + (MainActivity.this.select_pos_char + 1));
                    MainActivity.this.btn_answer_openkey.setVisibility(0);
                    if (MainActivity.this.old_sel_btnkey != null) {
                        MainActivity.this.old_sel_btnkey.setText(MainActivity.this.select_pos_str);
                    }
                    MainActivity.this.old_sel_btnkey = (Button) view;
                    MainActivity.this.select_pos_str = sel_btn.getText().toString();
                    sel_btn.setText("");
                    sel_btn.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.key_answer_selchar));
                    return;
                }
                MainActivity.this.btn_answer_openkey.setVisibility(8);
                MainActivity.this.select_pos_char = -1;
            }
        }
    }

    /* renamed from: blutbad.iqquiz.MainActivity$9 */
    class C02259 implements OnClickListener {
        C02259() {
        }

        public void onClick(View view) {
            View mView = view;
            int add_isid = MainActivity.this.Answer_add_Char(mView.getTag().toString());
            MainActivity.this.DrawAnswer();
            MainActivity.this.togele_btn_answer();
            mView.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.keyboard_keypress_scale));
//            ((Button) MainActivity.this.fl_question_answer.getChildAt(add_isid).findViewById(R.id.st_btn_key)).startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.key_answer_addchar));
            GameSound.PLAY_key_press();
            Log.d("Клавиатура", "Добавлена буква " + view.getTag());
        }
    }

    class DrawNewAnswer extends AsyncTask<String, String, String> {
        int elapce_Answer = MainActivity.this.ANSWER_SECOND_TO_ELAPCE;

        DrawNewAnswer() {
        }

        protected String doInBackground(String... s) {
            MainActivity.this.mGetAnswer = null;
            try {
                List<NameValuePair> params = new ArrayList();
                params.add(new BasicNameValuePair("cmd", "get_iqquiz"));
                params.add(new BasicNameValuePair("serial", Build.SERIAL));
                params.add(new BasicNameValuePair("money", String.valueOf(USER_CONTROL.money)));
                params.add(new BasicNameValuePair("count_question", String.valueOf(USER_CONTROL.count_question)));
                params.add(new BasicNameValuePair("count_question_true", String.valueOf(USER_CONTROL.count_question_true)));
                JSONObject json = MainActivity.this.jsonParser_Answer.makeHttpRequest(MainActivity.url_iqquiz, HttpPost.METHOD_NAME, params);
                try {
                    Log.d("Показать ответ ", params.toString() + " = " + json.toString());
                    if (json.getInt(MainActivity.SERVER_SUCCESS) == 1) {
                        if (json.getJSONArray("answer") == null || json.getJSONArray("answer").length() <= 0) {
                            MainActivity.this.mGetAnswer = null;
                        } else {
                            MainActivity.this.mGetAnswer = json.getJSONArray("answer");
                            MainActivity.this.cur_Online = String.valueOf(json.getInt("o"));
                            USER_CONTROL.setUserID(json.getInt("user_id"));
                        }
                    }
                } catch (JSONException e) {
                    MainActivity.this.mGetAnswer = null;
                    System.out.println("Exception DrawNewAnswer = " + e);
                }
            } catch (Exception e2) {
                MainActivity.this.mGetAnswer = null;
                System.out.println("Exception DrawNewAnswer = " + e2);
            }
            return null;
        }

        protected void onPostExecute(String result) {
            Log.d("DrawNewAnswer", "onPostExecute" + MainActivity.this.mGetAnswer);
            if (MainActivity.this.mGetAnswer == null || !MainActivity.this.isNetWork()) {
                MainActivity.this.fl_error_load.setVisibility(0);
                MainActivity.this.fl_main_conteiner_game.setVisibility(8);
            } else {
                try {
                    MainActivity.this.cur_Id = MainActivity.this.mGetAnswer.getJSONObject(0).getString("id");
                    MainActivity.this.cur_Answer = MainActivity.this.mGetAnswer.getJSONObject(0).getString("answer");
                    MainActivity.this.cur_Question = MainActivity.this.mGetAnswer.getJSONObject(0).getString("question");
                    GameSound.STOP_ALL();
                    this.elapce_Answer += MainActivity.this.cur_Question.split(" ").length;
                    MainActivity.this.tv_question_id.setText(MainActivity.this.cur_Id);
                    MainActivity.this.tv_question_text.setText(MainActivity.this.cur_Question);
                    MainActivity.this.tv_question_text.setVisibility(0);
                    MainActivity.this.tv_question_text.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.text_question_start));
                    MainActivity.this.fl_game_timer.setVisibility(0);
                    MainActivity.this.tv_time_elapce.setTextColor(MainActivity.this.getResources().getColor(R.color.timer_elapce_wait));
                    MainActivity.this.tv_time_elapce.setVisibility(0);
                    MainActivity.this.cpb_time_elapce.setProgressWithAnimation(0.0f);
                    MainActivity.this.cpb_time_elapce.setMaxProgress((float) this.elapce_Answer);
                    MainActivity.this.cpb_time_elapce.setVisibility(0);
                    MainActivity.this.Answer_Keys.clear();
                    MainActivity.this.Answer_OpenKeys.clear();
                    for (int i = 0; i < MainActivity.this.cur_Answer.length(); i++) {
                        MainActivity.this.Answer_Keys.add("?");
                        MainActivity.this.Answer_OpenKeys.add("");
                    }
                    MainActivity.this.DrawAnswer();
                    MainActivity.this.tv_menu_online.setText("(" + MainActivity.this.cur_Online + ")");
                } catch (Exception e) {
                    Log.d("DrawNewAnswer", "onPostExecute " + e);
                }
                if (MainActivity.this.QuestionTimer != null) {
                    MainActivity.this.QuestionTimer.cancel();
                    MainActivity.this.QuestionTimer = null;
                }
                MainActivity.this.QuestionTimer = new CountDownTimer((long) (this.elapce_Answer * 1000), 1000) {
                    public void onTick(long millisUntilFinished) {
                        DrawNewAnswer drawNewAnswer = DrawNewAnswer.this;
                        drawNewAnswer.elapce_Answer--;
                        MainActivity.this.tv_time_elapce.setText(String.valueOf(DrawNewAnswer.this.elapce_Answer));
                        MainActivity.this.cpb_time_elapce.setProgressWithAnimation((float) DrawNewAnswer.this.elapce_Answer);
                        if (DrawNewAnswer.this.elapce_Answer < 10) {
                            MainActivity.this.tv_time_elapce.setTextColor(MainActivity.this.getResources().getColor(R.color.timer_elapce_wait10));
                        }
                    }

                    public void onFinish() {
                        MainActivity.this.questionFinish();
                        Log.d("DrawNewAnswer", "onPostExecute onFinish");
                    }
                }.start();
                MainActivity.this.initKeyboard(MainActivity.this.cur_Answer);
                MainActivity.this.pb_question_load.setVisibility(8);
                MainActivity.this.fl_question_keyboard.setVisibility(0);
                MainActivity.this.ll_question_top.setVisibility(0);
            }
            super.onPostExecute(result);
        }

        protected void onPreExecute() {
            MainActivity.this.pb_question_load.setVisibility(0);
            MainActivity.this.tv_question_win_text.setVisibility(8);
            MainActivity.this.btn_answer_key.setVisibility(8);
            MainActivity.this.btn_answer_openkey.setVisibility(8);
            MainActivity.this.tv_money_total.setText(String.valueOf(USER_CONTROL.money));
            super.onPreExecute();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        instance = this;
        setRequestedOrientation(1);
        ((AdView) findViewById(R.id.adView)).loadAd(new Builder().build());
        MobileAds.initialize(getContext(), getResources().getString(R.string.app_ad_id));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        mSettings = getSharedPreferences("user_control", 0);
        USER_CONTROL.getSettingsData(mSettings);
        ((TextView) findViewById(R.id.tv_menu_version)).setText("Версия " + getResources().getString(R.string.app_ver));
        this.tv_menu_online = (TextView) findViewById(R.id.tv_menu_online);
        this.tv_menu_online.setText("");
        this.btn_menu_newgame = (Button) findViewById(R.id.btn_menu_newgame);
        this.btn_menu_newgame.setOnClickListener(this);
        this.btn_menu_resumegame = (Button) findViewById(R.id.btn_menu_resumegame);
        this.btn_menu_resumegame.setOnClickListener(this);
        this.btn_menu_statgame = (Button) findViewById(R.id.btn_menu_statgame);
        this.btn_menu_statgame.setOnClickListener(this);
        this.btn_menu_sendgame = (Button) findViewById(R.id.btn_menu_sendgame);
        this.btn_menu_sendgame.setOnClickListener(this);
        this.btn_menu_more = (Button) findViewById(R.id.btn_menu_more);
        this.btn_menu_more.setOnClickListener(this);
        this.btn_menu_exitgame = (Button) findViewById(R.id.btn_menu_exitgame);
        this.btn_menu_exitgame.setOnClickListener(this);
        this.fl_keyboard_list = (FrameLayout) findViewById(R.id.fl_keyboard_list);
        this.fl_game_timer = (FrameLayout) findViewById(R.id.fl_game_timer);
        this.fl_main_conteiner_game = (FrameLayout) findViewById(R.id.fl_main_conteiner_game);
        this.fl_main_conteiner_game.setVisibility(0);
        this.fl_error_load = (FrameLayout) findViewById(R.id.fl_error_load);
        this.fl_error_load.setVisibility(8);
        this.ll_question_top = (LinearLayout) findViewById(R.id.ll_question_top);
        this.ll_question_top.setVisibility(8);
        this.fl_question_keyboard = (FrameLayout) findViewById(R.id.fl_question_keyboard);
        this.fl_question_keyboard.setVisibility(8);
        this.cpb_time_elapce = (CircularProgressBar) findViewById(R.id.cpb_time_elapce);
        this.cpb_time_elapce.setVisibility(8);
        this.pb_question_load = (ProgressBar) findViewById(R.id.pb_question_load);
        this.pb_question_load.setVisibility(8);
        this.rl_main_game = (RelativeLayout) findViewById(R.id.rl_main_game);
        this.rl_main_game.setVisibility(8);
        this.ll_start_game = (LinearLayout) findViewById(R.id.ll_start_game);
        this.ll_start_game.setVisibility(0);
        this.fl_keyboard_conteiner = (FlowLayout) findViewById(R.id.fl_keyboard_conteiner);
        this.fl_question_answer = (FlowLayout) findViewById(R.id.fl_question_answer);
        this.btn_answer_key = (Button) findViewById(R.id.btn_answer_key);
        this.btn_answer_key.setVisibility(8);
        this.btn_answer_openkey = (Button) findViewById(R.id.btn_answer_openkey);
        this.btn_answer_openkey.setVisibility(8);
        this.btn_delete_key = (TextView) findViewById(R.id.btn_delete_key);
        this.tv_time_elapce = (TextView) findViewById(R.id.tv_time_elapce);
        this.tv_time_elapce.setText("0");
        this.tv_question_id = (TextView) findViewById(R.id.tv_question_id);
        this.tv_question_id.setText("0");
        this.tv_question_text = (TextView) findViewById(R.id.tv_question_text);
        this.tv_question_text.setText("");
        this.tv_money_total = (TextView) findViewById(R.id.tv_money_total);
        this.tv_money_total.setText(String.valueOf(USER_CONTROL.money));
        this.tv_user_level = (TextView) findViewById(R.id.tv_user_level);
        this.tv_user_level.setText(String.valueOf(USER_CONTROL.getUser_Level()));
        this.tv_question_count = (TextView) findViewById(R.id.tv_question_count);
        this.tv_question_count.setText(USER_CONTROL.getUser_CurQuestion() + "/" + USER_CONTROL.getUser_NextUp());
        this.tv_question_win_text = (TextView) findViewById(R.id.tv_question_win_text);
        this.tv_question_win_text.setVisibility(8);
        this.tv_question_count.setOnClickListener(new C02171());
        this.tv_user_level.setOnClickListener(new C02182());
        this.tv_money_total.setOnClickListener(new C02193());
        this.btn_delete_key.setOnClickListener(new C02204());
        this.btn_answer_openkey.setOnClickListener(new C02215());
        this.btn_answer_key.setOnClickListener(new C02226());
        ToggleButton btn_sound = (ToggleButton) findViewById(R.id.btn_sound);
        GameSound.Sound_State(USER_CONTROL.sound_state);
        btn_sound.setChecked(USER_CONTROL.sound_state);
        btn_sound.setOnCheckedChangeListener(new C02237());
        GameSound.Create(this);
        this.iv_logo = (ImageView) findViewById(R.id.iv_logo);
        this.iv_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_logo));
    }

    public static Context getContext() {
        return instance;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation != 2 && newConfig.orientation == 1) {
        }
        DrawAnswer();
        initKeyboard(this.cur_Answer);
        super.onConfigurationChanged(newConfig);
    }

    public void onBackPressed() {
        if (this.rl_main_game.getVisibility() == 0) {
            this.mGameStart = false;
            GameAddMoney.stop();
            this.ll_start_game.setVisibility(0);
            this.rl_main_game.setVisibility(8);
            if (this.QuestionTimer != null) {
                this.QuestionTimer.cancel();
                this.QuestionTimer = null;
            }
            this.btn_answer_key.setVisibility(8);
            this.tv_time_elapce.setText("0");
            this.tv_question_id.setText("0");
            this.tv_question_text.setText("");
            this.tv_question_win_text.setVisibility(8);
            this.tv_question_text.setVisibility(8);
            this.tv_time_elapce.setVisibility(8);
            this.fl_game_timer.setVisibility(8);
            this.fl_keyboard_conteiner.removeAllViews();
            this.fl_question_answer.removeAllViews();
            this.iv_logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_logo));
            return;
        }
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
        mRewardedVideoAd.resume(this);
        if (!this.mGameVisible && this.mGameStart) {
            this.mGameVisible = true;
            GameAddMoney.Resume();
            this.tv_time_elapce.setText("0");
            if (isNetWork()) {
                new DrawNewAnswer().execute(new String[0]);
            } else {
                this.fl_error_load.setVisibility(0);
                this.fl_main_conteiner_game.setVisibility(8);
            }
            Log.d("MainActivity", "onResume");
        }
    }

    protected void onPause() {
        mRewardedVideoAd.pause(getContext());
        this.mGameVisible = false;
        if (this.mGameStart) {
            GameAddMoney.Pause();
        }
        Log.d("MainActivity", "onPause");
        super.onPause();
    }

    protected void onDestroy() {
        mRewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_newgame:
                this.mGameStart = true;
                this.mGameVisible = true;
                GameAddMoney.create();
                if (isNetWork()) {
                    new DrawNewAnswer().execute(new String[0]);
                } else {
                    this.fl_error_load.setVisibility(0);
                    this.fl_main_conteiner_game.setVisibility(8);
                }
                v.setVisibility(8);
                ((Button) findViewById(R.id.btn_menu_resumegame)).setVisibility(0);
                this.ll_start_game.setVisibility(8);
                this.rl_main_game.setVisibility(0);
                return;
            case R.id.btn_menu_resumegame:
                this.mGameStart = true;
                this.mGameVisible = true;
                GameAddMoney.create();
                if (isNetWork()) {
                    new DrawNewAnswer().execute(new String[0]);
                } else {
                    this.fl_error_load.setVisibility(0);
                    this.fl_main_conteiner_game.setVisibility(8);
                }
                this.ll_start_game.setVisibility(8);
                this.rl_main_game.setVisibility(0);
                return;
            case R.id.btn_menu_statgame:
                open_GameStatistics();
                return;
            case R.id.btn_menu_sendgame:
                Intent sharingIntent = new Intent("android.intent.action.SEND");
                sharingIntent.setType(HTTP.PLAIN_TEXT_TYPE);
                sharingIntent.putExtra("android.intent.extra.SUBJECT", "Поиграй в IQ Викторину");
                sharingIntent.putExtra("android.intent.extra.TEXT", "Привет я достиг больших результатов в IQ Викторине, а ты сможешь так?\nhttps://play.google.com/store/apps/details?id=blutbad.iqquiz");
                startActivity(Intent.createChooser(sharingIntent, "Предложить другу"));
                return;
            case R.id.btn_menu_more:
                try {
                    DialogFragment more_games = new DialogMoreGames();
                    more_games.setStyle(1, 0);
                    more_games.show(getSupportFragmentManager(), more_games.getClass().getName());
                    return;
                } catch (Exception e) {
                    Log.d("SudokuPlayActivity", "Диалог монет " + e);
                    return;
                }
            case R.id.btn_menu_exitgame:
                finish();
                return;
            default:
                return;
        }
    }

    public void open_GameStatistics() {
        DialogFragment game_statistics = new DialogGameStatistics();
        game_statistics.setStyle(1, 0);
        game_statistics.show(getSupportFragmentManager(), game_statistics.getClass().getName());
    }

    public void open_GameMoney() {
        DialogFragment game_money = new DialogGameMoney();
        game_money.setStyle(1, 0);
        game_money.show(getSupportFragmentManager(), game_money.getClass().getName());
    }

    public void Reload_Question(View v) {
        if (isNetWork()) {
            new DrawNewAnswer().execute(new String[0]);
            this.fl_error_load.setVisibility(8);
            this.fl_main_conteiner_game.setVisibility(0);
            return;
        }
        this.fl_error_load.setVisibility(0);
        this.fl_main_conteiner_game.setVisibility(8);
    }

    public void User_Money_Change(int m) {
        USER_CONTROL.money += m;
        this.tv_money_total.setText(String.valueOf(USER_CONTROL.money));
        this.tv_money_total.startAnimation(AnimationUtils.loadAnimation(this, R.anim.change_money_count));
        USER_CONTROL.setData(mSettings);
    }

    public void User_Answer_Change() {
        this.tv_user_level.setText(String.valueOf(USER_CONTROL.getUser_Level()));
        this.tv_question_count.setText(USER_CONTROL.getUser_CurQuestion() + "/" + USER_CONTROL.getUser_NextUp());
        USER_CONTROL.setData(mSettings);
    }

    public void togele_btn_answer() {
        Log.d("Ответ", "size " + this.count_draw_char_answer + " length " + this.cur_Answer.length());
        if (this.count_draw_char_answer == this.cur_Answer.length()) {
            this.btn_answer_key.setVisibility(0);
        } else {
            this.btn_answer_key.setVisibility(8);
        }
    }

    private String get_enter_answer() {
        String ans = "";
        for (int i = 0; i < this.Answer_Keys.size(); i++) {
            if (((String) this.Answer_OpenKeys.get(i)).equals("")) {
                ans = ans + ((String) this.Answer_Keys.get(i));
            } else {
                ans = ans + ((String) this.Answer_OpenKeys.get(i));
            }
        }
        return ans;
    }

    private void Answer_dellast_Char() {
        if (this.select_pos_char < 0) {
            int i = this.Answer_Keys.size() - 1;
            while (i >= 0) {
                if (((String) this.Answer_Keys.get(i)).equals("?") || !((String) this.Answer_OpenKeys.get(i)).equals("")) {
                    i--;
                } else {
                    this.Answer_Keys.set(i, "?");
                    return;
                }
            }
        } else if (!((String) this.Answer_Keys.get(this.select_pos_char)).equals("?") && ((String) this.Answer_OpenKeys.get(this.select_pos_char)).equals("")) {
            this.Answer_Keys.set(this.select_pos_char, "?");
        }
    }

    private int Answer_add_Char(String ch) {
        if (this.select_pos_char >= 0) {
            this.Answer_Keys.set(this.select_pos_char, ch);
            return this.select_pos_char;
        }
        int i = 0;
        while (i < this.Answer_Keys.size()) {
            if (((String) this.Answer_Keys.get(i)).equals("?") && ((String) this.Answer_OpenKeys.get(i)).equals("")) {
                this.Answer_Keys.set(i, ch);
                return i;
            }
            i++;
        }
        return 0;
    }

    private void Hide_Key_keyboard(String ch) {
        ArrayList<String> tmp_Answer_OpenKeys = new ArrayList(this.Answer_OpenKeys);
        for (int i = 0; i < this.fl_keyboard_conteiner.getChildCount(); i++) {
            Button cur_btn_key = (Button) this.fl_keyboard_conteiner.getChildAt(i);
            String gkey = cur_btn_key.getText().toString();
            for (int isf = 0; isf < tmp_Answer_OpenKeys.size(); isf++) {
                if (((String) tmp_Answer_OpenKeys.get(isf)).equals(gkey)) {
                    cur_btn_key.setBackground(getResources().getDrawable(R.drawable.btn_key_answer_openkey));
                    cur_btn_key.setTextColor(getResources().getColor(R.color.anser_key_text));
                    tmp_Answer_OpenKeys.set(isf, "");
                    break;
                }
            }
        }
    }

    private void DrawAnswer() {
        this.fl_question_answer.removeAllViews();
        this.btn_answer_openkey.setVisibility(8);
        this.select_pos_char = -1;
        float cur_btn_answer_width = 10.0f;
        this.count_draw_char_answer = 0;
        for (int i = 0; i < this.Answer_Keys.size(); i++) {
            String b_str;
            View key = LayoutInflater.from(this).inflate(R.layout.style_keyboard_answer, null);
            Button st_btn_key = (Button) key.findViewById(R.id.st_btn_key);
            FrameLayout fl_btn_key = (FrameLayout) key.findViewById(R.id.fl_btn_key);
            st_btn_key.setTextColor(getResources().getColor(R.color.anser_key_text));
            if (((String) this.Answer_OpenKeys.get(i)).equals("")) {
                b_str = String.valueOf(this.Answer_Keys.get(i));
                if (b_str.equals("?")) {
                    st_btn_key.setBackground(getResources().getDrawable(R.drawable.btn_key_answer_normal));
                } else {
                    st_btn_key.setBackground(getResources().getDrawable(R.drawable.btn_key_answer_key));
                    this.count_draw_char_answer++;
                }
            } else {
                b_str = String.valueOf(this.Answer_OpenKeys.get(i));
                st_btn_key.setBackground(getResources().getDrawable(R.drawable.btn_key_answer_openkey));
                this.count_draw_char_answer++;
            }
            float btn_width = (float) (getDisplayWidth() / this.Answer_Keys.size());
            if (btn_width > 150.0f) {
                btn_width = 150.0f;
            }
            fl_btn_key.setLayoutParams(new LayoutParams((int) btn_width, ((int) btn_width) - 6));
            st_btn_key.setTag(Integer.valueOf(i));
            st_btn_key.setText(b_str);
            st_btn_key.setTextSize((float) ((int) (btn_width / 5.0f)));
            cur_btn_answer_width = btn_width / 5.0f;
            st_btn_key.setOnClickListener(new C02248());
            this.fl_question_answer.addView(key);
        }
        float text_size = (float) (((double) cur_btn_answer_width) / 1.5d);
        if (text_size < 16.0f) {
            text_size = 16.0f;
        }
        this.tv_question_text.setTextSize(text_size);
    }

    private void initKeyboard(String mAnswer) {
        int i;
        String s;
        ArrayList<String> dataAlfavit = new ArrayList();
        ArrayList<String> dataKey = new ArrayList();
        int mAnswer_length = 14 - mAnswer.length();
        boolean isAnswer_int = false;
        for (i = 0; i < mAnswer.length(); i++) {
            Character ch = Character.valueOf(mAnswer.charAt(i));
            if ("0123456789".contains(String.valueOf(ch))) {
                mAnswer_length++;
            } else {
                dataKey.add(String.valueOf(ch));
            }
            if (Character.isDigit(ch.charValue())) {
                isAnswer_int = true;
            }
        }
        for (char c = 'А'; c <= 'Я'; c = (char) (c + 1)) {
            if (dataAlfavit.size() < 14) {
                s = new String() + c;
                if (!mAnswer.contains(s)) {
                    dataAlfavit.add(s);
                }
            }
        }
        for (i = 0; i < mAnswer_length; i++) {
            int n = new Random().nextInt(dataAlfavit.size());
            s = (String) dataAlfavit.get(n);
            dataAlfavit.remove(n);
            dataKey.add(s);
        }
        Collections.shuffle(dataKey);
        if (isAnswer_int) {
            for (i = 0; i < 10; i++) {
                dataKey.add(String.valueOf(i));
            }
            this.fl_keyboard_list.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) pxFromDp(150.0f)));
        } else {
            this.fl_keyboard_list.setLayoutParams(new LinearLayout.LayoutParams(-1, (int) pxFromDp(80.0f)));
        }
        this.fl_keyboard_conteiner.removeAllViews();
        for (i = 0; i < dataKey.size(); i++) {
            float w_btn;
            View key = LayoutInflater.from(this).inflate(R.layout.style_keyboard, null);
            Button st_btn_key = (Button) key.findViewById(R.id.st_btn_key);
            FrameLayout fl_btn_key = (FrameLayout) key.findViewById(R.id.fl_btn_key);
            String getkey = (String) dataKey.get(i);
            float h_btn = 40.0f;
            if ("0123456789".contains(getkey)) {
                w_btn = (float) (getDisplayWidth() / 10);
                st_btn_key.setBackground(getResources().getDrawable(R.drawable.btn_key_num_selector));
            } else {
                int needDisplayWidth = getDisplayWidth();
                if (needDisplayWidth > 1300) {
                    needDisplayWidth = 1300;
                    int space_padding = (getDisplayWidth() - 1300) / 2;
                    this.fl_keyboard_conteiner.setLayoutParams(new LayoutParams(1300 + space_padding, -1));
                    this.fl_keyboard_conteiner.setPadding(space_padding, 0, 0, 0);
                    h_btn = 40.0f;
                } else {
                    this.fl_keyboard_conteiner.setLayoutParams(new LayoutParams(-1, -1));
                    this.fl_keyboard_conteiner.setPadding(0, 0, 0, 0);
                }
                w_btn = (float) (needDisplayWidth / 7);
            }
            fl_btn_key.setLayoutParams(new LayoutParams((int) w_btn, (int) pxFromDp(h_btn)));
            st_btn_key.setTag(getkey);
            st_btn_key.setText(getkey);
            st_btn_key.setOnClickListener(new C02259());
            this.fl_keyboard_conteiner.addView(key);
        }
    }

    public void questionFinish() {
        if (this.QuestionTimer != null) {
            this.QuestionTimer.cancel();
            this.QuestionTimer = null;
        }
        String q_answer = get_enter_answer();
        this.tv_time_elapce.setText("0");
        this.tv_time_elapce.setTextColor(getResources().getColor(R.color.timer_wait));
        this.cpb_time_elapce.setProgressWithAnimation(0.0f);
        this.tv_question_win_text.setVisibility(0);
        USER_CONTROL.count_question++;
        if (q_answer.equals(this.cur_Answer)) {
            this.tv_question_win_text.setText("Выигрыш");
            USER_CONTROL.count_question_true++;
            this.tv_question_win_text.setTextColor(getResources().getColor(R.color.anser_win_text_true));
            this.tv_question_win_text.setTextSize(40.0f);
            GameSound.PLAY_win_end();
        } else {
            this.tv_question_win_text.setText("Вы не угадали\nПравильный ответ\n\"" + this.cur_Answer + "\"");
            this.tv_question_win_text.setTextColor(getResources().getColor(R.color.anser_win_text_false));
            this.tv_question_win_text.setTextSize(20.0f);
        }
        User_Answer_Change();
        this.fl_question_keyboard.setVisibility(8);
        this.btn_answer_openkey.setVisibility(8);
        this.btn_answer_key.setVisibility(8);
        DrawAnswer();
        Log.d("MainActivity", "Конец вопроса - ответ=" + this.cur_Answer + " введено=" + q_answer);
        this.end_elapce_Answer = this.cur_Answer.length();
        this.tv_time_elapce.setText("(" + String.valueOf(this.end_elapce_Answer) + ")");
        if (!this.mGameStart || this.mGameVisible) {
            this.EndQuestionTimer = new CountDownTimer((long) (this.end_elapce_Answer * 1000), 1000) {
                public void onTick(long millisUntilFinished) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.end_elapce_Answer--;
                    MainActivity.this.tv_time_elapce.setText("<" + String.valueOf(MainActivity.this.end_elapce_Answer) + ">");
                    Log.d("MainActivity", "Finish onTick " + MainActivity.this.end_elapce_Answer);
                }

                public void onFinish() {
                    if (MainActivity.this.mGameStart) {
                        MainActivity.this.tv_time_elapce.setText("0");
                        if (MainActivity.this.isNetWork()) {
                            new DrawNewAnswer().execute(new String[0]);
                        } else {
                            MainActivity.this.fl_error_load.setVisibility(View.VISIBLE);
                            MainActivity.this.fl_main_conteiner_game.setVisibility(8);
                        }
                        MainActivity.this.EndQuestionTimer.cancel();
                        MainActivity.this.EndQuestionTimer = null;
                    }
                }
            }.start();
        }
    }

    public void loadRewardedVideoAd() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (!MainActivity.mRewardedVideoAd.isLoaded()) {
                    MainActivity.mRewardedVideoAd.loadAd(MainActivity.this.getResources().getString(R.string.interstitial_ad_rewarded_id), new Builder().build());
                    MainActivity.isVideoAd = false;
                }
            }
        });
    }

    public void onRewarded(RewardItem reward) {
        User_Money_Change(10);
        isVideoAd = false;
        Toast.makeText(this, "Вы получили +10 монеток", 0).show();
        Log.d("DialogGameMoney", "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount());
    }

    public void onRewardedVideoAdLeftApplication() {
        Log.d("DialogGameMoney", "onRewardedVideoAdLeftApplication");
    }

    public void onRewardedVideoAdClosed() {
        if (isVideoAd) {
            Toast.makeText(this, "Вы закрыли видео преждевременно", 0).show();
            return;
        }
        isVideoAd = false;
        loadRewardedVideoAd();
    }

    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        isVideoAd = false;
        Log.d("DialogGameMoney", "onRewardedVideoAdFailedToLoad");
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public void onRewardedVideoAdLoaded() {
        isVideoAd = true;
        Toast.makeText(this, "Вы можете получить монетки уже сейчас", 0).show();
        Log.d("DialogGameMoney", "onRewardedVideoAdLoaded");
    }

    public void onRewardedVideoAdOpened() {
        Log.d("DialogGameMoney", "onRewardedVideoAdOpened");
    }

    public void onRewardedVideoStarted() {
        Log.d("DialogGameMoney", "onRewardedVideoStarted");
    }

    private int getDisplayWidth() {
        return getWindowManager().getDefaultDisplay().getWidth();
    }

    public float getDensity() {
        return getResources().getDisplayMetrics().density;
    }

    public float dpFromPx(float px) {
        return px / getDensity();
    }

    public float pxFromDp(float dp) {
        return getDensity() * dp;
    }

    public boolean isNetWork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService("connectivity");
        NetworkInfo wifiInfo = cm.getNetworkInfo(1);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(0);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo == null || !wifiInfo.isConnected()) {
            return false;
        }
        return true;
    }
}
