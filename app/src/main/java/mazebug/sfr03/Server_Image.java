package mazebug.sfr03;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Provorny on 4/22/2016.
 */
public class Server_Image extends AsyncTask<Void, Void, Void> {
    Context context;
    String SERVER_ADDRESS;
    String image_code;
    String option_id;
    String userName;
    final String Image_address="http://sfrapplication.comli.com/sfr03/pictures/";

    public Server_Image(Context context, String userName, String SERVER_ADDRESS, String image_code, String option_id) {
        this.context = context;
        this.userName= userName;
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.image_code = image_code;
        this.option_id = option_id;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Information Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {


        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("image_code", userName+"_image_"+image_code));
        dataToSend.add(new BasicNameValuePair("optionID", userName+"_option_"+option_id));
        dataToSend.add(new BasicNameValuePair("Image_link", Image_address+userName+"_image_"+image_code+".JPG"));

        HttpParams httpParams = getHttpRequestParams();

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost(SERVER_ADDRESS);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(dataToSend));
            client.execute(httpPost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }
}