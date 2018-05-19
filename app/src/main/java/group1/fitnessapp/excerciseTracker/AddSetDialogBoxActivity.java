package group1.fitnessapp.excerciseTracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import group1.fitnessapp.R;

public class AddSetDialogBoxActivity extends AppCompatDialogFragment {

    private EditText enterRepsEt;
    private AddSetDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add_set_dialog_box, null);
        builder.setView(view).setTitle("Add Set")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String reps = enterRepsEt.getText().toString();
                        listener.sendText(reps);
                    }
                });

        enterRepsEt = (EditText) view.findViewById(R.id.enterRepsEt);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddSetDialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AddSetDialogListener {
        void sendText(String text);
    }
}
