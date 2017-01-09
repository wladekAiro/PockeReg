package com.example.wladek.pockeregapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class NewCardActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;

    NfcHandler nfcHandler;

    MaterialDialog.Builder builder;

    MaterialDialog dialog;

    EditText inputStudentNumber;
    Button btnSubmit;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        btnSubmit = (Button) findViewById(R.id.btnSubmitStdNumber);
        inputStudentNumber = (EditText) findViewById(R.id.txtStudentNumber);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        nfcHandler = new NfcHandler(nfcAdapter, NewCardActivity.this, NewCardActivity.class,
                NewCardActivity.this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    Toast.makeText(NewCardActivity.this, "Form error", Toast.LENGTH_LONG).show();
                } else {
                    scanCard();
                }
            }
        });
    }

    private void scanCard() {
        builder = new MaterialDialog.Builder(NewCardActivity.this);
        builder.title("Swipe card");
        builder.content("waiting for card");
        builder.negativeText("Cancel");
        builder.progress(true, 0);
        builder.cancelable(false);
        dialog = builder.build();
        dialog.show();

        onNewIntent(getIntent());

        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                Toast.makeText(NewCardActivity.this, "Card not detected", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String studentNumber = inputStudentNumber.getText().toString();

        if (studentNumber.isEmpty()) {
            inputStudentNumber.setError("student number must be provided");
            valid = false;
        } else {
            inputStudentNumber.setError(null);
        }

        return valid;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {

            if (dialog != null) {

                Toast.makeText(this, "Card detected", Toast.LENGTH_LONG).show();

                dialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcHandler.enableForeGroundDispatch();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcHandler.disableForeGroundDispatch();
    }
}
