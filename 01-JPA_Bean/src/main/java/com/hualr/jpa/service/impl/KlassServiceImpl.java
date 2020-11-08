package com.hualr.jpa.service.impl;

import com.hualr.jpa.dao.KlassDao;
import com.hualr.jpa.dao.StudentDao;
import com.hualr.jpa.service.api.KlassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/8 19:51
 * Version: 1.0.0
 */
@Service
public class KlassServiceImpl implements KlassService {
    @Autowired
    StudentDao studentDao;

    @Autowired
    KlassDao klassDao;
}
