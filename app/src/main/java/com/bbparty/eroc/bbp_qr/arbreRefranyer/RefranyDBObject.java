package com.bbparty.eroc.bbp_qr.arbreRefranyer;


public class RefranyDBObject {

    //private variables
    int id;
    int nivell;
    String pregunta;
    String resposta;
    String alternativa1;
    String alternativa2;
    String alternativa3;
    String alternativa4;


    // Empty constructor
    public RefranyDBObject(){

    }
    // constructor
    public RefranyDBObject(int _id, int _nivell, String _pregunta, String _resposta, String _alternativa1, String _alternativa2, String _alternativa3, String _alternativa4){
        id = _id;
        nivell = _nivell;
        pregunta = _pregunta;
        resposta = _resposta;
        alternativa1 = _alternativa1;
        alternativa2 = _alternativa2;
        alternativa3 = _alternativa3;
        alternativa4 = _alternativa4;
    }
    // constructor
    public RefranyDBObject(int _nivell, String _pregunta, String _resposta, String _alternativa1, String _alternativa2, String _alternativa3, String _alternativa4){
        nivell = _nivell;
        pregunta = _pregunta;
        resposta = _resposta;
        alternativa1 = _alternativa1;
        alternativa2 = _alternativa2;
        alternativa3 = _alternativa3;
        alternativa4 = _alternativa4;
    }



    // getting ID
    public int getID(){
        return id;
    }

    // setting id
    public void setID(int _id){
        id = _id;
    }

    // getting nivell
    public int getNivell(){
        return nivell;
    }

    // setting nivell
    public void setNivell(int _nivell){
        nivell = _nivell;
    }

    // getting pregunta
    public String getPregunta(){
        return pregunta;
    }

    // setting pregunta
    public void setPregunta(String _pregunta){
        pregunta = _pregunta;
    }

    // getting resposta
    public String getResposta(){
        return resposta;
    }

    // setting resposta
    public void setResposta(String _resposta){
        resposta = _resposta;
    }

    //Getting alternatives
    public String getAlternativa1(){
        return alternativa1;
    }
    public String getAlternativa2(){
        return alternativa2;
    }
    public String getAlternativa3(){
        return alternativa3;
    }
    public String getAlternativa4(){
        return alternativa4;
    }
    //Setting alternatives
    public void setAlternativa1(String _alternativa){
        alternativa1 = _alternativa;
    }
    public void setAlternativa2(String _alternativa){
        alternativa2 = _alternativa;
    }
    public void setAlternativa3(String _alternativa){
        alternativa3 = _alternativa;
    }
    public void setAlternativa4(String _alternativa){
        alternativa4 = _alternativa;
    }
}