package com.suneee.project.dao.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.suneee.project.model.Account;
import com.suneee.project.model.AccountExample;

public interface AccountMapper {

	int countByExample(AccountExample example);

	int deleteByExample(AccountExample example);

	int deleteByPrimaryKey(String username);

	int insert(Account record);

	int insertSelective(Account record);

	List<Account> selectByExample(AccountExample example);

	Account selectByPrimaryKey(String username);
	
	int updateByExampleSelective(@Param("record") Account record,
			@Param("example") AccountExample example);

	int updateByExample(@Param("record") Account record,
			@Param("example") AccountExample example);

	int updateByPrimaryKeySelective(Account record);

	int updateByPrimaryKey(Account record);
}