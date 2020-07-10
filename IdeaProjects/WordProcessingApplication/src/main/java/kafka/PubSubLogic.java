package kafka;

import java.util.*;
import java.util.Collections;
import java.util.Properties;

import analysistext.AnalysisTextDeserialiser;
import analysistext.AnalysisTextSerialiser;
import kafka.server.KafkaApis;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class PubSubLogic {
    private static final String LOCAL_HOST = "localhost";
    private static PubSubLogic pubSubLogicInstance;
    public static KafkaProducer<String,String> kafkaProducer;
    public static KafkaConsumer<String,String> kafkaConsumer;
    public static final String TOPIC_UNPROCESSED = "Unprocessed";
    public static final String TOPIC_PROCESSED = "Processed";
    //A UUID according to which strings are split
    private final static String splittingUUID = "86e6f012-c209-11ea-b3de-0242ac130004";

    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093;localhost:9094";
    private PubSubLogic(){

    }

    public static PubSubLogic getInstance(){
        if(pubSubLogicInstance == null){
            pubSubLogicInstance = new PubSubLogic();
        }
        return pubSubLogicInstance;
    }
    public <T> boolean sendMessage(String topic, T data){
        Properties kafkaProducerProperties = new Properties();
        kafkaProducerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVERS);
        kafkaProducerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                data instanceof String ? StringSerializer.class : AnalysisTextSerialiser.class);
        kafkaProducerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);

        try(KafkaProducer<String,T> kafkaProducer = new KafkaProducer<String, T>(kafkaProducerProperties)){
            ProducerRecord<String,T> producerRecord = new ProducerRecord<>(topic,data);
            kafkaProducer.send(producerRecord);
            return true;
        }
        catch(KafkaException ex){
            System.out.println("A KafkaException occurred: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * @param topic The topic whose data is to be retrieved
     * */
    public <T> List<String> retrieveLatestMessage(String topic){
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVERS);
        kafkaConsumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConsumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        try(KafkaConsumer<String,String> kafkaConsumer = new KafkaConsumer<String, String>(kafkaConsumerProperties)){

            List<String> retrievedDataList = new ArrayList<>();
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String,String> consumerRecords = kafkaConsumer.poll(1000);
            consumerRecords.records(topic).forEach(consumerRecord ->
                    retrievedDataList.add(consumerRecord.value()));
            return retrievedDataList;
        }
        catch (KafkaException ex){
            System.out.println("A KafkaException occurred: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * @param topic The topic whose data is to be retrieved
     * @param dataType The type used for data retrieval (Necessary for serialisers)
     * */
    public <T> List<T> retrieveLatestMessage(String topic,T dataType){
        Properties kafkaConsumerProperties = new Properties();
        kafkaConsumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,BOOTSTRAP_SERVERS);
        kafkaConsumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaConsumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                dataType instanceof String ? StringDeserializer.class : AnalysisTextDeserialiser.class);
        try(KafkaConsumer<String,T> kafkaConsumer = new KafkaConsumer<String, T>(kafkaConsumerProperties)){

            List<T> retrievedDataList = new ArrayList<>();
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String,T> consumerRecords = kafkaConsumer.poll(1000);
            consumerRecords.records(topic).forEach(consumerRecord ->
                    retrievedDataList.add(consumerRecord.value()));
            return retrievedDataList;
        }
        catch (KafkaException ex){
            System.out.println("A KafkaException occurred: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
}
