package org.mercycorps.translationcards.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyDecksFooter extends LinearLayout {

    @Bind(R.id.empty_my_decks_message)TextView emptyMyDecksMessage;
    @Bind(R.id.empty_my_decks_title)TextView emptyMyDecksTitle;
    @Bind(R.id.feedback_button)RelativeLayout feedbackButton;

    public MyDecksFooter(Context context) {
        super(context);
        init();
    }

    public MyDecksFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDecksFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyDecksFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.my_decks_footer, this);
        ButterKnife.bind(this);
        this.setOnClickListener(null);
    }

    public void emptyDecksView() {
        emptyMyDecksMessage.setVisibility(View.VISIBLE);
        emptyMyDecksTitle.setVisibility(View.VISIBLE);
        feedbackButton.setVisibility(View.GONE);
    }

    public void nonEmptyDecksView() {
        emptyMyDecksMessage.setVisibility(View.GONE);
        emptyMyDecksTitle.setVisibility(View.GONE);
        feedbackButton.setVisibility(View.VISIBLE);
    }
}
