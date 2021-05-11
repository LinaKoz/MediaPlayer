package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener ,newSong.myInfo,SongInfo.mySong{

    boolean isPlay = false;
    ArrayList<Song> songs;
    SharedPreferences sharedPreferences;
    private MaterialDialog mAnimatedDialog;
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    BottomNavigationView navigationViewDown;
    SongInfo songInfo;
    final String ADD_TAG="add",INFO_TAG="info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        navigationViewDown =findViewById(R.id.musicPlay);
        navigationViewDown.setOnNavigationItemSelectedListener(this);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songs = new ArrayList<>();
        sharedPreferences = getSharedPreferences("song_first", MODE_PRIVATE);
        Boolean firstTime = sharedPreferences.getBoolean("firstTime", true);
        if (firstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            songs.add(new Song("one More Cup Of Coffee", "Bob dylan", "http://www.syntax.org.il/xtra/bob.m4a", "https://i.pinimg.com/originals/e3/d9/c5/e3d9c5ab7347f1eb3c2376acb6a2bf5b.jpg"));
            songs.add(new Song("Sara", "Bob Dylan", "http://www.syntax.org.il/xtra/bob1.m4a", "https://upload.wikimedia.org/wikipedia/en/thumb/d/d6/Bob_Dylan_-_The_Freewheelin%27_Bob_Dylan.jpg/220px-Bob_Dylan_-_The_Freewheelin%27_Bob_Dylan.jpg"));
            songs.add(new Song("The Man In Me", "Bob Dylan", "http://www.syntax.org.il/xtra/bob2.mp3", "https://f4.bcbits.com/img/a4152887838_10.jpg"));

            saveData();
            editor.putBoolean("firstTime", false);
            editor.apply();

        } else
            loadData();

        songAdapter = new SongAdapter(songs, MainActivity.this);
        songAdapter.setActionsListener(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                Song fromSong = songs.get(fromPosition);
                songs.remove(fromSong);
                songs.add(toPosition, fromSong);
                songAdapter.notifyItemMoved(fromPosition, toPosition);
                saveData();
            }

            @Override
            public void onItemSwiped(int position) {
                mAnimatedDialog = new MaterialDialog.Builder(MainActivity.this)
                        .setTitle("Delete?")
                        .setMessage("Are you sure want to delete this song?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", R.drawable.delet, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                                songs.remove(position);
                                songAdapter.notifyDataSetChanged();
                                dialogInterface.dismiss();
                                saveData();
                            }
                        })
                        .setNegativeButton("Cancel", R.drawable.cancel, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                                songAdapter.notifyItemChanged(position);
                                dialogInterface.dismiss();
                            }
                        })
                        .setAnimation(R.raw.delete_anim)
                        .build();
                mAnimatedDialog.show();

            }
        });
        songAdapter.setSongListener(new SongAdapter.MySongListener() {
            @Override
            public void onSongClick(int position, View view) {
                Bundle bundle = new Bundle();
                bundle.putString("songName",songs.get(position).getSongName());
                bundle.putString("artistName",songs.get(position).getArtistName());
                bundle.putString("songImage",songs.get(position).getPhotoID());
                songInfo =new SongInfo();
                songInfo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.root_container,songInfo,INFO_TAG).addToBackStack(null).commit();
                invisibility();

            }
        });
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(songAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        songAdapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(songAdapter);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addSong:
//
                getSupportFragmentManager().beginTransaction().replace(R.id.root_container,new newSong(MainActivity.this),ADD_TAG).addToBackStack(null).commit();
                navigationViewDown.setBackgroundColor(getResources().getColor(R.color.purple));
                invisibility();
                return true;
            case R.id.play:
                if(!isPlay) {
//                    navigationViewDown.setBackgroundColor(1);
                    navigationViewDown.setBackgroundColor(getResources().getColor(R.color.babyblue2));
                    Intent intent2 = new Intent(MainActivity.this, MyService.class);
                    intent2.putExtra("command", "new_instance");
                    startService(intent2);
                    isPlay = true;

//                    progressBar();
                } else {

                    navigationViewDown.setBackgroundColor(getResources().getColor(R.color.babyblue2));
                    Intent intent3 = new Intent(MainActivity.this, MyService.class);
                    intent3.putExtra("command", "play");
                    startService(intent3);
//                    progressBar();
                }
                return true;
            case R.id.stop:
                navigationViewDown.setBackgroundColor(getResources().getColor(R.color.yellow));
                Intent intent4 = new Intent(MainActivity.this, MyService.class);
                intent4.putExtra("list", songs);
                intent4.putExtra("command", "pause");
                startService(intent4);
                            return true;

            case R.id.next:
                navigationViewDown.setBackgroundColor(getResources().getColor(R.color.babyBlue));

                Intent intent1 = new Intent(MainActivity.this, MyService.class);
                        intent1.putExtra("list", songs);
                        intent1.putExtra("command", "next");
                        startService(intent1);
                                        return true;
            case  R.id.prev:
                navigationViewDown.setBackgroundColor(getResources().getColor(R.color.turquoise));

                Intent intent2 = new Intent(MainActivity.this, MyService.class);
                        intent2.putExtra("list", songs);
                        intent2.putExtra("command", "prev");
                        startService(intent2);
                        return true;

        }

        return false;
    }


    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            getSupportFragmentManager().popBackStack();
            visibility();
        }
    
       else super.onBackPressed();
    }
    public void visibility(){

        recyclerView.setVisibility(View.VISIBLE);

    }
    public void invisibility(){

        recyclerView.setVisibility(View.GONE);
    }
    private void loadData() {
        try {
            FileInputStream fis = openFileInput("songList.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            songs = (ArrayList<Song>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try {
            FileOutputStream fos = openFileOutput("songList.dat", MODE_PRIVATE);
            ObjectOutputStream oow = new ObjectOutputStream(fos);
            oow.writeObject(songs);
            oow.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fun() {

    }

    @Override
    public void addSong(String nameS, String nameA, String link, String imagePath) {
        Fragment fragmentManager = getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        getSupportFragmentManager().beginTransaction().remove(fragmentManager).commit();
        visibility();
        songs.add(new Song(nameS,nameA,link,imagePath));
        saveData();
    }
}
