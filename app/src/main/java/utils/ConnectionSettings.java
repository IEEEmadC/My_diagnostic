package utils;

/**
 * Created by admin on 12/6/17.
 */

public class ConnectionSettings {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = "";
    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "174.138.62.211";
    /**
     * URLs del Web Service
     */
    public static final String GETData = "http://" + IP + PUERTO_HOST + "/obtener_dataversion.php"; //id:dataversion
    public static final String GETCountry = "http://" + IP + PUERTO_HOST + "/obtener_paises.php";
    public static final String GETBlood_type = "http://" + IP + PUERTO_HOST + "/obtener_tipoSangre.php";
    public static final String GETDiseases_category = "http://" + IP + PUERTO_HOST + "/obtener_categorias.php";
    public static final String GETSymptom = "http://" + IP + PUERTO_HOST + "/obtener_metas.php";
    public static final String GET_BY_ID = "http://" + IP + PUERTO_HOST + "/obtener_meta_por_id.php";
    public static final String UPDATE = "http://" + IP + PUERTO_HOST + "/actualizar_meta.php";
    public static final String DELETE = "http://" + IP + PUERTO_HOST + "/borrar_meta.php";
    public static final String INSERT = "http://" + IP + PUERTO_HOST + "/insertar_meta.php";


        public static final String GetDisease = "http://" + IP + PUERTO_HOST + "/obtener_enfermedades.php";// el id del arrayJson es "diseases"
            public static final String GetDiseaseSymptom = "http://" + IP + PUERTO_HOST + "/obtener_multitabla.php"; // el id del arrayJson es "multitable"

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

}
