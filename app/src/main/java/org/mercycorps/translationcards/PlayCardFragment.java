package org.mercycorps.translationcards;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayCardFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setLandscapeScreenOrientation();
        View playCardView = inflater.inflate(R.layout.play_card_view, container, false);
        setTranslationText(playCardView);
        return playCardView;
    }

    private void setTranslationText(View playCardView) {
        TextView playCardText = (TextView) playCardView.findViewById(R.id.play_card_text);
        playCardText.setText(getArguments().getString("translationText"));
        playCardText.setOnClickListener(this);
    }

    private void setLandscapeScreenOrientation() {
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }
}
