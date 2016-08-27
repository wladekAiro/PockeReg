package com.example.wladek.pockeregapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.pockeregapp.util.DatabaseHelper;

public class ProvideSchoolActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    EditText inputSchoolCode;
    Button btnSubmitCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_school);

        inputSchoolCode = (EditText) findViewById(R.id.inputSchoolCode);
        btnSubmitCode = (Button) findViewById(R.id.btnSubmitCode);

        btnSubmitCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCode();
            }
        });
    }

    private void validateCode() {
        String code = inputSchoolCode.getText().toString();

        View view = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(code)){
            inputSchoolCode.setError("You must provide school code ");
            view = inputSchoolCode;
            cancel = true;
        }

        if (cancel){
            view.requestFocus();
        }else {
            confirmSchool(code);
        }

    }

    private void confirmSchool(String code) {
        //Fetch school name and code from server.
        //Show details an dialog and confirm

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("Confirm Example school");
        builder.cancelable(false);
        builder.positiveText("Ok");
        builder.negativeText("Cancel");

        MaterialDialog dialog = builder.build();
        dialog.show();

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                Intent intent = new Intent(ProvideSchoolActivity.this, StudentDetailsActivity.class);
                startActivity(intent);
            }
        });

        builder.onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                inputSchoolCode.setText(null);
                dialog.dismiss();
            }
        });

    }
}
