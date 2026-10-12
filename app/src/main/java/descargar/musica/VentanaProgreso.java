package descargar.musica;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class VentanaProgreso extends JDialog {

    private final JLabel etiquetaEstado;
    private final JProgressBar barra;

    public VentanaProgreso(JFrame padre) {
        super(padre, "Descargando", false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(400, 130);
        setLocationRelativeTo(padre);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        etiquetaEstado = new JLabel("Buscando la cancion...");
        barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);
        barra.setIndeterminate(true);

        add(etiquetaEstado, BorderLayout.NORTH);
        add(barra, BorderLayout.CENTER);
    }

    public void setProgreso(int porcentaje) {
        barra.setIndeterminate(false);
        barra.setValue(porcentaje);
        etiquetaEstado.setText("Descargando... " + porcentaje + "%");
    }

    public void setEstado(String texto) {
        etiquetaEstado.setText(texto);
        barra.setIndeterminate(true);
    }
}