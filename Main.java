public class Main {
}


HashMap<String, RegistroNodo> nodos = new HashMap<>();
Scanner sc = new Scanner(System.in);


boolean validarIP(String ip){
    //int contador = 0;
    String[] ips = ip.split("\\.");

    //Mejora en esta funcion
    if(ip.isBlank() || ip == null){
        return false;
    }

    if(ips.length != 4){
        return false;
    }

    for(int i = 0 ; i < ips.length; i++){
        if(Integer.parseInt(ips[i]) < 0 ||  Integer.parseInt(ips[i]) > 255) {
            System.out.println(ips[i]);
            return false;
        }
    }

    return true;
}
//Funciones auxiliares necesarias
boolean esTipoNodoValido(String tipo) {
    try {
        RegistroNodo.TipoNodo.valueOf(tipo.toUpperCase());
        return true;
    } catch (IllegalArgumentException e) {
        //System.out.println("No es valido el tipo puesto [ROUTER/SWITCH/AP/HOST]");
        return false;
    }
}

boolean esEstadoNodoValido(String estado) {
    try {
        RegistroNodo.EstadoNodo.valueOf(estado.toUpperCase());
        return true;
    } catch (IllegalArgumentException e) {
        //System.out.println("No es valido el estado puesto [UP/DOWN/MAINT]");
        return false;
    }
}
//Fin de funciones auxiliares

void  altaNodo(String id, String hostname,String ip, String ubicacion, String tipo, String estado ) throws NotUniqueKey, NotValidIp, IllegalArgumentException{
        //validamos que no se repita el id por la funcionalidad de HashMap
        //validamos que el ID no este vacio
        //validamos tambien que la IP sea coherente

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede ser vacío");
        }
        if(nodos.containsKey(id) ){
            throw new NotUniqueKey("Ya se encuentra ese ID registrado");
        }
        if(!validarIP(ip)){
            throw new NotValidIp("Esta IP no es valida");
        }
        if(esTipoNodoValido(tipo)){
            if( esEstadoNodoValido(estado)){
                RegistroNodo.TipoNodo wrapperTipo = RegistroNodo.TipoNodo.valueOf(tipo.toUpperCase());
                RegistroNodo.EstadoNodo wrapperEstado = RegistroNodo.EstadoNodo.valueOf(estado.toUpperCase());

                RegistroNodo nodo =  new RegistroNodo(id, hostname, ip, ubicacion, wrapperTipo, wrapperEstado);
                nodos.put(id, nodo);
            }else{
                throw new IllegalArgumentException("No es valido el estado puesto [UP/DOWN/MAINT]");
            }


        }else{
            throw new IllegalArgumentException("No es valido el tipo puesto [ROUTER/SWITCH/AP/HOST]");
        }



}

//Mostrar todos los Nodos
ArrayList<RegistroNodo> listarNodos(){
        ArrayList<RegistroNodo> arrayNodos = new ArrayList<>(nodos.values());
        return arrayNodos;
}

boolean mostrarNodos(){
    //esta funcion la puedo mejorar para que funcione como decorador
    Integer contador = 1;
    if(nodos.isEmpty()){
        return false;
    }
    for(RegistroNodo nodo : listarNodos()){
        System.out.println(contador + ": " + nodo.toString());
        contador++;

    }
    return true;
}

RegistroNodo buscarPorId(String id) throws NotValidIdException{
    if(nodos.containsKey(id)) {
        RegistroNodo nodo = nodos.get(id);
        return nodo;
    }else{
        throw new NotValidIdException("No existe dicha id en la lista");
    }
}
//Funcion creada porque es usada por varias funciones
void verificarId(String id) throws NotValidIdException{
    if (id.isBlank() || id == null) {
        throw new NotValidIdException("El ID esta vacia o es nula");
    }

    if (!nodos.containsKey(id)){
        throw new NotValidIdException("No existe dicha id en la lista");
    }

}

void eliminarPorId(String id) throws NotValidIdException{
    verificarId(id);
    nodos.remove(id);
}

RegistroNodo buscarPorIpNormal(String ip) throws  NotValidIp {
    if (!validarIP(ip)) {
        throw new NotValidIp("La IP a buscar no es válida");
    }

    for (RegistroNodo nodo : nodos.values()) {
        if (nodo.getIp().equals(ip)) {
            return nodo;
        }

    }
    throw new NotValidIp("<< No existe dicha IP en el inventario >>");

}

    ArrayList<RegistroNodo> buscarPorIp (String ip){
        String regex = ".*" + Pattern.quote(ip) + ".*";
        Pattern pattern = Pattern.compile(regex);

        ArrayList<RegistroNodo> nodosFiltradosIp = new ArrayList<>();

        for (RegistroNodo nodo : nodos.values()) {
            String[] texto = nodo.toString().split("\\ ");
            ArrayList<String> texto2 = new ArrayList<>();
            texto2 = new ArrayList<>(Arrays.asList(texto));

            //DEBUG
            //System.out.println(texto2);

            Matcher matcher = pattern.matcher(texto[7]);            // es la posicion donde se splitea la IP

            if (matcher.find() || nodo.toString().contains(ip)) {  //si la ip tiene algo de lo que puse
                nodosFiltradosIp.add(nodo);
                //System.out.println("Encontrada en nodo: " + matcher.group());
            }
        }

        return nodosFiltradosIp;


    }

    ArrayList<RegistroNodo> buscarPorUbicacion (String ubicacion){
                ArrayList<RegistroNodo> nodosFiltrados = new ArrayList<>();
                String regex = ".*" + Pattern.quote(ubicacion) + ".*";
                Pattern pattern = Pattern.compile(regex);



                 for(RegistroNodo nodo: nodos.values()){
                     String[] texto =  nodo.toString().split("\\ ");
                     ArrayList<String> texto2 = new ArrayList<>(Arrays.asList(texto));
                     //System.out.println(texto2);

                     Matcher matcher = pattern.matcher(texto[13]);
                     if(matcher.find()){
                         nodosFiltrados.add(nodo);
                     }

                }


                return nodosFiltrados;
    }


ArrayList<RegistroNodo> filtrarEstados(String estado){
            ArrayList<RegistroNodo> nodosFiltrados = new ArrayList<>();

            if(!esEstadoNodoValido(estado)){
                return null;
            }
            RegistroNodo.EstadoNodo wrapperEstado = RegistroNodo.EstadoNodo.valueOf(estado.toUpperCase());


            for(RegistroNodo nodo : listarNodos()){
                if(nodo.getEstado().equals(wrapperEstado)){
                    nodosFiltrados.add(nodo);
                }

            }
            return  nodosFiltrados;

}

boolean cambiarEstado(String id) throws NotValidIdException{
        verificarId(id);

        String estado;
        System.out.println("Introduzca NUEVO estado: ");
        estado = sc.nextLine();

        if(esEstadoNodoValido(estado)){
            RegistroNodo nodo = nodos.get(id);
            nodo.setEstado(RegistroNodo.EstadoNodo.valueOf(estado.toUpperCase()));
            return true;
        }
        throw new IllegalArgumentException("Ese estado no es válido [UP/DOWN/MAINT]");

}

boolean guardarInventario (String ruta){
    try{
        //se puede mejorar con un BufferedObjectStream
        FileOutputStream fos = new FileOutputStream(ruta + "nodos.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(nodos);
        return true;
    }catch (Exception e){
        System.out.println(e.getMessage());
        return false;
    }
}

boolean guardarEnTxt(){
    try{
        FileWriter fw = new FileWriter("Inventario.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        for(RegistroNodo nodo : listarNodos()){
            bw.write(nodo.toString());
            bw.newLine();
        }
        bw.close();
        return true;
    }catch (Exception e){
        System.out.println(e.getMessage());
        return false;
    }
}
boolean cargarInventario(String ruta){
    try{
        FileInputStream fis = new FileInputStream(ruta + "nodos.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);
        nodos = (HashMap<String, RegistroNodo>) ois.readObject();
        return true;
    } catch(FileNotFoundException e){
        System.out.println("El fichero no existe. Por tanto, no se recuperan datos...");
        nodos = new HashMap<>();
        return false;


    }catch (IOException e){
        System.out.println("Error el leer el fichero. No se recuperarán los datos...");
        nodos = new HashMap<>();
        return false;

    }catch (ClassNotFoundException e){
        System.out.println("Error al reconstruir los datos. La memoria estará vacía...");
        nodos = new HashMap<>();

        return false;

    }


}
void generarNodosAleatorios(int cantidad) {
    //Crear aleatoriamente algunos nodos, segun variable cantidad

    Random random = new Random();

    String[] hostnames = {"router", "switch", "ap", "host", "server"};
    String[] ubicaciones = {"CPD", "Planta1", "Planta2", "Oficina", "Laboratorio"};

    RegistroNodo.TipoNodo[] tipos = RegistroNodo.TipoNodo.values();
    RegistroNodo.EstadoNodo[] estados = RegistroNodo.EstadoNodo.values();

    for (int i = 1; i <= cantidad; i++) {

        String id = "N" + (random.nextInt(900) + 100);

        String hostname = hostnames[random.nextInt(hostnames.length)] + "-" + random.nextInt(50);

        String ip = "192.168." + random.nextInt(255) + "." + random.nextInt(255);

        String ubicacion = ubicaciones[random.nextInt(ubicaciones.length)];

        String tipo = tipos[random.nextInt(tipos.length)].name();

        String estado = estados[random.nextInt(estados.length)].name();

        try {
            altaNodo(id, hostname, ip, ubicacion, tipo, estado);
        } catch (Exception e) {
            i--;                        // si falla vuelve a intentar
        }
    }
}

void main(String[] args){

    int opcion;
    do {
        mostrarMenu();
        try {
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> {
                    String id;
                    String hostname;
                    String ip;
                    String ubicacion;
                    String tipo;
                    String estado;
                    System.out.println("Introduzca ID: ");
                    id = sc.nextLine();
                    //Implementado para evitar escribir demás si ya el ID, está incorrecto
                    //En altaNodo se volvera a verificar
                    if(nodos.containsKey(id)){
                       throw new NotValidIdException("Esa ID ya esta registrado... Cuando cree otro nodo asegurese de que sea diferente [-]");
                    }

                    System.out.println("Introduzca Hostname: ");
                    hostname = sc.nextLine();
                    System.out.println("Introduzca IP: ");
                    ip = sc.nextLine();
                    System.out.println("Introduzca Ubicacion: ");
                    ubicacion = sc.nextLine();
                    System.out.println("Introduzca Tipo: ");
                    tipo = sc.nextLine();
                    System.out.println("Introduzca Estado: ");
                    estado = sc.nextLine();

                    //Esta funcion realiza una valdiacion de la IP
                    try{
                        altaNodo(id, hostname, ip, ubicacion, tipo, estado);
                        System.out.println("Nodo dado de alta satisfactoriamente [+]");
                        System.out.println();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }



                }
                case 2 -> {
                    if(!mostrarNodos()){ //le paso dicho array
                        System.out.println("Aun no hay nodos registrados");
                    }
                    //En cualquier oto caso, se mostraran los nodos
                    //la mejor funcion que he hecho

                }

                case 3 ->{
                    //Vaciar memoria
                    nodos.clear();
                    System.out.println("Memoria vaciada satisfactoriamente [+]");
                    System.out.println();
                }

                case 4 -> {
                    String id;
                    System.out.println("Coloque el ID a buscar: ");
                    id = sc.nextLine();
                        try{
                            System.out.println(buscarPorId(id).toString());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                }

                case 5 -> {
                    String id;
                    System.out.println("Introduzca el ID a eliminar: ");
                    id= sc.nextLine();
                    try{
                        eliminarPorId(id);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }

                case 6 ->{
                    String ip;
                    System.out.println("Introduzca la direccion IP: ");
                    ip = sc.nextLine();

                    try{
                        System.out.println(buscarPorIpNormal(ip)); //funcion original base
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                }
                case 7 ->{
                    String ip;
                    System.out.println("Introduzca la direccion IP: ");
                    ip = sc.nextLine();

                    if(ip.isEmpty()){
                        //no tiene sentido buscar nada
                        System.out.println("Esta funcionalidad esta desabilitada [-]... pero funciona");
                        continue;
                    }

                    try{
                        ArrayList<RegistroNodo> nodosFiltradosIp = buscarPorIp(ip);
                        if(nodosFiltradosIp.isEmpty()){
                            System.out.println("<< No se encontró ninguna coincidencia>>");
                        }else{
                            Integer contador = 1;
                            for(RegistroNodo nodo: nodosFiltradosIp){
                                System.out.println(contador  + ": " + nodo.toString());
                                contador++;
                            }
                        }

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }


                }
                case 8 -> {
                    //CambiarEstado
                    String id;
                    System.out.println("Introduzca el ID del Nodo >> ");
                    id = sc.nextLine();
                    try{
                        if(cambiarEstado(id)){
                            System.out.println("Estado cambiado satisfactoriamente [+]");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 9 ->{
                    String estado;
                    System.out.println("Introduzca el estado a FILTRAR: [UP/DOWN/MAINT]");

                    estado = sc.nextLine();

                    ArrayList<RegistroNodo> nodosFiltrados = filtrarEstados(estado);
                    //devuelve null si el estado es invalido
                    if(nodosFiltrados == null){
                            System.out.println("Este estado no es valido para buscar [UP/DOWN/MAINT]");
                    } else if (nodosFiltrados.size() == 0) {
                        System.out.println("No ha habido coincidencias >>");

                    } else{
                        Integer contador = 1;
                        for (RegistroNodo nodo : nodosFiltrados){
                            System.out.println(contador + ": " + nodo.toString());
                            contador++;
                        }
                    }



                }
                case 10 ->{
                    String ubicacion;
                    System.out.println("Introduzca la ubicaciona buscar >> ");
                    ubicacion = sc.nextLine();

                    Integer contador = 1;
                    ArrayList<RegistroNodo> nodosFiltrados = buscarPorUbicacion(ubicacion);
                    if(nodosFiltrados.isEmpty()){
                        System.out.println("<< No hay ningun nodo con dicha ubicacion >>");
                        continue;
                    }
                    for(RegistroNodo nodo : nodosFiltrados ){
                        System.out.println(contador + ": " + nodo.toString());
                        contador++;

                    }
                }
                case 11 -> {
                    //Exportar a .dat
                    String ruta;
                    System.out.println("Introduzca la ruta a guardar: [''para actual]");
                    ruta = sc.nextLine();

                    if(guardarInventario(ruta)){
                        System.out.println("Guardado de forma correcta");
                    }
                }

                case 12 ->{
                    //Exportar a Inventario.txt
                    if(guardarEnTxt()){
                        System.out.println("Datos guardados correctamente [+]");
                    }

                }


              case 13 ->{
                    //Remplazar el archivo en memoria
                  nodos.clear(); //mejorar -> guardar los datos actuales hasta que se compruebe si se puede recuperar los datos o no

                  String ruta;
                  System.out.println("Introduzca la ruta a recuperar: ['' para actual]");
                  ruta = sc.nextLine();

                  if(cargarInventario(ruta)){
                      System.out.println("Datos cargados correctamente...");
                      System.out.println("Se listaran los datos nuevos a continuacion");
                      System.out.println();
                      mostrarNodos();
                  }


              }
              case 14 ->{
                    programaPrueba();
              }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida, intenta de nuevo.");
            }
        } catch (OutOfMemoryError e) {
            System.out.println(e.getMessage());
            opcion = -1;
        }
        catch (NotValidIdException e) {
            System.out.println(e.getMessage());
            opcion = -1;
        } catch (Exception e) {
            System.out.println("Error: debes ingresar un número.");
            opcion = -1;
            //sc.nextLine();
        }
    } while (opcion != 0);
}

void mostrarMenu() {
    System.out.println("\n--- Menú Inventario de Nodos ---");
    System.out.println("1. Alta de nodo");
    System.out.println("2. Listado de nodos");
    System.out.println("3. Vaciar Memoria");
    System.out.println("4. Buscar por ID");
    System.out.println("5. Eliminar por ID");
    System.out.println("6. Busqueda por IP");
    System.out.println("7. Busqueda por IP [REGEX]");
    System.out.println("8. Cambiar estado");
    System.out.println("9. Filtrar segun Estado");
    System.out.println("10. Filtrar segun ubicacion");
    System.out.println("11. Guardar inventario");
    System.out.println("12. Exportar a Inventario.txt");
    System.out.println("13. Cargar inventario");
    System.out.println("14. PROGRAMA DE PRUEBA");
    System.out.println("0. Salir");
    System.out.print("Elige una opción: ");

}



void programaPrueba() {

    System.out.println("==== INICIO PROGRAMA DE PRUEBA ====");
    Integer cantidad;
    System.out.println("Introduzca el numero de nodos a generar: ");
    cantidad = sc.nextInt();

    sc.nextLine();   //limpiar buffer

    try {
        // 1. Crear 5 nodos
        generarNodosAleatorios(cantidad);
        System.out.println("\nNodos creados correctamente.\n");

    } catch (Exception e) {
        System.out.println("Error creando nodos de prueba: " + e.getMessage());
    }

    // 2. Listar nodos
    System.out.println("---- LISTADO INICIAL ----");
    mostrarNodos();

    // 3. Guardar inventario
    System.out.println("\nGuardando inventario...");
    guardarInventario("");

    // 4. Vaciar memoria
    System.out.println("\nVaciando inventario en memoria...");
    nodos.clear();

    System.out.println("Nodos en memoria tras vaciar: " + nodos.size());

    // 5. Cargar desde fichero
    System.out.println("\nCargando inventario desde fichero...");
    cargarInventario("");

    // 6. Listar de nuevo
    System.out.println("\n---- LISTADO TRAS CARGAR ----");

    mostrarNodos();

    System.out.println("==== FIN PROGRAMA DE PRUEBA ====");
}


