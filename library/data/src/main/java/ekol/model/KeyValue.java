package ekol.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyValue<K, V> {
	private K key;
	private V value;
	
	public KeyValue() {
		
	}
	
	public KeyValue(K key, V value) {
		this.key = key;
		this.value =value;
	}
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(27, 43).
                append(getKey()).
                toHashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KeyValue<?,?>))
            return false;
        if (object == this)
            return true;

        KeyValue<K,V> pair = (KeyValue<K,V>) object;
        return new EqualsBuilder().
                append(getKey(), pair.getKey()).
                isEquals();
    }

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
    
    
}
