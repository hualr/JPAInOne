package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.bean.Student;
import com.hualr.jpa.dao.KlassDao;
import com.hualr.jpa.dao.StudentDao;
import com.hualr.jpa.service.api.KlassService;
import com.hualr.jpa.service.api.StudentService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/9 19:31
 * Version: 1.0.0
 */
@SpringBootTest
class SpringInOneApplicationTest2 {
    @Autowired
    StudentService studentService;
    @Autowired
    KlassService klassService;
    @Autowired
    StudentDao studentDao;
    @Autowired
    KlassDao klassDao;
    @PersistenceContext
    EntityManager entityManager;
    @PersistenceUnit
    private EntityManagerFactory emFactory;


    @Test
    @Transactional
    @Rollback(false)
    /**
     * 懒加载要点: 懒加载应该设置在事务中 默认如此 可以修改配置 但是这样会造成性能的影响
     */
    public void test1() throws ExecutionException, InterruptedException {
      CompletableFuture<Boolean> future1=CompletableFuture.supplyAsync(() -> {
          boolean participate=isParticipate();
          boolean thread = otherThread1();
          closeSession(participate);
          return thread;
      });
      Boolean result1 = future1.get();
    }

    boolean otherThread1(){
        System.out.println(Thread.currentThread());
        Optional<Klass> klass = klassDao.findById("40286c8175a85ffe0175a8600d930000");
        Klass klass1 = klass.get();
        List<Student> students = klass1.getStudents();
        System.out.println(students);
        return true;
    }

    /**
     * 功能
     * 如果当前线程存在实体工厂,那么就不做任何操作
     * 如果当前线程不存在实体工厂,那么就自己创建一个实体工厂 并且将该线程和实体工厂进行绑定
     * @return
     */
    private boolean isParticipate() {
        boolean participate = false;
        //这个类主要是在维护当前线程的信息 判断当前线程是否存在entityManager
        if (TransactionSynchronizationManager.hasResource(emFactory)) {
            participate = true;
        } else {
            try {
                /**
                 * 比较entityManager和session 前者为jpa中的概念 后者为hibernate中的概念 线程绑定session意味着线程绑定者entitymanager
                 */
                EntityManager em = emFactory.createEntityManager();
                EntityManagerHolder emHolder = new EntityManagerHolder(em);
                TransactionSynchronizationManager.bindResource(emFactory, emHolder);
            } catch (PersistenceException ex) {

            }
        }
        return participate;
    }

    private void closeSession(boolean participate) {
        if (!participate) {
            EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager
                    .unbindResource(emFactory);
            EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
        }
    }
}