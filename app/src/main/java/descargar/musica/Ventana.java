package descargar.musica;

import java.util.concurrent.Flow;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;

public class Ventana extends javax.swing.JFrame {

    private Descargador cargador;

    private JLabel etiquetaInstruccion;
    private JTextField campoTextoLink;
    private JButton botonDescargar;

    public Ventana(Descargador descargarDesdeApp) {

        this.cargador = descargarDesdeApp;

        // configuracion de la ventana
        setTitle("Preparando todo");
        setSize(450, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // organiza los componentes Uno tras otro
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // inicializar componentes
        etiquetaInstruccion = new JLabel("Pega el enlace de YouTube o el Nombre de la Cancion:");
        campoTextoLink = new JTextField(30); // ancho de la caja de texto
        botonDescargar = new JButton("Descargar Musica");

        // que sucede cuando el usuario hace click en el boton
        botonDescargar.addActionListener(e -> {
            String entradaUsuario = campoTextoLink.getText().trim();

            if (!entradaUsuario.isEmpty()) {
                botonDescargar.setEnabled(false);
                botonDescargar.setText("Descargando...");

                // hilo secundario evita congelamiento de la ventana
                new Thread(() -> {
                    try {
                        cargador.descargarCancion(entradaUsuario);
                        javax.swing.JOptionPane.showMessageDialog(this, "Musica Descargada Con Exito!");

                    } catch (Exception ex) {
                        javax.swing.JOptionPane.showMessageDialog(this, "Hubo Un Error Al Descargar");
                        ex.printStackTrace();

                    } finally {
                        botonDescargar.setEnabled(true);
                        botonDescargar.setText("Descargar Musica");
                        campoTextoLink.setText("");

                    }
                }).start();

            }
        });

        // agregar componentes a la ventana , esto los hace visibles
        add(etiquetaInstruccion);
        add(campoTextoLink);
        add(botonDescargar);
    }
}