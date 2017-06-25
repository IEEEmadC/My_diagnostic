package db;

import android.content.Context;
import android.database.Cursor;


/**
 * Created by admin on 3/6/17.
 */

public class Database {

    private DBHelper dbHelper;

    public Database(Context context) {
        dbHelper = new DBHelper(context, "mydiagnostic", null, 1);
    }
    Cursor getDatabaseVersion() {
        return dbHelper.getReadableDatabase().rawQuery(
                "select id_version as id_disease , version, To_date as date from database_version"
                , null);
    }
    public Cursor getDiseases_category(){
        return dbHelper.getReadableDatabase().rawQuery(
                "select dc.id_disease_category ,dc.category_name ,dc.category_description ," +
                        "COUNT(d.id_diseases) from diseases_category dc, diseases d " +
                        "where dc.id_disease_category=d.id_disease_category group by d.id_diseases"
                ,null);
    }
    public Cursor getDiseases(){
        return dbHelper.getReadableDatabase().rawQuery(
                "select d.id_diseases as id_disease ,d.name_disease as name_disease ,d.description as description," +
                "COUNT(sd.id_diseases) as symptons from diseases d, symptoms_diseases sd " +
                "where d.id_diseases=sd.id_diseases group by sd.id_diseases"
                ,null);
    }
    public Cursor getDiseaseSymptoms(String id_disease) {
        return dbHelper.getReadableDatabase().rawQuery("select s.id_symptom as id_disease,s.symptom as name_disease " +
                "from symptoms_diseases sd, symptoms s where sd.id_symptom=s.id_symptom and " +
                "sd.id_diseases=?"
                ,new String[]{id_disease});
    }
    public Cursor getSymptoms() {
        return dbHelper.getReadableDatabase().rawQuery(
                "select id_symptom as id_disease , symptom as name_disease from symptoms"
                , null);
    }
    public Cursor getDiseasesSymptoms() {
        return dbHelper.getReadableDatabase().rawQuery(
                "select * from symptoms_diseases"
                , null);
    }
    void saveSymptom(String id, String symptom) {
        dbHelper.getWritableDatabase().execSQL(String.format("insert into symptoms values('%s','%s')", id, symptom));
    }
    void saveDisease(String id, String name,String description,String id_category) {
        dbHelper.getWritableDatabase().execSQL(String.format("insert into diseases values('%s','%s','%s','%s')", id, name,description,id_category));
    }
    void saveDiseaseCategory(String id, String name,String description) {
        dbHelper.getWritableDatabase().execSQL(String.format("insert into diseases_category values('%s','%s','%s')", id, name,description));
    }
    void saveSymptomDisease(String id, String id_disease,String id_symptom) {
        dbHelper.getWritableDatabase().execSQL(String.format("insert into symptoms_diseases values('%s','%s','%s')", id, id_disease,id_symptom));
    }
    void deleteData(){
        dbHelper.getWritableDatabase().execSQL("delete from symptoms");
        dbHelper.getWritableDatabase().execSQL("delete from User_system");
        dbHelper.getWritableDatabase().execSQL("delete from medicalhistory");
        dbHelper.getWritableDatabase().execSQL("delete from symptoms_diseases");
        dbHelper.getWritableDatabase().execSQL("delete from diseases");
        dbHelper.getWritableDatabase().execSQL("delete from diseases_category");
        dbHelper.getWritableDatabase().execSQL("delete from country");
        dbHelper.getWritableDatabase().execSQL("delete from allergies");
        dbHelper.getWritableDatabase().execSQL("delete from bloodtype");
        dbHelper.getWritableDatabase().execSQL("delete from database_version");
    }
    /*public void saveUser(SingleUser user){
        //dbHelper.getWritableDatabase().insertWithOnConflict();
    }*/
    public DBHelper getDbHelper() {
        return dbHelper;
    }

}
