package com.catelecom.billing.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer package_id;

    private String package_name;
    private Integer amount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date = new Date();

    public Package() {
        super();
    }

    public Package(Integer package_id, String package_name, Integer amount, Date create_date) {
        super();
        this.package_id = package_id;
        this.package_name = package_name;
        this.amount = amount;
        this.create_date = create_date;
    }

    public Integer getPackage_id() {
        return package_id;
    }

    public void setPackage_id(Integer package_id) {
        this.package_id = package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "Package{" +
                "package_id=" + package_id +
                ", package_name='" + package_name + '\'' +
                ", amount=" + amount +
                ", create_date=" + create_date +
                '}';
    }
}
