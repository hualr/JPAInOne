package com.hualr.jpa.bean;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
    @Column
    String headMaster;
    @Column
    /**
     * 1. ZNN 如果是一对多 一般设置1去放弃维护权 由多去维护
     * 2. target表示的始终是对端配置
     * 3. mappedBy 表示的是对端对应的bean属性name
     */
    @OneToMany(
            targetEntity = Student.class,
            mappedBy = "klass",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
            )
   // @Fetch(FetchMode.SUBSELECT)
    //注意到 多对多只能用List 不能用其他实际接口
    List<Student> students;

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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getHeadMaster() {
        return headMaster;
    }

    public void setHeadMaster(String headMaster) {
        this.headMaster = headMaster;
    }

    //ZNN 堆栈溢出  class想去找student student又想来找class
    @Override
    public String toString() {
        return "Klass{" +
                "classId='" + classId + '\'' +
                ", className='" + className + '\'' +
                ", headMaster='" + headMaster + '\'' +
                '}';
    }
}
