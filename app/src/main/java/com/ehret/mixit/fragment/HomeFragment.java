package com.ehret.mixit.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.utils.UIUtils;


public class HomeFragment extends Fragment {

    private Handler mHandler = new Handler();
    private TextView mCountdownTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);
        mCountdownTextView = (TextView) rootView.findViewById(R.id.whats_on);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Mise a jour du titre
        TextView mapText = (TextView) getActivity().findViewById(R.id.mapTextView);
        mapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW);
                mapIntent.setData(Uri.parse("geo:45.78375369999999,4.869024799999999?z=14&q=CPE+Lyon,+43+Boulevard+du+11+novembre,+69616+Villeurbanne"));
                UIUtils.filterIntent(getActivity(), "maps", mapIntent);
                startActivity(Intent.createChooser(mapIntent, "Venir à Mix-IT"));
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached("title_section_home","color_home");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(mCountdownRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        mHandler.removeCallbacks(mCountdownRunnable);

        final long currentTimeMillis = System.currentTimeMillis();

        // Show Loading... and load the view corresponding to the current state
        if (currentTimeMillis < UIUtils.CONFERENCE_START_MILLIS) {
            mHandler.post(mCountdownRunnable);
        } else if (currentTimeMillis > UIUtils.CONFERENCE_END_MILLIS) {
            mCountdownTextView.setText(R.string.whats_on_thank_you_title);
        }

    }


    /**
     * Event that updates countdown timer. Posts itself again to
     * {@link #mHandler} to continue updating time.
     */
    private final Runnable mCountdownRunnable = new Runnable() {
        public void run() {
            int remainingSec = (int) Math.max(0, (UIUtils.CONFERENCE_START_MILLIS - System.currentTimeMillis()) / 1000);
            final boolean conferenceStarted = remainingSec == 0;

            if (conferenceStarted) {
                // Conference started while in countdown mode, switch modes and
                // bail on future countdown updates.
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        refresh();
                    }
                }, 100);
                return;
            }

            final int secs = remainingSec % 86400;
            final int days = remainingSec / 86400;
            final String str;
            if (days == 0) {
                str = getResources().getString(
                        R.string.whats_on_countdown_title_0,
                        DateUtils.formatElapsedTime(secs));
            } else {
                str = getResources().getQuantityString(
                        R.plurals.whats_on_countdown_title, days, days,
                        DateUtils.formatElapsedTime(secs));
            }
            mCountdownTextView.setText(str);

            // Repost ourselves to keep updating countdown
            mHandler.postDelayed(mCountdownRunnable, 1000);
        }
    };
}
