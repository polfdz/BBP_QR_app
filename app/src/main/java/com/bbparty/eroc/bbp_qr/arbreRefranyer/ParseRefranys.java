package com.bbparty.eroc.bbp_qr.arbreRefranyer;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pol on 27/10/2015.
 */
public class ParseRefranys {
    Context context;
    DatabaseModel db;
    serverParse serverParse;
    public ParseRefranys(Context _context, DatabaseModel _db){
        context = _context;
        db = _db;
    }

    public void parse() throws IOException {
            AssetManager am = context.getAssets();
            InputStream is = am.open("refranys.xml");
            InputStreamReader text = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(text);
            //popUpToast(text.getEncoding());
            String line;
            String _nivell = null;
            String _pregunta = null;
            String _resposta = null;
            String _alternativa1 = null, _alternativa2 = null, _alternativa3 = null, _alternativa4 = null;
            while ( (line = reader.readLine()) != null){
                if(line.contains("Nivell")){
                    _nivell = line.substring(line.indexOf("N")+7, line.indexOf(":"));
                }else if(line.contains("pregunta:")){
                    String pattPregunta = "pregunta:\\s*";
                    String pattResposta1 = "\\s*\\.\\.\\.";
                    String pattResposta2 = "\\s*\\.*\\s*alternatives:\\s*";

                    Pattern patternPregunta = Pattern.compile(pattPregunta);
                    Pattern patternResposta1 = Pattern.compile(pattResposta1);
                    Pattern patternResposta2 = Pattern.compile(pattResposta2);
                    int index1 = 0;
                    int index2s = 0;
                    int index2e = 0;
                    int index3s = 0;
                    int index3e = 0;
                    Matcher matcher = patternPregunta.matcher(line);
                    if(matcher.find()) {
                        index1 = matcher.end();
                    }
                    Matcher matcherRes1 = patternResposta1.matcher(line);
                    if(matcherRes1.find()){
                        index2s = matcherRes1.start();
                        index2e = matcherRes1.end();
                    }
                    _pregunta = line.substring(index1,index2s);
                    //Log.i("Parse",_pregunta);

                    Matcher matchRes2 = patternResposta2.matcher(line);
                    if(matchRes2.find()){
                        index3s = matchRes2.start();
                        index3e = matchRes2.end();
                    }
                    _resposta = line.substring(index2e, index3s);
                    //Log.i("Parse",_resposta);

                    String alt = line.substring(index3e);
                    String alt1[] = alt.split("\\s*,\\s*");
                    if(alt1[0] != null){
                        _alternativa1 = alt1[0];
                        //Log.i("Parse",_alternativa1);
                    }
                    if(alt1[1] != null){
                        _alternativa2 = alt1[1];
                        //Log.i("Parse",_alternativa2);
                    }
                    if(alt1[2] != null){
                        _alternativa3 = alt1[2];
                        //Log.i("Parse",_alternativa3);
                    }
                    if(alt1[3] != null){
                        _alternativa4 = alt1[3];
                        //Log.i("Parse",_alternativa4);
                    }
                    db.addRefrany(new RefranyDBObject(Integer.parseInt(_nivell), _pregunta, _resposta, _alternativa1, _alternativa2, _alternativa3, _alternativa4));
					serverParse = new serverParse();
					try {
						JSONObject result = serverParse.execute(_pregunta, _resposta, _alternativa1, _alternativa2, _alternativa3, _alternativa4, _nivell, "milenbranca").get();
                       /* int conn = result.getInt("MessageCode");
                        while(conn != 200){
                            Log.d("PREGUTNA", _pregunta);
                            Log.d("RESPOSTA", _resposta);
                            Log.d("A1", _alternativa1);
                            Log.d("A2", _alternativa2);
                            Log.d("A3", _alternativa3);
                            Log.d("A4", _alternativa4);
                            Log.d("Nivell", _nivell);
                            Log.d("PERS", "milenbranca");


                            serverParse = new serverParse();
                            result = serverParse.execute(_pregunta, _resposta, _alternativa1, _alternativa2, _alternativa3, _alternativa4, _nivell, "milenbranca").get();
                            //conn = result.getInt("MessageCode");
                        }*/
                        //wait(200);
                      /*  while(conn != 200){
                            serverParse.cancel(true);
                            result = serverParse.execute(_pregunta, _resposta, _alternativa1, _alternativa2, _alternativa3, _alternativa4, _nivell, "milenbranca").get();
                            conn = result.getInt("MessageCode");
                        }*/
					}catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}/* catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                }
            }

    }
}
