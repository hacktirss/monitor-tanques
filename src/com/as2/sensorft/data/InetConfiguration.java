/**
 * InetConfiguration
 * IP configuration
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InetConfiguration {
    private String protocol;
    private String host;
    private String usr;
    private String pwd;
    private int port;

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getUsr() {
        return usr;
    }

    public String getPwd() {
        return pwd;
    }

    public int getPort() {
        return port;
    }

    public InetConfiguration setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public InetConfiguration setHost(String host) {
        this.host = host;
        return this;
    }

    public InetConfiguration setUsr(String usr) {
        this.usr = usr;
        return this;
    }

    public InetConfiguration setPwd(String pwd) {
        this.pwd = pwd;
        return this;
    }

    public InetConfiguration setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return "InetConfiguration{" + "host=" + host + ", usr=" + usr + ", pwd=" + pwd + ", port=" + port + '}';
    }

    public static InetConfiguration parse(ResultSet rs) throws SQLException {
        return new InetConfiguration()
                .setProtocol(rs.getString("protocol"))
                .setHost(rs.getString("host"))
                .setUsr(rs.getString("usr"))
                .setPwd(rs.getString("pwd"))
                .setPort(rs.getInt("port"));
    }
}
