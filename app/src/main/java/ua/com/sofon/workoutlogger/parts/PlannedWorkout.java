package ua.com.sofon.workoutlogger.parts;

import java.util.Date;
import java.text.SimpleDateFormat;
import ua.com.sofon.workoutlogger.util.DateUtil;

/**
 * Created on 14.08.2015.
 * @author Dimowner
 */
public class PlannedWorkout extends BaseParticle {

	public PlannedWorkout() {
		this.planDate = new Date();
		this.workout = new Workout();
	}

	public PlannedWorkout(long id, Date planDate, Date performDate, int duration,
								 int state, Workout workout) {
		if (id >= 0) {
			this.id = id;
		}
		if (planDate != null) {
			this.planDate = planDate;
		} else {
			this.planDate = new Date();
			this.planDate.setTime(0);
		}
		if (performDate != null) {
			this.performDate = performDate;
		} else {
			this.performDate = null;
		}
		if (duration > 0 || duration == DURATION_NOT_SPECIFIED) {
			this.duration = duration;
		}
		if (state == STATE_DEFAULT
				|| state == STATE_ACTIVE
				|| state == STATE_DONE) {
			this.state = state;
		}
//		if (workoutID > 0 || workoutID == WORKOUT_ID_NOT_SPECIFIED) {
//			this.workoutID = workoutID;
//		}
		if (workout != null) {
			this.workout = workout;
		} else {
			this.workout = new Workout();
		}
	}

	public Date getPlanDate() {
		return planDate;
	}

	public void setPlanDate(Date planDate) {
		if (planDate != null) {
			this.planDate = planDate;
		} else {
			this.planDate = new Date();
			this.planDate.setTime(0);
		}
	}

	public String getPlanDateStr() {
		return DateUtil.getActiveDateFormat().format(planDate);
	}

	public String getPlanDateStr(SimpleDateFormat format) {
		return format.format(planDate);
	}

	public String getPerformDateStr() {
		if (performDate != null) {
			return DateUtil.getActiveDateFormat().format(performDate);
		}
		return "";
	}

	public String getPerformDateStr(SimpleDateFormat format) {
		return format.format(performDate);
	}

	public Date getPerformDate() {
		return performDate;
	}

	public void setPerformDate(Date date) {
		this.performDate = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		if (duration > 0 || duration == DURATION_NOT_SPECIFIED) {
			this.duration = duration;
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		if (state == STATE_DEFAULT
				|| state == STATE_ACTIVE
				|| state == STATE_DONE) {
			this.state = state;
		}
	}

//	public long getWorkoutID() {
//		return workoutID;
//	}
//
//	public void setWorkoutID(long workoutID) {
//		if (workoutID > 0 || workoutID == WORKOUT_ID_NOT_SPECIFIED) {
//			this.workoutID = workoutID;
//		}
//	}

	public Workout getWorkout() {
		return workout;
	}

	public void setWorkout(Workout workout) {
		if (workout != null) {
			this.workout = workout;
		} else {
			this.workout = new Workout();
		}
	}

	@Override
	public String toString() {
		return "PlannedWorkout[ id: '" + id + "', "
			+ "plan_date: '" + getPlanDateStr() + "', "
			+ "perform_date: '" + getPerformDateStr() + "', "
			+ "duration: '" + duration + "', "
			+ "state: '" + state + "', "
			+ "workout[" + workout.toString()
//			+ "workout_id: '" + workoutID +
			+ "]]";

	}

	public static final int STATE_DEFAULT	= 0;
	public static final int STATE_ACTIVE	= 1;
	public static final int STATE_DONE		= 2;

	public static final int DURATION_NOT_SPECIFIED	 = -1;
//	public static final int WORKOUT_ID_NOT_SPECIFIED = -1;

	private Date planDate;
	private Date performDate = null;
	private int  duration	 = DURATION_NOT_SPECIFIED;
	private int  state		 = STATE_DEFAULT;
//	private long workoutID	 = WORKOUT_ID_NOT_SPECIFIED;
	private Workout workout;
}
