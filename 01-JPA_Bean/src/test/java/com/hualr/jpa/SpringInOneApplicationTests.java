package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.bean.Student;
import com.hualr.jpa.dao.KlassDao;
import com.hualr.jpa.dao.StudentDao;
import com.hualr.jpa.service.api.KlassService;
import com.hualr.jpa.service.api.StudentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        student1.setKlass(klass);
        Student student2=new Student();
        student2.setAge(11);
        student2.setStudentName("网民");
        //klass.setStudents(Arrays.asList(student1,student2));
        /**
         * 1. 单独save 1 此时,由于1中set了多属性,因此,会顺便save多 但是多对应的1属性是无法拿到的
         */
//        klassDao.save(klass);
        /**
         * 2. 多去save -->谁维护表,那么谁去save.这才是核心:这才不会出现外键丢失的情况
         */
        studentDao.save(student1);
    }

    /**
     * 在同一个事务中,save的确有更新的作用.
     */

    @Test
    @Transactional
    @Rollback(false)
    public void test2() {
        Klass klass=new Klass();
        klass.setClassName("小葵班");
        //第一次 save 1
         klassDao.save(klass);
        Student student1=new Student();
        student1.setAge(11);
        student1.setKlass(klass);
        List<Student> students=new ArrayList<>();
        students.add(student1);
        //ZNN 这里无法直接Arrays.asList
        klass.setStudents(students);
        klass.setHeadMaster("梨花 1");
        //第二次save 1
        klassDao.save(klass);
    }
}
