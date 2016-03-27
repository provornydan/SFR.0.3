package mazebug.sfr03;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class Choice_new extends AppCompatActivity {
    DatabaseHelper data = new DatabaseHelper(this);
    ImageView saveIt;
    String idName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting the Toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Choice_new.this, SFR_Overview.class);
                intent.putExtra("The id", idName);
                intent.putExtra("From Option", false);
                startActivity(intent);
            }
        });

        //Get the id from SFROverview
        Intent extras = getIntent();
        Bundle extrasBundle = extras.getExtras();
        if(extrasBundle!=null){
            if(extrasBundle.getString("The id")!=null) idName=extrasBundle.getString("The id");}

        //Send it back and check if SFROverview is after Choice_New
        saveIt = (ImageView)findViewById(R.id.ivcn1);
        saveIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.insertAnOption(idName, "ByeBye");
                Intent intent = new Intent(Choice_new.this, SFR_Overview.class);
                intent.putExtra("The id", idName);
                intent.putExtra("From Option", true);
                startActivity(intent);
            }
        });
    }
    public void siteInformation(View view){
        //Intent intent = new Intent(Choice_new.this, Option_detailsCN.class);
        //startActivity(intent);
    }
}
