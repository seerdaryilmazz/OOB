package ekol.crm.configuration.component;

import java.util.Objects;

public class EmptyObjectVerification implements EmptyVerification {

	@Override
	public boolean isEmpty(Object value) {
		return Objects.isNull(value);
	}

}
