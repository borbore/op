package com.suneee.base.util;

import java.util.HashMap;

/**
 * 分页数据封装类，该类携带分页参数和当前页数据。在分页查询中通过传入和返回该类达到统一 分页操作的目的
 * ClassName: Pager <br/>  
 * date: 2014年9月30日 下午5:38:10 <br/>  
 * @author Forint  
 * @version v1.0
 */
public class Pager extends JsonResult
{
    private final static int PAGE_SIZE = 20;
    private int totalRows; // 总行数
    private int startRow;
    private int pageSize = Pager.PAGE_SIZE; // 每页显示的行数
    private int currentPage; // 当前页号
    private int totalPages; // 总页数
    private String pageNo;
    private String pageScale;

    public Pager()
    {
    }

    public Pager(final String pageNo)
    {
        setPageNo(pageNo);
    }

    public Pager(final String pageNo, final String pageScale)
    {
        setPageNo(pageNo);
        setPageScale(pageScale);
    }

    public void setTotalRows(final int totalRows)
    {
        this.totalRows = totalRows;
        totalPages = totalRows / pageSize;

        final int mod = totalRows % pageSize;
        if (mod > 0)
        {
            totalPages++;
        }
        setCurrentPage(this.currentPage);
    }
    
    //可设置页面显示记录条数
    public void setTotalRows(final int totalRows,int page_size)
    {
    	this.pageSize=page_size;
        this.totalRows = totalRows;
        totalPages = totalRows / pageSize;

        final int mod = totalRows % pageSize;
        if (mod > 0)
        {
            totalPages++;
        }
        setCurrentPage(this.currentPage);
    }

    /**
     * 设置当前页
     * 
     * @param currentPage
     */
    protected void setCurrentPage(final int currentPage)
    {
        // 当前页小于第一页
        if (currentPage < 1)
        {
            this.currentPage = 1;
        } else if (currentPage > this.totalPages)
        { // 当前页大于总页数
            this.currentPage = this.totalPages;
        } else
        {
            this.currentPage = currentPage;
        }
        this.pageNo = String.valueOf(this.currentPage);
        this.startRow = (this.currentPage - 1) * this.pageSize;
        if (this.currentPage == 0)
        {
            this.startRow = 0;
        }
    }

    /**
     * @return the totalRows
     */
    public int getTotalRows()
    {
        return totalRows;
    }

    /**
     * @return the totalPages
     */
    public int getTotalPages()
    {
        return totalPages;
    }

    public void setPageNo(final String pageNo)
    {
        if (pageNo == null || "".equals(pageNo))
        {
            this.currentPage = 1;
        } else
        {
            try
            {
                this.currentPage = Integer.parseInt(pageNo);
            } catch (final Exception e)
            {
                this.currentPage = 1;
            }
        }
        this.pageNo = String.valueOf(this.currentPage);
    }

    public String getPageScale()
    {
        return pageScale;
    }

    public void setPageScale(final String pageScale)
    {
        this.pageScale = pageScale;
        if (pageScale == null || "".equals(pageScale))
        {
            this.pageSize = Pager.PAGE_SIZE;
        } else
        {
            try
            {
                this.pageSize = Integer.parseInt(pageScale);
            } catch (final Exception e)
            {
                this.pageSize = Pager.PAGE_SIZE;
            }
        }
        this.pageScale = String.valueOf(this.pageSize);
    }

    public String getPageNo()
    {
        return pageNo;
    }

    public int getStartRow()
    {
        return this.startRow;
    }

    public int getPageSize()
    {
        return this.pageSize;
    }

}
