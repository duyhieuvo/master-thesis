@Entity
@Table
public class CurrentReadingPosition {
    @Id
    private int sourcePartition;
    private long currentReadingPosition;
	//Constructors, getter, setter methods
}