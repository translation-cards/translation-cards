package org.mercycorps.translationcards.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

public abstract class AbstractTranslationCardsActivity extends AppCompatActivity {
    private Bitmap currentBitmap;
    private ImageView currentBitmapView;

    protected void setBitmap(int resId, int drawableId) {
        recycleBitmap();
        currentBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        currentBitmapView = (ImageView) findViewById(resId);
        currentBitmapView.setImageBitmap(currentBitmap);
    }

    protected void recycleBitmap() {
        if (currentBitmap != null) {
            currentBitmap.recycle();
            if (currentBitmapView != null) currentBitmapView.setImageBitmap(null);
            currentBitmap = null;
            System.gc();
        }
    }

    //Convert a density pixel value to a pixel value
    protected int densityPixelsToPixels(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return Math.round(dp * metrics.density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView();
        setBitmapsForActivity();
        ButterKnife.bind(this);
        initStates();
    }

    public abstract void inflateView();

    public abstract void setBitmapsForActivity();

    protected void initStates() {
    }

    protected Object getObjectFromIntent(String serializableKey){
        return getIntent().getSerializableExtra(serializableKey);
    }

    protected void startNextActivity(Context currentContext, Class nextActivityClass) {
        Intent intent = new Intent(currentContext, nextActivityClass);
        startActivity(intent);
    }

    protected static final ButterKnife.Setter<View, Integer> VISIBILITY = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer visibility, int index) {
            view.setVisibility(visibility);
        }
    };

    protected void updateListViewCentered(ListView view, Boolean isEmpty) {
        int isCentered = isEmpty ? RelativeLayout.TRUE : 0;
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT, isCentered);
        view.setLayoutParams(params);
    }
}
