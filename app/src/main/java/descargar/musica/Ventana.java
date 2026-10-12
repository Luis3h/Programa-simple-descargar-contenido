package descargar.musica;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class Ventana extends javax.swing.JFrame {

    private Descargador cargador;

    private JLabel etiquetaInstruccion;
    private JTextField campoTextoLink;
    private JButton botonDescargar;

    public Ventana(Descargador descargarDesdeApp) {

        this.cargador = descargarDesdeApp;

        // configuracion de la ventana
        setTitle("Descargador de Musica");
        setSize(450, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // organiza los componentes Uno tras otro
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // inicializar componentes
        etiquetaInstruccion = new JLabel("Escribe el nombre de la cancion y el artista:");
        campoTextoLink = new JTextField(30); // ancho de la caja de texto
        botonDescargar = new JButton("Descargar Musica");

        // que sucede cuando el usuario hace click en el boton
        botonDescargar.addActionListener(e -> iniciarDescarga());
        campoTextoLink.addActionListener(e -> iniciarDescarga());

        // agregar componentes a la ventana , esto los hace visibles
        add(etiquetaInstruccion);
        add(campoTextoLink);
        add(botonDescargar);
    }

    public void iniciarDescarga() {
        String entradaUsuario = campoTextoLink.getText().trim();

        if (entradaUsuario.isEmpty()) {
            return;
        }

        botonDescargar.setEnabled(false);
        campoTextoLink.setEnabled(false);
        botonDescargar.setText("Descargando...");

        VentanaProgreso progreso = new VentanaProgreso(this);
        progreso.setVisible(true);

        SwingWorker<Void, Integer> trabajo = new SwingWorker<>() {

            @Override
            protected Void doInBackground() throws Exception {
                cargador.descargarCancion(
                        entradaUsuario,
                        porcentaje -> publish(porcentaje),
                        texto -> SwingUtilities.invokeLater(() -> progreso.setEstado(texto)));
                return null;
            }

            @Override
            protected void process(List<Integer> porcentajes) {
                progreso.setProgreso(porcentajes.get(porcentajes.size() - 1));
            }

            @Override
            protected void done() {
                progreso.dispose();
                try {
                    get();
                    JOptionPane.showMessageDialog(Ventana.this, "Musica Descargada Con Exito!");
                    campoTextoLink.setText("");
                } catch (Exception ex) {
                    Throwable causa = ex.getCause() != null ? ex.getCause() : ex;
                    JOptionPane.showMessageDialog(Ventana.this,
                            "Hubo Un Error Al Descargar\n" + causa.getMessage());
                    causa.printStackTrace();
                } finally {
                    botonDescargar.setEnabled(true);
                    campoTextoLink.setEnabled(true);
                    botonDescargar.setText("Descargar Musica");
                }
            }
        };
        trabajo.execute();
    }
}