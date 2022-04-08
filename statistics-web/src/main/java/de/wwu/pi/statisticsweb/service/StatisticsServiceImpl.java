package de.wwu.pi.statisticsweb.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private KafkaTemplate<String, Double> dataPointKafkaTemplate;
	
	@Autowired
	private ReplyingKafkaTemplate<String, String, Double> functionCallKafkaTemplate;
	
	
	/**
	 * Sends a message to the statistics backend with a new DataPoint
	 * @param Double x new DataPoint.
	 */
	public void addStatistics(Double x) {
		dataPointKafkaTemplate.send("KDPT", x);
	}

	/**
	 * Query the minimum of all stored DataPoints.
	 * @return the minimum.
	 */
	public Double getMinimum() {
		Double result = this.callFunction("minimum");
		
		return result;
	}

	/**
	 * Query the maximum of all stored DataPoints.
	 * @return the maximum.
	 */
	public Double getMaximum() {
		Double result = this.callFunction("maximum");
		return result;
	}

	/**
	 * Query the average of all stored DataPoints.
	 * @return the average.
	 */
	public Double getAverage() {
		Double result = this.callFunction("average");
		return result;
	}

	/**
	 * Retrieves a function value from the backend using synchronous Kafka messages.
	 * @param String function to be called. 
	 * @return value of this function for all DataPoints
	 */
	protected Double callFunction(String function) {
		// Send to the KFCQ topic
		ProducerRecord<String, String> pr = new ProducerRecord<>("KFCQ", function);
		// Await answer in the KFCReplies topic
		pr.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "KFCReplies".getBytes()));
		// The correlation id does not need to be set manually. This additional metadata will be used to map the result, that is stored in a reply topic,
		// to the awaiting caller.
		// Synchronously call Kafka:
		RequestReplyFuture<String, String, Double> future = 
				functionCallKafkaTemplate.sendAndReceive(pr);
		try {
	        ConsumerRecord<String, Double> resultWrapper = future.get(10, TimeUnit.SECONDS);
			Double result = resultWrapper.value();
			return result;
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
