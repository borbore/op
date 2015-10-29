package com.suneee.project.dto;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * DTO
 * 
 * @author rcj
 * 
 */
public class DTO extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -2415132608720172214L;
	
	private static String PAGE_NO_KEY = "pageNo";
	
	private static String PAGE_SIZE_KEY = "pageSize";
	
	private static String TOTAL_ROWS_KEY = "totalRows";
	
	private static String TOTAL_PAGES_KEY = "totalPages";
	
	private static String RESULT_KEY = "result";

	public static DTO of(String key, Object value) {
		DTO dto = new DTO();
		dto.put(key, value);
		return dto;
	}
	
	public static DTO ofPager(DTO condition, Long totalRows, List<DTO> result) {
		totalRows = totalRows != null ? totalRows : 0;
		
		Long pageNo = Long.parseLong( condition.get(PAGE_NO_KEY).toString());
		Long pageSize = Long.parseLong( condition.get(PAGE_SIZE_KEY).toString());
		Long totalPages = totalRows > pageSize ? (totalRows + pageSize - 1) / pageSize : 1;
		if(pageNo > totalPages) {
			pageNo = totalPages;
		}
		
		DTO paginateResult = new DTO();
		paginateResult.put(PAGE_NO_KEY, pageNo);
		paginateResult.put(PAGE_SIZE_KEY, pageSize);
		paginateResult.put(TOTAL_ROWS_KEY, totalRows);
		paginateResult.put(TOTAL_PAGES_KEY, totalPages);
		paginateResult.put(RESULT_KEY, result);
		
		return paginateResult;
	}

	@Override
	public Object put(String key, Object value) {
		if (containsKey(key.toUpperCase()) && !containsKey(key)) {
			remove(key.toUpperCase());
		}
		return super.put(key, value);
	}
	
	public <T> T get(String key, Class<T> returnType) {
		Object value =  get(key);
		if(value != null) {
			return returnType.cast(value);
		}
		return null;
	}
}
