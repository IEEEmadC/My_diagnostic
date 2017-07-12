package org.dev4u.hv.my_diagnostic;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton toggleSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toggleSettings = (ToggleButton) findViewById(R.id.toggleUpdates);
        //StateListDrawable stateListDrawable = (StateListDrawable) toggleSettings.getBackground();
        //AnimationDrawable animationDrawable = (AnimationDrawable) stateListDrawable.getCurrent();
        //animationDrawable.selectDrawable(animationDrawable.getNumberOfFrames() - 1);
    }
}
