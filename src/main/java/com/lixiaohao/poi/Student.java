package com.lixiaohao.poi;

/**
 * Created by lixiaohao on 2017/3/9
 *
 * @Description
 * @Create 2017-03-09 12:07
 * @Company
 */
public class Student {
    int id;
    int age;
    String name;

    public Student(int id, String name, int age) {
        this.id = id;
        this.age = age;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
