package com.example.wladek.pockeregapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.pockeregapp.pojo.School;
import com.example.wladek.pockeregapp.util.DatabaseHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProvideSchoolActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    EditText inputSchoolCode;
    Button btnSubmitCode;
    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_school);
        databaseHelper = new DatabaseHelper(this);

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

        if (TextUtils.isEmpty(code)) {
            inputSchoolCode.setError("You must provide school code ");
            view = inputSchoolCode;
            cancel = true;
        }

        if (cancel) {
            view.requestFocus();
        } else {
            confirmSchool(code);
        }

    }

    private void confirmSchool(String code) {
        //Fetch school name and code from server.
        //Show details an dialog and confirm

        sweetAlertDialog = new SweetAlertDialog(ProvideSchoolActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetAlertDialog.setTitleText("Please wait ...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        saveSchoolDetails(null);

//        Map<String, String> schoolParams = new HashMap<String, String>();
//
//        schoolParams.put("schoolCode", code);
//
//        Response.Listener<JSONObject> loginResponseListener = new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    String schoolName = response.getString("schoolName");
//                    String schoolCode = response.getString("schoolCode");
//                    School schoolDetails = new School();
//                    schoolDetails.setSchoolName(schoolName);
//                    schoolDetails.setSchoolCode(schoolCode);
//                    schoolDetails.setStatus(SchoolStatus.ACTIVE);
//                    sweetAlertDialog.dismiss();
//
//                    saveSchoolDetails(schoolDetails);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        SchoolRequest loginRequest = new SchoolRequest(new JSONObject(schoolParams), loginResponseListener);
//        RequestQueue queue = Volley.newRequestQueue(ProvideSchoolActivity.this);
//        queue.add(loginRequest);
    }

    private void saveSchoolDetails(final School schoolDetails) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        builder.title("Confirm ");
//        builder.content(schoolDetails.getSchoolName());
        builder.content("Test");
        builder.cancelable(false);
        builder.positiveText("Ok");
        builder.negativeText("Cancel");

        MaterialDialog dialog = builder.build();
        dialog.show();

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
//                databaseHelper.setSchool(schoolDetails);
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
