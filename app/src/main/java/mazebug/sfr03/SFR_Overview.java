package mazebug.sfr03;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
    RelativeLayout relMain;

    LinearLayout editLayout1, editLayout2, editLayout3, editLayout4, editLayout5, editLayout6, editLayout7, editLayout8, editLayout9, editLayout10, editLayout11, editLayout12, editLayout13;
    LinearLayout[] editLayouts, secondeditLayouts;
    LinearLayout textLayout1, textLayout2, textLayout3, textLayout4, textLayout5, textLayout6, textLayout7, textLayout8, textLayout9, textLayout10, textLayout11, textLayout12, textLayout13;
    LinearLayout[] textLayouts, secondTextLayouts;
    RelativeLayout r1, r2, r3, r4, r5, r6, r7, r8;
    EditText edit1, edit2, edit3, edit4, edit5, edit6, edit7, edit8, edit9, edit10, edit11, edit12, edit13; EditText[] edits, secondedits;
    TextView text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12, text13; TextView[] texts, secondtexts;
    ImageView plusImage;    View view1;


    LinearLayout horizontal;

    ArrayList<TextView> options = new ArrayList<>();
    ArrayList<String> OptionNames = new ArrayList<>();
    ArrayList<String> OptionID = new ArrayList<>();
    ArrayList<String> OptionTown = new ArrayList<>();
    ArrayList<String> OptionCounty= new ArrayList<>();
    ArrayList<String> OptionPostCode = new ArrayList<>();
    ArrayList<String> OptionHeight= new ArrayList<>();
    TextView tvov, tvo;
    LinearLayout optionLayout, linearDetails;
    Boolean show = true; int n=0;
    EditText lastChance;

    ScrollView scrollmain, scrollOption;
    HorizontalScrollView hscroll;
    Cursor rs;
    Cursor optionCursor;
    Boolean create;

    int alg = -1;
    Bundle extrasBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new DatabaseHelper(this);
        create = true;


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfr__overview);
        TitleSite =(TextView)findViewById(R.id.title);
        addOption();
        findElements();
        lastChance=(EditText)view1.findViewById(R.id.eto3_1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvov =(TextView)findViewById(R.id.tvov1);
        scrollmain=(ScrollView)findViewById(R.id.SVOV1);
        hscroll = (HorizontalScrollView)findViewById(R.id.SVOV2);
        relMain = (RelativeLayout)findViewById(R.id.relMain);

        Intent extras = getIntent();
        extrasBundle = extras.getExtras();
        if(extrasBundle!=null){
        if(extrasBundle.getString("Get Site")!=null)    title= extrasBundle.getString("Get Site");
            else title = edit1.getText().toString();
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

            optionCursor = data.getAllOptions(idName);
            while(optionCursor.moveToNext()){
                TextView texts = new TextView(this);
                texts.setText("Option " + Integer.toString(optionCursor.getPosition() + 1));

                int textSize =5;
                int textInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());


                texts.setTypeface(Typeface.DEFAULT);
                texts.setTextColor(Color.parseColor("#999999"));
                LinearLayout.LayoutParams lay2 = new  LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
                texts.setGravity(Gravity.CENTER);horizontal.setGravity(Gravity.CENTER_VERTICAL);

                int sizeImage = 20;
                int imageInDp = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, sizeImage, getResources().getDisplayMetrics());
                lay2.setMargins(imageInDp, 0, 0, 0);

                horizontal.addView(texts, lay2);

                title=text1.getText().toString();
                TitleSite.setText(title);

                options.add(texts);
                OptionNames.add(optionCursor.getString(1));
                OptionID.add(optionCursor.getString(0));
                OptionTown.add(optionCursor.getString(2));
                OptionCounty.add(optionCursor.getString(3));
                OptionPostCode.add(optionCursor.getString(4));
                OptionHeight.add(optionCursor.getString(5));

            }


            for(int i=0; i<options.size(); i++){
                final int ord = i;
                options.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollmain.pageScroll(View.FOCUS_UP);
                        hideGeneral();
                        EditText optionsname = (EditText)view1.findViewById(R.id.eto1);
                        if(alg!=-1){
                            OptionNames.set(alg, optionsname.getText().toString() );
                            OptionTown.set(alg, edit10.getText().toString());
                            OptionCounty.set(alg, edit11.getText().toString());
                            OptionHeight.set(alg, edit12.getText().toString());
                            OptionPostCode.set(alg, edit13.getText().toString());
                        }

                        optionsname.setText(OptionNames.get(ord));
                        tvo.setText(OptionNames.get(ord));
                        edit10.setText(OptionTown.get(ord));
                        text10.setText(OptionTown.get(ord));
                        edit11.setText(OptionCounty.get(ord));
                        text11.setText(OptionCounty.get(ord));
                        edit12.setText(OptionHeight.get(ord));
                        text12.setText(OptionHeight.get(ord));
                        edit13.setText(OptionPostCode.get(ord));
                        text13.setText(OptionPostCode.get(ord));

                        resetColors();

                        options.get(ord).setTextColor(Color.WHITE);
                        options.get(ord).setPaintFlags(options.get(ord).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        alg=ord;
                    }
                });
            }


            tvov.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alg=-1;
                    scrollmain.fullScroll(View.FOCUS_UP);
                    showGeneral();
                    resetColors();
                    tvov.setTextColor(Color.WHITE);
                    tvov.setPaintFlags(tvov.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
            });
        }

        //sWIPEtEST
        scrollmain.setOnTouchListener(new OnSwipeTouchListener(SFR_Overview.this) {

            public void onSwipeRight() {
                alg = -1;
                scrollmain.fullScroll(View.FOCUS_UP);
                showGeneral();
                resetColors();
                tvov.setTextColor(Color.WHITE);
                tvov.setPaintFlags(tvov.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            public void onSwipeLeft() {

                scrollmain.pageScroll(View.FOCUS_UP);
                hideGeneral();
                EditText optionsname = (EditText) view1.findViewById(R.id.eto1);
                if (alg != -1) {
                    OptionNames.set(alg, optionsname.getText().toString());
                    OptionTown.set(alg, edit10.getText().toString());
                    OptionCounty.set(alg, edit11.getText().toString());
                    OptionHeight.set(alg, edit12.getText().toString());
                    OptionPostCode.set(alg, edit13.getText().toString());
                }

                alg = 0;

                optionsname.setText(OptionNames.get(alg));
                tvo.setText(OptionNames.get(alg));
                edit10.setText(OptionTown.get(alg));
                text10.setText(OptionTown.get(alg));
                edit11.setText(OptionCounty.get(alg));
                text11.setText(OptionCounty.get(alg));
                edit12.setText(OptionHeight.get(alg));
                text12.setText(OptionHeight.get(alg));
                edit13.setText(OptionPostCode.get(alg));
                text13.setText(OptionPostCode.get(alg));

                resetColors();

                options.get(alg).setTextColor(Color.WHITE);
                options.get(alg).setPaintFlags(options.get(alg).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

        });




        scrollOption= (ScrollView)view1.findViewById(R.id.SVO1);
        tvo=(TextView)view1.findViewById(R.id.tvo9);

        scrollOption.setOnTouchListener(new OnSwipeTouchListener(SFR_Overview.this) {
            public void onSwipeRight() {



                    scrollmain.pageScroll(View.FOCUS_UP);
                    hideGeneral();
                    EditText optionsname = (EditText) view1.findViewById(R.id.eto1);
                    OptionNames.set(alg, optionsname.getText().toString());
                    OptionTown.set(alg, edit10.getText().toString());
                    OptionCounty.set(alg, edit11.getText().toString());
                    OptionHeight.set(alg, edit12.getText().toString());
                    OptionPostCode.set(alg, edit13.getText().toString());

                    alg--;
                    if(alg==-1){
                        scrollmain.fullScroll(View.FOCUS_UP);
                        showGeneral();
                        resetColors();
                        tvov.setTextColor(Color.WHITE);
                        tvov.setPaintFlags(tvov.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);}
                    optionsname.setText(OptionNames.get(alg));
                    tvo.setText(OptionNames.get(alg));
                    edit10.setText(OptionTown.get(alg));
                    text10.setText(OptionTown.get(alg));
                    edit11.setText(OptionCounty.get(alg));
                    text11.setText(OptionCounty.get(alg));
                    edit12.setText(OptionHeight.get(alg));
                    text12.setText(OptionHeight.get(alg));
                    edit13.setText(OptionPostCode.get(alg));
                    text13.setText(OptionPostCode.get(alg));

                    resetColors();

                    options.get(alg).setTextColor(Color.WHITE);
                    options.get(alg).setPaintFlags(options.get(alg).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            }

            public void onSwipeLeft() {
                if(alg<(optionCursor.getCount()));{
                scrollmain.pageScroll(View.FOCUS_UP);
                hideGeneral();
                EditText optionsname = (EditText) view1.findViewById(R.id.eto1);
                if (alg != -1) {
                    OptionNames.set(alg, optionsname.getText().toString());
                    OptionTown.set(alg, edit10.getText().toString());
                    OptionCounty.set(alg, edit11.getText().toString());
                    OptionHeight.set(alg, edit12.getText().toString());
                    OptionPostCode.set(alg, edit13.getText().toString());
                }
                alg++;
                optionsname.setText(OptionNames.get(alg));
                tvo.setText(OptionNames.get(alg));
                edit10.setText(OptionTown.get(alg));
                text10.setText(OptionTown.get(alg));
                edit11.setText(OptionCounty.get(alg));
                text11.setText(OptionCounty.get(alg));
                edit12.setText(OptionHeight.get(alg));
                text12.setText(OptionHeight.get(alg));
                edit13.setText(OptionPostCode.get(alg));
                text13.setText(OptionPostCode.get(alg));

                resetColors();

                options.get(alg).setTextColor(Color.WHITE);
                options.get(alg).setPaintFlags(options.get(alg).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);}
            }


        });



       /* optionLayout=(LinearLayout)view1.findViewById(R.id.LLO8);
        optionLayout.setVisibility(View.GONE);
        linearDetails= (LinearLayout)view1.findViewById(R.id.LLO7);
        linearDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                if((n%2)==1){
                    optionLayout.setVisibility(View.VISIBLE);
                    edit12.requestFocus();
                    //InputMethodManager imm = (InputMethodManager)getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                    //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                }
                if((n%2)==0){
                    optionLayout.setVisibility(View.GONE);
                }
            }
        }); */

        showGeneral();
        tvov.setTextColor(Color.WHITE);
        tvov.setPaintFlags(tvov.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
       if(extrasBundle!=null)   if(extrasBundle.getBoolean("From Option")==true){
            resetColors();
           if(!options.isEmpty()) {
               options.get((options.size() - 1)).setTextColor(Color.WHITE);
               options.get((options.size() - 1)).setPaintFlags(options.get((options.size() - 1)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
               hideGeneral();
               EditText optionsname = (EditText) view1.findViewById(R.id.eto1);
               optionCursor.moveToLast();
               optionsname.setText(optionCursor.getString(1));
               tvo.setText(optionCursor.getString(1));
               text10.setText(optionCursor.getString(1));
               text11.setText(optionCursor.getString(1));
               text12.setText(optionCursor.getString(1));
               text13.setText(optionCursor.getString(1));


               options.get((options.size() - 1)).requestFocus();
               hscroll.postDelayed(new Runnable() {
                   public void run() {
                       hscroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                   }
               }, 80L);
           }
           //menu.getItem(1).setVisible(false);
           //menu.getItem(0).setVisible(true);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu2, menu);
        this.menu=menu;
        if(idName!=null) {menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(true);}
        if(create) menu.getItem(0).setVisible(false);
        else menu.getItem(0).setVisible(true);
       /* if(extrasBundle!=null)   if(extrasBundle.getBoolean("From Option")==true) {
            menu.getItem(1).setVisible(false);
            menu.getItem(0).setVisible(true);

        }*/
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

             if(arr.get(0).isEmpty()) {
                 Toast.makeText(SFR_Overview.this, "Site Name is empty", Toast.LENGTH_LONG).show();
                 edit1.setText(text1.getText().toString());
             }
            else{
            if(create){
                String itsname2 =  arr.get(0);
                boolean a = data.insertData(arr);
            if (a)
                Toast.makeText(SFR_Overview.this, "Site added Successfully", Toast.LENGTH_LONG).show();
            else Toast.makeText(SFR_Overview.this, "Failed", Toast.LENGTH_LONG).show();
                create = false;
                menu.getItem(0).setVisible(true);
            }
            else{
                if(extrasBundle!=null)   if(extrasBundle.getBoolean("From Option")==true){   if(alg==-1) alg=0;
                    OptionNames.set(alg, edit9.getText().toString());
                    OptionTown.set(alg, edit10.getText().toString());
                    OptionCounty.set(alg, edit11.getText().toString());
                    OptionHeight.set(alg, edit12.getText().toString());
                    OptionPostCode.set(alg, edit13.getText().toString()); }
                    for(int i=0; i<OptionNames.size(); i++){
                        data.updateOption(OptionID.get(i), OptionNames.get(i), OptionTown.get(i), OptionCounty.get(i), OptionPostCode.get(i), OptionHeight.get(i));
                    }


                boolean a = data.updateDatabase(idName, returnText(edit1), returnText(edit2), returnText(edit3), returnText(edit4), returnText(edit5), returnText(edit6), returnText(edit7), returnText(edit8));
                if (a)
                    Toast.makeText(SFR_Overview.this, "Site updated Successfully", Toast.LENGTH_LONG).show();
                else Toast.makeText(SFR_Overview.this, "Failed", Toast.LENGTH_LONG).show();
            }


            changeEditToText(edits, texts, textLayouts, editLayouts);
            /*editLayout1.setVisibility(View.INVISIBLE);
            textLayout1.setVisibility(View.VISIBLE);
            text1.setText(edit1.getText());*/

            {
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(true);
                //startActivity(new Intent(SFR_Overview.this, MySFR.class));
            }
            TitleSite.setText(text1.getText().toString());}
        }

            if (id == R.id.eidtPencil) {
                changeTextToEdit(texts, edits, editLayouts, textLayouts);
                menu.getItem(2).setVisible(false);
                menu.getItem(1).setVisible(true);
            }

            if(id == R.id.addOpt){
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                if(idName==null){
                    Cursor cursor = data.getThisID();
                    if(cursor.getCount()>0){
                        cursor.moveToNext();}
                    idName=cursor.getString(0);
                }
                data.insertAnOption(idName, null);
                intent.putExtra("The id", idName);
                intent.putExtra("From Option", true);

                startActivity(intent);

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
        for(int i=0; i<secondTextLayouts.length; i++){
            secondeditLayouts[i].setVisibility(View.INVISIBLE);
            secondTextLayouts[i].setVisibility(View.VISIBLE);
        }
        for(int i=0; i<secondedits.length; i++){
            secondtexts[i].setText(secondedits[i].getText());
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
        for(int i=0; i<secondTextLayouts.length; i++){
            secondTextLayouts[i].setVisibility(View.INVISIBLE);
            secondeditLayouts[i].setVisibility(View.VISIBLE);
        }
        for(int i=0; i<secondedits.length; i++){
            secondedits[i].setText(secondtexts[i].getText());
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
        editLayout8 = (LinearLayout)findViewById(R.id.LL8_1);
        editLayout9 = (LinearLayout)view1.findViewById(R.id.LLO5_1);
        editLayout10= (LinearLayout)view1.findViewById(R.id.LLO1_1);
        editLayout11= (LinearLayout)view1.findViewById(R.id.LLO2_1);
        editLayout12= (LinearLayout)view1.findViewById(R.id.LLO4_1);
        editLayout13= (LinearLayout)view1.findViewById(R.id.LLO3_1);
        editLayouts = new LinearLayout[] {editLayout1, editLayout2, editLayout3, editLayout4, editLayout5, editLayout6, editLayout7, editLayout8};
        secondeditLayouts= new LinearLayout[]{editLayout9, editLayout10, editLayout11, editLayout12, editLayout13 };

        textLayout1 = (LinearLayout)findViewById(R.id.LL1_2);
        textLayout2 = (LinearLayout)findViewById(R.id.LL2_2);
        textLayout3 = (LinearLayout)findViewById(R.id.LL3_2);
        textLayout4 = (LinearLayout)findViewById(R.id.LL4_2);
        textLayout5 = (LinearLayout)findViewById(R.id.LL5_2);
        textLayout6 = (LinearLayout)findViewById(R.id.LL6_2);
        textLayout7 = (LinearLayout)findViewById(R.id.LL7_2);
        textLayout8 = (LinearLayout)findViewById(R.id.LL8_2);
        textLayout9 = (LinearLayout)view1.findViewById(R.id.LLO5_2);
        textLayout10= (LinearLayout)view1.findViewById(R.id.LLO1_2);
        textLayout11= (LinearLayout)view1.findViewById(R.id.LLO2_2);
        textLayout12= (LinearLayout)view1.findViewById(R.id.LLO4_2);
        textLayout13= (LinearLayout)view1.findViewById(R.id.LLO3_2);
        textLayouts = new LinearLayout[] {textLayout1, textLayout2, textLayout3, textLayout4, textLayout5, textLayout6, textLayout7, textLayout8};
        secondTextLayouts= new LinearLayout[]{textLayout9, textLayout10, textLayout11, textLayout12, textLayout13};

        edit1 = (EditText)findViewById(R.id.etov1);
        edit2 = (EditText)findViewById(R.id.etov2);
        edit3 = (EditText)findViewById(R.id.etov3);
        edit4 = (EditText)findViewById(R.id.etov4);
        edit5 = (EditText)findViewById(R.id.etov5);
        edit6 = (EditText)findViewById(R.id.etov6);
        edit7 = (EditText)findViewById(R.id.etov7);
        edit8 = (EditText)findViewById(R.id.etov8);
        edit9= (EditText)view1.findViewById(R.id.eto1);
        edit10= (EditText)view1.findViewById(R.id.eto1_1);
        edit11= (EditText)view1.findViewById(R.id.eto2_1);
        edit12= (EditText)view1.findViewById(R.id.eto4_1);
        edit13= (EditText)view1.findViewById(R.id.eto3_1);
        edits = new EditText[] {edit1, edit2,edit3, edit4, edit5, edit6, edit7, edit8};
        secondedits= new EditText[] {edit9, edit10, edit11, edit12, edit13};


        text1 = (TextView)findViewById(R.id.tvov1_2);
        text2 = (TextView)findViewById(R.id.tvov2_2);
        text3 = (TextView)findViewById(R.id.tvov3_2);
        text4 = (TextView)findViewById(R.id.tvov4_2);
        text5 = (TextView)findViewById(R.id.tvov5_2);
        text6 = (TextView)findViewById(R.id.tvov6_2);
        text7 = (TextView)findViewById(R.id.tvov7_2);
        text8 = (TextView)findViewById(R.id.tvov8_2);
        text9 = (TextView)view1.findViewById(R.id.tvo9);
        text10= (TextView)view1.findViewById(R.id.tvo1_2);
        text11= (TextView)view1.findViewById(R.id.tvo2_2);
        text12= (TextView)view1.findViewById(R.id.tvo4_2);
        text13= (TextView)view1.findViewById(R.id.tvo3_2);
        texts = new TextView[] {text1, text2, text3, text4, text5, text6, text7, text8};
        secondtexts= new TextView[] {text9, text10, text11, text12, text13};

        r1 = (RelativeLayout)findViewById(R.id.RL1);
        r2 = (RelativeLayout)findViewById(R.id.RL2);
        r3= (RelativeLayout)findViewById(R.id.RL3);
        r4= (RelativeLayout)findViewById(R.id.RL4);
        r5 = (RelativeLayout)findViewById(R.id.RL5);
        r6 = (RelativeLayout)findViewById(R.id.RL6);
        r7 = (RelativeLayout)findViewById(R.id.RL7);
        r8 = (RelativeLayout)findViewById(R.id.RL8);

        horizontal = (LinearLayout)findViewById(R.id.LLOV2);
    }
    public String returnText (EditText edit) {
        String a = edit.getText().toString();
        return a;
    }

    public void addOption(){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll =(LinearLayout)findViewById(R.id.LLOV1);
        view1 = inflater.inflate(R.layout.content_option, null,false);
        ImageView imageView = (ImageView)view1.findViewById(R.id.ivo1);
        imageView.setBackgroundResource(R.drawable.bible);
        ll.addView(view1);

    }
    public void hideGeneral(){
        r1.setVisibility(View.GONE);
        r2.setVisibility(View.GONE);
        r3.setVisibility(View.GONE);
        r4.setVisibility(View.GONE);
        r5.setVisibility(View.GONE);
        r6.setVisibility(View.GONE);
        r7.setVisibility(View.GONE);
        r8.setVisibility(View.GONE);

        view1.setVisibility(View.VISIBLE);

    }
    public void showGeneral(){
        r1.setVisibility(View.VISIBLE);
        r2.setVisibility(View.VISIBLE);
        r3.setVisibility(View.VISIBLE);
        r4.setVisibility(View.VISIBLE);
        r5.setVisibility(View.VISIBLE);
        r6.setVisibility(View.VISIBLE);
        r7.setVisibility(View.VISIBLE);
        r8.setVisibility(View.VISIBLE);

        view1.setVisibility(View.GONE);
    }
    public void resetColors() {
        for(TextView tv:options){
            tv.setTextColor(Color.parseColor("#999999"));
            tv.setPaintFlags(0);
        }
        tvov.setTextColor(Color.parseColor("#999999"));
        tvov.setPaintFlags(0);
    }
}
