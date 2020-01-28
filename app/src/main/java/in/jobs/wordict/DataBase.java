package in.jobs.wordict;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    public static String DatabaseName = "DICTIONARY";
    public static String TableName = "words";
    public static String KeyName0 = "ID";
    public static String KeyName1 = "name";

    public DataBase(Context context) {
        super(context, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CT = "CREATE TABLE " + TableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT)";
        sqLiteDatabase.execSQL(CT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addData(String item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KeyName1,item);

        long result = db.insert(TableName,null,contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    public boolean delete(String word)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TableName, KeyName1 + "=?", new String[]{word}) > 0;
    }
    public Cursor getList()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(" SELECT * FROM " + TableName , null);
        return data;
    }
}
