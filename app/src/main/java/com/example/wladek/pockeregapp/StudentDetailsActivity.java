package com.example.wladek.pockeregapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.wladek.pockeregapp.pojo.Student;
import com.example.wladek.pockeregapp.util.DatabaseHelper;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class StudentDetailsActivity extends AppCompatActivity {

    final int CAMERA_REQUEST = 321;
    final int GALLERY_REQUEST = 3233;
    final int RESULT_OK = -1;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;

    MaterialDialog.Builder builder;
    MaterialDialog dialog;

    ImageView imgStudentPic;
    FloatingActionButton fab;
    Picasso mPicasso;

    DatabaseHelper dbHelper;

    EditText inputFirstName;
    EditText inputSecondName;
    EditText inputSurName;
    EditText inputStudentNumber;
    EditText inputParentName;
    EditText inputParentIdNumber;
    EditText inputParentPhoneNumber;

    Button btnSubmit;

    String photoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DatabaseHelper(this);

        mPicasso = Picasso.with(this);

        imgStudentPic = (ImageView) findViewById(R.id.imgStudentPic);

        inputFirstName = (EditText) findViewById(R.id.inputFirstName);
        inputSecondName = (EditText) findViewById(R.id.inputSecondName);
        inputSurName = (EditText) findViewById(R.id.inputSurName);
        inputStudentNumber = (EditText) findViewById(R.id.inputStudentNumber);
        inputParentName = (EditText) findViewById(R.id.inputParentName);
        inputParentIdNumber = (EditText) findViewById(R.id.inputParentIdNumber);
        inputParentPhoneNumber = (EditText) findViewById(R.id.inputParentPhoneNumber);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGalleryOptions();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createStudentRecord();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.students_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newStudent:
                resetField();
                return true;
            case R.id.regCard:
                Intent intent = new Intent(StudentDetailsActivity.this, NewCardActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showGalleryOptions() {
        boolean wrapInScrollView = true;
        builder = new MaterialDialog.Builder(this);
        builder.title("Upload");
        builder.customView(R.layout.select_gallery_layout, wrapInScrollView);
        builder.cancelable(true);

        dialog = builder.build();
        dialog.show();

        View customView = dialog.getCustomView();

        ImageButton imgBtnGallery = (ImageButton) customView.findViewById(R.id.imgBtnGallery);
        ImageButton imgBtnCam = (ImageButton) customView.findViewById(R.id.imgBtnCam);
        TextView txtCamera = (TextView) customView.findViewById(R.id.txtCamera);
        TextView txtGallery = (TextView) customView.findViewById(R.id.txtGallery);

        imgBtnCam.setOnClickListener(new CustomClickListener(this, "imgBtnCam"));
        imgBtnGallery.setOnClickListener(new CustomClickListener(this, "imgBtnGallery"));
        txtGallery.setOnClickListener(new CustomClickListener(this, "imgBtnGallery"));
        txtCamera.setOnClickListener(new CustomClickListener(this, "imgBtnCam"));
    }


    private class CustomClickListener implements View.OnClickListener {
        String btnName;
        Context context;

        public CustomClickListener(Context context, String imgBtnCam) {
            this.btnName = imgBtnCam;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (btnName.equals("imgBtnCam")) {

                dialog.dismiss();

                launchCamera(context);

            } else if (btnName.equals("imgBtnGallery")) {
                dialog.dismiss();
                launchGallery(context);
            }

        }
    }

    public void launchCamera(Context context) {
        cameraPhoto = new CameraPhoto(context);
        try {
            startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
            cameraPhoto.addToGallery();
        } catch (IOException e) {
            Toast.makeText(context, "Something went wrong while taking a photo",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void launchGallery(Context context) {
        galleryPhoto = new GalleryPhoto(context);
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                setPhoto(cameraPhoto.getPhotoPath());

            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                setPhoto(galleryPhoto.getPath());
            }
        }
    }

    public void setPhoto(String photoPath) {

        if (photoPath != null) {
            mPicasso
                    .load(new File(photoPath))
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.error_circle)
                    .resize(400, 400)
                    .into(imgStudentPic);

            this.photoUrl = photoPath;
        }
    }


    public void createStudentRecord() throws InterruptedException {

        String firstName = inputFirstName.getText().toString();
        String secondName = inputSecondName.getText().toString();
        String surName = inputSurName.getText().toString();
        String studentNumber = inputStudentNumber.getText().toString();
        String parentName = inputParentName.getText().toString();
        String parentIdNo = inputParentIdNumber.getText().toString();
        String parentPhoneNo = inputParentPhoneNumber.getText().toString();

        View focusView = null;

        boolean invalid = false;

        if (TextUtils.isEmpty(firstName)) {
            inputFirstName.setError("please provide first name");
            focusView = inputFirstName;
            invalid = true;
        }

        if (TextUtils.isEmpty(secondName)) {
            inputSecondName.setError("please provide second name");
            focusView = inputSecondName;
            invalid = true;
        }

        if (TextUtils.isEmpty(surName)) {
            inputSurName.setError("please provide surname");
            focusView = inputSurName;
            invalid = true;
        }

        if (TextUtils.isEmpty(studentNumber)) {
            inputStudentNumber.setError("please provide student registration number");
            focusView = inputStudentNumber;
            invalid = true;
        }

        if (TextUtils.isEmpty(parentName)) {
            inputParentName.setError("please provide parent's full name");
            focusView = inputParentName;
            invalid = true;
        }

        if (TextUtils.isEmpty(parentIdNo)) {
            inputParentIdNumber.setError("please provide parent's ID number");
            focusView = inputParentIdNumber;
            invalid = true;
        }

        if (TextUtils.isEmpty(parentPhoneNo)) {
            inputParentPhoneNumber.setError("please provide parent's phone number");
            focusView = inputParentPhoneNumber;
            invalid = true;
        }

        if (invalid) {
            focusView.requestFocus();
        } else {
            Student student = new Student();
            student.setFirstName(firstName);
            student.setSchoolCode(secondName);
            student.setSurName(surName);
            student.setStudentNo(studentNumber);
            student.setParentFullName(parentName);
            student.setParentIdNumber(parentIdNo);
            student.setParentPhoneNumber(parentPhoneNo);

            if (TextUtils.isEmpty(photoUrl)) {
                Toast.makeText(getApplicationContext(), "Error !!! , No photo provided.", Toast.LENGTH_LONG).show();
                return;
            } else {
                student.setPhotoPath(photoUrl);

                String response = dbHelper.createStudent(student);

                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                Thread.sleep(2000);

                resetField();

            }
        }

    }

    private void resetField() {
        inputFirstName.setText(null);
        inputSecondName.setText(null);
        inputSurName.setText(null);
        inputStudentNumber.setText(null);
        inputParentName.setText(null);
        inputParentIdNumber.setText(null);
        inputParentPhoneNumber.setText(null);
    }

}
