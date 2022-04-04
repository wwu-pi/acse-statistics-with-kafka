package de.wwu.pi.statisticsweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@EnableKafka
@Configuration
@SpringBootApplication
public class StatisticsWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatisticsWebApplication.class, args);
	}
	
	@Bean
	public KafkaTemplate<String, Double> dataPointKafkaTemplate(
			ProducerFactory<String, Double> pr) {
		return new KafkaTemplate<>(pr);
	}
	
	@Bean
	public ReplyingKafkaTemplate<String, String, Double> functionCallKafkaTemplate(
			ProducerFactory<String, String> producerFactory,
			ConcurrentMessageListenerContainer<String, Double> cmlc) {
		return new ReplyingKafkaTemplate<>(producerFactory, cmlc);
	}
	
	@Bean
	public ConcurrentMessageListenerContainer<String, Double> listenerContainer(
			ConcurrentKafkaListenerContainerFactory<String, Double> cklcf) {
		ConcurrentMessageListenerContainer<String, Double> result = cklcf.createContainer("KFCReplies");
		result.getContainerProperties().setGroupId("KafkaStatisticsFC");
		result.setAutoStartup(false);
		return result;
	}
}
