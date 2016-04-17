package mazebug.sfr03;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class testMap extends AppCompatActivity implements GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener {
    private static GoogleMap map;
    DatabaseHelper data;
    Cursor optionCursor;
    public Marker first;
    public double lat, lon;
    EditText latText, longText;
    RelativeLayout block;
    Menu menu;
    Bundle extrasBundle;

    TextView bottomLine;
    String OptionID;
    int optionOrder;
    String idName;
    boolean MarkerMoved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomLine = (TextView)findViewById(R.id.tvm1);
        data = new DatabaseHelper(this);

        MarkerMoved=false;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(testMap.this, SFR_Overview.class);
                intent.putExtra("The id", idName);
                intent.putExtra("from Maps", true);
                intent.putExtra("Option order", optionOrder);
                startActivity(intent);
            }
        });

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setOnMarkerDragListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMapClickListener(this);

        first = map.addMarker(new MarkerOptions()
                .visible(false)
                .position(new LatLng(0, 0))
                .title("Location")
                .draggable(true)
                .snippet("First Marker"));

        map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(54.648413, -4.416504), 5f) );

        // use map to move camera into position

        Intent extras = getIntent();
        extrasBundle = extras.getExtras();
        if(extrasBundle!=null){
            optionOrder=extrasBundle.getInt("Option order");
            OptionID = extrasBundle.getString("Option id");
            optionCursor=data.getOptionByName(OptionID);
            optionCursor.moveToNext();
            idName = extrasBundle.getString("The id");
            if(optionCursor.getString(6)!=null){
            Double lat = Double.parseDouble(optionCursor.getString(6));
            Double lon = Double.parseDouble(optionCursor.getString(7));
            first.setPosition(new LatLng(lat, lon));
            first.setVisible(true);
            bottomLine.setVisibility(View.GONE);}
        }
    }


    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub

    }

    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        LatLng dragPosition = first.getPosition();
        double dragLat = dragPosition.latitude;
        double dragLong = dragPosition.longitude;
        Log.i("info", "on drag end :" + dragLat + " dragLong :" + dragLong);
        Toast.makeText(getApplicationContext(), "Marker Dragged..!", Toast.LENGTH_LONG).show();

        menu.getItem(0).setVisible(true);


    }

    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub

    }


    public void onMapClick(LatLng arg0) {

    }



    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub

        //create new marker when user long clicks
        first.setPosition(arg0);
        first.setVisible(true);
        bottomLine.setVisibility(View.GONE);



        CameraPosition INIT =
                new CameraPosition.Builder()
                        .target(arg0)

                        .zoom(15F)
                        .bearing(300F) // orientation
                        .build();

        // use map to move camera into position
        //map.moveCamera(CameraUpdateFactory.newCameraPosition(INIT));

        menu.getItem(0).setVisible(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu3, menu);
        this.menu=menu;
        menu.getItem(0).setVisible(false);
        if(MarkerMoved){
            menu.getItem(0).setVisible(true);
        }
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
        }
        if (id == R.id.mapdone){



            if(first.getPosition().latitude!=0){
            String Latitude = Double.toString(first.getPosition().latitude);
            String Longitude = Double.toString(first.getPosition().longitude);
            data.updateCoord(OptionID, Latitude, Longitude );
                menu.getItem(0).setVisible(false);
            }
            Toast.makeText(testMap.this, "Location Saved", Toast.LENGTH_LONG).show();

        }

        if (id == R.id.mapeidt){

            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);


        }

        return super.onOptionsItemSelected(item);
    }
}





