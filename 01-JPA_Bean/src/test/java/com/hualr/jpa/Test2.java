package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.bean.Student;
import com.hualr.jpa.dao.StudentDao;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Function: select深入<br>
 * Creating Time: 2021/2/10 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */
@SpringBootTest
public class Test2 {
    Klass klass = new Klass();
    Student student1 = new Student();
    @Resource
    private StudentDao studentDao;
    @Resource
    private TransactionTemplate transactionTemplate;


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
     * <h2>理解查询: 在一个事务中,查询的结果只和第一次查询有关.</h2>
     * 即为:<br>
     * 当我们开启了两个事务A B<br>
     * 事务A进行修改id=1行后并未提交,此时事务B开始,查询id=1的值.则之后就算使用当前查,查询的也只是id=1在事务A未提交之前的数据<br>
     * 事务A进行id=1修改后,此时事务B开始,如果此时事务A已经提交了,那么此时查询的就是事务A更改的值<br>
     * <p>
     * 在一个事务中,两次一摸一样的查询在本事务没有修改的情况下,不会出现不一样的结果<br>
     * 所以在一个事务中, 使用排他锁之前,不需要进行不加锁的查询<br>
     */
    @Test
    @Transactional
    @Rollback(false)
    public void test1() {
        //01 事务1开启
        //03 此时对1行表加锁
        studentDao.findStudentById(1).ifPresent(student -> {
            student.setAge(1006);
            //05 修改后进行提交 此时数据库更新 锁失效
            studentDao.save(student);
        });
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test2() {
        //03 首先查一次表
        studentDao.findById(1);
        //04 事务2开启
        //06 此时对1行表加锁
        studentDao.findStudentById(1).ifPresent(student -> {
            student.setStudentName("ami");
            studentDao.save(student);
        });
    }

    /**
     *
     */
    @Test
    @Transactional
    @Rollback(false)
    public void test6() {
        Student student1;
        Student student2;
        while (true) {
            student1 = studentDao.findStudentById(1).orElse(null);
            //只会查询一次在一个事务中
            student2 = studentDao.findStudentById(1).orElse(null);
        }
    }
}
