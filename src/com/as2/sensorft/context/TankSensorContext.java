/**
 * TankSensorContext
 * Utilerías de Contexto
 * © 2021, DETI Desarrollo y Transferencia de Informática
 * http://detisa.com.mx
 * @author Rolando Esquivel Villafaña, Softcoatl
 * @version 1.0
 * @since Ago 2021
 */
package com.as2.sensorft.context;

import com.softcoatl.context.APPContext;
import com.softcoatl.database.DBException;
import com.softcoatl.context.services.DBService;
import com.softcoatl.database.mysql.MySQLHelper;
import com.softcoatl.integration.ServiceLoader;
import java.util.Properties;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class TankSensorContext {

    private static final Logger log = Logger.getLogger(TankSensorContext.class);

    public abstract static class ContextFactory {
        public static TankSensorContext getContext() {
            return new TankSensorContext();
        }
    }

    private TankSensorContext() { 
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                log.info("Received SIGTERM signal. Terminating process...");
            }
        });
    }

    private void setBDSession() {
        try {
            MySQLHelper.getInstance().forceExecute("USE epsilon");
            MySQLHelper.getInstance().forceExecute("SET lc_time_names = 'es_MX'");
            log.info("Configurado BD EPSILON v1.0");
        } catch (DBException DBE) {
            log.fatal("Error iniciando contexto en MySQL", DBE);
        }
    }

    public TankSensorContext configure(Properties poSystemProperties) {
        APPContext.getInstance().getProperties().putAll(poSystemProperties);
        return this;
    }

    public TankSensorContext initDataBaseService(String psDataBaseName) throws ReflectiveOperationException, NamingException {
        ServiceLoader.loadService(DBService.class, psDataBaseName, psDataBaseName);
        setBDSession();
        return this;
    }
}
