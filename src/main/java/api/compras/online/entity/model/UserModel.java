package api.compras.online.entity.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public abstract class UserModel {

    @Column(name = "name")
    private String name;

    @Column(name = "e_mail")
    private String eMail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "zip_code")
    private String zipCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_dt")
    private Date registerDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "modify_dt")
    private Date modifyDt;
}
