package com.simple.pojo;

public class Test {
    private Integer id;

    private String username;

    private String phome;

    private String sex;

    private String age;

    public Test(Integer id, String username, String phome, String sex, String age) {
        this.id = id;
        this.username = username;
        this.phome = phome;
        this.sex = sex;
        this.age = age;
    }

    public Test() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPhome() {
        return phome;
    }

    public void setPhome(String phome) {
        this.phome = phome == null ? null : phome.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age == null ? null : age.trim();
    }
}