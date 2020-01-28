package in.jobs.wordict;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;

public class External extends AppCompatActivity {

    Button button ;
    Intent intent ;
    String url;
    Uri uri;
    DBhelper dBhelper;
    SQLiteDatabase sqldatabase;
    TextView file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external);

        button = (Button)findViewById(R.id.button) ;
        file = (TextView)findViewById(R.id.file);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    //String PathHolder = data.getData().toString();

                    //Toast.makeText(External.this, PathHolder , Toast.LENGTH_LONG).show();

                    uri = data.getData();

                    StringBuilder stringBuilder = new StringBuilder();
                    try (InputStream inputStream =
                                 getContentResolver().openInputStream(uri);
                         BufferedReader reader = new BufferedReader(
                                 new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(External.this, "Denied", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(External.this, "Not There", Toast.LENGTH_LONG).show();
                    }

                    url = uri.toString();
                    //  Toast.makeText(External.this, stringBuilder.toString() , Toast.LENGTH_LONG).show();

                    // file.setText(url);


                    final String JSON_DATA = stringBuilder.toString();


                    try {
                        final JSONObject obj = new JSONObject(JSON_DATA);
                        final JSONArray geodata = obj.getJSONArray("words");
                        final int n = geodata.length();
                        for (int i = 0; i < n; ++i) {
                            final JSONObject person = geodata.getJSONObject(i);
                            String english,ant,syn,def,hindi;
                            ant = person.getString("Antonyms");
                            def = person.getString("Defenition");
                            english = person.getString("English");
                            hindi = person.getString("Hindi");
                            syn = person.getString("Synonyms");




                            boolean isinserted = DBhelper.getInstance(getApplicationContext()).insert(english,hindi,def,ant,syn);
                            if (isinserted==true)
                                Toast.makeText(this,"Database Created",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(this,"Error in creating database",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    sqldatabase.close();
                    break;

                }
        }

    }
}