package org.mercycorps.translationcards.activity.addDeck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import java.util.List;

public class DestinationLanguagesAdapter extends BaseAdapter {
    private Context context;
    private List<String> languages;

    public DestinationLanguagesAdapter(Context context, List<String> languages) {
        this.context = context;
        this.languages = languages;
    }

    @Override
    public int getCount() {
        return languages.size();
    }

    @Override
    public String getItem(int position) {
        return languages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView chip;
        if(convertView == null) {
            chip = new TextView(context);
        } else {
            chip = (TextView) convertView;
        }

        createLanguageChip(position, chip);

        return chip;
    }

    private void createLanguageChip(int position, TextView chip) {
        chip.setText(languages.get(position));
        chip.setTextColor(context.getResources().getColor(R.color.textColor));
        chip.setTypeface(null, Typeface.BOLD);
        chip.setSingleLine();
        chip.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int padding = densityPixelsToPixels(8);
        chip.setPadding(padding, padding, padding, padding);
        chip.setBackgroundResource(R.drawable.language_chip_background);

        BitmapDrawable img = (BitmapDrawable)context.getResources().getDrawable(R.drawable.ic_cancel_white_24dp);
        if(img != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(img.getBitmap(), densityPixelsToPixels(20), densityPixelsToPixels(20), false);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), scaledBitmap);
            chip.setCompoundDrawablesWithIntrinsicBounds(null, null, bitmapDrawable, null);
            chip.setCompoundDrawablePadding(padding);
        }
    }

    protected int densityPixelsToPixels(float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * metrics.density);
    }

    public void add(String language) {
        if(!languages.contains(language)) {
            languages.add(language);
            notifyDataSetChanged();
        }
    }

    public List<String> getLanguages() {
        return languages;
    }
}
