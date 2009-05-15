package de.mpg.mpizkoeln.kerner.anna.sequencesreader;

import java.util.Properties;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class SequencesReader extends AbstractStep {

    @Override
    public boolean checkRequirements(DataObject data) {
        Properties properties = getStepProperties();
        System.err.println(properties);
        return false;
    }

}
