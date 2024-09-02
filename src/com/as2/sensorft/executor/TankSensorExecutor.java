/**
 * TankSensorExecutor
 * Ejecutor de comandos 
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.executor;

import com.as2.sensorft.exceptions.TankSensorException;
import com.softcoatl.comm.ChannelInterface;
import com.softcoatl.comm.exceptions.CommException;
import com.softcoatl.tls.data.Delivery;
import com.softcoatl.tls.data.Inventory;
import com.softcoatl.tls.exception.TLSException;
import com.softcoatl.tls.protocol.TLSProtocol;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class TankSensorExecutor {

    private static final Logger LOG = Logger.getLogger(TankSensorExecutor.class);

    public List<Delivery> getDelivery(ChannelInterface channel, TLSProtocol protocol) throws CommException, TLSException {

        List<Delivery> deliveries = new ArrayList<>();
        LOG.info("Asking for delivery data");
        for(String command : protocol.deliveryCommand()) {
            LOG.info("Requesting delivery " + command);
            if (channel.send(command)) {
                deliveries.addAll(protocol.parseDelivery(new String(channel.read())));
            }
        }
        if (deliveries.isEmpty()) {
            throw new TankSensorException("Delivery data not received");
        }
        return deliveries;
    }

    public List<Inventory> getInventory(ChannelInterface channel, TLSProtocol protocol) throws CommException, TLSException {

        List<Inventory> inventory = new ArrayList<>();
        LOG.info("Asking for inventory data");
        for(String command: protocol.inventoryCommand()) {
            LOG.info("Requesting inventory " + command);
            if (channel.send(command)) {
                inventory.addAll(protocol.parseInventory(new String(channel.read())));
            }
        }
        if (inventory.isEmpty()) {
            throw new TankSensorException("Inventory data not received");
        }
        return inventory;
    }

    public boolean setDateTime(ChannelInterface channel, TLSProtocol protocol) throws CommException, TLSException {

        LOG.info("Setting Date Time");
        if (channel.send(protocol.dateTimeCommand())) {
            return protocol.validateDateTime(new String(channel.read()));
        }
        return false;
    }
}
