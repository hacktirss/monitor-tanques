/**
 * SerialConfigurationDAO
 * DAO de la clase SerialConfiguration
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

public class SerialConfigurationDAO {

    private static final Logger log = Logger.getLogger(SerialConfigurationDAO.class);

    public static SerialConfiguration getDevice() throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "SELECT nombre port, baudRate, dataBits, stopBits, UPPER( parity ) parity, " +
                        "CASE WHEN UPPER( controlFlow ) = 'NONE' THEN 'N' WHEN UPPER( controlFlow ) = 'SOFTWARE' THEN 'S' WHEN UPPER( controlFlow ) = 'HARDWARE' THEN 'H' ELSE 'N' END controlFlow " +
                        "FROM dev_tty D JOIN dev_conf A ON A.dev_tty = D.id JOIN conf_tty C ON C.id = A.conf_tty WHERE D.cliente = 'S' AND D.activo = 'S'";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Recuperando configuración serie del dispositivo");
                    return SerialConfiguration.parse(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new TankSensorException(ex);
        }
        throw new TankSensorException("No se encontró información del dispositivo que define el Sensor de Tanques");
    }
}
