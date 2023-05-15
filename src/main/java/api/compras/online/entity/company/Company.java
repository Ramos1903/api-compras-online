package api.compras.online.entity.company;

import api.compras.online.entity.model.UserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "company")
@Entity
@PrimaryKeyJoinColumn(name = "seq_company")
public class Company extends UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seq_company", nullable = false)
    private Long id;

    @Column(name = "cnpj")
    private String cnpj;
}
