package de.mpg.mpizkoeln.kerner.anna.datachangedservice;

import java.util.EventObject;

@SuppressWarnings("serial")
public class DataChangedEvent extends EventObject {

    public DataChangedEvent(Object source) {
        super(source);
      
    }

}
