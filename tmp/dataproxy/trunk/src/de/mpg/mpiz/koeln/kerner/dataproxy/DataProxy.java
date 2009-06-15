package de.mpg.mpiz.koeln.kerner.dataproxy;


public interface DataProxy {

	DataBean getDataBean() throws DataBeanAccessException;
	void updateDataBean(DataBean dataBean) throws DataBeanAccessException;

}
