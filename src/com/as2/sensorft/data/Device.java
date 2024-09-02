/**
 * Device
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

public class Device {

    private int id;
    private String nombre;
    private String cliente;
    private String activo;
    private String tipo;
    private String protocol;

    public enum TIPO {
        IP,
        TTY
    }

    public enum PROTOCOL {
        Autostik,
        Eeco,
        Incon,
        TS550,
        VeederTLS2X0,
        VeederTLS3X0,
        VeederTLS4X0,
        VeederTLS5X0,
        VeederFafnirTLS4X0,
        VeederOPWTLS2X0,
        VeederOPWTLS3X0
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCliente() {
        return cliente;
    }

    public String getActivo() {
        return activo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getProtocol() {
        return protocol;
    }

    public Device setId(int id) {
        this.id = id;
        return this;
    }

    public Device setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public Device setCliente(String cliente) {
        this.cliente = cliente;
        return this;
    }

    public Device setActivo(String activo) {
        this.activo = activo;
        return this;
    }

    public Device setTipo(String tipo) {
        this.tipo = tipo;
        return this;
    }    

    public Device setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    public String toString() {
        return "Device{" + "id=" + id + ", nombre=" + nombre + ", cliente=" + cliente + ", activo=" + activo + ", tipo=" + tipo + '}';
    }

    public static Device parse(ResultSet rs) throws SQLException {
        return new Device()
                .setId(rs.getInt("id"))
                .setNombre(rs.getString("nombre"))
                .setCliente(rs.getString("cliente"))
                .setTipo(rs.getString("tipo"))
                .setProtocol(rs.getString("protocol").trim())
                .setActivo(rs.getString("activo"));
    }
}
