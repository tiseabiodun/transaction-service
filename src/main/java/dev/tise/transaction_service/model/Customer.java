package dev.tise.transaction_service.model;

import jakarta.persistence.*;


@Entity
@Table(name = "customer")
public class Customer {

    public Customer() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private String state;
    private String email;

    private String customerAccount;

    public Customer(String name, int age, String state, String email) {
        this.name = name;
        this.age = age;
        this.state = state;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(Account customerAccount) {
        this.customerAccount = String.valueOf(customerAccount);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", state='" + state + '\'' +
                ", email='" + email + '\'' +
                ", customerAccount='" + customerAccount + '\'' +
                '}';
    }
}
