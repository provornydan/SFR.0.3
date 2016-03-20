package mazebug.sfr03;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SFR_Overview extends AppCompatActivity {
    DatabaseHelper data;  Menu menu;
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    ArrayList<String> arr;
    String title;
    String idName;
    TextView TitleSite;

    LinearLayout editLayout1, editLayout2, editLayout3, editLayout4, editLayout5, editLayout6, editLayout7, editLayout8;
    LinearLayout[] editLayouts;
    LinearLayout textLayout1, textLayout2, textLayout3, textLayout4, textLayout5, textLayout6, textLayout7, textLayout8;
    LinearLayout[] textLayouts;
    EditText edit1, edit2, edit3, edit4, edit5, edit6, edit7, edit8; EditText[] edits;
    TextView text1, text2, text3, text4, text5, text6, text7, text8; TextView[] texts;

    Cursor rs;
    Boolean create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new DatabaseHelper(this);
        create = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfr__overview);
        TitleSite =(TextView)findViewById(R.id.title);
        findElements();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent extras = getIntent();
        Bundle extrasBundle = extras.getExtras();
        if(extrasBundle!=null){ title= extrasBundle.getString("Get Site");
        if(extrasBundle.getString("The id")!=null) idName=extrasBundle.getString("The id");}

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TitleSite.setText(title);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SFR_Overview.this, MySFR.class));
            }
        });

       // getSupportActionBar().

        e1=(EditText)findViewById(R.id.etov1);
        e2=(EditText)findViewById(R.id.etov2);
        e3=(EditText)findViewById(R.id.etov3);
        e4=(EditText)findViewById(R.id.etov4);
        e5=(EditText)findViewById(R.id.etov5);
        e6=(EditText)findViewById(R.id.etov6);
        e7=(EditText)findViewById(R.id.etov7);
        e8=(EditText)findViewById(R.id.etov8);

        if(idName!=null){
            changeEditToText(edits, texts, textLayouts, editLayouts);
            rs=data.getIdData(idName);

            if(rs.getCount()>0){
                rs.moveToNext();}

            for(int i=0; i<edits.length; i++){
                edits[i].setText(rs.getString(i+1));
                texts[i].setText(rs.getString(i+1));
            }

            create = false;


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        this.menu=menu;
        if(idName!=null) {menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mysettings) {
            return true;
        }

        if (id == R.id.oursearch) {
            arr = new ArrayList<>();
            arr.add(e1.getText().toString());
            arr.add(e2.getText().toString());
            arr.add(e3.getText().toString());
            arr.add(e4.getText().toString());
            arr.add(e5.getText().toString());
            arr.add(e6.getText().toString());
            arr.add(e7.getText().toString());
            arr.add(e8.getText().toString());

            if(create){
            boolean a = data.insertData(arr);
            if (a)
                Toast.makeText(SFR_Overview.this, "Site added Successfully", Toast.LENGTH_LONG).show();
            else Toast.makeText(SFR_Overview.this, "Failed", Toast.LENGTH_LONG).show();
                create = false;
            }
            else{
                boolean a = data.updateDatabase(idName, returnText(edit1), returnText(edit2), returnText(edit3), returnText(edit4), returnText(edit5), returnText(edit6), returnText(edit7), returnText(edit8));
                if (a)
                    Toast.makeText(SFR_Overview.this, "Site updated Successfully", Toast.LENGTH_LONG).show();
                else Toast.makeText(SFR_Overview.this, "Failed", Toast.LENGTH_LONG).show();}


            changeEditToText(edits, texts, textLayouts, editLayouts);
            editLayout1.setVisibility(View.INVISIBLE);
            textLayout1.setVisibility(View.VISIBLE);
            text1.setText(edit1.getText());

            {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(true);
                //startActivity(new Intent(SFR_Overview.this, MySFR.class));
            }
            TitleSite.setText(text1.getText().toString());
        }

            if (id == R.id.eidtPencil) {
                changeTextToEdit(texts, edits, editLayouts, textLayouts);
                menu.getItem(1).setVisible(false);
                menu.getItem(0).setVisible(true);
            }

            return super.onOptionsItemSelected(item);
        }

    public void changeEditToText(EditText[] edit, TextView[] text, LinearLayout[] textLayouts, LinearLayout[] editLayouts){
        for(int i=0; i<textLayouts.length; i++){
            editLayouts[i].setVisibility(View.INVISIBLE);
            textLayouts[i].setVisibility(View.VISIBLE);
        }
        for(int i=0; i<edit.length; i++){
            text[i].setText(edit[i].getText());
        }
    }

    public void changeTextToEdit(TextView[] text, EditText[] edit, LinearLayout[] editLayouts, LinearLayout[] textLayouts){
        for(int i=0; i<textLayouts.length; i++){
            textLayouts[i].setVisibility(View.INVISIBLE);
            editLayouts[i].setVisibility(View.VISIBLE);
        }
        for(int i=0; i<edit.length; i++){
            edit[i].setText(text[i].getText());
        }
    }

    public void findElements(){
        editLayout1 = (LinearLayout)findViewById(R.id.LL1_1);
        editLayout2 = (LinearLayout)findViewById(R.id.LL2_1);
        editLayout3 = (LinearLayout)findViewById(R.id.LL3_1);
        editLayout4 = (LinearLayout)findViewById(R.id.LL4_1);
        editLayout5 = (LinearLayout)findViewById(R.id. LL5_1);
        editLayout6 = (LinearLayout)findViewById(R.id. LL6_1);
        editLayout7 = (LinearLayout)findViewById(R.id. LL7_1);
        editLayout8 = (LinearLayout)findViewById(R.id. LL8_1);
        editLayouts = new LinearLayout[] {editLayout1, editLayout2, editLayout3, editLayout4, editLayout5, editLayout6, editLayout7, editLayout8 };

        textLayout1 = (LinearLayout)findViewById(R.id.LL1_2);
        textLayout2 = (LinearLayout)findViewById(R.id.LL2_2);
        textLayout3 = (LinearLayout)findViewById(R.id.LL3_2);
        textLayout4 = (LinearLayout)findViewById(R.id.LL4_2);
        textLayout5 = (LinearLayout)findViewById(R.id.LL5_2);
        textLayout6 = (LinearLayout)findViewById(R.id.LL6_2);
        textLayout7 = (LinearLayout)findViewById(R.id.LL7_2);
        textLayout8 = (LinearLayout)findViewById(R.id.LL8_2);
        textLayouts = new LinearLayout[] {textLayout1, textLayout2, textLayout3, textLayout4, textLayout5, textLayout6, textLayout7, textLayout8};

        edit1 = (EditText)findViewById(R.id.etov1);
        edit2 = (EditText)findViewById(R.id.etov2);
        edit3 = (EditText)findViewById(R.id.etov3);
        edit4 = (EditText)findViewById(R.id.etov4);
        edit5 = (EditText)findViewById(R.id.etov5);
        edit6 = (EditText)findViewById(R.id.etov6);
        edit7 = (EditText)findViewById(R.id.etov7);
        edit8 = (EditText)findViewById(R.id.etov8);
        edits = new EditText[] {edit1, edit2,edit3, edit4, edit5, edit6, edit7, edit8};


        text1 = (TextView)findViewById(R.id.tvov1_2);
        text2 = (TextView)findViewById(R.id.tvov2_2);
        text3 = (TextView)findViewById(R.id.tvov3_2);
        text4 = (TextView)findViewById(R.id.tvov4_2);
        text5 = (TextView)findViewById(R.id.tvov5_2);
        text6 = (TextView)findViewById(R.id.tvov6_2);
        text7 = (TextView)findViewById(R.id.tvov7_2);
        text8 = (TextView)findViewById(R.id.tvov8_2);
        texts = new TextView[] {text1, text2, text3, text4, text5, text6, text7, text8};

    }
    public String returnText (EditText edit) {
        String a = edit.getText().toString();
        return a;
    }

    public void addOption(View view) {
        LayoutInflater inflater = (LayoutInflater)this.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll =(LinearLayout)findViewById(R.id.LLOV1);
        View view1 = inflater.inflate(R.layout.content_option, null,false);
        ll.addView(view1);
    }
}
