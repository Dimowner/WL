package ua.com.sofon.workoutlogger.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import ua.com.sofon.workoutlogger.Config;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGV;

/**
 * Activity shows saved application logs.
 * @author Dimowner
 */
public class LogsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logs);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		TextView content = (TextView) findViewById(R.id.logs_contnet);
		content.setText(readFileData());

//		getLogCat string
//		try {
//			Process process = Runtime.getRuntime().exec("logcat -d");
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(process.getInputStream()));
//
//			StringBuilder log=new StringBuilder();
//			String line = "";
//			while ((line = bufferedReader.readLine()) != null) {
//				log.append(line);
//			}
//			content.setText(log.toString());
//		} catch (IOException e) {
//			LOGV(LOG_TAG, "", e);
//		}
	}

	private String readFileData() {
		LOGV(LOG_TAG, "readFileData");
		File sdcard = LogUtils.getAlbumStorageDir(Config.APP_DIRECTORY);

		//Get the text file
		File file = new File(sdcard, Config.LOG_FILE_NAME);

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		} catch (IOException e) {
			//You'll need to add proper error handling here
			LOGE(LOG_TAG, "", e);
		}
		return text.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		LOGV(LOG_TAG, "onCreateoptionMenu");
		getMenuInflater().inflate(R.menu.menu_logs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		LOGV(LOG_TAG, "onOptionsItemSelected");

		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_send_email:
				LOGV(LOG_TAG, "send email");
				sendFileByEmail();
				return true;
			case R.id.action_open_file_location:
				LOGV(LOG_TAG, "open file location");
				openFileLocation();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void sendFileByEmail() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{"dimmony@gmail.com"});
		i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
		i.putExtra(Intent.EXTRA_TEXT, "body of email");
		File file = new File(
				LogUtils.getAlbumStorageDir(Config.APP_DIRECTORY).getAbsolutePath()
						+ "/" + Config.LOG_FILE_NAME
		);
		i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(LogsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	private void openFileLocation() {
		File root = LogUtils.getAlbumStorageDir(Config.APP_DIRECTORY);
		Uri uri = Uri.fromFile(root);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "resource/folder");
		intent.setData(uri);
		startActivity(Intent.createChooser(intent, "Choose application"));
	}

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
