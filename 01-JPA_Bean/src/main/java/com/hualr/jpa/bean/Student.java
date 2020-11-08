package com.hualr.jpa.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/8 19:30
 * Version: 1.0.0
 */
@Entity
@Table
public class Student {
    /**
     * 1 ZNN 如果配置为自增类型 那么这意味着主键必须为数字类型的值
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer studentId;
    @Column
    private String studentName;
    @Column
    private Integer age;

    /**
     * 1. 关联关系没有@Column注解
     */
    @OneToOne(
            targetEntity = Klass.class,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Klass klass;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Klass getKlass() {
        return klass;
    }

    public void setKlass(Klass klass) {
        this.klass = klass;
    }
}
