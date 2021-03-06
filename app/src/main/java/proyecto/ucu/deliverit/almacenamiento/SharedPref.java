package proyecto.ucu.deliverit.almacenamiento;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref {
    private static final String SHARED_PREF_NAME = "SharedPreferencesDeliverIT";

    private static final String ID_DELIVERY = "idDelivery";

    private static final String TOKEN = "token";

    private static final String LATITUD = "latitud";
    private static final String LONGITUD = "longitud";

    private static final String CONTADOR_NOTIFICACION = "contador_notificacion";

    private static final String VIAJE_EN_CURSO = "viaje_en_curso";

    public static long getIdDelivery (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        long idDelivery = sharedPref.getLong(ID_DELIVERY, 0);
        return idDelivery;
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong(ID_DELIVERY, 0);
        editor.commit();
    }

    public static void guardarIdDelivery (Context context, long idDelivery) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong(ID_DELIVERY, idDelivery);
        editor.commit();
    }

    public static String getToken (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String token = sharedPref.getString(TOKEN, "");
        return token;
    }

    public static void guardarToken (Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static Double getLatitud (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Double latitud = Double.parseDouble(sharedPref.getString(LATITUD, "0"));
        return latitud;
    }

    public static void guardarLatitud (Context context, Double latitud) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(LATITUD, String.valueOf(latitud));
        editor.commit();
    }

    public static Double getLongitud (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Double longitud = Double.parseDouble(sharedPref.getString(LONGITUD, "0"));
        return longitud;
    }

    public static void guardarLongitud (Context context, Double longitud) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(LONGITUD, String.valueOf(longitud));
        editor.commit();
    }

    public static Integer getContadorNotificacion (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Integer contador = sharedPref.getInt(CONTADOR_NOTIFICACION, 1);
        return contador;
    }

    public static void guardarContadorNotificacion (Context context, Integer contador) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(CONTADOR_NOTIFICACION, contador);
        editor.commit();
    }

    public static Integer getViajeEnCurso (Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Integer viajeEnCurso = sharedPref.getInt(VIAJE_EN_CURSO, 0);
        return viajeEnCurso;
    }

    public static void guardarViajeEnCurso (Context context, Integer viajeEnCurso) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(VIAJE_EN_CURSO, viajeEnCurso);
        editor.commit();
    }
}
