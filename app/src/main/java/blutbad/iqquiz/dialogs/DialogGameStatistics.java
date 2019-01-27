package blutbad.iqquiz.dialogs;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import blutbad.iqquiz.R;
import blutbad.iqquiz.classes.USER_CONTROL;

public class DialogGameStatistics extends DialogFragment {
    final String LOG_TAG = "DialogGameStatistics";
    View cur_views;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Статистика");
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.cur_views = inflater.inflate(R.layout.game_statistics, null);
        ((TextView) this.cur_views.findViewById(R.id.tv_dialog_1)).setText(String.valueOf(USER_CONTROL.count_question));
        ((TextView) this.cur_views.findViewById(R.id.tv_dialog_2)).setText(String.valueOf(USER_CONTROL.count_question_true));
        ((TextView) this.cur_views.findViewById(R.id.tv_dialog_3)).setText(String.valueOf(USER_CONTROL.getUser_Level()));
        ((TextView) this.cur_views.findViewById(R.id.tv_dialog_4)).setText(String.valueOf(USER_CONTROL.getUser_CurQuestion()) + " из " + String.valueOf(USER_CONTROL.getUser_NextUp()));
        ((TextView) this.cur_views.findViewById(R.id.tv_dialog_5)).setText(String.valueOf(USER_CONTROL.getUser_NextUp() - USER_CONTROL.getUser_CurQuestion()));
        TextView tv_dialog_6 = (TextView) this.cur_views.findViewById(R.id.tv_dialog_6);
        tv_dialog_6.setText("?");
        if (USER_CONTROL.getUserID() > 0) {
            tv_dialog_6.setText(String.valueOf(USER_CONTROL.getUserID()));
        }
        return this.cur_views;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("DialogGameStatistics", "onDismiss: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("DialogGameStatistics", "onCancel: onCancel");
    }
}
