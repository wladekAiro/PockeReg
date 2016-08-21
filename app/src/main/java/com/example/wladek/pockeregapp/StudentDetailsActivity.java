package com.example.wladek.pockeregapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mPicasso = Picasso.with(this);

        imgStudentPic = (ImageView) findViewById(R.id.imgStudentPic);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGalleryOptions();
            }
        });
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
        }
    }

    public void launchGallery(Context context){
        galleryPhoto = new GalleryPhoto(context);
        startActivityForResult(galleryPhoto.openGalleryIntent() , GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {

                setPhoto(cameraPhoto.getPhotoPath());

            }else if (requestCode == GALLERY_REQUEST){
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
        }
    }

}
