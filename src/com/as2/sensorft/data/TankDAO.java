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

import static com.as2.sensorft.data.InetConfigurationDAO.DEV;
import com.as2.sensorft.exceptions.TankSensorException;
import com.softcoatl.integration.ServiceLocator;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class TankDAO {

    private static final Logger log = Logger.getLogger(Tank.class);

    public static List<Tank> getTanques() throws TankSensorException {
        DataSource ds = ServiceLocator.getInstance().getDataSource("DATASOURCE");
        String sql = "SELECT LPAD(T.tanque, 2, '0') tanque, T.producto, T.clave_producto FROM tanques T WHERE estado = 1";
        List<Tank> tanques = new ArrayList<>();

        try (Connection conn = ds.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    log.info("Recuperando tanque: " + rs.getString("tanque"));
                    tanques.add(Tank.parse(rs));
                }
                return tanques;
            }
        } catch (SQLException ex) {
            log.error(ex);
            throw new TankSensorException(ex);
        }
        }
}
