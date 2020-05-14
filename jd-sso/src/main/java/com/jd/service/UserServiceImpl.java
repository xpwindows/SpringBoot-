package com.jd.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jd.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	@Autowired(required = false)
	private UserMapper userMapper;
	/**
	 * true   当前用户输入的内容 已经存在
	 * false  表示用户不存在   数据可以用
	 * param:   用户输入的数据
	 * type: 参数类型  1 username、2 phone、3 email
	 */
	@Override
	public boolean findCheckUser(String param, Integer type) {
		//1.定义查询的字段,两个三目运算组成
		String column=(type==1)?"username":((type==2)?"phone":"email");
		//2.校验数据库中是否有数据
		//2.1.构建查询条件构造器
		QueryWrapper<User> queryWrapper = new QueryWrapper();
		//2.2.添加查询条件
		queryWrapper.eq(column,param);
		//2.3.获取查询的结果的数据条数
		int count = userMapper.selectCount(queryWrapper);
		//3.返回;count=0  返回false表示数据库没有对应数据；否则返回true
		return count==0?false:true;
	}
}
