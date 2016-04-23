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
public class Server_Site extends AsyncTask<Void, Void, Void> {
    Context context;
    String SERVER_ADDRESS;
    String image_code;
    String option_id;
    String link;

    public Server_Site(Context context, String SERVER_ADDRESS, String image_code, String option_id, String link) {
        this.context = context;
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.image_code = image_code;
        this.option_id = option_id;
        this.link = link;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Information Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {


        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("image_code", image_code));
        dataToSend.add(new BasicNameValuePair("optionID", option_id));
        dataToSend.add(new BasicNameValuePair("Image_link", link));

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
