package mazebug.sfr03;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Provorny on 4/22/2016.
 */
public class Server_Site extends AsyncTask<String, Void, String> {


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
    String localID;

    DatabaseHelper data;

    String siteID;

    boolean inserted=false;
    final String Image_address = "http://sfrapplication.comli.com/sfr03/pictures/";


    public Server_Site(Context context, String userName, String SERVER_ADDRESS, String idSite, String nameSite, String area,
                       String owners, String cid, String csr, String site, String powvf, String powo2, String date, String localID) {
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
        this.localID=localID;
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
                data.updateSiteWithServer(idSite, builder.toString());  }}
        catch (Exception e){e.printStackTrace();}


            try {

                Cursor optionCursor = data.getAllOptions(localID);
                while (optionCursor.moveToNext()) {

                    Cursor siteOnServer = data.getIdData(optionCursor.getString(8));
                    siteOnServer.moveToNext();
                    siteID = siteOnServer.getString(9);



                    if (optionCursor.getString(9).equals("1")) {
                        new Server_Option(context, userName, "http://sfrapplication.comli.com/sfr03/insertOption.php", optionCursor.getString(0), siteID, optionCursor.getString(1), optionCursor.getString(2),
                                optionCursor.getString(3), optionCursor.getString(4), optionCursor.getString(5), optionCursor.getString(6), optionCursor.getString(7), optionCursor.getString(0)).execute();
                        data.setNotCreatedOptions(optionCursor.getString(0));
                        data.setNotEditedOptions(optionCursor.getString(0));
                        Toast.makeText(context, "LOADING...", Toast.LENGTH_SHORT).show();
                    } else //if (optionCursor.getString(10).equals("1"))
                    {
                        Toast.makeText(context, "UPDATING...", Toast.LENGTH_SHORT).show();
                        Cursor optionOnServer  = data.getOptionByName(optionCursor.getString(0));
                        optionOnServer.moveToNext();
                        String optionID = optionOnServer.getString(8);


                        new Server_Option(context, userName, "http://sfrapplication.comli.com/sfr03/updateOption.php", optionID, siteID, optionCursor.getString(1), optionCursor.getString(2),
                                optionCursor.getString(3), optionCursor.getString(4), optionCursor.getString(5), optionCursor.getString(6), optionCursor.getString(7), optionCursor.getString(0)).execute();
                        data.setNotEditedOptions(optionCursor.getString(0));

                    }
                }

            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }




        }


    @Override
    protected String doInBackground(String... params) {


        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("Site_ID", idSite));
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

}