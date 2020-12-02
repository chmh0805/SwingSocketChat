package protocol;

import lombok.Data;

@Data
public class RequestDto {
	private String id;
	private String text;
	private String to;
}
