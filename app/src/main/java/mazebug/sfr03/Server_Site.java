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
    String userName;
    String idSite;
    String nameSite;
    String area;
    String owners;
    String cid;
    String csr;
    String site;
    String powvf;
    String powo2;
    String date;
    final String Image_address = "http://sfrapplication.comli.com/sfr03/pictures/";

    public Server_Site(Context context, String userName, String SERVER_ADDRESS, String idSite, String nameSite, String area,
                       String owners, String cid, String csr, String site, String powvf, String powo2, String date) {
        this.context = context;
        this.userName = userName;
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.idSite = idSite;
        this.nameSite = nameSite;
        this.area = area;
        this.owners = owners;
        this.cid = cid;
        this.csr = csr;
        this.site = site;
        this.powvf = powvf;
        this.powo2 = powo2;
        this.date = date;

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {


        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("Site_ID", userName + "_site_" + idSite));
        dataToSend.add(new BasicNameValuePair("site_name", nameSite));
        dataToSend.add(new BasicNameValuePair("search_area", area));
        dataToSend.add(new BasicNameValuePair("owners", owners));
        dataToSend.add(new BasicNameValuePair("CID", cid));
        dataToSend.add(new BasicNameValuePair("CSR", csr));
        dataToSend.add(new BasicNameValuePair("Site", site));
        dataToSend.add(new BasicNameValuePair("pow_vf", powvf));
        dataToSend.add(new BasicNameValuePair("pow_o2", powo2));
        dataToSend.add(new BasicNameValuePair("dateAdded", date));


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