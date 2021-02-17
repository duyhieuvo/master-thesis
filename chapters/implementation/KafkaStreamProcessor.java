public class KafkaStreamProcessor {
	private KafkaProducer<String,String> producer;
	private KafkaConsumer<String,String> consumer;
	//Initialize producer and consumer, subscribe consumer to Kafka topic
	public void transformRawEvent(){
		while(true) {
			//Pull a new batch of messages from Kafka
			ConsumerRecords<String, String> consumerRecords 
				= consumer.poll(Duration.ofSeconds(10));
			//..
			try{
				//Begin the transaction
				producer.beginTransaction();
				
				//Loop through the pulled messages
				for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
					producerRecord = processConsumedRecord(consumerRecord);
					//Send transformed event to 'transformed-event' topic
					RecordMetadata recordMetadata=producer.send(producerRecord).get();
				}
			
				//Update the offset on the source topic 'raw-event' in the same transaction
				producer.sendOffsetsToTransaction(getOffsetToCommitOnSourceTopic(consumerRecords),
																		consumer.groupMetadata());
				
				//Commit the transaction
				producer.commitTransaction();
			} catch (ProducerFencedException  e) {
				producer.close();
			} catch (CommitFailedException e) {
				producer.abortTransaction();
			} //..
		}
	}	
	//Helper methods	
}