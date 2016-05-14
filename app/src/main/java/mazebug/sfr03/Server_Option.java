package mazebug.sfr03;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Provorny on 4/22/2016.
 */
public class Server_Option extends AsyncTask<String, Void, String> {
    Context context;
    String SERVER_ADDRESS = "http://sfrapplication.comli.com/sfr03/SavePicture.php";
    String userName;
    String idOption;
    String siteId;
    String name;
    String town;
    String county;
    String postcode;
    String height;
        String latitude;
        String longitude;
        final String Image_address = "http://sfrapplication.comli.com/sfr03/pictures/";
    String localID;

    DatabaseHelper data;

        public Server_Option(Context context, String userName, String SERVER_ADDRESS,  String idOption ,String siteId,String name,String town,String county,String postcode,
                String height,String latitude,String longitude, String localID) {
            this.context = context;
            this.userName = userName;
            this.SERVER_ADDRESS = SERVER_ADDRESS;
            this.idOption  = idOption;
            this.siteId = siteId;
            this.name= name;
            this.town  = town;
            this.county= county;
            this.postcode= postcode;
            this.height = height;
            this.latitude = latitude;
            this.longitude = longitude;
            this.localID = localID;
    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        try{
        if(!aVoid.isEmpty()) {
            char[] code = aVoid.toCharArray();
            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (true) {
                if (Character.isDigit(code[i])) {
                    builder.append(code[i]);
                    i++;
                } else break;
            }

            data = new DatabaseHelper(context);
            if (!builder.toString().isEmpty())
                data.updateOptionWithServer(idOption, builder.toString());
        } }
        catch (Exception e){e.printStackTrace();}

        try {

            Cursor optionOnServer  = data.getOptionByName(localID);
            optionOnServer.moveToNext();
            String optionID = optionOnServer.getString(8);
            Toast.makeText(context, optionID, Toast.LENGTH_LONG).show();
            Cursor imageCursor = data.getImageData(localID);
            //if(imageCursor.getCount()==0)  imageCursor=data.getOptionFromServer(idOption);
            while (imageCursor.moveToNext()) {
                if (imageCursor.getString(2).equals("1")) {


                    //SERVER_ADDRESS = "http://sfrapplication.comli.com/sfr03/SavePicture.php";
                    Uri takenPhotoUri = Uri.fromFile(new File((imageCursor.getString(1))));
                    Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
                    new UploadImage(takenImage, imageCursor.getString(0), imageCursor.getString(0), optionID).execute();

                }
                Toast.makeText(context, "Image Inserted...", Toast.LENGTH_SHORT ).show();
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected String doInBackground(String... params) {


        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("optionID", idOption));
        dataToSend.add(new BasicNameValuePair("siteID", siteId));
        dataToSend.add(new BasicNameValuePair("name", name));
        dataToSend.add(new BasicNameValuePair("town", town));
        dataToSend.add(new BasicNameValuePair("county", county));
        dataToSend.add(new BasicNameValuePair("postcode", postcode));
        dataToSend.add(new BasicNameValuePair("height", height));
        dataToSend.add(new BasicNameValuePair("latitude", latitude));
        dataToSend.add(new BasicNameValuePair("longitude", longitude));


        HttpParams httpParams = getHttpRequestParams();

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse response = client.execute(httpPost);

            InputStream ips  = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));

            StringBuilder sb = new StringBuilder();
            String s;
            while(true )
            {
                s = buf.readLine();
                if(s==null || s.length()==0)
                    break;
                sb.append(s);

            }
            buf.close();
            ips.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;

    }

    public class UploadImage extends AsyncTask<Void, Void, Void>{
        Bitmap image;
        String name;
        String imageCode2;
        String optionId;

        public UploadImage(Bitmap image, String name, String imageCode2, String optionId){
            this.image=image;
            this.name=name;
            this.imageCode2=imageCode2;
            this.optionId=optionId;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT ).show();

            new Server_Image(context, userName, "http://sfrapplication.comli.com/sfr03/insertImage.php", imageCode2, optionId).execute();
            data.setNotCreatedImages(imageCode2);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);


            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", name));

            HttpParams httpParams = getHttpRequestParams();

            SERVER_ADDRESS = "http://sfrapplication.comli.com/sfr03/SavePicture.php";
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
}