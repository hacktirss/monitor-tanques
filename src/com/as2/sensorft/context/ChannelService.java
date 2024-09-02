/**
 * ChannelService
 * Servicio de Comunicaciones
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.context;

import com.softcoatl.comm.ChannelInterface;
import com.softcoatl.context.APPContext;
import com.softcoatl.integration.Service;
import javax.naming.Reference;

public class ChannelService implements Service {

    public static final String DOT = ".";
    public static final String EXC_PPRTY_FACTORY = "factory";
    public static final String EXC_PREFIX = "channel";
    public static final String NAME = "COMM_CHANNEL";

    @Override
    public String getPropertyStandarName(String psServiceName, String psPropertyName) {
        return EXC_PREFIX + DOT + psServiceName + DOT + psPropertyName;
    }

    @Override
    public Reference getReference(String serviceName) {
        return new Reference(ChannelInterface.class.getCanonicalName(), APPContext.getInitParameter(getPropertyStandarName(serviceName, EXC_PPRTY_FACTORY)), null);
    }

    @Override
    public boolean isDefined(String serviceName) {
        return APPContext.hasInitParameter(getPropertyStandarName(serviceName, EXC_PPRTY_FACTORY));
    }    
}
