package in.jobs.wordict;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.emrekose.copyexternaldb.CopyDatabase;

import java.util.ArrayList;

/*public class DBhelper extends CopyDatabase {

    String Table_Name = "Abcde";
    String ColumnName = "English";
    Context mcontext;

    public DBhelper(Context context, int version, String databaseName) {
        super(context, version, databaseName);
        mcontext = context;
    }


    */


    public class DBhelper extends SQLiteOpenHelper {

        private static DBhelper sInstance;

    public static String DatabaseName = "Abcde";
    public static String Table_Name = "Abcde";
    public static String KeyName2 = "English";
    public static String KeyName3 = "Hindi";
    public static String KeyName4 = "Defenition";
    public static String KeyName5 = "Antonyms";
    public static String KeyName6 = "Synonyms";



    public DBhelper(Context context) {
        super(context, DatabaseName, null, 4);
    }

        public static synchronized DBhelper getInstance(Context context) {

        if (sInstance==null){
            sInstance = new DBhelper((context.getApplicationContext()));
        }
        return sInstance;
        }

        @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CT = "CREATE TABLE " + Table_Name + "(" + KeyName2 + " TEXT," + KeyName3 + " TEXT," + KeyName4 + " TEXT," + KeyName5 + " TEXT," + KeyName6 + " TEXT)";
        sqLiteDatabase.execSQL(CT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }



    public ArrayList<String> GetAllWords(String query){

        ArrayList<String> arrayList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

       Cursor cursor = sqLiteDatabase.query(
                Table_Name,
                new String[] {KeyName2},
               KeyName2 + " LIKE ?",
                new String[] {query + "%"},
                null ,null,null);
      //  Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM Dictionary", null);
     //   cursor.moveToFirst();

        int index = cursor.getColumnIndex(KeyName2);

        while(cursor.moveToNext()){
            arrayList.add(cursor.getString(index));

        }
        return arrayList;
    }


    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(oldVersion);
    }

    public String GetMean(String word)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String mean = null;
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + Table_Name + " where " + KeyName2 + "=  '"+word+"'", null );

        while (cursor.moveToNext())
        {
            mean = cursor.getString(cursor.getColumnIndex("Hindi"));
        }
        return mean;
    }

    public String GetDef(String word)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String def = null;
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + Table_Name + " where " + KeyName2 + "=  '"+word+"'", null );

        while (cursor.moveToNext())
        {
            def = cursor.getString(cursor.getColumnIndex("Defenition"));
        }
        return def;
    }

    public String GetAnt(String word)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String ant = null;
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + Table_Name + " where " + KeyName2 + "=  '"+word+"'", null );

        while (cursor.moveToNext())
        {
            ant = cursor.getString(cursor.getColumnIndex("Antonyms"));
        }
        return ant;
    }

    public String GetSyn(String word)
    {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String syn = null;
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + Table_Name + " where " + KeyName2 + "=  '"+word+"'", null );

        while (cursor.moveToNext())
        {
            syn = cursor.getString(cursor.getColumnIndex("Synonyms"));
        }
        return syn;
    }




        public void deleteDuplicates(){
            getWritableDatabase().execSQL("delete from Abcde where English not in (SELECT MIN(English ) FROM Abcde GROUP BY Hindi)");
        }


        public boolean insert(String english, String hindi, String defenition, String antonyms, String synonyms)
        {
            SQLiteDatabase db =  this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KeyName2 , english);
            values.put(KeyName3 , hindi);
            values.put(KeyName4 , defenition);
            values.put(KeyName5 , antonyms);
            values.put(KeyName6 , synonyms);

            long result = db.insert(Table_Name, null, values);
            if(result==-1)
                return false;
            else
                return true;
        }





}
