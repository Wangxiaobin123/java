import java.io.Serializable;

/**
 * @author shengbin
 */
public class ReportLogUtils extends LogInfo implements LogUtils, Serializable {
    private static final long serialVersionUID = -7683693368590071408L;

    /**
     * @param e 异常
     * @return 返回异常信息
     */
    @Override
    public String getException(Throwable e) {
        Throwable cause = e.getCause();
        String message = e.getMessage();
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement aStackTrace : stackTrace) {
            System.out.println(aStackTrace);
        }
        return null;
    }
}
