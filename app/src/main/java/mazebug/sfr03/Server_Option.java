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
public class Server_Option extends AsyncTask<Void, Void, Void> {
    Context context;
    String SERVER_ADDRESS;
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

        public Server_Option(Context context, String userName, String SERVER_ADDRESS,  String idOption ,String siteId,String name,String town,String county,String postcode,
                String height,String latitude,String longitude) {
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

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT ).show();;
    }

    @Override
    protected Void doInBackground(Void... params) {


        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("optionID", userName + "_option_" + idOption));
        dataToSend.add(new BasicNameValuePair("siteID", userName+"_site_"+ siteId));
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