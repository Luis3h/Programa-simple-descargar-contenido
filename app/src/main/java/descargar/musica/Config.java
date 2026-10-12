package descargar.musica;

public class Config {

    // clase para almacenar la configuracion de la aplicacion

    private final String carpetaDestino;
    private final String formato;

    // constructor
    public Config(String carpetaDestino, String formato) {
        this.carpetaDestino = carpetaDestino;
        this.formato = formato;
    }

    // getters
    public String getCarpetaDestino() {
        return carpetaDestino;
    }

    public String getFormato() {
        return formato;
    }

}
