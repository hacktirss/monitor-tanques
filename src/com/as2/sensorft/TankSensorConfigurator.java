/**
 * TankSensorConfiguration
 * Utilerías de Contexto
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft;

import com.as2.sensorft.context.ChannelService;
import com.as2.sensorft.context.ProtocolService;
import com.as2.sensorft.data.Device;
import com.as2.sensorft.data.DeviceDAO;
import com.as2.sensorft.data.InetConfiguration;
import com.as2.sensorft.data.InetConfigurationDAO;
import com.as2.sensorft.data.ParametrosDAO;
import com.as2.sensorft.data.SerialConfiguration;
import com.as2.sensorft.data.SerialConfigurationDAO;
import com.as2.sensorft.exceptions.TankSensorException;
import com.as2.sensorft.executor.TankSensorExecutor;
import com.softcoatl.comm.ChannelInterface;
import com.softcoatl.comm.IPChannel;
import com.softcoatl.comm.IPChannelFactory;
import com.softcoatl.comm.TTYChannel;
import com.softcoatl.comm.TTYChannelFactory;
import com.softcoatl.comm.exceptions.CommException;
import com.softcoatl.tls.protocol.TLSProtocol;
import com.softcoatl.tls.protocol.TLS2X0Factory;
import com.softcoatl.tls.protocol.TLS4X0Factory;
import com.softcoatl.context.APPContext;
import com.softcoatl.integration.ServiceLoader;
import com.softcoatl.integration.ServiceLocator;
import com.softcoatl.tls.exception.TLSException;
import java.io.IOException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class TankSensorConfigurator {

    private static final Logger LOG = Logger.getLogger(TankSensorConfigurator.class);

    private static final String CHANNEL_PREFIX = "channel.";
    private static final String PROTOCOL_PREFIX = "protocol.";
    private static final String FACTORY_POSFIX = ".factory";

    private TankSensorConfigurator() {}

    private static void configureIP() throws ReflectiveOperationException, NamingException, TankSensorException {
        LOG.info("IP Channel Configured");
        InetConfiguration conf = InetConfigurationDAO.get();
        APPContext.setInitParameter(IPChannel.HOST, conf.getHost());
        APPContext.setInitParameter(IPChannel.PORT, String.valueOf(conf.getPort()));
        APPContext.setInitParameter(CHANNEL_PREFIX + ChannelService.NAME + FACTORY_POSFIX, IPChannelFactory.class.getCanonicalName());
        ServiceLoader.loadService(ChannelService.class, ChannelService.NAME, ChannelService.NAME);
    }

    private static void configureTTY() throws ReflectiveOperationException, NamingException, TankSensorException {
        LOG.info("TTY Channel Configured");
        SerialConfiguration conf = SerialConfigurationDAO.getDevice();
        APPContext.setInitParameter(TTYChannel.TTY_PORT, conf.getPort());
        APPContext.setInitParameter(TTYChannel.TTY_RATE, String.valueOf(conf.getBaudRate()));
        APPContext.setInitParameter(TTYChannel.TTY_LENGTH, String.valueOf(conf.getDataBits()));
        APPContext.setInitParameter(TTYChannel.TTY_STOP, String.valueOf(conf.getStopBits()));
        APPContext.setInitParameter(TTYChannel.TTY_PARITY, conf.getParity());
        APPContext.setInitParameter(CHANNEL_PREFIX + ChannelService.NAME + FACTORY_POSFIX, TTYChannelFactory.class.getCanonicalName());
        ServiceLoader.loadService(ChannelService.class, ChannelService.NAME, ChannelService.NAME);
    }

    private static void configureProtocolVeederTLS2X0() throws ReflectiveOperationException, NamingException {
        LOG.info("Protocol VeederTLS 2X0 Configured");
        APPContext.setInitParameter(PROTOCOL_PREFIX + ProtocolService.NAME + FACTORY_POSFIX, TLS2X0Factory.class.getCanonicalName());
        ServiceLoader.loadService(ProtocolService.class, ProtocolService.NAME, ProtocolService.NAME);
    }

    private static void configureProtocolVeederTLS4X0() throws ReflectiveOperationException, NamingException {
        LOG.info("Protocol VeederTLS 4X0 Configured");
        APPContext.setInitParameter(PROTOCOL_PREFIX + ProtocolService.NAME + FACTORY_POSFIX, TLS4X0Factory.class.getCanonicalName());
        ServiceLoader.loadService(ProtocolService.class, ProtocolService.NAME, ProtocolService.NAME);
    }

    private static void initTankSensor() {

        try (ChannelInterface channel = ((ChannelInterface) ServiceLocator.getInstance().getService(ChannelService.NAME)).open()) {
            TLSProtocol protocol = (TLSProtocol) ServiceLocator.getInstance().getService(ProtocolService.NAME);
            if ("1".equals(ParametrosDAO.getParameter("setSensorTimestamp", "0"))) {
                new TankSensorExecutor().setDateTime(channel, protocol);
            }
        } catch (IOException | CommException | TLSException  ex) {
            LOG.error(ex);
        }
    }

    public static void configure() throws TankSensorException {
        Device device = DeviceDAO.getDevice();

        try {
            LOG.info(String.format("Configure Process for %s Sensor using %s connection type.", device.getProtocol(), device.getTipo()));
            switch(Device.TIPO.valueOf(device.getTipo())) {
                case IP -> configureIP();
                case TTY -> configureTTY();
                default -> throw new TankSensorException("Error configurando el servicio.");
            }

            switch(Device.PROTOCOL.valueOf(device.getProtocol())) {
                case Autostik, Eeco, Incon, VeederTLS2X0, VeederOPWTLS2X0 -> configureProtocolVeederTLS2X0();
                case TS550, VeederTLS3X0, VeederTLS4X0, VeederOPWTLS3X0, VeederFafnirTLS4X0 -> configureProtocolVeederTLS4X0();
                default -> throw new TankSensorException("Error configurando el servicio.");
            }

            initTankSensor();
        } catch (ReflectiveOperationException | NamingException ex) {
            LOG.error(ex);
            throw new TankSensorException(ex);
        }
    }
}
