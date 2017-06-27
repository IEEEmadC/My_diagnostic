package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


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
                "d.id_disease_category , dc.category_name ,COUNT(sd.id_diseases) as symptons from diseases d, symptoms_diseases sd , diseases_category dc " +
                "where d.id_diseases=sd.id_diseases and d.id_disease_category=dc.id_disease_category group by sd.id_diseases"
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
    public Cursor getCountry() {
        return dbHelper.getReadableDatabase().rawQuery(
                "select * from country"
                , null);
    }
    public Cursor getBloodType() {
        return dbHelper.getReadableDatabase().rawQuery(
                "select * from bloodtype"
                , null);
    }

    public Cursor getUsers(){
        return dbHelper.getReadableDatabase().rawQuery(
                "select * from User_system"
                , null);
    }
    public User getUser(String username) {
        User u;
        Cursor userCursor=  dbHelper.getReadableDatabase().rawQuery(
                "select u.username, u.fullname, u.genre, u.birthday, u.id_country, u.id_bloodtype , " +
                "c.name_country, b.bloodtype "+
                "from User_system u, country c, bloodtype b "+
                "where u.id_country=c.id_country and u.id_bloodtype=b.id_bloodtype and u.username=?"
                ,new String[]{username});
        if(userCursor.moveToFirst()){
            if(userCursor.getCount()>0){
                u = new User(
                        userCursor.getString(0),
                        userCursor.getString(1),
                        userCursor.getString(2),
                        userCursor.getString(3),
                        userCursor.getString(4),
                        userCursor.getString(5),
                        userCursor.getString(6),
                        userCursor.getString(7)
                );
                return u;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    public void saveUser(User user) {
        ContentValues initialValues = new ContentValues();
        initialValues.put("username", user.getUsername()); // the execution is different if _id is 2
        initialValues.put("fullname",user.getFullname());
        initialValues.put("genre",user.getGenre());
        initialValues.put("birthday",user.getBirthday());
        initialValues.put("id_country",user.getId_country());
        initialValues.put("id_bloodtype",user.getId_bloodtype());
        initialValues.put("id_allergy","0");
        int id = (int) dbHelper.getWritableDatabase().insertWithOnConflict("User_system", null, initialValues, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Se actualizo user ","filas afectadas "+id);
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
    void saveCountry(String id, String name,String short_name) {
        dbHelper.getWritableDatabase().execSQL(String.format("insert into country values('%s','%s','%s')", id, name,short_name));
    }
    void saveBloodType(String id, String type) {
        dbHelper.getWritableDatabase().execSQL(String.format("insert into bloodtype values('%s','%s')", id, type));
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
