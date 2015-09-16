package ua.com.sofon.workoutlogger.parts;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;
import ua.com.sofon.workoutlogger.util.DateUtil;

/**
 * Created on 14.08.2015.
 * @author Dimowner
 */
public class TrainedWorkout extends Workout {

	public TrainedWorkout() {
		super();
		this.planDate = new Date();
	}

	public TrainedWorkout(int id, String name, String description,
								 Date planDate, Date performDate, int duration,
								 int state, List<TrainedExercise> exerciseList) {
		super(id, name, description, exerciseList);

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
	}

	//----- START Parcelable implementation ----------
	public TrainedWorkout(Parcel in) {
		String[] data = new String[2];
		in.readStringArray(data);
		name = data[0];
		description = data[1];
		Bundle b = in.readBundle();
		planDate = (Date) b.getSerializable("plan_date");
		performDate = (Date) b.getSerializable("perform_date");
		int[] intData = new int[3];
		in.readIntArray(intData);
		id = intData[0];
		duration = intData[1];
		state = intData[2];
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeLong(id);
		out.writeStringArray(new String[]{name, description});
		Bundle b = new Bundle();
		b.putSerializable("plan_date", planDate);
		b.putSerializable("perform_date", performDate);
		out.writeBundle(b);
		out.writeIntArray(new int[]{id, duration, state});
	}

	public static final Parcelable.Creator<TrainedWorkout> CREATOR
			= new Parcelable.Creator<TrainedWorkout>() {
		public TrainedWorkout createFromParcel(Parcel in) {
			return new TrainedWorkout(in);
		}

		public TrainedWorkout[] newArray(int size) {
			return new TrainedWorkout[size];
		}
	};
	//----- END Parcelable implementation ----------

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
		if (state == STATE_ACTIVE
				|| state == STATE_DONE
				|| state == STATE_TODAY
				|| state == STATE_MISSED) {
			this.state = state;
		} else {
			this.state = STATE_DEFAULT;
		}
	}

	@Override
	public void setExerciseList(List<TrainedExercise> exerciseList) {
		if (exerciseList != null) {
			this.exerciseList = exerciseList;
			for (int i = 0; i < exerciseList.size(); i++) {
				exerciseList.get(i).setTrainedWorkoutID(id);
			}
		}
	}

	@Override
	public void addExercise(TrainedExercise e) {
		if (e != null) {
			e.setTrainedWorkoutID(id);
			exerciseList.add(e);
		}
	}

	@Override
	public String toString() {
		StringBuilder exes = new StringBuilder();
		for (int i = 0; i < exerciseList.size(); i++) {
			exes.append(exerciseList.get(i).toString()).append(", ");
		}
		return "TrainedWorkout["
			+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
         + SQLiteHelper.COLUMN_TW_NAME + " = '" + name + "', "
         + SQLiteHelper.COLUMN_TW_DESCRIPTION + " = '" + description + "', "
			+ SQLiteHelper.COLUMN_TW_PLAN_DATE + " = '" + getPlanDateStr() + "', "
			+ SQLiteHelper.COLUMN_TW_DATE + " = '" + getPerformDateStr() + "', "
			+ SQLiteHelper.COLUMN_TW_DURATION + " = '" + duration + "', "
			+ SQLiteHelper.COLUMN_TW_STATE + " = '" + state + "', ["
			+ exes.toString()
			+ "]]";
	}


	public static final int STATE_DEFAULT	= 0;
	public static final int STATE_ACTIVE	= 1;
	public static final int STATE_DONE		= 2;
	public static final int STATE_TODAY		= 3;
	public static final int STATE_MISSED	= 4;

	public static final int DURATION_NOT_SPECIFIED	 = -1;

	private Date planDate;
	private Date performDate = null;
	private int  duration = DURATION_NOT_SPECIFIED;
	private int  state = STATE_DEFAULT;
}
