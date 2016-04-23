package mazebug.sfr03;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MySFR extends AppCompatActivity {
    EditText mysearch; Menu menu; TextView title;
    DatabaseHelper data = new DatabaseHelper(this);
    Cursor rs;
    public final String APP_TAG = "MyCustomApp";
    private static final String SERVER_ADDRESS="http://sfrapplication.comli.com/sfr03/SavePicture.php";

    ArrayList<LinearLayout> blocks = new ArrayList<>();
    ArrayList<TextView> textblocks = new ArrayList<>();
    ArrayList<String> sites = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();

    final String userName="danik1brat";
    public String imageName;
    final String Image_address="http://sfrapplication.comli.com/sfr03/pictures/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sfr);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mysearch = (EditText)findViewById(R.id.searchBar);
        title = (TextView)findViewById(R.id.title);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MySFR.this, SFR_Overview.class));
            }
        });

        final LinearLayout lm = (LinearLayout) findViewById(R.id.panel1);

        // create the layout params that will be used to define how your
        // button will be displayed
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);

        rs= data.getAllData();

        //Create four
        while(rs.moveToNext())
        {
            sites.add(rs.getString(1));
            ids.add(rs.getString(0));
            // Create LinearLayout
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setBackgroundResource(R.drawable.border);
            /*ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MySFR.this, SFR_Overview.class);
                    rs.moveToFirst();
                    intent.putExtra("Get Site", rs.getString(0));
                    startActivity(intent);
                }
            });*/
            blocks.add(ll);

            //Set Image
            LinearLayout FL = new LinearLayout(this);
            FL.setOrientation(LinearLayout.VERTICAL);
            FL.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(R.drawable.confirmed);
            //iv.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
            int sizeImage = 20;

            int imageInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeImage, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageInDp, imageInDp);



            int sizeInDP = 16;

            int marginInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources().getDisplayMetrics());
            layoutParams.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
            //60,50,60,60
            FL.addView(iv, layoutParams);

            ll.addView(FL);

            //Linear Layout with the texts
            LinearLayout vl = new LinearLayout(this);
            vl.setOrientation(LinearLayout.VERTICAL);
            vl.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            //vl.setBackgroundResource(R.drawable.confirmed);

            //Texts fot view
            //Text1
            TextView text1 = new TextView(this);
            text1.setText(rs.getString(1));
            text1.setTextSize(17);
            text1.setTypeface(Typeface.DEFAULT);
            text1.setTextColor(Color.parseColor("#000000"));
            LinearLayout.LayoutParams lay2 = new  LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
            lay2.setMargins(0, 10, 0, 0);
            vl.addView(text1, lay2);

            //Text2
            TextView text2 = new TextView(this);
            text2.setText("Modified: " + rs.getString(2));
            text2.setTextSize(14);
            text2.setTypeface(Typeface.DEFAULT);
            text2.setTextColor(Color.parseColor("#999999"));
            LinearLayout.LayoutParams lay3 = new  LinearLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
            lay3.setMargins(0, 0, 0, 0);
            vl.addView(text2, lay3);




            ll.addView(vl);
            textblocks.add(text1);

            /* //Create Button
            final Button btn = new Button(this);
            // Give button an ID
            btn.setId(j+1);
            btn.setText("Add To Cart");
            // set the layoutParams on the button
            btn.setLayoutParams(params);

            final int index = j;
            // Set click listener for button
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i("TAG", "index :" + index);

                    Toast.makeText(getApplicationContext(),
                            "Clicked Button Index :" + index,
                            Toast.LENGTH_LONG).show();

                }
            });

            //Add button to LinearLayout
            ll.addView(btn);*/
            //Add button to LinearLayout defined in XML
            lm.addView(ll);
        }

        for(int i=0; i<blocks.size(); i++){
            final int ord = i;
            blocks.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MySFR.this, SFR_Overview.class);
                    intent.putExtra("Get Site", sites.get(ord));
                    intent.putExtra("The id", ids.get(ord));
                    startActivity(intent);
                }
            });
        }

        mysearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = mysearch.getText().toString().toLowerCase();
                char[] letters = search.toCharArray();
                for (int i = 0; i < blocks.size(); i++) {
                    final int ord = i;
                    String name = sites.get(ord).toLowerCase();
                    if (name.startsWith(search)) {
                        blocks.get(ord).setVisibility(View.VISIBLE);


                        TextView tv = textblocks.get(ord);
                        SpannableString text = new SpannableString(textblocks.get(ord).getText().toString());
                        text.setSpan(new TextAppearanceSpan(getBaseContext(), R.style.MainStyle), 0, letters.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new TextAppearanceSpan(getBaseContext(), R.style.style0), letters.length, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        tv.setText(text, TextView.BufferType.SPANNABLE);
                    } else blocks.get(ord).setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

         /*   Cursor imageCursor = data.getAllImages();
            while(imageCursor.moveToNext()) {
                data.updateImage(imageCursor.getString(0), getPhotoFileUri(imageCursor.getString(0)));
                Uri takenPhotoUri = Uri.fromFile(new File(getPhotoFileUri(imageCursor.getString(0))));
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                new UploadImage(takenImage, imageCursor.getString(0)).execute();
                new Server_Image(this, userName, "http://sfrapplication.comli.com/sfr03/insertImage.php", imageCursor.getString(0), imageCursor.getString(2)).execute();
            }
            */


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu=menu;
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
        if (id == R.id.search){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            title.setVisibility(View.GONE);
            mysearch.setVisibility(View.VISIBLE);

            mysearch.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(1).setVisible(true);

        }

        if (id == R.id.cancel){
            title.setVisibility(View.VISIBLE);
            mysearch.setVisibility(View.GONE);

            menu.getItem(0).setVisible(true);
            menu.getItem(2).setVisible(true);
            menu.getItem(1).setVisible(false);

            mysearch.setText("");
        }

        return super.onOptionsItemSelected(item);
    }

    public String getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return mediaStorageDir.getPath()+File.separator+fileName;
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    private class UploadImage extends AsyncTask<Void, Void, Void>{
        Bitmap image;
        String name;


        public UploadImage(Bitmap image, String name){
            this.image=image;
            this.name=name;
            imageName = name;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", userName+"_image_"+name));

            HttpParams httpParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(SERVER_ADDRESS);

            try{
                httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(httpPost);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
        private HttpParams getHttpRequestParams(){
            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
            HttpConnectionParams.setSoTimeout(httpRequestParams, 1000*30);
            return  httpRequestParams;
        }
}
