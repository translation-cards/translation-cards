package org.mercycorps.translationcards.refactor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.mercycorps.translationcards.data.NewTranslationContext;

import butterknife.ButterKnife;

public abstract class AbstractTranslationCardsActivity extends AppCompatActivity {


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

    static final ButterKnife.Setter<View, Integer> VISIBILTY = new ButterKnife.Setter<View, Integer>() {
        @Override public void set(View view, Integer visibility, int index) {
            view.setVisibility(visibility);
        }
    };
}
