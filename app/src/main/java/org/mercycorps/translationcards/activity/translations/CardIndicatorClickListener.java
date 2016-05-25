package org.mercycorps.translationcards.activity.translations;

import android.view.View;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.service.TranslationService;

class CardIndicatorClickListener implements View.OnClickListener {

    private View translationItem;
    private int position;
    private TranslationService translationService;

    public CardIndicatorClickListener(View translationItem, int position, TranslationService translationService) {
        this.translationItem = translationItem;
        this.position = position;
        this.translationService = translationService;
    }

    @Override
    public void onClick(View view) {
        View translationChild = translationItem.findViewById(R.id.translation_child);
        if (translationChild.getVisibility() == View.GONE) {
            translationChild.setVisibility(View.VISIBLE);
            translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.collapse_arrow);
            translationService.expandCard(position);
        } else {
            translationChild.setVisibility(View.GONE);
            translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.expand_arrow);
            translationService.minimizeCard(position);
        }
    }
}
