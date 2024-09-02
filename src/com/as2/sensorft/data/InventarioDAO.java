/**
 * InventoryDAO
 * DAO de la clase Inventory
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.data;

import com.as2.sensorft.exceptions.TankSensorException;
import com.softcoatl.integration.ServiceLocator;
import com.softcoatl.tls.data.Inventory;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class InventarioDAO {
    
    private static final Logger log = Logger.getLogger(InventarioDAO.class);

    public static boolean create(Inventory inventario) throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "INSERT INTO tanques_h ( tanque, producto, volumen_actual, volumen_faltante, volumen_compensado, altura, agua, temperatura, fecha_hora_veeder, fecha_hora_s ) "
                + "SELECT tanque, producto, ?, ?, ?, ?, ?, ?, ?, NOW() FROM tanques WHERE tanque = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inventario.getVolumen().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setInt(2, inventario.getFaltante().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setInt(3, inventario.getTcVolumen().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setBigDecimal(4, inventario.getAltura());
            ps.setBigDecimal(5, inventario.getAgua());
            ps.setBigDecimal(6, inventario.getTemperatura().setScale(2, RoundingMode.HALF_EVEN));
            ps.setTimestamp(7, new Timestamp(inventario.getFecha().getTimeInMillis()));
            ps.setInt(8, inventario.getTanque());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error(ex);
        }
        return false;
    }

    public static boolean update(Inventory descarga) throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "UPDATE tanques SET volumen_actual = ?, volumen_faltante = ?, volumen_compensado = ?, altura = ?, agua = ?, temperatura = ?, fecha_hora_veeder = ?, fecha_hora_s = NOW() WHERE tanque = ?";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, descarga.getVolumen().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setInt(2, descarga.getFaltante().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setInt(3, descarga.getTcVolumen().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setBigDecimal(4, descarga.getAltura());
            ps.setBigDecimal(5, descarga.getAgua());
            ps.setBigDecimal(6, descarga.getTemperatura().setScale(2, RoundingMode.HALF_EVEN));
            ps.setTimestamp(7, new Timestamp(descarga.getFecha().getTimeInMillis()));
            ps.setInt(8, descarga.getTanque());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error(ex);
        }
        return false;
    }
    
    public static boolean register(Inventory descarga) throws TankSensorException {
        return update(descarga) && create(descarga);
    }
}
