package blutbad.iqquiz.dialogs;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import blutbad.iqquiz.BuildConfig;
import blutbad.iqquiz.R;

public class DialogMoreGames extends DialogFragment implements OnClickListener {
    final String LOG_TAG = "DialogGameStatistics";
    Button btn_2048;
    Button btn_flappyhero;
    Button btn_iqquiz;
    Button btn_sudoku;
    View cur_views;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.cur_views = inflater.inflate(R.layout.more_games, null);
        this.btn_2048 = (Button) this.cur_views.findViewById(R.id.btn_2048);
        this.btn_2048.setOnClickListener(this);
        this.btn_iqquiz = (Button) this.cur_views.findViewById(R.id.btn_iqquiz);
        this.btn_iqquiz.setOnClickListener(this);
        this.btn_sudoku = (Button) this.cur_views.findViewById(R.id.btn_sudoku);
        this.btn_sudoku.setOnClickListener(this);
        this.btn_flappyhero = (Button) this.cur_views.findViewById(R.id.btn_flappyhero);
        this.btn_flappyhero.setOnClickListener(this);
        return this.cur_views;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_2048) {
            ourl("blutbad.x2048");
        }
        if (v.getId() == R.id.btn_iqquiz) {
            ourl(BuildConfig.APPLICATION_ID);
        }
        if (v.getId() == R.id.btn_sudoku) {
            ourl("blutbad.sudoku");
        }
        if (v.getId() == R.id.btn_flappyhero) {
            ourl("blutbad.flappyhero");
        }
        dismiss();
        Log.d("DialogGameStatistics", "onClick: " + ((Button) v).getText());
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("DialogGameStatistics", "onDismiss: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("DialogGameStatistics", "onCancel: onCancel");
    }

    private void ourl(String u) {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + u)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + u)));
        }
    }
}
