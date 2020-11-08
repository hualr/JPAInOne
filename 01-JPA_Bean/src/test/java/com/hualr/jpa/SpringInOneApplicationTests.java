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
import java.util.Optional;
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
        Klass klass = new Klass();
        klass.setClassName("小葵班");
        Student student1 = new Student();
        student1.setAge(11);
        student1.setStudentName("网民");
        student1.setKlass(klass);
        Student student2 = new Student();
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
        Klass klass = new Klass();
        klass.setClassName("小葵班");
        //第一次 save 1
        klassDao.save(klass);
        Student student1 = new Student();
        student1.setAge(11);
        student1.setKlass(klass);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        //ZNN 这里无法直接Arrays.asList
        klass.setStudents(students);
        klass.setHeadMaster("梨花 1");
        //第二次save 1
        klassDao.save(klass);
    }

    /**
     * 事务的隔离机制
     * 1. 隔离机制导致的查询结果不是看什么时候事务开始 而是看什么时候进行第一次查询 第一次查询一定是从实际数据库中拉取的(这是我目前的理解)
     * 2. 两个事务同时save 由于都是基于第一次的查询 因此会存在save覆盖的现象
     * 3. 为了避免覆盖 我们可以指定save的属性为只update修改过的属性 即为只update set过的属性
     * 4. jpa的优化机制可以保证save之前和save之后没有变化的时候,就不会进行任何更新
     *
     *在此处我引入了动态更新属性 该属性保证如果原本就没有更新一个空值 那么就不会以此时的null覆盖更新
     */

    /**
     * 理解排他锁
     * 实际上排他锁并非是当前查 在jpa中,如果即便使用排他锁,此时读取的也是第一次select的值.这是为了保证事务的一致性
     * 你无法让两次一毛一样的查询出现不一样的结果 这违背了事务的一致性 所以,在使用排他锁之前,千万不要进行一次不加锁的查询
     * 否则此时排他锁查询到的东西,一定是第一次查询的结果
     */
    @Test
    @Transactional
    @Rollback(false)
    public void test3() {
        //01 事务1开启
        //03 此时对4行表加锁
        Optional<Student> studentOption = studentDao.findStudentById(4);
        Student student = studentOption.get();
        student.setAge(1003);
        //05 修改后进行提交 此时数据库更新 锁失效
        studentDao.save(student);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test4() {
        //02 事务2开始
        //04 此时想要对4行表查询加锁 但是发现此时4表上已经存在锁 因此陷入等待状态
        //06 此时,检验到锁不存在 因此继续进行操作 先加锁 后进行当前查询
        Optional<Student> studentOption = studentDao.findStudentById(4);
        Student student = studentOption.get();
        student.setStudentName("小秘密1111122222");
        studentDao.save(student);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test5() {
        Optional<Student> studentOption;
        Student student1 = null;
        Student student2 = null;
        while (true) {
            student1 = student2;
            //只会查询一次在一个事务中
            studentOption = studentDao.findById(4);
            student2 = studentOption.get();
            if (student1 != null && !student1.equals(student2)) {
                System.out.println("student1" + student1);
                System.out.println("student2" + student2);
            }
        }
    }

    /**
     * 对于排他锁 会保持时刻的查询 但是没有任何意义啊 因为一旦加锁了 这行数据就不会进行任何修改了
     * 且如果进行了第一次查询 排他锁查询的也只是第一次查询的结果 没有任何意义!!
     */

    @Test
    @Transactional
    @Rollback(false)
    public void test6() {
        Optional<Student> studentOption;
        Student student1 = null;
        Student student2 = null;
        Student student3 = studentDao.findById(4).get();
        while (true) {
            student1 = student2;
            //只会查询一次在一个事务中
            studentOption = studentDao.findStudentById(4);
            student2 = studentOption.get();
            if (student1 != null && !student1.equals(student2)) {
                System.out.println("student1" + student1);
                System.out.println("student2" + student2);
            }
        }
    }
}
