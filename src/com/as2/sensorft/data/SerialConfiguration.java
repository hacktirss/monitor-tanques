/**
 * SerialConfiguration
 * Serial Port configuration
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SerialConfiguration {

    private String port;
    private int baudRate;
    private int dataBits;
    private int stopBits;
    private String parity;
    private String contrloFlow;

    public  String getPort() {
        return port;
    }

    public int getBaudRate() {
        return baudRate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public String getParity() {
        return parity;
    }

    public String getContrloFlow() {
        return contrloFlow;
    }

    public SerialConfiguration setPort(String port) {
        this.port = port;
        return this;
    }

    public SerialConfiguration setBaudRate(int baudRate) {
        this.baudRate = baudRate;
        return this;
    }

    public SerialConfiguration setDataBits(int dataBits) {
        this.dataBits = dataBits;
        return this;
    }

    public SerialConfiguration setStopBits(int stopBits) {
        this.stopBits = stopBits;
        return this;
    }

    public SerialConfiguration setParity(String parity) {
        this.parity = parity;
        return this;
    }

    public SerialConfiguration setContrloFlow(String contrloFlow) {
        this.contrloFlow = contrloFlow;
        return this;
    }

    @Override
    public String toString() {
        return "SerialConfiguration{" + "baudRate=" + baudRate + ", dataBits=" + dataBits + ", stopBits=" + stopBits + ", parity=" + parity + ", contrloFlow=" + contrloFlow + '}';
    }

    public static SerialConfiguration parse(ResultSet rs) throws SQLException {
        return new SerialConfiguration()
                .setPort(rs.getString("port"))
                .setDataBits(rs.getInt("dataBits"))
                .setBaudRate(rs.getInt("baudRate"))
                .setStopBits(rs.getInt("stopBits"))
                .setParity(rs.getString("parity"))
                .setContrloFlow(rs.getString("controlFlow"));
    }
}
