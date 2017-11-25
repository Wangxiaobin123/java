import java.io.Serializable;

/**
 *
 * @author shengbin
 */
public abstract class LogInfo implements Serializable{
    private static final long serialVersionUID = -610995202518367613L;
    String info;
    String time;
    String requestGoal;

}
