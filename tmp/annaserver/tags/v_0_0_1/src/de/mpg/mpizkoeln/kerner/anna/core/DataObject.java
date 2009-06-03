package de.mpg.mpizkoeln.kerner.anna.core;

import java.util.Properties;

public class DataObject {

    public final static String INIT_SEQ_SET = "init-seq-set";

    private final Properties props;

    public DataObject() {
        props = new Properties(initDefaults());
    }

    private static Properties initDefaults() {
        Properties defaults = new Properties();
        defaults.setProperty(INIT_SEQ_SET, Boolean.toString(true));
        return defaults;
    }

    public Properties getProperties() {
        return props;
        //return new Properties(props);
    }
}
