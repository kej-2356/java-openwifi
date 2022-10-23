package entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LocationHistory {
	private int id;
	private String lhLat;
	private String lhLnt;
	private String viewDate;
}
