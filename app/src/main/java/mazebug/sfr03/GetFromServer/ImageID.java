package mazebug.sfr03.GetFromServer;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import mazebug.sfr03.DatabaseHelper;
import mazebug.sfr03.Server_Site;

/**
 * Created by Provorny on 5/26/2016.
 */
public class ImageID extends AsyncTask<String, Void, String> {
    DatabaseHelper data;
    Context context;
    ArrayList<String> serverImg = new ArrayList<>();
    AlertDialog alertDialog;

    public ImageID(Context context, AlertDialog alertDialog){
        this.context = context;
        this.alertDialog = alertDialog;
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 1000 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    @Override
    protected String doInBackground(String... params) {

        HttpParams httpParams = getHttpRequestParams();

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost("http://sfrapplication.comli.com/sfr03/GetImage.php");

        try {
            HttpResponse response = client.execute(httpPost);

            InputStream ips = response.getEntity().getContent();
            BufferedReader buf = new BufferedReader(new InputStreamReader(ips, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            String s;
            while (true) {
                s = buf.readLine();
                if (s == null || s.length() == 0)
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
        if (s.isEmpty()) Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();


        String jsonStr = s;
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray ids = jsonObj.getJSONArray("theID");

                for (int i = 0; i < ids.length(); i++) {
                    JSONObject c = ids.getJSONObject(i);

                    String image_id = c.getString("imageID");
                    String option_id = c.getString("optionID");

                    data = new DatabaseHelper(context);

                        if(data.getImageFromServer(image_id).getCount()==0) {
                            Cursor getOFServer = data.getOptionFromServer(option_id);
                            getOFServer.moveToNext();
                            String localOption = getOFServer.getString(0);
                            getOFServer.close();
                            serverImg.add(image_id);


                            data.insertAnImage(localOption, null);

                            Cursor getLastImage = data.getThisImage();
                            getLastImage.moveToNext();
                            String lastImage = getLastImage.getString(0);
                            getLastImage.close();
                            data.setNotCreatedImages(lastImage);
                            data.UpdateImageWithServer(lastImage, image_id);
                        }
                }
                for(String str:serverImg) {
                    Cursor newCursor = data.getImageFromServer(str);
                    newCursor.moveToNext();
                    if (newCursor.getString(2)==null)
                        //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                        new GetImage(context, str, alertDialog).execute();
                }
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
