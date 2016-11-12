package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.Document;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "empl")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empl {
    private String firstname;
    private String lastname;
    private Integer age;
    private Integer salary;
    private Long createdDate;

    public Empl() {
    }

    public Empl(Document document) {
        this.firstname = document.getString("firstname");
        this.lastname = document.getString("lastname");
        this.age = document.getInteger("age");
        this.salary = document.getInteger("salary");
        this.createdDate = document.getLong("createdDate");
    }

    @Override
    public String toString() {
        return "com.model.Empl ( firstname=" + firstname + ", lastname" + lastname + ", age=" + age + ", salary=" + salary + ", createdDate=" + createdDate + ");";
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    @JsonIgnore
    public Document getDocument() {
        return new Document()
                .append("firstname", this.firstname)
                .append("lastname", this.lastname)
                .append("age", this.age)
                .append("salary", this.salary)
                .append("createdDate", this.createdDate);
    }
}