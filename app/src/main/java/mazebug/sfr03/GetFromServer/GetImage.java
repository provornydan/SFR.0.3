package mazebug.sfr03.GetFromServer;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import mazebug.sfr03.DatabaseHelper;

/**
 * Created by Provorny on 5/26/2016.
 */
public class GetImage extends AsyncTask<Void, Void, Bitmap> {
    DatabaseHelper data;
    Context context;
    String APP_TAG = "MyCustomApp";
    String serverImg;
    AlertDialog alertDialog;

    public GetImage(Context context, String serverImg, AlertDialog alertDialog){
        this.context=context;
        this.serverImg = serverImg;
        this.alertDialog = alertDialog;
    }
    @Override
    protected Bitmap doInBackground(Void... params) {

                        String url = "http://sfrapplication.comli.com/sfr03/pictures/"+serverImg+".JPG";

                        try{
                            URLConnection connection= new URL(url).openConnection();
                            connection.setConnectTimeout(1000 * 30);
                            connection.setReadTimeout(1000* 30);

                            return BitmapFactory.decodeStream((InputStream)connection.getContent());

                        }
                        catch(Exception e){
                            e.printStackTrace();
                            return  null;
                        }
                    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

            if (bitmap != null) {
                try {
                    data=  new DatabaseHelper(context);

                    Toast.makeText(context, "Getting Images", Toast.LENGTH_SHORT).show();
                    Cursor getTheLocal = data.getImageFromServer(serverImg);
                    getTheLocal.moveToNext();
                    String localID = getTheLocal.getString(0);


                    File file = new File(getPhotoFileUri(localID));
                    FileOutputStream fOut = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                    MediaStore.Images.Media.insertImage(context.getContentResolver()
                            , file.getAbsolutePath(), file.getName(), file.getName());



                    data.updateImage(localID, file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            new ImageID(context, alertDialog).execute();
        }

    public int imageinDP(int size){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
    }

    public String getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return mediaStorageDir.getPath()+File.separator+fileName+".JPG";
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
}

