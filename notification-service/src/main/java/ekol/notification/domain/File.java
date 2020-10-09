package ekol.notification.domain;

import java.io.Serializable;

import lombok.Data;

@Data
public class File implements Serializable {
	private String filename;
	private String fileContent;
}
