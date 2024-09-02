/**
 * Tank
 * VO que representa la tabla epsilon.dev_tty
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Tank {

    private String tanque;
    private String producto;
    private String claveProducto;

    public String getTanque() {
        return tanque;
    }

    public String getProducto() {
        return producto;
    }

    public String getClaveProducto() {
        return claveProducto;
    }

    public Tank setTanque(String tanque) {
        this.tanque = tanque;
        return this;
    }

    public Tank setProducto(String producto) {
        this.producto = producto;
        return this;
    }

    public Tank setClaveProducto(String claveProducto) {
        this.claveProducto = claveProducto;
        return this;
    }

    @Override
    public String toString() {
        return "Tank{" + "tanque=" + tanque + ", producto=" + producto + ", claveProducto=" + claveProducto + '}';
    }

    public static Tank parse(ResultSet rs) throws SQLException {
        return new Tank()
                .setTanque(rs.getString("tanque"))
                .setProducto(rs.getString("producto"))
                .setClaveProducto(rs.getString("clave_producto"));
    }
}
