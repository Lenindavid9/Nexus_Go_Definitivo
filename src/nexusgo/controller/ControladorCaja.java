
package nexusgo.controller;

import nexusgo.view.AperturaCierre;
import nexusgo.model.CajaDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorCaja implements ActionListener{
    

    private AperturaCierre vista;
    private CajaDao dao;

    public ControladorCaja(AperturaCierre vista) {
        this.vista = vista;
        this.dao = new CajaDao();

        vista.getBtnApertura().addActionListener(this);
        vista.getBtnCalcular().addActionListener(this);

        vista.getTxtMontoF().setEnabled(false);
        vista.getBtnCalcular().setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnApertura()) {
            double monto = Double.parseDouble(vista.getTxtMontoInicial().getText().replace("$", "").trim());
            if (monto > 0) {
                dao.guardarApertura(monto);
                vista.getLbltxtMontoA().setText("$" + monto);
                vista.getTxtMontoF().setEnabled(true); 
            }
        } else if (e.getSource() == vista.getBtnCalcular()) {
            double montoFinal = Double.parseDouble(vista.getTxtMontoF().getText().replace("$", "").trim());
            dao.guardarCierre(montoFinal);
            vista.getLbltxtMontoTV().setText("$" + montoFinal);
        }
    }
}
