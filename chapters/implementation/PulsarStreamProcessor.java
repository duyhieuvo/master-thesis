public class PulsarStreamProcessor {
    private PulsarClient client;
    private Producer<String> producer;
    private Consumer<String> consumer;
	//..
    public void transformRawEvent(){
        //..
        while(true){
            try {
                //Read new record from the buffer queue
                message = consumer.receive();

                //Transform the raw event
                String transformedEvent = processRawEvent(message);

                //Create a transaction 
                Transaction txn = client
                        .newTransaction()
                        .withTransactionTimeout(5, TimeUnit.MINUTES)
                        .build()
                        .get();

                //Publish the transformed event to output topic
                producer.newMessage(txn)
                         .key(customerId)
                         .value(objectMapper.writeValueAsString(transformedRecord))
                         .send();

                //Acknowledge the consumption of message on input topic
                consumer.acknowledgeAsync(message.getMessageId(),txn);
                txn.commit().get();

            } catch (PulsarClientException e) {
                e.printStackTrace();
            } //..
        }
    }
}
