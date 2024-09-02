/**
 * ParametrosDAO
 * DAO de la clase Device
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.data;

import com.as2.sensorft.exceptions.TankSensorException;
import com.softcoatl.integration.ServiceLocator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class ParametrosDAO {

    private static final Logger log = Logger.getLogger(ParametrosDAO.class);

    public static String getParameter(String key, String defaultValue) throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "SELECT IFNULL( valor, ? ) valor FROM parametros WHERE nombre = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, defaultValue);
            ps.setString(2, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Recuperando parametro " + key + " = " + rs.getString("valor"));
                    return rs.getString("valor");
                }
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new TankSensorException(ex);
        }
        throw new TankSensorException("No se encontró información del dispositivo que define el Sensor de Tanques");
    }
}
