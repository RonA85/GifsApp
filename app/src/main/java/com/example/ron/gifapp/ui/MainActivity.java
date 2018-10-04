package com.example.ron.gifapp.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.example.ron.gifapp.R;
import com.example.ron.gifapp.model.GIF;
import com.example.ron.gifapp.model.GifResponse;
import com.example.ron.gifapp.vm.GifViewModel;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements GifsAdapter.OnGifClickListener, TextWatcher {

    @BindView(R.id.et_search)
    MaterialEditText etSearch;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.rv_gifs)
    RecyclerView rvGifs;

    private Unbinder unbinder;
    private GifsAdapter gifsAdapter;
    private GifViewModel gifViewModel;
    private MediaPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        player = MediaPlayer.create(this,R.raw.toy);
        player.setLooping(true);
        gifViewModel = ViewModelProviders.of(this).get(GifViewModel.class);
        setupRecycler();
        etSearch.addTextChangedListener(this);
        gifViewModel.getGIfsLiveData().observe(this, new Observer<GifResponse>() {
            @Override
            public void onChanged(@Nullable GifResponse gifResponse) {
                assert gifResponse != null;
                progressBar.setVisibility(View.GONE);
                List<GIF> gifs = gifResponse.getGifs();
                Log.d(getClass().getSimpleName(),gifs.get(0).getId());
                gifsAdapter = new GifsAdapter(MainActivity.this,gifs ,MainActivity.this);
                rvGifs.setAdapter(gifsAdapter);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        player.start();
    }

    private void setupRecycler() {
        rvGifs.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGifs.setLayoutManager(layoutManager);

    }

    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        unbinder.unbind();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if(!s.toString().isEmpty()){
            gifViewModel.searchGifs(s.toString());
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClickGIf(GIF gif) {
        hideSoftKeyboard();
        GIfPreviewDialog.newInstance().show(getSupportFragmentManager(), "GIfPreviewDialog");
        GIfPreviewDialog.setGIfUrl(
                gif.getImages().getOriginalStill().getUrl(),
                gif.getImages().getPreview().getMp4(),
                gif.getSlug());
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}
