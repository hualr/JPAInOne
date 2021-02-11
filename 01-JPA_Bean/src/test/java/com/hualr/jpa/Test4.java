package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.bean.Student;
import com.hualr.jpa.dao.KlassDao;
import com.hualr.jpa.dao.StudentDao;
import com.hualr.jpa.service.api.KlassService;
import com.hualr.jpa.service.api.StudentService;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

/**
 * Function: 简化理解1<br>
 * Creating Time: 2021/2/10 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */
@SpringBootTest
public class Test4 {
    @Resource
    StudentService studentService;
    @Resource
    KlassService klassService;
    @Resource
    StudentDao studentDao;
    @Resource
    KlassDao klassDao;

    Klass klass = new Klass();
    Student student1 = new Student();
    Student student2 = new Student();

    @BeforeEach
    public void setUp() {
        klass.setClassName("小葵班").setHeadMaster("远坂院长");
        student1.setAge(11).setStudentName("小白").setKlass(klass);
        student2.setAge(12).setStudentName("小黑");

    }

    /**
     * 单独save 1方 此时,由于1中set了多属性,因此,会顺便save多 即便是没有save多
     */
    @org.junit.jupiter.api.Test
    @Transactional
    @Rollback(false)
    public void test1() {
        studentDao.save(student1);
    }

    /**
     * 多方去进行save 只有多也set了单,才会一块进行save
     */
    @org.junit.jupiter.api.Test
    @Transactional
    @Rollback(false)
    public void test2() {
        klass.setStudents(Arrays.asList(student1, student2));
        klassDao.save(klass);
    }

}
