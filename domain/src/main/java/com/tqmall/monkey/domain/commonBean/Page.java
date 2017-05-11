package com.tqmall.monkey.domain.commonBean;

import java.io.Serializable;
import java.util.List;


public class Page<T> implements Serializable {
	private static final long serialVersionUID = 3509375972998939764L;
	public static String CURRENT_INDEX = "currentIndex";
	public static String PAGE_SIZE = "pageSize";
	public static String INDEX="index";
	/**
	 * 当前页码
	 */
	private int currentIndex;

	/**
	 * 页数大小
	 */
	private int pageSize;

	/**
	 * 总数
	 */
	private int totalNumber;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 下一页
	 */
	private int nextIndex;

	/**
	 * 上一页
	 */
	private int preIndex;

	/**
	 * 当前页内容
	 */
	private List<T> Items;


	/**
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param totalNumber
	 * @param currentIndex
	 * @param pageSize
	 * @param items
	 */
	public Page(int totalNumber, int currentIndex, int pageSize, List<T> items){
		this.totalNumber = totalNumber;
		this.currentIndex = currentIndex;
		this.pageSize = pageSize;
		this.Items = items;
	}

	public Page() {
	}


	/**
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return
	 */
	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * @param currentIndex,当前页码
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * @return
	 */
	public int getTotalNumber() {
		return totalNumber;
	}

	/**
	 * @param totalNumber
	 */
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	/**
	 * ��ҳ��
	 * @return
	 */
	public int getTotalPage() {

		int size = this.totalNumber/this.pageSize;
		if(this.totalNumber%this.pageSize != 0){
			size = size + 1;
		}
		this.totalPage = size;

		return this.totalPage;
	}

	/**
	 * 到下一页
	 *
	 * @return
	 */
	public int getNextIndex() {

		if(this.currentIndex >= this.getTotalPage()){
			this.nextIndex = this.currentIndex;
		}else{
			this.nextIndex = this.currentIndex + 1;
		}
		return nextIndex;
	}

	/**
	 *  上一页
	 * @return
	 */
	public int getPreIndex() {

		if(this.currentIndex <= 1){
			this.preIndex = 0;
		}else{
			this.preIndex = this.currentIndex -1;
		}

		return preIndex;
	}

	/**
	 * @return
	 */
	public List<T> getItems() {
		return Items;
	}

	/**
	 * @param items
	 */
	public void setItems(List<T> items) {
		Items = items;
	}

}
