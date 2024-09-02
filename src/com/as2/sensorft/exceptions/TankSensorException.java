package com.as2.sensorft.exceptions;

import com.softcoatl.tls.exception.TLSException;

public class TankSensorException extends TLSException {
    
    public TankSensorException() {
    }

    public TankSensorException(String arg0) {
        super(arg0);
    }

    public TankSensorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TankSensorException(Throwable arg0) {
        super(arg0);
    }

    public TankSensorException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }
}
