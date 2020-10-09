package ekol.resource;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ozer on 22/02/2017.
 */
public class RequestContext extends ConcurrentHashMap<String, Object> {

    protected static final ThreadLocal<RequestContext> threadLocal = new ThreadLocal<RequestContext>() {
        @Override
        protected RequestContext initialValue() {
            try {
                return RequestContext.class.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    };

    protected RequestContext() {
        super();
    }

    public static RequestContext getCurrentContext() {
        return threadLocal.get();
    }

    public String getRefreshToken() {
        return (String) get("refreshToken");
    }

    public void setRefreshToken(String refreshToken) {
        put("refreshToken", refreshToken);
    }

    public static void unset() {
        threadLocal.remove();
    }
}
