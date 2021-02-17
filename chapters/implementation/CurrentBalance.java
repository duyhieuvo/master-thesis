@Entity
@Table
public class CurrentBalance {
    @Id
    private String customerId;
    private float currentBalance;
    private int sourcePartition;
	//Constructors, getter, setter methods
}