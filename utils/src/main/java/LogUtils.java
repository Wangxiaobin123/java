import java.io.Serializable;

/**
 * @author shengbin
 */
public interface LogUtils{

    /**
     * @param e 异常
     * @return 返回异常信息
     */
     String getException(Throwable e);
}