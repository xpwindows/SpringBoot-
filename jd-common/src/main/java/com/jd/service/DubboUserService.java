package com.jd.service;

import com.jd.pojo.User;

public interface DubboUserService {
    void saveUser(User user);

    String doLogin(User user);
}
