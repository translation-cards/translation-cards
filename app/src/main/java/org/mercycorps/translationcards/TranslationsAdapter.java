package org.mercycorps.translationcards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by njimenez on 2/9/16.
 */
public class TranslationsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Dictionary dictionary;

    public TranslationsAdapter(Context context, Dictionary dictionary){
        this.context = context;
        this.dictionary = dictionary;
    }

    public void setDictionary(Dictionary dictionaryIndex) {
        this.dictionary = dictionaryIndex;
    }

    @Override
    public int getGroupCount() {
        return dictionary.getTranslationCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dictionary.getTranslation(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dictionary.getTranslation(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.translation_item, parent, false);
        }

        TextView originTranslationText = (TextView) convertView.findViewById(R.id.origin_translation_text);
        originTranslationText.setText(((Dictionary.Translation)getGroup(groupPosition)).getLabel());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
