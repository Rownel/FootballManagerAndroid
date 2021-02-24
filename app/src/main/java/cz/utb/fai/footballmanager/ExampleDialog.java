package cz.utb.fai.footballmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppComponentFactory;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ExampleDialog extends AppCompatDialogFragment {

    public Activity activity;

    public  ExampleDialog(Activity activity)
    {
        this.activity = activity;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Spinner mySpinner = (Spinner) this.activity.findViewById(R.id.aSpinner);
        String text = mySpinner.getSelectedItem().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information")
                .setMessage(text)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return  builder.create();
    }


}
