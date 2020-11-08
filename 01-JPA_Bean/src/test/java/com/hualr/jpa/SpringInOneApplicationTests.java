package com.hualr.jpa;

import com.hualr.jpa.bean.Klass;
import com.hualr.jpa.service.api.KlassService;
import com.hualr.jpa.service.api.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringInOneApplicationTests {
    @Autowired
    StudentService studentService;
    @Autowired
    KlassService klassService;
    @Test
    void contextLoads() {
    }
}
