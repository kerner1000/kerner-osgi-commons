package de.mpg.mpizkoeln.kerner.anna.sequencesreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.bioutils.fasta.FASTAFile;
import org.bioutils.fasta.FASTAFileImpl;
import org.bioutils.fasta.FASTASequence;

import de.kerner.commons.file.LazyStringReader;
import de.kerner.osgi.commons.util.ToOSGiLogServiceLogger;
import de.mpg.mpizkoeln.kerner.anna.core.AbstractStep;
import de.mpg.mpizkoeln.kerner.anna.core.DataBean;

public class SequencesReader extends AbstractStep {

    private final static String INPUT_FILE_KEY = "input.file";
    private Properties properties = null;

    @Override
    public boolean checkRequirements(DataBean data) {
        properties = getStepProperties();
        AbstractStep.LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG, "Properties: " + properties, null);
        return new File(properties.getProperty(INPUT_FILE_KEY)).canRead();
    }

    @Override
    public DataBean run(DataBean data) throws Exception {
        AbstractStep.LOGGER.log(this, ToOSGiLogServiceLogger.LEVEL.DEBUG,
                "We have been activated. Going to do our thing ", null);
        File file = new File(properties.getProperty(INPUT_FILE_KEY));
        LazyStringReader reader = new LazyStringReader(file);
        FASTAFile fastaFile = new FASTAFileImpl(reader.getString());
        Collection<FASTASequence> sequences = new ArrayList<FASTASequence>();
        System.out.println(data.getProperties());
        return null;
    }
}
