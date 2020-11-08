package com.hualr.jpa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/8 19:32
 * Version: 1.0.0
 */
@Entity
@Table
public class Klass {

    @Id
    @Column(name = "uuid", columnDefinition = "char(32) comment '主键'")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    String classId;
    @Column
    String className;
/*    @Column
    List<Student> students;*/

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
