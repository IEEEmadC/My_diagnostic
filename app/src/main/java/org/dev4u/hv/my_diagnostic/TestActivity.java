package org.dev4u.hv.my_diagnostic;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import db.Database;
import db.LocalDatabase;

public class TestActivity extends AppCompatActivity {

    private LocalDatabase localDatabase;
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.activity_test);

        // un comentario
        localDatabase = new LocalDatabase(this);


        ((Button)findViewById(R.id.btnTest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
    }

    public void test(){
        localDatabase.deleteData();
        localDatabase.getSymptoms();
        //como se ejecuta en un hilo hay que esperar a que haya terminado
    }
}
