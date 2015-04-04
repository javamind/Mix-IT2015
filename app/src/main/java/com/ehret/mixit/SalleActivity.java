package com.ehret.mixit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ehret.mixit.utils.UIUtils;

public class SalleActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);

        int level = getIntent().getIntExtra(UIUtils.ARG_KEY_ROOM, 0);

        ImageView image = (ImageView) findViewById(R.id.image_salle);
        image.setImageResource(level == 0 ? R.drawable.plan_etage_0 : R.drawable.plan_etage_1);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
