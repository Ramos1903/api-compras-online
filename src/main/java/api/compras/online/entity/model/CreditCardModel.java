package api.compras.online.entity.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public abstract class CreditCardModel {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "number")
    private String number;

    @Column(name = "valid_thru")
    private String validThru;

    @Column(name = "sec_code")
    private String secCode;

    @Column(name = "flag_name")
    private String flagName;

    @Column(name = "is_credit")
    private Boolean isCredit;
}
