package group1.fitnessapp.excerciseTracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import group1.fitnessapp.R;

public class CustomDialogBoxActivity extends AppCompatDialogFragment {

    private TextView customTxt;
    private String customMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_custom_dialog_box, null);
        builder.setView(view).setTitle("Error").setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        customTxt = (TextView) view.findViewById(R.id.customTxt);
        customTxt.setText(customMessage);

        return builder.create();
    }

    public void setDialogText(String text) {
        customMessage = text;
    }


}
