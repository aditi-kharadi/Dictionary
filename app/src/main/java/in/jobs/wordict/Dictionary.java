package in.jobs.wordict;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

import static in.jobs.wordict.History.PREFS;


public class Dictionary extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView m,d,s,a;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;
    Toolbar toolbar;
    // TabLayout tabLayout;

    static int i=0;

    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> arrayList;
    DBhelper dBhelper;
    DBHistory history;
    DataBase db;
    SQLiteDatabase sqldatabase;

    TextView meaning;
    String word;
    String mean,def,ant,syn;
    CheckBox save;

    Boolean share=false;
    Boolean his = History.getData();

    Context context;

    Calendar calander;
    SimpleDateFormat simpledateformat;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();

                shareIt();


            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        save = (CheckBox) findViewById(R.id.save);
        meaning = (TextView)findViewById(R.id.meaning);


        m = (TextView)findViewById(R.id.m);
        d = (TextView)findViewById(R.id.d);
        s = (TextView)findViewById(R.id.s);
        a = (TextView)findViewById(R.id.a);




        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = simpledateformat.format(calander.getTime());



        dBhelper = new DBhelper(this);
        sqldatabase = dBhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
/*if(i==0) {
    values.put(dBhelper.KeyName2, "Abcdefghi");
    //    sqldatabase.insert(DBhelper.Table_Name , null ,values);
    values.put(dBhelper.KeyName3, "ujdfm");
    //   sqldatabase.insert(DBhelper.Table_Name , null ,values);
    values.put(dBhelper.KeyName4, "njsae");
    //  sqldatabase.insert(DBhelper.Table_Name , null ,values);
    values.put(dBhelper.KeyName5, "jkhjhui");
    //  sqldatabase.insert(DBhelper.Table_Name , null ,values);
    values.put(dBhelper.KeyName6, "fhfgh");


    sqldatabase.insert(DBhelper.Table_Name, null, values);

    //Toast.makeText(this, i , Toast.LENGTH_LONG).show();
    dBhelper.deleteDuplicates();
    sqldatabase.close();
i++;
}*/
        arrayList = new ArrayList<>();
       /* dBhelper = new DBhelper(this, 1, "Abcde.db");
        dBhelper.close();
        try {
            dBhelper.openDatabase();
        }catch (Exception e){}
        try {
            dBhelper.createDatabase();
        }
        catch (Exception e){}
*/
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.searc);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1){
                    arrayList.addAll(dBhelper.GetAllWords(s.toString()));
                    Set<String> set = new LinkedHashSet<>();

                    // Add the elements to set 
                    set.addAll(arrayList);
                    arrayList.clear();

                    // add the elements of set
                    // with no duplicates to the list
                    arrayList.addAll(set);



                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(Dictionary.this,
                            android.R.layout.simple_list_item_1 , arrayList));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                word = (String) parent.getItemAtPosition(position);
                save.setChecked(false);
                search(view);
            }
        });



        viewPager = (ViewPager)findViewById(R.id.fragment);
        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);

     //   tabLayout = (TabLayout) findViewById(R.id.tabs);
       // tabLayout.setupWithViewPager(viewPager);


        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(3);
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {

                OnChangeTab(position);
            }




            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void OnChangeTab(int position) {

        if(position == 0)
        {
            m.setTextSize(20);
            m.setBackgroundColor(getColor(R.color.black));
            m.setTextColor(getColor(R.color.white));
            d.setTextSize(15);
            d.setBackgroundColor(getColor(R.color.light));
            d.setTextColor(getColor(R.color.black));
            s.setTextSize(15);
            s.setBackgroundColor(getColor(R.color.light));
            s.setTextColor(getColor(R.color.black));
            a.setTextSize(15);
            a.setBackgroundColor(getColor(R.color.light));
            a.setTextColor(getColor(R.color.black));


            TextView text = (TextView) findViewById(R.id.meaning);
            text.setText("\u2022 "+mean);

        }
        if(position == 1)
        {
            m.setTextSize(15);
            m.setBackgroundColor(getColor(R.color.light));
            m.setTextColor(getColor(R.color.black));
            d.setTextSize(20);
            d.setBackgroundColor(getColor(R.color.black));
            d.setTextColor(getColor(R.color.white));
            s.setTextSize(15);
            s.setBackgroundColor(getColor(R.color.light));
            s.setTextColor(getColor(R.color.black));
            a.setTextSize(15);
            a.setBackgroundColor(getColor(R.color.light));
            a.setTextColor(getColor(R.color.black));

            TextView text = (TextView) findViewById(R.id.defenition);
            text.setText("\u2022 "+def);
        }
        if(position == 2)
        {
            m.setTextSize(15);
            m.setBackgroundColor(getColor(R.color.light));
            m.setTextColor(getColor(R.color.black));
            d.setTextSize(15);
            d.setBackgroundColor(getColor(R.color.light));
            d.setTextColor(getColor(R.color.black));
            s.setTextSize(20);
            s.setBackgroundColor(getColor(R.color.black));
            s.setTextColor(getColor(R.color.white));
            a.setTextSize(15);
            a.setBackgroundColor(getColor(R.color.light));
            a.setTextColor(getColor(R.color.black));

            TextView text = (TextView) findViewById(R.id.synonyms);
            text.setText("\u2022 "+syn);
        }
        if(position == 3)
        {
            m.setTextSize(15);
            m.setBackgroundColor(getColor(R.color.light));
            m.setTextColor(getColor(R.color.black));
            d.setTextSize(15);
            d.setBackgroundColor(getColor(R.color.light));
            d.setTextColor(getColor(R.color.black));
            s.setTextSize(15);
            s.setBackgroundColor(getColor(R.color.light));
            s.setTextColor(getColor(R.color.black));
            a.setTextSize(20);
            a.setBackgroundColor(getColor(R.color.black));
            a.setTextColor(getColor(R.color.white));

            TextView text = (TextView) findViewById(R.id.antonyms);
            text.setText("\u2022 "+ant);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_saved) {
            Intent i = new Intent(this , saved_words.class);
            startActivity(i);

        } else if (id == R.id.nav_history) {
            Intent i = new Intent(this , History.class);
            startActivity(i);

        }
         else if (id == R.id.nav_share) {
             share();
        }
        else if (id == R.id.nav_upload) {
            Intent i = new Intent(this , External.class);
            startActivity(i);

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void shareIt() {

        if(share==true) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = word + "\n\n" + "Meaning:\n\u2022" + mean + "\n\n" + "Defenition:\n\u2022" + def +
                    "\n\n" + "Synonyms:\n\u2022" + syn + "\n\n" + "Antonyms:\n\u2022" + ant;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Word");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else{
            Toast.makeText(this,"nothing to share",Toast.LENGTH_LONG).show();
        }

    }
    private void share()
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Wordict: a English to Hindi Dictionary";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dictionary");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    public void SaveWord(View view)
    {
        if(save.isChecked())
        {
          //  db = new DataBase(this);
            //db.addData(word);
           /* db = new DataBase(this);
            sqldatabase = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(db.KeyName1 , word);

            sqldatabase.insert(DataBase.TableName , null ,values);
            sqldatabase.close();*/


            db = new DataBase(this);

                db.addData(word);
                Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void search(View view)
    {
        share=true;
        InputMethodManager inputManager = (InputMethodManager)

                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        mean = dBhelper.GetMean(word);
        mean = mean.replaceAll(",","\n\u2022");

        def = dBhelper.GetDef(word);
        def = def.replaceAll(",","\n\u2022");
        def = def.replaceAll(";","\n\n");
        def = def.replaceAll(":","\n");

        ant = dBhelper.GetAnt(word);
        ant = ant.replaceAll(",","\n\u2022");

        syn = dBhelper.GetSyn(word);
        syn = syn.replaceAll(",","\n\u2022");

        viewPager.setCurrentItem(0);

        TextView text = (TextView) findViewById(R.id.meaning);
        text.setText("\u2022 "+mean);



        SharedPreferences sharedPreferences = getSharedPreferences(PREFS,0);
        boolean test = sharedPreferences.getBoolean("your_key", false);

       if(test==false) {

           history = new DBHistory(this);
           history.addData(word+"\n                  ("+date+")");

       }
    }

}
