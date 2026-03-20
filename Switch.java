public class Switch extends RegistroNodo{


    private Integer puertos;
    private Integer numberVLAN;

    Switch(){
        this.puertos = 1;
        this.numberVLAN = 1;
    }

    Switch(Integer puertos, Integer numberVLAN){
        this.puertos = puertos;
        this.numberVLAN = numberVLAN;
    }
}
