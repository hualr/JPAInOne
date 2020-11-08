package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.bean.Student;
import com.hualr.jpa.dao.KlassDao;
import com.hualr.jpa.dao.StudentDao;
import com.hualr.jpa.service.api.KlassService;
import com.hualr.jpa.service.api.StudentService;
import java.util.Arrays;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class SpringInOneApplicationTests {
    @Autowired
    StudentService studentService;
    @Autowired
    KlassService klassService;
    @Autowired
    StudentDao studentDao;
    @Autowired
    KlassDao klassDao;

    @Test
    @Transactional
    @Rollback(false)
    public void test1() {
        Klass klass=new Klass();
        klass.setClassName("小葵班");
        Student student1=new Student();
        student1.setAge(11);
        student1.setStudentName("网民");

        Student student2=new Student();
        student2.setAge(11);
        student2.setStudentName("网民");
        klass.setStudents(Arrays.asList(student1,student2));
        /**
         * 1. 单独save 1 此时,由于1中set了多属性,因此,会顺便save多 但是多对应的1属性是无法拿到的
         */
        klassDao.save(klass);
    }
}
