package ekol.config;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.*;

public class RedisCacheKey implements Serializable {
	public static final SimpleKey EMPTY = new SimpleKey();

	
	private final String applicationName;
	private final String className;
	private final String methodName;
	private final Object[] params;

	public RedisCacheKey(String applicationName, String className, String methodName, Object... elements) {
		Assert.notNull(elements, "Elements must not be null");
		Assert.notNull(applicationName, "Application Name must not be null");
		Assert.notNull(className, "Class Name must not be null");
		Assert.notNull(methodName, "Method Name must not be null");
		
		this.applicationName = applicationName;
		this.className = className;
		this.methodName = methodName;
		this.params = new Object[elements.length];
		System.arraycopy(elements, 0, this.params, 0, elements.length);
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj || (obj instanceof RedisCacheKey
				&& Objects.equals(this.applicationName, ((RedisCacheKey) obj).applicationName)
				&& Objects.equals(this.className, ((RedisCacheKey) obj).className)
				&& Objects.equals(this.methodName, ((RedisCacheKey) obj).methodName)
				&& Arrays.deepEquals(this.params, ((RedisCacheKey) obj).params)));
	}

	@Override
	public final int hashCode() {
		return new HashCodeBuilder()
			.append(applicationName)
			.append(className)
			.append(methodName)
			.append(params)
			.toHashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
	}
}
