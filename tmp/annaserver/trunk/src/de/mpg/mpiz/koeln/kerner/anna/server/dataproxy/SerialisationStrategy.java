package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy;

import java.io.File;

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

public interface SerialisationStrategy {

	DataBean readFromDisk(File file) throws DataBeanAccessException;

	void writeDataBean(DataBean dataBean, File file) throws DataBeanAccessException;

	

}
