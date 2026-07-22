package nexusgo.controller;

import nexusgo.view.AperturaCierre;
import nexusgo.model.CajaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import nexusgo.model.Usuario;

public class ControladorCaja implements ActionListener {

    private AperturaCierre vista;
    private CajaDao dao;
    private final Usuario usuarioLogueado;
    private int idCajaActual = 0;

    public ControladorCaja(AperturaCierre vista, Usuario usuarioLogueado) {
        this.vista = vista;
        this.usuarioLogueado = usuarioLogueado;
        this.dao = new CajaDao();

        vista.getBtnApertura().addActionListener(this);
        vista.getBtnCalcular().addActionListener(this);

        vista.getTxtMontoF().setEnabled(false);
        vista.getBtnCalcular().setEnabled(false);
    }

    public int getICajaActual() {
        return idCajaActual;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnApertura()) {
            double monto = Double.parseDouble(vista.getTxtMontoInicial().getText().replace("$", "").trim());
            if (monto > 0) {
                idCajaActual = dao.guardarApertura(monto, usuarioLogueado.getIdUsuario());
                if (idCajaActual > 0) {
                    vista.getLbltxtMontoA().setText("$" + monto);
                    vista.getTxtMontoF().setEnabled(true);
                    vista.getBtnCalcular().setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo abrir la caja en la base de datos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (e.getSource() == vista.getBtnCalcular()) {
            double montoFinal = Double.parseDouble(vista.getTxtMontoF().getText().replace("$", "").trim());
            dao.guardarCierre(idCajaActual, montoFinal, montoFinal);
            vista.getLbltxtMontoTV().setText("$" + montoFinal);
        }
    }
}
