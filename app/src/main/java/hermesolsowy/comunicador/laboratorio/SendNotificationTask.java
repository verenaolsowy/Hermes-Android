package hermesolsowy.comunicador.laboratorio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.ContactsContract;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import java.util.ArrayList;

/**
 * Created by Verena on 16/02/2016.
 */
public class SendNotificationTask extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
       /* ArrayList<Notificacion> lista = (ArrayList<Notificacion>) params[0];
        final Context contexto = (Context) params[1];
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        String listaNotiJson = gson.toJson(lista);

        System.out.println(listaNotiJson);

        Database db = new Database(contexto);
        ContactsContract.Settings settings = db.getConfiguracion();
        String url = "http://" + settings.getDireccionIP() + ":" + settings.getPuerto() + "/hermes/monitor";


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
                System.out.println("Error de IO al conectarse a la URL");
                e1.printStackTrace();
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));


            try {
                writer.write(listaNotiJson);
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
            System.out.println("else del enviar notificacion, no hay red");
            //no hay red, acá avisa con un toast
            Handler handler =  new Handler(contexto.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(contexto, R.string.alerta_enviar_notificacion, Toast.LENGTH_LONG).show();
                }
            });
        }
*/
        return null;
    }
}
