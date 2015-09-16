package ua.com.sofon.workoutlogger.parts;

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;
import ua.com.sofon.workoutlogger.util.DateUtil;

/**
 * Class contains one body weight evaluation.
 * @author Dimowner
 */
public class BodyWeight extends BaseParticle implements Parcelable {

	public BodyWeight() {
		dateTime = new Date();
		weight = WEIGHT_NOT_SPECIFIED;
		fatIndex = FAT_INDEX_NOT_SPECIFIED;
		comment = "";
	}

	public BodyWeight(int id, Date dateTime, float weight, float fatIndex, String comment) {
		if (id >= 0) {
			this.id = id;
		}
		if (dateTime != null) {
			this.dateTime = dateTime;
		} else {
			this.dateTime = new Date();
			this.dateTime.setTime(0);
		}
		if (weight > 0) {
			this.weight = weight;
		}
		if (fatIndex >= 0 && fatIndex <= 100) {
			this.fatIndex = fatIndex;
		}
		if (comment != null && !comment.isEmpty()) {
			this.comment = comment;
		}
	}

	//----- START Parcelable implementation ----------
	public BodyWeight(Parcel in) {
		id = in.readInt();
		dateTime = (Date) in.readSerializable();
		float[] floatData = new float[2];
		in.readFloatArray(floatData);
		weight = floatData[0];
		fatIndex = floatData[1];
		comment = in.readString();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeSerializable(dateTime);
		out.writeString(comment);
		out.writeFloatArray(new float[] {weight, fatIndex});
	}

	public static final Parcelable.Creator<BodyWeight> CREATOR
			= new Parcelable.Creator<BodyWeight>() {
		public BodyWeight createFromParcel(Parcel in) {
			return new BodyWeight(in);
		}

		public BodyWeight[] newArray(int size) {
			return new BodyWeight[size];
		}
	};
	//----- END Parcelable implementation ----------

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		if (dateTime != null) {
			this.dateTime = dateTime;
		}
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		if (weight > 0) {
			this.weight = weight;
		}
	}

	public float getFatIndex() {
		return fatIndex;
	}

	public void setFatIndex(float fatIndex) {
		if (fatIndex >= 0 && fatIndex <= 100) {
			this.fatIndex = fatIndex;
		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		if (comment != null && !comment.isEmpty()) {
			this.comment = comment;
		}
	}

	public String getDateStr() {
		return DateUtil.getActiveDateFormat().format(dateTime);
	}

	public String getTimeStr() {
		return DateUtil.timeFormat.format(dateTime);
	}

	@Override
	public String toString() {
		return "Exercise:["
				+ SQLiteHelper.COLUMN_ID + " = '" + id + "', "
				+ SQLiteHelper.COLUMN_WEIGHING_DATE_TIME + " = '"
				+ DateUtil.getActiveDateTimeFormat().format(dateTime) + "', "
				+ SQLiteHelper.COLUMN_WEIGHT + " = '" + weight + "', "
				+ SQLiteHelper.COLUMN_FAT_INDEX + " = '" + fatIndex + "', "
				+ SQLiteHelper.COLUMN_WEIGHT_COMMENT + " = '" + comment + "', "
				+ "']";
	}


	public static final float FAT_INDEX_NOT_SPECIFIED = -1;
	public static final float WEIGHT_NOT_SPECIFIED = -1;

	private Date dateTime;
	private float weight = WEIGHT_NOT_SPECIFIED;
	private float fatIndex = FAT_INDEX_NOT_SPECIFIED;
	private String comment = "";
}
