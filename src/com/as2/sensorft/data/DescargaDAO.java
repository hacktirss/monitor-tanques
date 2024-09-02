/**
 * DeliveryDAO
 * DAO de la clase Delivery
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.data;

import com.as2.sensorft.exceptions.TankSensorException;
import com.softcoatl.integration.ServiceLocator;
import com.softcoatl.tls.data.Delivery;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class DescargaDAO {
    
    private static final Logger log = Logger.getLogger(DescargaDAO.class);

    public static boolean exists(Delivery descarga) throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "SELECT COUNT(*) items "
                + "FROM descargas "
                + "WHERE tanque = ? "
                + "AND DATE_FORMAT( fInicio, '%Y-%m-%d %H:%i' ) = DATE_FORMAT( ?, '%Y-%m-%d %H:%i' ) "
                + "AND DATE_FORMAT( fFin, '%Y-%m-%d %H:%i' ) = DATE_FORMAT( ?, '%Y-%m-%d %H:%i' )";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, descarga.getTanque());
            ps.setTimestamp(2, new Timestamp(descarga.getfInicio().getTimeInMillis()));
            ps.setTimestamp(3, new Timestamp(descarga.getfFin().getTimeInMillis()));
            log.debug(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info( rs.getInt("items") > 0 ? "Delivery previamente registrada." : "Registrando Delivery.");
                    return rs.getInt("items") > 0;
                }
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return false;
    }

    public static boolean create(Delivery descarga) throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "INSERT INTO descargas ( tanque, producto, claveProducto, tInicial, vInicial, tcVinicial, fInicio, tFinal, vFinal, tcVfinal, fFin, aumento, tcAumento, fechaInsercion ) "
                + "SELECT tanque, producto, clave_producto, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW() FROM tanques WHERE tanque = ? AND DATE_SUB( CURDATE(), INTERVAL 365 DAY ) < DATE( ? )";
        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, descarga.gettInicial().setScale(2, RoundingMode.HALF_EVEN));
            ps.setInt(2, descarga.getvInicial().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setInt(3, descarga.getTcVInicial().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setTimestamp(4, new Timestamp(descarga.getfInicio().getTimeInMillis()));
            ps.setBigDecimal(5, descarga.gettFinal().setScale(2, RoundingMode.HALF_EVEN));
            ps.setInt(6, descarga.getvFinal().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setInt(7, descarga.getTcVFinal().setScale(0, RoundingMode.HALF_EVEN).intValue());
            ps.setTimestamp(8, new Timestamp(descarga.getfFin().getTimeInMillis()));
            ps.setBigDecimal(9, descarga.getAumento().setScale(0, RoundingMode.HALF_EVEN));
            ps.setBigDecimal(10, descarga.getTcAumento().setScale(0, RoundingMode.HALF_EVEN));
            ps.setInt(11, descarga.getTanque());
            ps.setTimestamp(12, new Timestamp(descarga.getfFin().getTimeInMillis()));
            log.debug(ps);
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        log.info("Delivery registrada con id " + rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return false;
    }

    public static boolean register(Delivery descarga) throws TankSensorException {
        return exists(descarga) || create(descarga);
    }
}
