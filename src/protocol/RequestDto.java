package protocol;

import lombok.Data;

@Data
public class RequestDto {
	private String gubun;
	private String id;
	private String text;
	private String to;
}
