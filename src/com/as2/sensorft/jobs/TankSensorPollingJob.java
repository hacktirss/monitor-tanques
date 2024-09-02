/**
 * TankSensorPollingJog
 * Job de polleo de Tanques
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.jobs;

import com.as2.sensorft.context.ChannelService;
import com.as2.sensorft.context.ProtocolService;
import com.as2.sensorft.data.DescargaDAO;
import com.as2.sensorft.data.InventarioDAO;
import com.as2.sensorft.exceptions.TankSensorException;
import com.as2.sensorft.executor.TankSensorExecutor;
import com.softcoatl.comm.ChannelInterface;
import com.softcoatl.comm.exceptions.CommException;
import com.softcoatl.integration.ServiceLocator;
import com.softcoatl.tls.data.Delivery;
import com.softcoatl.tls.data.Inventory;
import com.softcoatl.tls.exception.TLSException;
import com.softcoatl.tls.protocol.TLSProtocol;
import org.apache.log4j.Logger;

public class TankSensorPollingJob implements Runnable {

    private static final Logger log = Logger.getLogger(TankSensorPollingJob.class);

    private void processInventory(Inventory inventario) {
        try {
            log.debug("Processing inventory " + inventario);
            InventarioDAO.register(inventario);
        } catch (TankSensorException ex) {
            log.error(ex);
        }
    }

    private void processDelivery(Delivery descarga) {
        try {
            log.debug("Processing delivery " + descarga);
            DescargaDAO.register(descarga);
        } catch (TankSensorException ex) {
            log.error(ex);
        }
    }

    private void getInventory(ChannelInterface channel, TLSProtocol protocol) {
        try {
            new TankSensorExecutor().getInventory(channel, protocol).stream().forEach(this::processInventory);
        } catch (CommException | TLSException rte){
            log.fatal(rte);
        }
    }

    private void getDelivery(ChannelInterface channel, TLSProtocol protocol) {
        try {
            new TankSensorExecutor().getDelivery(channel, protocol).stream().forEach(this::processDelivery);
        } catch (CommException | TLSException rte){
            log.fatal(rte);
        }
    }

    @Override
    public void run() {

        try (ChannelInterface channel = ((ChannelInterface) ServiceLocator.getInstance().getService(ChannelService.NAME)).open()) {
            TLSProtocol protocol = (TLSProtocol) ServiceLocator.getInstance().getService(ProtocolService.NAME);
            getInventory(channel, protocol);
            getDelivery(channel, protocol);
        } catch (RuntimeException rte){
            log.fatal("Error inesperado ", rte);
        } catch (Throwable t){
            log.fatal("Error inesperado ", t);
        }
    }
}
