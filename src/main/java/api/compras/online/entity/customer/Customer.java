package api.compras.online.entity.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode
@Data
@Table(name = "customer")
@Entity
@PrimaryKeyJoinColumn(name = "seq_customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq_customer", nullable = false)
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

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

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerCard> cards;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerTransaction> transactions;

}
