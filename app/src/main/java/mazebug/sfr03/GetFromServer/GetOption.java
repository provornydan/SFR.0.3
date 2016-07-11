package mazebug.sfr03.GetFromServer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import mazebug.sfr03.DatabaseHelper;
import mazebug.sfr03.MySFR;
import mazebug.sfr03.Server_Site;

/**
 * Created by Provorny on 5/15/2016.
 */
public class GetOption extends AsyncTask<String, Void, String>{
    Context context;
    DatabaseHelper data;
    Cursor siteCursor;
    AlertDialog alertDialog;

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    public GetOption(Context context, AlertDialog alertDialog){
        this.context=context;
        this.alertDialog=alertDialog;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpParams httpParams = getHttpRequestParams();

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost("http://sfrapplication.comli.com/sfr03/GetOption.php");

        try {
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.isEmpty()) Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();


        String jsonStr = s;
        if(jsonStr!=null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray ids = jsonObj.getJSONArray("theID");

                for(int i=0; i<ids.length(); i++){
                    JSONObject c = ids.getJSONObject(i);

                    String option_id = c.getString("option_id");
                    String site_id = c.getString("site_id");
                    String name  = c.getString("name");
                    String town = c.getString("town");
                    String county = c.getString("county");
                    String postcode = c.getString("postcode");
                    String height = c.getString("height");
                    String latitude = c.getString("latitude");
                    String longitude = c.getString("longitude");

                    data = new DatabaseHelper(context);

                    Cursor checkSite = data.getSiteFromServer(site_id);
                    checkSite.moveToNext();
                    String localId = checkSite.getString(0);
                    checkSite.close();

                    Cursor OptionOnServer  = data.getOptionFromServer(option_id);
                    if(OptionOnServer.getCount()==0) {

                    data.insertAnOption(localId, null, "0");

                    Cursor checkOption = data.getThisOption();
                    checkOption.moveToNext();
                    String localOption = checkOption.getString(0);
                    checkOption.close();

                    data.updateOption(localOption, name, town, county, postcode, height);
                    data.updateCoord(localOption, latitude, longitude);
                    data.updateOptionWithServer(localOption, option_id); }

                }

            }
            catch (Exception e){Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();}
            Toast.makeText(context, "Succes", Toast.LENGTH_SHORT).show();

            new ImageID(context, alertDialog).execute();

            Cursor siteCursor = data.getData();

            //context.startActivity(new Intent(context, MySFR.class));

            ArrayList<Integer> trial= new ArrayList<Integer>();
            while (siteCursor.moveToNext()) {
                if (siteCursor.getString(10).equals("1")) {
                    //alertDialog.show();
                    Server_Site site  = new Server_Site(context, null, "http://sfrapplication.comli.com/sfr03/insertSite.php", siteCursor.getString(0), siteCursor.getString(1), siteCursor.getString(2), siteCursor.getString(3), siteCursor.getString(4),
                            siteCursor.getString(5), siteCursor.getString(6), siteCursor.getString(7), siteCursor.getString(8), siteCursor.getString(9), siteCursor.getString(0), alertDialog);

                    site.execute();
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT ).show();
                    data.setNotCreatedSite(siteCursor.getString(0));
                    data.setNotEditedSite(siteCursor.getString(0));
                    trial.add(1);
                } else if (siteCursor.getString(11).equals("1")) {
                    Toast.makeText(context, "ChangedSite...", Toast.LENGTH_SHORT).show();
                    //alertDialog.show();
                    new Server_Site(context, null, "http://sfrapplication.comli.com/sfr03/updateSite.php", siteCursor.getString(12), siteCursor.getString(1), siteCursor.getString(2), siteCursor.getString(3), siteCursor.getString(4),
                            siteCursor.getString(5), siteCursor.getString(6), siteCursor.getString(7), siteCursor.getString(8), siteCursor.getString(9), siteCursor.getString(0), alertDialog).execute();
                    data.setNotEditedSite(siteCursor.getString(0));
                    trial.add(1);
                }
                else{
                   // context.startActivity(new Intent(context, MySFR.class));
                }
            }
                   /* if(trial.size()==0){
                        alertDialog.dismiss(); Toast.makeText(context, "Info is up to Date", Toast.LENGTH_LONG).show();}  */

            //siteCursor.close();
            data.close();

        }
    }
}
