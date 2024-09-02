/**
 * InetConfigurationDAO
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

public class InetConfigurationDAO {

    public static final String DEV = "/dev/ttySensor";

    private static final Logger log = Logger.getLogger(InetConfigurationDAO.class);

    public static InetConfiguration get() throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "SELECT C.protocol, C.host, C.usr, C.pwd, C.port "
                + "FROM dev_tty D JOIN conf_ip C ON C.iddev = D.id WHERE D.cliente = 'S' AND D.activo = 'S'";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Recuperando configuración inet del dispositivo: " + DEV);
                    return InetConfiguration.parse(rs);
                }
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new TankSensorException(ex);
        }
        throw new TankSensorException("No se encontró información del dispositivo que define el Sensor de Tanques");
    }
}
