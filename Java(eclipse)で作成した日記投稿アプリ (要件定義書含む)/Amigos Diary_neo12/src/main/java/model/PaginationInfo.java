package model;

import java.io.Serializable;

public class PaginationInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int currentPage; // 現在のページ番号
	private int totalCount; // 全体の件数
	private int recordsPerPage; // 1ページあたりの表示件数
	private int totalPages; // 全体のページ数
	private int startRecord; // 現在のページで表示する最初のレコードのインデックス (SQLのLIMIT句のOFFSET)

	public PaginationInfo(int currentPage, int totalCount, int recordsPerPage) {
		this.currentPage = currentPage;
		this.totalCount = totalCount;
		this.recordsPerPage = recordsPerPage;
		calculatePagination();
	}

	private void calculatePagination() {
		this.totalPages = (int) Math.ceil((double) totalCount / recordsPerPage);

		if (this.totalPages == 0) {
			this.currentPage = 0; // 件数が0の場合、ページ数も0
		} else if (this.currentPage > this.totalPages) {
			this.currentPage = this.totalPages;
		}
		if (this.currentPage < 1 && this.totalPages > 0) {
			this.currentPage = 1;
		}

		this.startRecord = (this.currentPage > 0 ? (this.currentPage - 1) : 0) * this.recordsPerPage; // currentPageが0の場合はstartRecordも0に
		if (totalCount == 0) { // 総件数が0の場合は開始レコードも0
			startRecord = 0;
		}
	}

	// ゲッターメソッド
	public int getCurrentPage() {
		return currentPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getRecordsPerPage() {
		return recordsPerPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getStartRecord() {
		return startRecord;
	}
}