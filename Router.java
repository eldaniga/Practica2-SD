public class Router extends RegistroNodo {
    private String ASN;
    private String protocolo;

    Router(){
        this.ASN = "";
        this.protocolo = "";

    }


    Router(String ASN, String protocolo){
        this.ASN = ASN;
        this.protocolo = protocolo;
    }
}
