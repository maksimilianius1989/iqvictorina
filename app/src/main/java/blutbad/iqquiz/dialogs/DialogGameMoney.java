package blutbad.iqquiz.dialogs;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import blutbad.iqquiz.R;
import blutbad.iqquiz.MainActivity;
import blutbad.iqquiz.classes.GameAddMoney;
import java.util.Timer;
import java.util.TimerTask;

public class DialogGameMoney extends DialogFragment implements OnClickListener {
    final String LOG_TAG = "DialogGameStatistics";
    View cur_views;
    private Button dialog_money_btn_videomoney;
    private ProgressBar dialog_money_pb_videomoney;
    Timer timer = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.cur_views = inflater.inflate(R.layout.game_money, null);
        final Button dialog_money_btn_money = (Button) this.cur_views.findViewById(R.id.dialog_money_btn_money);
        final TextView dialog_money_text = (TextView) this.cur_views.findViewById(R.id.dialog_money_text);
        final TextView dialog_money_tv_timer = (TextView) this.cur_views.findViewById(R.id.dialog_money_tv_timer);
        dialog_money_tv_timer.setText(GameAddMoney.curEndTimer);
        if (GameAddMoney.isEndTimer) {
            dialog_money_btn_money.setVisibility(0);
            dialog_money_text.setVisibility(8);
            dialog_money_tv_timer.setVisibility(8);
        } else {
            dialog_money_btn_money.setVisibility(8);
            dialog_money_text.setVisibility(0);
            dialog_money_tv_timer.setVisibility(0);
        }
        dialog_money_btn_money.setOnClickListener(this);
        this.dialog_money_btn_videomoney = (Button) this.cur_views.findViewById(R.id.dialog_money_btn_videomoney);
        this.dialog_money_btn_videomoney.setOnClickListener(this);
        this.dialog_money_pb_videomoney = (ProgressBar) this.cur_views.findViewById(R.id.dialog_money_pb_videomoney);
        if (MainActivity.isVideoAd) {
            this.dialog_money_btn_videomoney.setVisibility(0);
            this.dialog_money_pb_videomoney.setVisibility(8);
        } else {
            this.dialog_money_btn_videomoney.setVisibility(8);
            this.dialog_money_pb_videomoney.setVisibility(0);
        }
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {

            /* renamed from: blutbad.iqquiz.dialogs.DialogGameMoney$1$1 */
            class C02341 implements Runnable {
                C02341() {
                }

                public void run() {
                    if (GameAddMoney.isEndTimer) {
                        dialog_money_btn_money.setVisibility(0);
                        dialog_money_text.setVisibility(8);
                        dialog_money_tv_timer.setVisibility(8);
                        dialog_money_tv_timer.setText("00:00");
                        DialogGameMoney.this.timer.cancel();
                        DialogGameMoney.this.timer = null;
                        return;
                    }
                    dialog_money_tv_timer.setText(GameAddMoney.curEndTimer);
                }
            }

            public void run() {
                try {
                    DialogGameMoney.this.getActivity().runOnUiThread(new C02341());
                } catch (Exception e) {
                    System.out.println("DialogGameMoney Exception=" + e);
                }
            }
        }, 0, 1000);
        return this.cur_views;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.dialog_money_btn_money) {
//            ((MainActivity) getActivity()).User_Money_Change(30);
            v.setVisibility(8);
            GameAddMoney.create();
        } else if (v.getId() == R.id.dialog_money_btn_videomoney) {
            if (MainActivity.mRewardedVideoAd.isLoaded()) {
                MainActivity.mRewardedVideoAd.show();
            } else {
//                ((MainActivity) getActivity()).loadRewardedVideoAd();
            }
        }
        dismiss();
        Log.d("DialogGameStatistics", "onClick: " + ((Button) v).getText());
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        Log.d("DialogGameStatistics", "onDismiss: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("DialogGameStatistics", "onCancel: onCancel");
    }
}
