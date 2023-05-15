package api.compras.online.entity.customer;

import api.compras.online.entity.model.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "customer")
@Entity
@PrimaryKeyJoinColumn(name = "seq_customer")
public class Customer extends UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq_customer", nullable = false)
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerCard> cards;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerTransaction> transactions;

}
