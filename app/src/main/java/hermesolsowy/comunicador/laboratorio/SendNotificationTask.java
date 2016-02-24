package hermesolsowy.comunicador.laboratorio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Verena on 16/02/2016.
 */
public class SendNotificationTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        Notificacion notificacion = (Notificacion) params[0];
        final Context contexto = (Context) params[1];
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        String notificacionJson = gson.toJson(notificacion);


        Database db = new Database(contexto);
        Configuracion configuracion = db.getConfiguracion();
        String url = "http://" + configuracion.getDireccionIP() + ":" + configuracion.getPuerto() + "/hermes/monitor";


        //-----Para chequear si hay conexion a internet
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoNet = cm.getActiveNetworkInfo();

        if (infoNet != null && infoNet.isConnectedOrConnecting()) {

            OutputStream os = null;
            HttpURLConnection conn = null;
            try {
                URL urlObject = new URL(url);
                conn = (HttpURLConnection) urlObject.openConnection();
                conn.setReadTimeout(100000);
                conn.setDoOutput(true);
                os = conn.getOutputStream();
            } catch (Exception e1) {
                System.out.println("Error en la URL");
                e1.printStackTrace();
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));


            try {
                writer.write(notificacionJson);
                writer.close();
                os.close();
                int code = conn.getResponseCode();
                System.out.println("code:" + code);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
                in.close();
                System.out.println(conn.getResponseMessage());

            } catch (IOException e) {
                System.out.println("Error de al enviar datos ");
                e.printStackTrace();
            }
        }else{
            System.out.println("Verifique su conexión");
            Handler handler =  new Handler(contexto.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(contexto, "Notificación no enviada, problema de conexión", Toast.LENGTH_LONG).show();
                }
            });
        }
        return null;
    }
}
