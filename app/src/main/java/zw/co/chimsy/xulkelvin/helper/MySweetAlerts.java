package zw.co.chimsy.xulkelvin.helper;

import android.content.Context;

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
        sweetAlertDialog.setTitleText("ERROR!!!");
        sweetAlertDialog.setContentText(errorMessage);
        sweetAlertDialog.show();
    }

    /* Warning*/
    public void sweetAlertWarning(String warningMessage) {
        // A warning message：
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Try This Ticket Again")
                .setContentText(warningMessage)
                .show();
    }

    /* Success*/
    public void sweetAlertSuccess(String successMessage) {
        // A success message：
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("SUCCESS...")
                .setContentText(successMessage)
                .show();
    }

}
