package org.mercycorps.translationcards.activity.translations;

import android.graphics.drawable.LayerDrawable;
import android.view.View;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.TranslationService;

class CardIndicatorClickListener implements View.OnClickListener {

    private View view;
    private int position;
    private TranslationService translationService;
    private Translation translation;

    public CardIndicatorClickListener(View view, int position, TranslationService translationService,
                                      Translation item) {
        this.view = view;
        this.position = position;
        this.translationService = translationService;
        this.translation = item;
    }

    @Override
    public void onClick(View view) {
        View translationChild = this.view.findViewById(R.id.translation_child);
        View v = this.view.findViewById(R.id.translation_card_parent);
        int rightPadding= v.getPaddingRight();
        int leftPadding= v.getPaddingLeft();

        if (translationChild.getVisibility() == View.GONE) {
            translationChild.setVisibility(View.VISIBLE);
            this.view.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.collapse_arrow);
            translationService.expandCard(position);
            v.setBackgroundResource(R.drawable.card_top_background_expanded);
        } else {
            translationChild.setVisibility(View.GONE);
            this.view.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.expand_arrow);
            translationService.minimizeCard(position);
            v.setBackgroundResource(R.drawable.card_top_background);
        }
        v.setPadding(leftPadding, 0, rightPadding, 0);

        LayerDrawable bgDrawable = (LayerDrawable)v.getBackground();
        if(!translation.isAudioFilePresent()){
            bgDrawable.setAlpha(CardListAdapter.DISABLED_OPACITY);
        }
        else{
            bgDrawable.setAlpha(CardListAdapter.DEFAULT_OPACITY);
        }
    }
}
