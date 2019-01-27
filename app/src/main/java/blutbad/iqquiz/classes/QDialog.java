package blutbad.iqquiz.classes;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class QDialog {

    /* renamed from: blutbad.iqquiz.classes.QDialog$1 */
    static class C02331 implements OnClickListener {
        C02331() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    public static void noMoney(Context th) {
        Builder builder = new Builder(th);
        builder.setTitle("Монетки");
        builder.setMessage("У Вас не хватает монеток");
        builder.setCancelable(true);
        builder.setPositiveButton(17039370, new C02331()).create().show();
    }
}
