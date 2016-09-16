package org.mercycorps.translationcards.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Chip extends LinearLayout {

    @Bind(R.id.chip_text) TextView chipText;

    private OnDeleteListener onDeleteListener = new OnDeleteListener() {
        @Override
        public void delete() {}
    };

    public Chip(Context context) {
        super(context);
        init();
    }

    public Chip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Chip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.chip, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.delete_chip)
    public void deleteChip() {
        onDeleteListener.delete();
    }

    public void setText(String text) {
        chipText.setText(text);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void delete();
    }
}
