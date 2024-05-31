package entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data

@Entity
@Table(name = "courts")
public class Court {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
    private Date date;

    private double cout;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "portefeuille_id")
    private Portefeuille portefeuille;

	public Court(Long id, Date date, double cout, Portefeuille portefeuille) {
		super();
		this.id = id;
		this.date = date;
		this.cout = cout;
		this.portefeuille = portefeuille;
	}

	public Court() {
		super();
	}

	public Court(Date date, double cout, Portefeuille portefeuille) {
		super();
		this.date = date;
		this.cout = cout;
		this.portefeuille = portefeuille;
	}

}
