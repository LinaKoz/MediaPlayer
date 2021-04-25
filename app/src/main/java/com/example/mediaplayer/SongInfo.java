package com.example.mediaplayer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import xyz.hanks.library.bang.SmallBangView;

public class SongInfo extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    interface  mySong{
        void fun();
    }
    mySong callBack;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            callBack = (mySong) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement the myInfo intrerface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.song_info,container,false);
        Bundle bundle = getArguments();
        SmallBangView imageView1;
        TextView songName =rootView.findViewById(R.id.songNameInfo);
        TextView artistName = rootView.findViewById(R.id.artistInfo);
        ImageView imageView =rootView.findViewById(R.id.songImageInfo);
        String song_name,artist_name,image;
        song_name =bundle.getString("songName");
        artist_name =bundle.getString("artistName");
        image=bundle.getString("songImage");
        songName.setText(song_name);
        artistName.setText(artist_name);
        Glide.with(this).load(image).centerCrop().into(imageView);
        return rootView;
    }
}
