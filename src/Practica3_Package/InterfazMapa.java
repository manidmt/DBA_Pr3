package Practica3_Package;

/**
 *
 * @author acarriq
 */
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Practica3_Package.enums.Comportamiento;

public class InterfazMapa extends JFrame {

    private JPanel panel;
    private JButton[][] botones;
    private Coordenadas inicioSeleccionado;
    private Coordenadas objetivoSeleccionado;
    private boolean seleccionandoInicio = true;
    private boolean seleccionandoObjetivo = false;
    private JLabel contadorPasosLabel;
    private JLabel coordenadasLabel;
    private Buscador buscador;

    public InterfazMapa(int filas, int columnas, Buscador buscador) {
        super("Mapa del Agente");
        panel = new JPanel(new GridLayout(filas, columnas));
        botones = new JButton[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton();
                final int filaFinal = i; // Variable final para i
                final int columnaFinal = j; // Variable final para j

                botones[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        manejarClicEnBoton(filaFinal, columnaFinal);
                    }
                });

                botones[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        mostrarCoordenadas(filaFinal, columnaFinal);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        ocultarCoordenadas();
                    }
                });
                panel.add(botones[i][j]);
                this.buscador = buscador;

            }
        }

        // Label para el numero de pasos
        contadorPasosLabel = new JLabel("Pasos: 0");
        this.add(contadorPasosLabel, BorderLayout.SOUTH); //  parte inferior

        //Label para mostrar las coordenadas
        coordenadasLabel = new JLabel("Coordenadas: ");
        this.add(coordenadasLabel, BorderLayout.NORTH);

        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600); // tamaño interfaz
        this.setVisible(true);
    }

    public void actualizarBoton(int fila, int columna, String texto) {
        JButton boton = botones[fila][columna];

        // Color de fondo de la casilla
        if ("-1".equals(texto)) {
            boton.setBackground(Color.BLACK); // Color para obstáculos
        } else if ("I".equals(texto)) {
            boton.setBackground(Color.PINK); // Color para el inicio
        } else if ("F".equals(texto)) {
            boton.setBackground(Color.RED); // Color para el objetivo
        } else if ("C".equals(texto)) {
            boton.setBackground(Color.LIGHT_GRAY); // Color para las casillas visitadas
        } else if ("X".equals(texto)) {
            boton.setBackground(Color.BLUE); // Color para la casilla actual
        } else {
            boton.setBackground(Color.WHITE); // Color por defecto
        }
    }

    private void manejarClicEnBoton(int fila, int columna) {
        JButton boton = botones[fila][columna];
        if (seleccionandoInicio) {
            inicioSeleccionado = new Coordenadas(fila, columna);
            boton.setBackground(Color.GREEN);
            seleccionandoInicio = false;
            seleccionandoObjetivo = false;
            buscador.getEntorno().setPosicion(inicioSeleccionado);
            buscador.setComportamiento(Comportamiento.COMUNICACION_SANTA);
        }
    }

    public void actualizarContadorPasos(int numPasos) {
        contadorPasosLabel.setText("Pasos: " + numPasos);
    }

    private void mostrarCoordenadas(int fila, int columna) {
        coordenadasLabel.setText("Coordenadas: (" + fila + ", " + columna + ")");
    }

    private void ocultarCoordenadas() {
        coordenadasLabel.setText("");
    }

    public Coordenadas getInicioSeleccionado() {
        return inicioSeleccionado;
    }

    public Coordenadas getObjetivoSeleccionado() {
        return objetivoSeleccionado;
    }

    public void setObjetivoSeleccionado(Coordenadas objetivoSeleccionado) {
        this.objetivoSeleccionado = objetivoSeleccionado;
    }

}
