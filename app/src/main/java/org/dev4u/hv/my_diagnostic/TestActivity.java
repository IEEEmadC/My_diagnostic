package org.dev4u.hv.my_diagnostic;

import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import db.Database;
import db.Disease;
import db.LocalDatabase;
import db.Symptom;
import utils.AutoCompleteAdapter;
import utils.DiseaseUtilitesSingleton;

public class TestActivity extends AppCompatActivity {

    private LocalDatabase localDatabase;
    private Database db;
    private String symptoms[];
    Handler handler = new Handler();
    private AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.activity_test);
        //symptoms = new String[];

        // un comentario
        localDatabase = new LocalDatabase(this);


        ((Button)findViewById(R.id.btnTest)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });
        ((Button)findViewById(R.id.btnTest2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test2();
            }
        });
    }

    public void test(){
        localDatabase.deleteData();
        //localDatabase.getSymptoms();
        //localDatabase.getDisease();
        //localDatabase.getSymptomsDisease();
        localDatabase.setTextView( (TextView)findViewById(R.id.lblPrueba) );
        localDatabase.initDatabase();
        //como se ejecuta en un hilo hay que esperar a que haya terminado
    }

    public void test2(){
        DiseaseUtilitesSingleton.getInstance().init(this);
        new Thread(new Runnable() {
            public void run() {
                DiseaseUtilitesSingleton.getInstance().fillData();
                handler.post(new Runnable() {
                    public void run() {
                        llenarCompleter();
                    }
                });
            }
        }).start();
        //llenando los sintomas
    }

    private void llenarCompleter(){
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
                DiseaseUtilitesSingleton.getInstance().getSymptomsNames());
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.txtSymptom);
        autoCompleteTextView.setAdapter(adapter);
    }
}
