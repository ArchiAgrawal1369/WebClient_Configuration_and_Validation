package com.nagarro.MiniAssignment2.model;
import java.util.List;

public class Response {
	private List<User> data;
	private PageInfo pageInfo;

	public List<User> getData() {
		return data;
	}

	public void setData(List<User> data) {
		this.data = data;
	}

	public PageInfo getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfo pageInfo) {
		this.pageInfo = pageInfo;
	}

	public Response(List<User> data, PageInfo pageInfo) {
		super();
		this.data = data;
		this.pageInfo = pageInfo;
	}

	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "UserResponse [data=" + data + ", pageInfo=" + pageInfo + "]";
	}
	
	
	public static class PageInfo {
		private boolean hasNextPage;
		private boolean hasPreviousPage;
		private int total;

		public boolean isHasNextPage() {
			return hasNextPage;
		}

		public void setHasNextPage(boolean hasNextPage) {
			this.hasNextPage = hasNextPage;
		}

		public boolean isHasPreviousPage() {
			return hasPreviousPage;
		}

		public void setHasPreviousPage(boolean hasPreviousPage) {
			this.hasPreviousPage = hasPreviousPage;
		}

		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public PageInfo(boolean hasNextPage, boolean hasPreviousPage, int total) {
			super();
			this.hasNextPage = hasNextPage;
			this.hasPreviousPage = hasPreviousPage;
			this.total = total;
		}

		public PageInfo() {
			super();
		}

		@Override
		public String toString() {
			return "PageInfo [hasNextPage=" + hasNextPage + ", hasPreviousPage=" + hasPreviousPage + ", total=" + total
					+ "]";
		}

	}
}