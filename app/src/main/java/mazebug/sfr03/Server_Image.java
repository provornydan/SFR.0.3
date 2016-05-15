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
public class Server_Image extends AsyncTask<String, Void, String> {
    Context context;
    String SERVER_ADDRESS;
    String image_code;
    String option_id;
    String userName;
    String SERVER_IMAGE_ID;
    Bitmap bitmap;
    final String Image_address="http://sfrapplication.comli.com/sfr03/pictures/";

    DatabaseHelper data;

    public Server_Image(Context context, String userName, String SERVER_ADDRESS, String image_code, String option_id, Bitmap bitmap, String SERVER_IMAGE_ID) {
        this.context = context;
        this.userName= userName;
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.image_code = image_code;
        this.option_id = option_id;
        this.bitmap=bitmap;
        this.SERVER_IMAGE_ID = SERVER_IMAGE_ID;
    }


    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(context, "Image Nr. "+image_code, Toast.LENGTH_LONG).show();

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
                Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show();

                data = new DatabaseHelper(context);
               if (!builder.toString().isEmpty())
                    data.UpdateImageWithServer(image_code, builder.toString());
                    data.setNotCreatedImages(image_code);
                    new UploadImage(bitmap, builder.toString(), option_id).execute();

            }
        }
        catch (Exception e){e.printStackTrace();}

    }


    @Override
    protected String doInBackground(String... params) {

        //SERVER_ADDRESS = "http://sfrapplication.comli.com/sfr03/SavePicture.php";
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("optionID", option_id));
        dataToSend.add(new BasicNameValuePair("Image_link", Image_address+image_code + ".JPG"));

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
        String OptionID;

        public UploadImage(Bitmap image, String name, String OptionID){
            this.image=image;
            this.name=name;
            this.OptionID=OptionID;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT ).show();
            new UpdateImage(context, userName, null, name, OptionID).execute();
            //new Server_Image(context, userName, "http://sfrapplication.comli.com/sfr03/insertImage.php", imageCode2, optionId).execute();
            //data.setNotCreatedImages(imageCode2);
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
