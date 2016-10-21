package com.emporia.common.util.crash.reporter.mailreporter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.emporia.common.util.crash.AbstractCrashReportHandler;
import com.emporia.common.util.crash.reporter.xml.DomEmailParser;
import com.emporia.common.util.crash.reporter.xml.EmailInfo;
import com.emporia.common.util.crash.reporter.xml.EmailParser;

import java.io.File;

/**
 * send crash file by email
 * @see Mail
 * @author sky
 */
public class CrashEmailReporter extends AbstractCrashReportHandler {
	private static final String TAG = CrashEmailReporter.class.getSimpleName();
	private EmailParser mEmailParser;
	public CrashEmailReporter(Context context) {
		super(context);
	}

	@Override
	protected boolean sendReport(String subject, String body, File file) {
		Log.i(TAG, "start send email" + ", subject:" + subject + ", body:" + body + ", file:" + file);
		boolean result = false;
		//outlook smtp.live.com, port 587
		//qq smtp.qq.com, 25
		//163 smtp.163.com, 25
		//gmail smtp.gmail.com, 465/587
		try {
			mEmailParser = new DomEmailParser();
			EmailInfo emailInfo = mEmailParser.parser(getContext().getAssets().open("emails.xml"), "utf-8");
			Log.d(TAG, "emailInfo:" + emailInfo.toString());
			int index = (int) (Math.floor(Math.random() * emailInfo.getFromEmails().size()));
			EmailInfo.AccountInfo accountInfo = emailInfo.getFromEmails().get(index);
			Mail m = new Mail(accountInfo.getUser(), accountInfo.getPass());
			String[] toArr = emailInfo.getToEmails().toArray(new String[emailInfo.getToEmails().size()]);
			m.setAuth(true);
			m.setHost(emailInfo.getHost());
			m.setPort(emailInfo.getPort());
			m.setTo(toArr);
			m.setFrom(accountInfo.getUser());
			m.setSubject(subject);
			m.setBody(body);
			if (file != null) {
				m.addAttachment(file.getPath(), file.getName());
			}
			if(m.send()) {
				deleteCache(file);
				result = true;
				Log.i(TAG, "Email sent successful.");
			} else {
				result = false;
				Log.e(TAG, "Email was not sent.");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
//		sendEmailByGmail(getContext(), subject, body, file);
		return result;

	}
	/** delete zip file and cache file */
	private void deleteCache(File file) {
		if (file != null) {
			file.delete();
			clear();
		}
	}

	private void sendEmailByGmail(Context context, String subject, String body, File file) {
		Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
		sendEmailIntent.setType("*/*");
		sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "sky.chen@emporiatelecom.com" });
		sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendEmailIntent.putExtra(Intent.EXTRA_TEXT, body);
		Uri fileUri = Uri.parse(file.getPath());
		sendEmailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
		sendEmailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(sendEmailIntent);
	}

}
