package com.example.mediaplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class newSong extends Fragment {
    private Context context;
    int PICK_IMAGE = 1;
    File file;
    SharedPreferences photoNumber;
    FloatingActionButton apply,openGallery,openCamera;
    EditText songName,songLink,artistName;
    String nameS,nameA,link;
    final int CAMERA_REQUEST = 1;
    final int MY_CAMERA_PERMISSION_CODE = 100;
    final int WRITE_PERMISSION_REQUEST=1;
    final  int IMAGE_PICK_CODE=1000;
    final  int PERMISSION_CODE=1001;
    String imagePath;
    Uri  imageUri;
    ImageView imageSong;
    public newSong( Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    interface  myInfo{
        void addSong(String nameS, String nameA, String link, String imagePath);
    }
    myInfo callBack;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (myInfo) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement the myInfo intrerface");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imagePath = file.getAbsolutePath();
            Glide
                    .with(newSong.this)
                    .load(imageUri)
                    .centerCrop()
                    .into(imageSong);

        }
        else if ( requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK){
//            imageSong.setVisibility(View.VISIBLE);
            imageUri = data.getData();
            Glide
                    .with(newSong.this)
                    .load(imageUri)
                    .centerCrop()
                    .into(imageSong);

        }
    }

    public void pickImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_song,container,false);

        apply=rootView.findViewById(R.id.apply);
        openCamera =rootView.findViewById(R.id.openCamerabtn);
        openGallery =rootView.findViewById(R.id.openGallerybtn);
        songName=rootView.findViewById(R.id.songNameET);
        songLink=rootView.findViewById(R.id.linkET);
        artistName = rootView.findViewById(R.id.artistNameET);

        imageSong = rootView.findViewById(R.id.songImageNew);

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int randNumber = new Random().nextInt();
               String photoName =String.valueOf(randNumber);
               String finalName = "pic"+photoName+".jpg";
              file=new File(getActivity().getExternalFilesDir(null),finalName);
              imageUri = FileProvider.getUriForFile(context, "com.example.mediaplayer.provider", file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,CAMERA_REQUEST);

            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWritePermission = PermissionChecker.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            }
        }
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(PermissionChecker.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)==PermissionChecker.PERMISSION_DENIED){
                        String [] permission={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }

                } pickImageFromGallery();

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameS=songName.getText().toString();
                nameA= artistName.getText().toString();
                link =songLink.getText().toString();
                if(nameS!=null&&nameA!=null&&link!=null){
                    callBack.addSong(nameS,nameA,link,imageUri.toString());
                }

            }
        });

        return rootView;
    }




}
