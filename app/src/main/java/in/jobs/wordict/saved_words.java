package in.jobs.wordict;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class saved_words extends AppCompatActivity{

    DataBase db;
    ListView listView;
    String name;
    ArrayAdapter obj2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_words);

        listView = (ListView) findViewById(R.id.list);



        db = new DataBase(this);
        final ArrayList<String> arrayList = new ArrayList<>();
        final Cursor data = db.getList();
        if (data.getCount() == 0) {
            Toast.makeText(this, "List is Empty", Toast.LENGTH_LONG).show();
            ;
        } else {
            while (data.moveToNext()) {
                if (!arrayList.contains(data.getString(1))) {
                    arrayList.add(data.getString(1));
                }
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(listAdapter);
            }
        }

        obj2 = new ArrayAdapter(saved_words.this
                , android.R.layout.select_dialog_item, arrayList);
        listView.setAdapter(obj2);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                name = (String) listView.getItemAtPosition(i);
                Intent a = new Intent(saved_words.this, Show.class);
                a.putExtra("WORDD", name);
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

                db = new DataBase(saved_words.this);
                db.delete(name);

                Toast.makeText(saved_words.this, "Word Deleted", Toast.LENGTH_LONG).show();

                return true;
            }

        });


    }

}