package descargar.musica;

public class Config {

    private final String carpetaDestino;
    private final String formato;

    public Config(String carpetaDestino, String formato) {
        this.carpetaDestino = carpetaDestino;
        this.formato = formato;
    }

    public String getCarpetaDestino() {
        return carpetaDestino;
    }

    public String getFormato() {
        return formato;
    }

}
