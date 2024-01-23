package com.trip.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trip.common.exception.BusinessException;
import com.trip.user.pojo.UserPojo;
import com.trip.user.vo.RegisterVo;
import org.springframework.stereotype.Service;

@Service
public interface LoginService extends IService<UserPojo> {
    void register(RegisterVo registerVo) throws BusinessException;

    void login(RegisterVo registerVo) throws BusinessException;
}
