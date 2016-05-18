package mazebug.sfr03;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Provorny on 5/13/2016.
 */
public class UpdateImage extends AsyncTask<String, Void, String> {
    Context context;
    String SERVER_ADDRESS;
    String image_code;
    String option_id;
    String userName;
    DatabaseHelper data;
    final String Image_address="http://sfrapplication.comli.com/sfr03/pictures/";

    public UpdateImage(Context context, String userName, String SERVER_ADDRESS, String image_code, String option_id) {
        this.context = context;
        this.userName= userName;
        this.SERVER_ADDRESS = SERVER_ADDRESS;
        this.image_code = image_code;
        this.option_id = option_id;
    }

    @Override
    protected String doInBackground(String... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("image_code", image_code));
        dataToSend.add(new BasicNameValuePair("optionID", option_id));
        dataToSend.add(new BasicNameValuePair("Image_link", Image_address+image_code + ".JPG"));

        SERVER_ADDRESS = "http://sfrapplication.comli.com/sfr03/updateImage.php";

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

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(context, "Image Updated", Toast.LENGTH_LONG);
        context.startActivity(new Intent(context, MySFR.class));
    }
}
