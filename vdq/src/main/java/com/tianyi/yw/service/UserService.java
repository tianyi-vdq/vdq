package com.tianyi.yw.service;

import com.tianyi.yw.common.ReturnResult;
import com.tianyi.yw.model.User;

public interface UserService {

	ReturnResult<User> login(String account, String password);

}
