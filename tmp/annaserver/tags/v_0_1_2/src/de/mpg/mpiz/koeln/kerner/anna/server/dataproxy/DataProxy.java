package de.mpg.mpiz.koeln.kerner.anna.server.dataproxy;

import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.anna.server.dataproxy.data.DataBeanAccessException;

public interface DataProxy {

	DataBean getDataBean() throws DataBeanAccessException;

	void updateDataBean(DataBean dataBean) throws DataBeanAccessException;

}
