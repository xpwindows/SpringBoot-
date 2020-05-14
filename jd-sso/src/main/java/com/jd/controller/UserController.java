package com.jd.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jd.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jd.service.UserService;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	/**
	 * 检查用户数据是否存在   ;前端jsonp的数据请求，会在url后带一个callback的参数
	 * url:   http://sso.jt.com/user/check/{param}/{type} ?callback=jqury1233434
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject findCheckUser(String callback, @PathVariable String param,@PathVariable Integer type){
      	//1.查询数据库，检查数据是否存在；
		boolean flag= userService.findCheckUser(param,type);
		JSONPObject jsonpObject=null;
		try {
			//2.调用通用的前端信息封装类方法，调用成功方法
			jsonpObject = new JSONPObject(callback, SysResult.success(flag));
		} catch (Exception e) {
			e.printStackTrace();
			//3.出现异常 调用失败方法
			jsonpObject=new JSONPObject(callback,SysResult.fail());
		}
		return jsonpObject;
	}
	/**
	 * 首页回显用户登录信息；
	 * uri: /user/query/{token}
	 * 请求参数： 秘钥token, 回调函数名称callback
	 * 需要返回的数据： JSONP格式的SysResult数据；
	 */
	@RequestMapping("/query/{token}")
	public JSONPObject findUserByToken (@PathVariable String token,String callback){
		//1.根据秘钥查询用户信息，在redis中查询
		String userJSON = jedisCluster.get(token);
		JSONPObject jsonpObject=null;
		//1.1 没有查询到数据的情况
		if (StringUtils.isEmpty(userJSON)){
			 jsonpObject = new JSONPObject(callback, SysResult.fail());
		}else {
			//1.2表示用户数据获取成功
			jsonpObject = new JSONPObject(callback, SysResult.success(userJSON));
		}
		return jsonpObject;
	}
}
