package org.dev4u.hv.my_diagnostic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
         ((TextView)findViewById(R.id.link1)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.link2)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.link3)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.link4)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
