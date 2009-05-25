package de.mpg.mpizkoeln.kerner.anna.sequencesreader;

import java.io.File;
import java.util.Properties;

import kerner.commons.file.LazyStringReader;
import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataObject;

public class SequencesReader extends AbstractStep {

    private final static String INPUT_FILE_KEY = "input.file";
    private Properties properties = null;

    @Override
    public boolean checkRequirements(DataObject data) {
        properties = getStepProperties();
        AbstractStep.LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG,
                "Properties: " + properties, null);
        return new File(properties.getProperty(INPUT_FILE_KEY)).canRead();
    }

    @Override
    public DataObject run(DataObject data) throws Exception {
        AbstractStep.LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG,
                "We have been activated. Going to do our thing ", null);
        File file = new File(properties.getProperty(INPUT_FILE_KEY));
        LazyStringReader reader = new LazyStringReader(file);
        //System.out.println(reader.getString());
        Collection<FASTASequence>
        return null;
    }
}
