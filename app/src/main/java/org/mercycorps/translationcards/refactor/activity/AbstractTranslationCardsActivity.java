package org.mercycorps.translationcards.refactor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.mercycorps.translationcards.data.NewTranslationContext;

import butterknife.ButterKnife;

public abstract class AbstractTranslationCardsActivity extends AppCompatActivity {
    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView();
        ButterKnife.bind(this);
        initStates();
    }

    public abstract void inflateView();

    protected void initStates() {
    }

    protected NewTranslationContext getContextFromIntent(){
        return (NewTranslationContext) getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);

    }

    protected void startNextActivity(Context currentContext, Class nextActivityClass) {
        Intent intent = new Intent(currentContext, nextActivityClass);
        startActivity(intent);
    }
}
