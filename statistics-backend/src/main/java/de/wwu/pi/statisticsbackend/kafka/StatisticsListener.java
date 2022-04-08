package de.wwu.pi.statisticsbackend.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import de.wwu.pi.statisticsbackend.data.model.DataPoint;
import de.wwu.pi.statisticsbackend.data.repo.DataPointRepository;

@Component
public class StatisticsListener {
		
	@Autowired
	private DataPointRepository repo;
	
	@KafkaListener(id="KafkaStatisticsDPListener", topics="KDPT"
			// The value for groupId can be found in application.properties
			// Those Kafka listeners that belong to the same groupId will DISJOINTY retrieve the inputs!
			// That means that no two listeners in the same consumer group will receive the same data.
			// This can be used to simulate queues.
			// In the specified scenario, it only makes sense that each instance of the backend defines a different consumergroup
			// so that backends receive the data point.
			// Further information from https://kafka.apache.org/documentation/#design_consumerposition :
			// "Our topic is divided into a set of totally ordered partitions, each of which is consumed by exactly 
			// one consumer within each subscribing consumer group at any given time.
			// There are more pitfalls here; if groupId is not set, idIsGroup in @KafkaListener determines whether id is used instead (default is true).
			// Since in listenForFunctionCall, we did not configure groupId, and because idIsGroup is true, the consumers in KafkaStatisticsFCListener 
			// share the incoming messages.
			, groupId="${backend.datapoint.consumergroup}")
	public void listenForDataPoint(Double d) {
		DataPoint dp = new DataPoint(d);
		repo.save(dp);
	}
	
	@KafkaListener(id="KafkaStatisticsFCListener", topics="KFCQ")
	@SendTo // Uses replyto information and correlationid to send back answer
	public Double listenForFunctionCall(ConsumerRecord<String, String> cr) {		
		String function = cr.value();
		Double functionCallResult;
		switch (function) {
			case "average":
				functionCallResult = repo.findAvgX();
				break;
			case "minimum":
				functionCallResult = repo.findMinX();
				break;
			case "maximum":
				functionCallResult = repo.findMaxX();
				break;
			default: 
				throw new IllegalStateException(function);	
		}
		functionCallResult = functionCallResult == null ? 0.0 : functionCallResult;
		return functionCallResult;
		
	}
}
