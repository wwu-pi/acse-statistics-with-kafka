package de.wwu.pi.statisticsbackend.data.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

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
