package com.orange.ar.sceneform_example.helpers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;

import com.orange.ar.sceneform_example.R;


public class Utilities {


    public static AlertDialog dialog;

    public static void showDialog(final Activity activity,
                                  String msg,
                                  String btnPositiveTxt,
                                  String btnNegativeTxt,
                                  int icon,
                                  View.OnClickListener listener) {

        if (activity == null || activity.isFinishing()) return;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.message_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView tv_dialogbody = dialogView.findViewById(R.id.dialog_body);
        final Button btn_confirm = dialogView.findViewById(R.id.btn_confirm);
        final Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
        final ImageView iv_logo = dialogView.findViewById(R.id.iv_logo);


        if (icon != 0) {
            iv_logo.setVisibility(View.VISIBLE);
            iv_logo.setImageDrawable(ResourcesCompat.getDrawable(activity.getResources()
                    , icon, null));
        } else {
            iv_logo.setVisibility(View.GONE);

        }

        dialog = dialogBuilder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        tv_dialogbody.setText(msg);
        if (btnPositiveTxt != null) {
            btn_confirm.setText(btnPositiveTxt);
            btn_confirm.setOnClickListener(listener);
        } else {
            btn_confirm.setVisibility(View.GONE);
        }

        if (btnNegativeTxt != null) {
            btn_cancel.setText(btnNegativeTxt);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } else {
            btn_cancel.setVisibility(View.GONE);
        }

        // btn_confirm.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}
