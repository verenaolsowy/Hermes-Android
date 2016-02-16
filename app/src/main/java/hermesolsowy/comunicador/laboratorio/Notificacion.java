package hermesolsowy.comunicador.laboratorio;

import java.util.Date;

/**
 * Created by Verena on 16/02/2016.
 */
public class Notificacion {

    private String contenido;
    private String contexto;
    private String categoria;
    private String nombre;
    private String apellido;
    private String fecha_envio;

    public Notificacion(String apellido, String nombre, String categoria, String contexto, String contenido) {
            this.apellido = apellido;
            this.contexto = contexto;
            this.categoria = categoria;
            this.nombre = nombre;
            this.contenido = contenido;

            fecha_envio = (new Date()).toString(); //la fecha de envío es la fecha de ahora
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(String fecha_envio) {
        this.fecha_envio = fecha_envio;
    }
}
