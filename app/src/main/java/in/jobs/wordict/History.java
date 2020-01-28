package in.jobs.wordict;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    ListView listView;
    Switch sw;
    String name;
    DBHistory db;
    ArrayAdapter obj2;
    String word,abc;
    public static boolean his = true;

    SharedPreferences sharedPrefs;
    public static final String PREFS = "examplePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        listView = (ListView) findViewById(R.id.list);

       sw = (Switch) findViewById(R.id.off);

        final SharedPreferences examplePrefs = getSharedPreferences(PREFS,0);
        final SharedPreferences.Editor editor = examplePrefs.edit();
        sw.setChecked(examplePrefs.getBoolean("your_key", false));   //false default value.


        //his = sharedPrefs.getBoolean("History", true);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("your_key", true);
                    editor.commit();
                    Toast.makeText(History.this, "Search History turned OFF", Toast.LENGTH_LONG).show();
                }
                else{
                    editor.putBoolean("your_key", false);
                    editor.commit();
                    Toast.makeText(History.this, "Search History turned ON", Toast.LENGTH_LONG).show();
                }

               /* if (isChecked) {
                    // The toggle is enabled

                    Toast.makeText(History.this, "Search History is turned OFF", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(History.this, "Search History is turned ON", Toast.LENGTH_LONG).show();

                    // The toggle is disabled
                }*/
            }
        });

      /*  sqldatabase = new DataBase(this);
        db = sqldatabase.getWritableDatabase();*/



           // Toast.makeText(this, "What", Toast.LENGTH_LONG).show();
          db = new DBHistory(this);
          final ArrayList<String> arrayList = new ArrayList<>();
          final Cursor data = db.getList();
          if (data.getCount() == 0) {
              Toast.makeText(this, "List is Empty", Toast.LENGTH_LONG).show();
              ;
          } else {
              while (data.moveToNext()) {
                  arrayList.add(data.getString(1));
                  ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                  listView.setAdapter(listAdapter);
              }


          obj2 = new ArrayAdapter(History.this
                  , android.R.layout.select_dialog_item, arrayList);
          listView.setAdapter(obj2);
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                  name = (String) listView.getItemAtPosition(i);
                  Intent a = new Intent(History.this, Show.class);

                  if(name.contains(" ")){
                      name= name.substring(0, name.indexOf(" "));
                      abc = name;
                  }


                  a.putExtra("WORDD", abc);
                  startActivity(a);

              }
          });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               final int position, long id) {
                    // TODO Auto-generated method stub
                    name = (String) listView.getItemAtPosition(position);



                    obj2.remove(obj2.getItem(position));
                    obj2.notifyDataSetChanged();

                    db = new DBHistory(History.this);
                    db.delete(name);

                    Toast.makeText(History.this, "Word Deleted", Toast.LENGTH_LONG).show();

                    return true;
                }

            });
      }
    }
    public static Boolean getData() {return his;}
}
