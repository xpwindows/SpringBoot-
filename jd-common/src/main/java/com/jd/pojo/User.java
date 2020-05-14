package com.jd.pojo;

//用来封装用户信息的类


import com.alibaba.dubbo.config.RegistryConfig;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.CallableStatement;

@Data    //自动添加get和set方法
@Accessors(chain = true) //链式赋值
@TableName("tb_user")  //对应数据库的表名
public class User extends BasePojo{
    @TableId(type = IdType.AUTO) //主键自增
    private Long id;
    private String username;   //用户名
    private String password;   //用户密码
    private String phone;    //电话
    private String email;   //邮箱


}

