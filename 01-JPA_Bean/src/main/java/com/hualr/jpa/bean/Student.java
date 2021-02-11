package com.hualr.jpa.bean;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Author: zongqi
 * Function:
 * Creating Time：2020/11/8 19:30
 * Version: 1.0.0
 */
@Entity
@Table
//这个注解可以保证之前为空的值 之后不会被更新
@DynamicUpdate
@Data
@Accessors(chain = true)
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
            //ZNN 级联关系的保存由此确认 不设置会出现瞬时状态导致无法增删改查的问题
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Klass klass;

}
