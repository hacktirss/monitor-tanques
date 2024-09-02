/**
 * TankSensor
 * Clase principal del Sistema de Monitoreo de Tanques TankSensor®
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft;

import com.detisa.commons.ServiceRegister;
import com.as2.sensorft.context.TankSensorContext.ContextFactory;
import com.as2.sensorft.exceptions.TankSensorException;
import com.as2.sensorft.jobs.TankSensorPollingJob;
import com.softcoatl.utils.PropertyLoader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.naming.NamingException;

public class TankSensor  {

    private static final String VERSION = "4.5.4";
    private static final String MODULE = "Sensor";

    public static void main(String[] args) throws TankSensorException {
        try {
            ContextFactory.getContext()
                    .configure(PropertyLoader.load("tank.properties"))
                    .initDataBaseService("DATASOURCE");
            ServiceRegister.registerWSVersion(TankSensor.class, MODULE, VERSION);
            TankSensorConfigurator.configure();
            Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new TankSensorPollingJob(), 10, 600, TimeUnit.SECONDS);
        } catch (TankSensorException | IOException | ReflectiveOperationException | URISyntaxException | NoSuchAlgorithmException | NamingException ex) {
            throw new TankSensorException(ex);
        }
    }
}
