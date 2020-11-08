package com.hualr.jpa.dao;

import com.hualr.jpa.bean.Klass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/8 19:41
 * Version: 1.0.0
 */
@Repository
public interface KlassDao extends JpaRepository<Klass,String> {
}
