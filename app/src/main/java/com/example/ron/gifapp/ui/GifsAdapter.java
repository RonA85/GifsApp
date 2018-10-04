package com.example.ron.gifapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ron.gifapp.R;
import com.example.ron.gifapp.model.GIF;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GifsAdapter extends RecyclerView.Adapter<GifsAdapter.GifsViewHolder> {

    private List<GIF> gifs;
    private Context context;
    private OnGifClickListener listener;


    public interface OnGifClickListener {
        void onClickGIf(GIF gif);
    }

    public GifsAdapter(Context context, List<GIF> gifs, OnGifClickListener listener) {
        this.context = context;
        this.gifs = gifs;
        this.listener = listener;
    }

    @Override
    public GifsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gif_item, parent, false);
        return new GifsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GifsViewHolder holder, int position) {
        final GIF gif = gifs.get(position);
        holder.username.setText(gif.getUsername().equals("") ? "Unknown" : gif.getUsername());
        if(gif.getSource().length()>30){
            holder.source.setText(gif.getSource().equals("") ? "Unknown" : gif.getSource().substring(0,29));
        }else
            holder.source.setText(gif.getSource().equals("") ? "Unknown" : gif.getSource());
        holder.rate.setText(gif.getRating());
        Glide.with(context).load(gif.getImages().getFixedHeightStill().getUrl()).into(holder.poster);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickGIf(gif);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gifs != null ? gifs.size() : 0;
    }

    class GifsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_gif)
        CardView card;
        @BindView(R.id.iv_poster)
        ImageView poster;
        @BindView(R.id.tv_username)
        TextView username;
        @BindView(R.id.tv_source)
        TextView source;
        @BindView(R.id.tv_rate)
        TextView rate;

        private GifsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
