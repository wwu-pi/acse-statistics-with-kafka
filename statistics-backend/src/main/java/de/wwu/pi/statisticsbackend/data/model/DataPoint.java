package de.wwu.pi.statisticsbackend.data.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
/**
 * Class storing the submitted data points.
 */
public class DataPoint implements Serializable {

	private static final long serialVersionUID = 5831900771828913491L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotNull
	private double x;
	
	public DataPoint(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}
}
