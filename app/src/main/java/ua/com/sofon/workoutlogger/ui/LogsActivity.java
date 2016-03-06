package ua.com.sofon.workoutlogger.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import ua.com.sofon.workoutlogger.Config;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.DateUtil;
import ua.com.sofon.workoutlogger.util.FileUtil;
import ua.com.sofon.workoutlogger.util.LogUtils;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Base activity shows saved application logs.
 * @author Dimowner
 */
public class LogsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logs);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
		}

		content = (TextView) findViewById(R.id.logs_contnet);
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

	protected String readFileData() {
		File sdcard = FileUtil.getStorageDir(FileUtil.getAppDirectoryName());

		//Get the text file
		File file = new File(sdcard, Config.LOG_FILE_NAME);

		//Read text from file
		StringBuilder text = new StringBuilder();

		try {
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;

				while ((line = br.readLine()) != null) {
					text.append(line);
					text.append('\n');
				}
				br.close();
			}
		} catch (IOException e) {
			//You'll need to add proper error handling here
			LOGE(LOG_TAG, "", e);
		}
		if (text.length() > 0) {
			return text.toString();
		} else {
			return getString(R.string.log_no_data);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_logs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		} else if (item.getItemId() == R.id.action_send_email) {
			sendFileByEmail();
			return true;
		} else if (item.getItemId() == R.id.action_open_file_location) {
			openFileLocation();
			return true;
		} else if (item.getItemId() == R.id.action_clear) {
			LogUtils.clearLogFile();
			content.setText(readFileData());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void sendFileByEmail() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL, new String[]{Config.EXCEPTION_RECEIVER_EMAIL});
		i.putExtra(Intent.EXTRA_SUBJECT, "Application name, Лог файл, "
				+ DateUtil.formatDateTime(new Date()));
		i.putExtra(Intent.EXTRA_TEXT, "");
		File dirPath = FileUtil.getStorageDir(FileUtil.getAppDirectoryName());
		if (dirPath != null) {
			File file = new File(dirPath.getAbsolutePath() + "/" + Config.LOG_FILE_NAME);
			i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		}
		try {
			startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(LogsActivity.this, R.string.log_no_email_clients, Toast.LENGTH_SHORT).show();
		}
	}

	protected void openFileLocation() {
		File root = FileUtil.getStorageDir(FileUtil.getAppDirectoryName());
		Uri uri = Uri.fromFile(root);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "resource/folder");
		intent.setData(uri);
		startActivity(Intent.createChooser(intent, getString(R.string.log_choose_app)));
	}

	/** Вьюшка содержащая логи. */
	protected TextView content;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
