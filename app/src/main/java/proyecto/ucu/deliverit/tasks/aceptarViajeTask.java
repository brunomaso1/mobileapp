package proyecto.ucu.deliverit.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import proyecto.ucu.deliverit.almacenamiento.SharedPref;
import proyecto.ucu.deliverit.entidades.Viaje;
import proyecto.ucu.deliverit.main.NotificacionActivity;

/**
 * Created by JMArtegoytia on 24/02/2017.
 */

public class AceptarViajeTask extends AsyncTask<Void, Void, Void> {
    NotificacionActivity activityPadre;
    Integer idDelivery;
    Integer idViaje;

    public AceptarViajeTask(NotificacionActivity activityPadre, Integer idDelivery, Integer idViaje) {
        this.activityPadre = activityPadre;
        this.idDelivery = idDelivery;
        this.idViaje = idViaje;
    }

    @Override
    protected Void doInBackground(Void... params) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.1.44:8080/BackCore/ws/viaje/aceptarViaje/" + this.idViaje + "/"
                + this.idDelivery;

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
            activityPadre.aceptarTaskRetorno(Integer.parseInt(response.body().toString()));
        } catch (IOException e) {
            e.printStackTrace();
            activityPadre.aceptarTaskRetorno(-1);
        }
        return null;
    }
}