package omkabel.Project.myapplication.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefManager {
    public static final String SP_LOGIN_APP = "SollarCell";
    public static final String SP_KIPAS = "spKipas";
    public static final String SP_LAMPU = "spLampu";
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharePrefManager(Context context){
        sp = context.getSharedPreferences(SP_LOGIN_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpKipas(){
        return sp.getString(SP_KIPAS, "");
    }

    public String getSpLampu(){
        return sp.getString(SP_LAMPU, "");
    }

}
