package zw.co.chimsy.xulkelvin.helper;

import android.content.Context;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class MySweetAlerts {

    private Context context;

    public MySweetAlerts(Context context) {
        this.context = context;
    }

    /*Errors*/
    public void sweetAlertError(String errorMessage) {
        SweetAlertDialog sweetAlertDialog;
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("ERROR!");
        sweetAlertDialog.setContentText(errorMessage);
        sweetAlertDialog.show();
    }

    /* Warning*/
    public void sweetAlertWarning(String warningMessage) {
        // A warning message：
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("WARNING!")
                .setContentText(warningMessage)
                .show();
    }

    /* Success*/
    public void sweetAlertSuccess(String successMessage) {
        // A success message：
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("SUCCESS!")
                .setContentText(successMessage)
                .show();
    }

    /* Confirm KYC Details */
    public void sweetAlertConfirmKYC(String content) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Confirm Student Details Below?")
                .setContentText(content)
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // processPayment(regNum, paymentMethod, amount);

                        Toast.makeText(context, "Confirm Button Clicked: Your Action Goes Here", Toast.LENGTH_SHORT).show();
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelText("No")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

}
