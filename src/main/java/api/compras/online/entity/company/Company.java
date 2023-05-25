package api.compras.online.entity.company;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode
@Data
@Table(name = "company")
@Entity
@PrimaryKeyJoinColumn(name = "seq_company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq_company", nullable = false)
    private Long id;

    @Column(name = "cnpj")
    private String cnpj;

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
