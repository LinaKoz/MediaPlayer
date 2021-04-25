package com.example.mediaplayer;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> implements ItemTouchHelperAdapter{

    private List<Song> songs;
    private ItemTouchHelper mTouchHelper;
    private ItemTouchHelperAdapter actionsListener;
    private MySongListener songListener;
    private Context context;

    interface MySongListener{
        void onSongClick(int position, View view);
    }

    public void setSongListener(MySongListener listener){
        this.songListener=listener;
    }

    public void setActionsListener(ItemTouchHelperAdapter actionsListener) {
        this.actionsListener = actionsListener;
    }

    public SongAdapter(List<Song> songs,Context context) {
        this.context =context;
        this.songs = songs;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(actionsListener!=null)
            actionsListener.onItemMove(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        if(actionsListener!=null)
            actionsListener.onItemSwiped(position);
    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper=touchHelper;
    }


    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {

        TextView songName;
        TextView artistName;
        ImageView albumPhoto;
        TextView linkSongEt;
        GestureDetector mGestureDetector;

        public SongViewHolder(View itemView) {
            super(itemView);
            songName=itemView.findViewById(R.id.songNameTv);
            artistName=itemView.findViewById(R.id.artistNameTv);
            albumPhoto=itemView.findViewById(R.id.songImage);
            linkSongEt=itemView.findViewById(R.id.linkSongEt);
            mGestureDetector=new GestureDetector(itemView.getContext(), this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(songListener!=null)
                        songListener.onSongClick(getAdapterPosition(), view);
                }
            });
        }


        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }


    }


    @Override
    public SongViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.song, parent, false);
        SongViewHolder songViewHolder=new SongViewHolder(view);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder( SongViewHolder holder, int position) {
        Song song=songs.get(position);
        holder.songName.setText(song.getSongName());
        holder.artistName.setText(song.getArtistName());
        Glide.with(context).load(song.getPhotoID()).centerCrop().into(holder.albumPhoto);
        holder.linkSongEt.setText(song.getLink());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
