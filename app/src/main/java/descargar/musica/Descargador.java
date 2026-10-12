
package descargar.musica;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Descargador {

    private final Config config;
    private static final String YTDLP = "yt-dlp";
    private static final String PLAYER_CLIENTS = "youtube:player_client=android,visionos,web_embedded";
    private static final Pattern PATRON_PROGRESO = Pattern.compile("(\\d+(?:\\.\\d+)?)% of");

    // constructor
    public Descargador(Config config) {
        this.config = config;
    }

    public void probarYtdlp() throws Exception {

        List<String> comando = List.of(YTDLP, "--version");

        int codigo = ejecutarComando(comando, linea -> {
        });
        System.out.println("codigo de salida: " + codigo);
    }

    public void descargarCancion(String entrada, Consumer<Integer> alProgreso, Consumer<String> alEstado)
            throws Exception {

        File carpeta = new File(config.getCarpetaDestino());
        carpeta.mkdirs();
        String busqueda = "https://music.youtube.com/search?q="
                + URLEncoder.encode(entrada, StandardCharsets.UTF_8);

        List<String> comando = new ArrayList<>();
        comando.add(YTDLP);
        comando.add("--extractor-args");
        comando.add(PLAYER_CLIENTS);
        comando.add("--no-warnings");
        comando.add("--newline");
        comando.add("-x");
        comando.add("--audio-format");
        comando.add(config.getFormato());
        comando.add("--no-playlist");
        comando.add("-o");
        comando.add(config.getCarpetaDestino() + File.separator + "%(title)s.%(ext)s");
        comando.add("-I");
        comando.add("1");
        comando.add("--embed-thumbnail");
        comando.add("--convert-thumbnails");
        comando.add("jpg");
        comando.add("--embed-metadata");
        comando.add("--");
        comando.add(busqueda);

        int codigo = ejecutarComando(comando, linea -> {
            if (linea.startsWith("[download]")) {
                Matcher m = PATRON_PROGRESO.matcher(linea);
                if (m.find()) {
                    alProgreso.accept((int) Double.parseDouble(m.group(1)));
                }
            } else if (linea.startsWith("[ExtractAudio]") || linea.startsWith("[EmbedThumbnail]")) {
                alEstado.accept("Convirtiendo y agregando portada...");
            }
        });

        if (codigo == 0) {
            System.out.println("Descarga Completa");
        } else {
            throw new RuntimeException("Error De Descarga Codigo: " + codigo);
        }
    }

    private int ejecutarComando(List<String> comando, Consumer<String> alRecibirLinea) throws Exception {

        ProcessBuilder pb = new ProcessBuilder(comando);

        pb.redirectErrorStream(true);
        Process proceso = pb.start();

        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(proceso.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                System.out.println(linea);
                alRecibirLinea.accept(linea);
            }
        }

        int codigo = proceso.waitFor();
        return codigo;
    }
}