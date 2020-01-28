package in.jobs.wordict;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Show extends AppCompatActivity {

    TextView word,meaning,defenition,synonyms,antonyms;
    String wrd,mean,def,syn,ant;

    DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        word = (TextView) findViewById(R.id.word);
        meaning = (TextView) findViewById(R.id.txt1);
        defenition = (TextView) findViewById(R.id.txt2);
        synonyms = (TextView) findViewById(R.id.txt3);
        antonyms = (TextView) findViewById(R.id.txt4);

        wrd=getIntent().getExtras().getString("WORDD");
        word.setText(wrd);

        dBhelper = new DBhelper(this);

        mean = dBhelper.GetMean(wrd);
       mean = mean.replaceAll(",","\n\u2022");

        def = dBhelper.GetDef(wrd);
        def = def.replaceAll(",","\n\u2022");
        def = def.replaceAll(";","\n\n");
        def = def.replaceAll(":","\n");

        syn = dBhelper.GetSyn(wrd);
        syn = syn.replaceAll(",","\n\u2022");

        ant = dBhelper.GetAnt(wrd);
        ant = ant.replaceAll(",","\n\u2022");


        meaning.setText("\u2022 "+ mean);
        defenition.setText("\u2022 "+ def);
        antonyms.setText("\u2022 "+ ant);
        synonyms.setText("\u2022 "+ syn);


    }
}
