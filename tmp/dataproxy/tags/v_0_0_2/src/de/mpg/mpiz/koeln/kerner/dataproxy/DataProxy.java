package de.mpg.mpiz.koeln.kerner.dataproxy;

import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBeanAccessException;
import de.mpg.mpiz.koeln.kerner.dataproxy.dataimpl.DataBeanImpl;


public interface DataProxy {

	DataBeanImpl getDataBean() throws DataBeanAccessException;
	void updateDataBean(DataBeanImpl dataBean) throws DataBeanAccessException;

}
