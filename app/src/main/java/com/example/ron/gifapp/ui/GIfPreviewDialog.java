package com.example.ron.gifapp.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.anthonycr.progress.AnimatedProgressBar;
import com.bumptech.glide.Glide;
import com.example.ron.gifapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GIfPreviewDialog extends DialogFragment implements DialogInterface.OnKeyListener {

    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.gif_layout)
    LinearLayout gifLayout;
    @BindView(R.id.progressbar)
    AnimatedProgressBar progressBar;
    private static final int SAVE_ON_GALLERY = 1;
    private static String gifPath, gifVideo, getSlug;

    public static GIfPreviewDialog newInstance() {

        GIfPreviewDialog frag = new GIfPreviewDialog();
        frag.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
        return frag;
    }

    public static void setGIfUrl(String _gifPath, String _gifVideo, String _slug) {
        gifPath = _gifPath;
        gifVideo = _gifVideo;
        getSlug = _slug;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(STYLE_NO_FRAME, android.R.style.TextAppearance_Theme_Dialog);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dView = inflater.inflate(R.layout.dialog_gif_preview, null);
        ButterKnife.bind(this, dView);
        videoView.setVideoURI(Uri.parse(gifVideo));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        videoView.start();
        Dialog dialog = new AlertDialog.Builder(getActivity()).setView(dView).create();
        dialog.setOnKeyListener(this);
        return dialog;
    }

    @OnClick({R.id.ib_share_gif,
            R.id.ib_download_gif
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_share_gif:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                share.putExtra(Intent.EXTRA_TEXT, gifPath);
                startActivity(Intent.createChooser(share, "Share Gif!"));
                break;
            case R.id.ib_download_gif:

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_ON_GALLERY);
                } else {
                    new GifAsyncTask().execute();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }



    private class GifAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //play next segment specifying its duration
      //      segmentedProgressBar.playSegment(3000);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            Log.d(getClass().getSimpleName(), "download " + gifPath);

            try {
                return Glide
                        .with(getActivity())
                        .load(gifPath)
                        .asBitmap()
                        .into(100, 100)
                        .get();

            } catch (Exception ex) {
                Log.d(getClass().getSimpleName(), ex.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            progressBar.setProgress(100);
            if (bitmap == null) {
                return;
            }
            //save our gif
            saveImageToGallery(bitmap);
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        MediaStore.Images.Media.insertImage(
                getActivity().getContentResolver(),
                bitmap,
                "Gif",
                getSlug
        );
        Toast.makeText(getActivity(),R.string.toast_save_image,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }


    @Override
    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SAVE_ON_GALLERY) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new GifAsyncTask().execute();
            } else {
                Toast.makeText(getActivity(), "You must allow this permission to save image", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
