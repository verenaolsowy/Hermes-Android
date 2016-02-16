package hermesolsowy.comunicador.laboratorio;

/**
 * Created by Verena on 06/02/2016.
 */
public class Configuracion {
    private String direccionIP;
    private Integer puerto;

    public Configuracion(String ip, Integer puerto){
        this.direccionIP = ip;
        this.puerto = puerto;
    }

    public String getDireccionIP() {
        return direccionIP;
    }

    public void setDireccionIP(String direccionIP) {
        this.direccionIP = direccionIP;
    }

    public Integer getPuerto() {
        return puerto;
    }

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

}
