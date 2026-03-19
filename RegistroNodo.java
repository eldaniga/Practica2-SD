//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.io.Serializable;

class RegistroNodo implements Serializable {
    public enum TipoNodo {
        ROUTER,
        SWITCH,
        AP,
        HOST
    }
    public  enum EstadoNodo{
        UP,
        DOWN,
        MAINT
    }


    private String id;
    private String hostname;
    private String ip;
    private String ubicacion;
    private TipoNodo tipo;
    private EstadoNodo estado;

    public static Integer serialVersionUID;


    public RegistroNodo (){
        this.hostname = "";
        this.ip ="";
        this.ubicacion = "";
        this.id = "";
        this.tipo = TipoNodo.HOST;
        this.estado = EstadoNodo.DOWN; //consideraciones por defecto

    }
    public RegistroNodo(String id, String hostname, String ip, String ubicacion, TipoNodo tipo, EstadoNodo estado){
        this.id = id;
        this.hostname = hostname;
        this.ip = ip;
        this.ubicacion = ubicacion;

        this.tipo = tipo;
        this.estado = estado;


    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public TipoNodo getTipo() {
        return tipo;
    }

    public void setTipo(TipoNodo tipo) {
        this.tipo = tipo;
    }
    public EstadoNodo getEstado () {
        return this.estado;
    }

    public void setEstado(EstadoNodo estado) {
       this.estado = estado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;           // misma referencia
        if (obj == null || getClass() != obj.getClass()) return false; // diferente tipo

        RegistroNodo other = (RegistroNodo) obj;
        return (id != null && id.equals(other.id) ); // comparar por id
    }
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public String toString(){
        return (" Id: " + this.id +
                " Hostname: " + this.hostname +
                " Direccion IP: " + this.ip +
                " Tipo: " + this.tipo +
                " Estado: " + this.estado +
                " Ubicacion: " + this.ubicacion);
    }
}



