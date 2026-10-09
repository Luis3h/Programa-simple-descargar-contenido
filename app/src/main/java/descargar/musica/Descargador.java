
package descargar.musica;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Descargador {

    private final Config config;

    public Descargador(Config config) {
        this.config = config;
    }

    public void probarYtdlp() throws Exception {

        ProcessBuilder pb = new ProcessBuilder("yt-dlp", "--version");

        pb.redirectErrorStream(true);
        Process proceso = pb.start();

        BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        String linea;

        while ((linea = lector.readLine()) != null) {
            System.out.println(linea);

        }
        int codigo = proceso.waitFor();
        System.out.println("codigo de salida: " + codigo);

    }

    public void descargarCancion(String entrada) throws Exception {

        File carpeta = new File(config.getCarpetaDestino());
        carpeta.mkdirs();

        List<String> comando = new ArrayList<>();
        comando.add("yt-dlp");
        comando.add("--extractor-args");
        comando.add("youtube:player_client=android,visionos,web_embedded");
        comando.add("--no-warnings");
        comando.add("-x");
        comando.add("--audio-format");
        comando.add(config.getFormato());
        comando.add("--no-playlist");
        comando.add("-o");
        comando.add(config.getCarpetaDestino() + File.separator + "%(title)s.%(ext)s");
        comando.add(entrada);

        ProcessBuilder pb = new ProcessBuilder(comando);

        pb.redirectErrorStream(true);
        Process proceso = pb.start();

        BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
        String linea;

        while ((linea = lector.readLine()) != null) {
            System.out.println(linea);

        }

        int codigo = proceso.waitFor();

        if (codigo == 0) {
            System.out.println("Descarga Completa");
        } else {
            System.out.println("Error De Descarga Codigo: " + codigo);
        }
    }

}