package api.compras.online.entity.customer;

import api.compras.online.entity.model.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "customer_card")
@Entity
@PrimaryKeyJoinColumn(name = "seq_customer_card")
public class CustomerCard extends UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq_customer_card", nullable = false)
    private Long id;

    @Column(name = "encripted_card_json")
    private String encriptedCardJson;

    @Column(name = "private_key")
    private String privateKey;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "is_active")
    private String isActive;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_dt")
    private Date registerDt;

    @Temporal(TemporalType.DATE)
    @Column(name = "modify_dt")
    private Date modifyDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq_customer")
    private Customer customer;
}
