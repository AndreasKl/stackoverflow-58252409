package net.andreaskluth.kafkashouldwrk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class KafkaShouldWrkApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaShouldWrkApplication.class, args);
  }

  @Component
  static class SomeListener {

    @KafkaListener(topics = "topicName", groupId = "foo")
    public void listen(String message) {
      System.out.println("Received Message in group foo: " + message);
    }
  }

  @Configuration
  static class KafkaConfiguration {

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
        ConsumerFactory<Object, Object> kafkaConsumerFactor,
        ConcurrentKafkaListenerContainerFactoryConfigurer configurer) {

      ConcurrentKafkaListenerContainerFactory<Object, Object> factory =
          new ConcurrentKafkaListenerContainerFactory<>();
      configurer.configure(factory, kafkaConsumerFactor);

      ContainerProperties containerProperties = factory.getContainerProperties();
      containerProperties.setMissingTopicsFatal(false);
      return factory;
    }
  }
}
