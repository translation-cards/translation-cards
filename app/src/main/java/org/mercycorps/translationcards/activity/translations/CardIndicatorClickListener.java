package org.mercycorps.translationcards.activity.translations;

import android.view.View;

import org.mercycorps.translationcards.R;

class CardIndicatorClickListener implements View.OnClickListener {

    private TranslationsActivity translationsActivity;
    private View translationItem;
    private int position;

    public CardIndicatorClickListener(TranslationsActivity translationsActivity, View translationItem, int position) {
        this.translationsActivity = translationsActivity;

        this.translationItem = translationItem;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        View translationChild = translationItem.findViewById(R.id.translation_child);
        if (translationChild.getVisibility() == View.GONE) {
            translationChild.setVisibility(View.VISIBLE);
            translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.collapse_arrow);
            translationsActivity.translationCardStates.set(position, true);
        } else {
            translationChild.setVisibility(View.GONE);
            translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.expand_arrow);
            translationsActivity.translationCardStates.set(position, false);
        }
    }
}
