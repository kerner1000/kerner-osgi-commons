package de.mpg.mpizkoeln.kerner.anna.generictester;

import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class StepTester extends AbstractStep {

    @Override
    public boolean checkRequirements(DataObject data) {
        System.out.println(getStepProperties());
        return Boolean.parseBoolean(data.getProperties().getProperty(DataObject.INIT_SEQ_SET));
    }

    

   
}
