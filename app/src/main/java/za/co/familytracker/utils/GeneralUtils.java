package za.co.familytracker.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import za.co.familytracker.dialogs.LinkDeviceCallback;

public class GeneralUtils
{
    public static void makeToast(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static AlertDialog createAlertDialog(Context context, String title, String message, boolean isPrompt, final LinkDeviceCallback linkDeviceCallback)
    {
        AlertDialog toReturn = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        final EditText edTxtName = new EditText(context);

        if(isPrompt)
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(40, 40,40,40);
            edTxtName.setLayoutParams(lp);
            edTxtName.setHint("Name");

            builder.setView(edTxtName);
        }

        if(isPrompt)
        {
            builder.setCancelable(true);
            builder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            if(edTxtName.getText() != null && !edTxtName.getText().toString().equals(""))
                            {
                                linkDeviceCallback.linkDevice(edTxtName.getText().toString());
                            }

                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }else
        {
            builder.setCancelable(false);
            builder.setPositiveButton(
                    "Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        toReturn = builder.create();

        return toReturn;

    }
}
