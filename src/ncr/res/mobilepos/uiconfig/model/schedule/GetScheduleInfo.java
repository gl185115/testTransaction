package ncr.res.mobilepos.uiconfig.model.schedule;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "GetScheduleInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class GetScheduleInfo extends ResultBase {
	
	@XmlElement(name = "schedule")
	private Schedule schedule;

	/**
	 * @return the schedule
	 */
	public Schedule getSchedule() {
		return schedule;
	}

	/**
	 * @param the schedule to set
	 */
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("schedule: ").append(schedule.toString());
		return sb.toString();
	}
}
