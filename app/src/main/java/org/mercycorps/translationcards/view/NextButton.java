package org.mercycorps.translationcards.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NextButton extends LinearLayout {
    @Bind(R.id.next_button_layout)LinearLayout nextButton;
    @Bind(R.id.next_button_text)TextView nextButtonText;
    @Bind(R.id.next_button_image)ImageView nextButtonImage;

    public NextButton(Context context) {
        super(context);
        init();
    }

    public NextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NextButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.next_button, this);
        ButterKnife.bind(this);
    }

    public void disable() {
        nextButton.setClickable(false);
        nextButtonText.setTextColor(ContextCompat.getColor(getContext(), R.color.textDisabled));
        nextButtonImage.setBackgroundResource(R.drawable.forward_arrow_disabled);
    }

    public void enable() {
        nextButton.setClickable(true);
        nextButtonText.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
        nextButtonImage.setBackgroundResource(R.drawable.forward_arrow);
    }
}
