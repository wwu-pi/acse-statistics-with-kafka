package de.wwu.pi.statisticsbackend.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.wwu.pi.statisticsbackend.data.model.DataPoint;

public interface DataPointRepository extends JpaRepository<DataPoint, Long> {
	
	@Query("SELECT AVG(x) as average FROM DataPoint")
	Double findAvgX();
	
	@Query("SELECT Max(x) as average FROM DataPoint")
	Double findMaxX();

	@Query("SELECT Min(x) as average FROM DataPoint")
	Double findMinX();

}
