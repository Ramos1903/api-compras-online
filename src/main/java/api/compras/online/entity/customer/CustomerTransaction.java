package api.compras.online.entity.customer;

import api.compras.online.entity.company.Company;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "customer_transaction")
@PrimaryKeyJoinColumn(name = "seq_customer_transaction")
public class CustomerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq_customer_transaction", nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "creation_dt")
    private Date date;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "is_credit")
    private Boolean isCredit;

    @Column(name = "customerEncriptedCard")
    private String customerEncriptedCard;

    @Column(name = "is_successfully")
    private Boolean isSuccessfully;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq_customer")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq_company")
    private Company company;
}
