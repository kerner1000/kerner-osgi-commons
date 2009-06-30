package de.mpg.mpiz.koeln.kerner.anna.server.dataproxyImpl;

import java.io.File;

import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.data.DataBeanAccessException;


interface SerialisationStrategy {

	DataBean readFromDisk(File file) throws DataBeanAccessException;

	void writeDataBean(DataBean dataBean, File file)
			throws DataBeanAccessException;

}
