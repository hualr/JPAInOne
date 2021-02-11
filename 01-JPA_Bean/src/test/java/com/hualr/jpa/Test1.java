package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.bean.Student;
import com.hualr.jpa.dao.StudentDao;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Function: 理解save<br>
 * Creating Time: 2021/2/10 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */
@Slf4j
@SpringBootTest
@EnableTransactionManagement
public class Test1 {
    private final Klass klass = new Klass();
    private final Student student1 = new Student();
    @Resource
    private StudentDao studentDao;
    @Resource
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        klass.setClassName("小葵班");
        student1.setAge(11).setStudentName("小白").setKlass(klass);
    }

    /**
     * 1. 只要添加了事务注解 就意味着整个方法的commit操作一定在完成所有动作之后 毕竟是AOP嘛
     */

    @SneakyThrows
    @Test
    @Transactional
    @Rollback(false)
    public void test1() {
        studentDao.save(student1);
        studentDao.flush();
        Thread.sleep(10000L);
        System.out.println("完成事务提交");
    }

    /**
     * 2. saveAndFlush的唯一作用即为在save作用到数据库的时候还无法确定的值可以通过flush之后拿到 比如ID
     */
    @SneakyThrows
    @Test
    @Transactional
    @Rollback(false)
    public void test2() {
        studentDao.saveAndFlush(student1);
        System.out.println(student1.getStudentId());
        Thread.sleep(10000L);
        System.out.println("完成事务提交");
    }

    /**
     * 3. 想要使得save立即生效 而中途不会等待太久 那么就尝试去掉事务或者简化事务
     */
    @SneakyThrows
    @Test
    public void test3() {
        studentDao.save(student1);
        System.out.println("完成事务提交");
    }

    /**
     * 4. 编程式事务
     */
    @Test
    public void test4() {
        final Integer execute = transactionTemplate.execute(new TransactionCallback<Integer>() {
            @Nullable
            @Override
            public Integer doInTransaction(TransactionStatus transactionStatus) {
                Integer result;
                try {
                    studentDao.save(student1);
                    result = 1;
                } catch (Exception e) {
                    transactionStatus.setRollbackOnly();
                    System.out.println("Transfer error!");
                    throw e;
                }
                return result;
            }
        });
        System.out.println(execute);
    }
}

