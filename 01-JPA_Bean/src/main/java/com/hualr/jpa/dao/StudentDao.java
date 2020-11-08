package com.hualr.jpa.dao;

import com.hualr.jpa.bean.Student;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/8 19:41
 * Version: 1.0.0
 */

/**
 * 1. dao层是接口层
 * 2. 对应的JpaRepository为 Bean类+主键属性
 */
@Repository
public interface StudentDao extends JpaRepository<Student, Integer> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "from Student where studentId=:id")
    Optional<Student> findStudentById(@Param("id") Integer id);
}
