/**
 * DeviceDAO
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

public class DeviceDAO {

    private static final Logger log = Logger.getLogger(DeviceDAO.class);

    public static Device getDevice() throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "SELECT D.id, D.nombre, D.cliente, D.activo, D.tipo, P.valor protocol FROM dev_tty D JOIN parametros P ON TRUE "
                + "WHERE P.nombre = 'sensor' AND D.cliente = 'S' AND D.activo = 'S'";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Recuperando dispositivo");
                    return Device.parse(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new TankSensorException(ex);
        }
        throw new TankSensorException("No se encontró información del dispositivo que define el Sensor de Tanques");
    }
}
