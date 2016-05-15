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
import mazebug.sfr03.Server_Site;

/**
 * Created by Provorny on 5/15/2016.
 */
public class GetSite extends AsyncTask<String, Void, String>{
    Context context;
    DatabaseHelper data;
    Cursor siteCursor;
    AlertDialog alertDialog;
    String s0;
    String s1;
    String s2;
    String s3;
    String s4;
    String s5;
    String s6;
    String s7;
    String s8;
    String s9;
    String s10;
    String s11;
    String s12;
    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    public GetSite(Context context, AlertDialog alertDialog){
        this.context=context;
        this.alertDialog=alertDialog;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpParams httpParams = getHttpRequestParams();

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost("http://sfrapplication.comli.com/sfr03/GetSite.php");

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
        String jsonStr = s;
        if(jsonStr!=null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray ids = jsonObj.getJSONArray("theID");

                    for(int i=0; i<ids.length(); i++){
                    JSONObject c = ids.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");
                    String area = c.getString("area");
                    String owners = c.getString("owners");
                    String cid = c.getString("cid");
                    String csr = c.getString("csr");
                    String site = c.getString("site");
                    String pow_vf = c.getString("pow_vf");
                    String pow_o2 = c.getString("pow_o2");
                    String date = c.getString("date");
                    ArrayList<String> arr= new ArrayList<>();   arr.add(name); arr.add(area); arr.add(owners); arr.add(cid); arr.add(csr); arr.add(site); arr.add(pow_vf);
                        arr.add(pow_o2);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = sdf.format(new Date());
                    data = new DatabaseHelper(context);

                        Cursor checkSite = data.getSiteFromServer(id);
                        if(checkSite.getCount()==0) data.insertData(arr, strDate, "0", "0", id);


                }

            }
            catch (Exception e){Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();}
            Toast.makeText(context, "Finish", Toast.LENGTH_SHORT).show();

            Cursor siteCursor = data.getData();


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
            }
            if(trial.size()==0){
                alertDialog.dismiss(); Toast.makeText(context, "Info is up to Date", Toast.LENGTH_LONG).show();}

            siteCursor.close();
            data.close();

        }
    }
}
