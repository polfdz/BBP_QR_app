package com.bbparty.eroc.bbp_qr.arbreRefranyer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Pol on 08/12/2015.
 */
public class serverParse extends AsyncTask<String, String, JSONObject> {
    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;
    private Context context;
    Constants constants;
    JSONObject result;
    String pregunta, resposta, alternativa1, alternativa2, alternativa3, alternativa4, nivell, usuari;
    String bus_type;
    public serverParse(){
        constants = new Constants();
        result = new JSONObject();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... userInfo) {
        while(!isCancelled()) {
            try {
                pregunta = userInfo[0];
                resposta = userInfo[1];
                alternativa1 = userInfo[2];
                alternativa2 = userInfo[3];
                alternativa3 = userInfo[4];
				alternativa4 = userInfo[5];
                nivell = userInfo[6];
                usuari = userInfo[7];

                bus_type = userInfo[1];
                afegirRefrany(pregunta, resposta, alternativa1, alternativa2, alternativa3, alternativa4, nivell, usuari);
            } catch (Exception e) {
                Log.d("Error", e.getMessage());
            }
            return result;
        }
        this.onCancelled();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress.toString());
    }

    @Override
    protected void onPostExecute(JSONObject res) {
        super.onPostExecute(res);
    }

    public JSONObject afegirRefrany(String p, String r, String a1, String a2, String a3, String a4, String n, String u) {
        AsyncHttpClient client = new SyncHttpClient();
        RequestParams rp = new RequestParams();
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("Authentication",constants.AUTH_KEY);
        param.put("Function", "afegirRefrany");
        param.put("pregunta", p);
        param.put("resposta", r);
        param.put("alternativa1", a1);
        param.put("alternativa2", a2);
        param.put("alternativa3", a3);
		        param.put("alternativa4", a4);
        param.put("nivell", n);
        param.put("usuari", u);

        RequestParams params = new RequestParams(param);

        client.get(constants.BASE_URL + "webservice.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jObject) {
                    result = jObject;
            }
            @Override
            public void onFailure(int a, Header[] as, String aas, Throwable e) {
                try {
                    Log.d("THROWABLE", e.toString() + " \n HEADER _" + as.toString() + "\n String: " +aas.toString());
                    result.put("result", 503);
                    Log.d("RESULT2", result.toString());

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        });
        return result;
    }
    @Override
    protected void onCancelled(){
        super.onCancelled();
    }
}
