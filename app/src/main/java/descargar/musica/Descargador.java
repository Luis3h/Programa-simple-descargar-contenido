
package descargar.musica;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Descargador {

    private final Config config;

    // constructor
    public Descargador(Config config) {
        this.config = config;
    }

    // metodo para probar si yt-dlp esta instalado y funcionando
    public void probarYtdlp() throws Exception {

        List<String> comando = List.of("yt-dlp", "--version");

        int codigo = ejecutarComando(comando);
        System.out.println("codigo de salida: " + codigo);

    }

    // metodo para descargar una cancion de youtube
    public void descargarCancion(String entrada) throws Exception {

        File carpeta = new File(config.getCarpetaDestino());
        carpeta.mkdirs();

        // comando para ejecutar yt-dlp con los argumentos necesarios
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
        comando.add("--default-search");
        comando.add("ytsearch");
        comando.add("--");
        comando.add(entrada);

        int codigo = ejecutarComando(comando);

        if (codigo == 0) {
            System.out.println("Descarga Completa");
        } else {
            throw new RuntimeException("Error De Descarga Codigo: " + codigo);
        }
    }

    private int ejecutarComando(List<String> comando) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(comando);

        pb.redirectErrorStream(true);
        Process proceso = pb.start();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(proceso.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;

            // bucle para leer la salida del proceso y mostrarla en consola
            while ((linea = lector.readLine()) != null) {
                System.out.println(linea);

            }
        }

        int codigo = proceso.waitFor();
        return codigo;

    }

}