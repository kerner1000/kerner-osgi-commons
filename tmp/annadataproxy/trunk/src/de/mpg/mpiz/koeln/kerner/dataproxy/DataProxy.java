package de.mpg.mpiz.koeln.kerner.dataproxy;

import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBean;
import de.mpg.mpiz.koeln.kerner.dataproxy.data.DataBeanAccessException;


public interface DataProxy {

	DataBean getDataBean() throws DataBeanAccessException;
	void updateDataBean(DataBean dataBean) throws DataBeanAccessException;

}
