package com.trip.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trip.common.constant.UserConstant;
import com.trip.common.exception.BusinessException;
import com.trip.user.dao.UserDao;
import com.trip.user.pojo.UserPojo;
import com.trip.user.service.LoginService;
import com.trip.user.vo.RegisterVo;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl extends ServiceImpl<UserDao, UserPojo> implements LoginService {
    @Override
    public void register(RegisterVo registerVo) throws BusinessException{
        UserPojo existUser = this.getOne(new QueryWrapper<UserPojo>().eq("username", registerVo.getUsername()));
        UserPojo newUser = new UserPojo();
        if (existUser == null){
            newUser.setUsername(registerVo.getUsername());
            newUser.setPassword(BCrypt.hashpw(registerVo.getPassword(), BCrypt.gensalt(UserConstant.BCRYPT_SALT)));
//            newUser.setPassword(registerVo.getPassword());
            super.save(newUser);
        }else{
            throw new BusinessException(400, "Username Already Exist");
        }
    }

    @Override
    public void login(RegisterVo registerVo) throws BusinessException {
        UserPojo existUser = this.getOne(new QueryWrapper<UserPojo>().eq("username", registerVo.getUsername()));
        if (existUser == null || !BCrypt.checkpw(registerVo.getPassword(), existUser.getPassword())){
//            if (existUser == null || !registerVo.getPassword().equals(existUser.getPassword())){
            throw new BusinessException(400, "Invalid Username and Password");
        }
    }
}
