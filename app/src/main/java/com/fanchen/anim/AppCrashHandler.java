package com.fanchen.anim;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 自定义异常处理器
 * 
 * @author fanchen
 * 
 */
public class AppCrashHandler implements UncaughtExceptionHandler {

	private AnimAppliction mContext;

	private UncaughtExceptionHandler mDefaultHandler;
	/** 防止多线程中的异常导致读写不同步问题的lock **/
	private Lock lock = null;
	/** 本地保存文件日志 **/
	private final String CRASH_REPORTER_EXTENSION = ".crash";
	/** 日志tag **/
	private final String STACK_TRACE = "logStackTrance";
	/** 保存文件名 **/
	private final String crash_pref_path = "app_crash_pref.xml";

	private AppCrashHandler() {
		lock = new ReentrantLock(true);
	}

	/**
	 * 获得单例对象
	 * 
	 * @param context
	 * @return AppCrashHandler
	 */
	public static AppCrashHandler shareInstance(AnimAppliction context) {
		AppCrashHandler crashhandler = InstanceHolder.crashHandler;
		crashhandler.initCrashHandler(context);
		return crashhandler;
	}

	/**
	 * 使用初始化方法初始化，防止提前初始化或者重复初始化
	 * 
	 * @param cxt
	 */
	private void initCrashHandler(AnimAppliction cxt) {
		if (!hasInitilized()) {
			mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(this);
			mContext = cxt;
		}
	}

	public interface InstanceHolder {
		public static AppCrashHandler crashHandler = new AppCrashHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleExceptionMessage(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(STACK_TRACE, "Error : ", e);
			}
			mContext.finishActivity();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleExceptionMessage(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				// Toast 显示需要出现在一个线程的消息队列中
				Looper.prepare();
				Toast.makeText(mContext, "程序出错啦，即将退出", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		String fileName = System.currentTimeMillis() + CRASH_REPORTER_EXTENSION;
		String crashFileName = saveExceptionToFile(ex, fileName);
		SharedPreferences.Editor editor = mContext.getSharedPreferences(crash_pref_path, Context.MODE_PRIVATE).edit();
		editor.putString(STACK_TRACE, crashFileName);
		editor.commit();
		return true;
	}

	/**
	 * 是否已初始化
	 * 
	 * @return
	 */
	public boolean hasInitilized() {
		return mContext != null;
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 * @throws IOException
	 */
	private String saveExceptionToFile(Throwable ex, String fileName) {
		File saveFile = null;
		PrintWriter printWriter = null;
		try {
			lock.tryLock();
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File sdCardDir = new File( Environment.getExternalStorageDirectory() +"/android/data/" +  mContext.getApplicationInfo().packageName);// 获取SDCard目录
				if(!sdCardDir.exists())
					sdCardDir.mkdirs();
				saveFile = new File(sdCardDir, fileName);
			} else {
				saveFile = new File(mContext.getFilesDir(), fileName);
			}
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}
			printWriter = new PrintWriter(saveFile);
			String result = formatException(ex);
			printWriter.write(result);
			printWriter.flush();
			Log.e("CrashException", result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
			lock.unlock();
		}

		return saveFile != null ? saveFile.getAbsolutePath() : null;
	}

	/**
	 * 格式化异常信息
	 * 
	 * @param e
	 * @return
	 */
	private String formatException(Throwable e) {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] stackTrace = e.getStackTrace();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (stackTrace != null) {
			String timeStramp = sdf
					.format(new Date(System.currentTimeMillis()));
			String format = String.format("DateTime:%s\nExceptionName:%s\n\n",
					timeStramp, e.getLocalizedMessage());
			sb.append(format);
			for (int i = 0; i < stackTrace.length; i++) {
				StackTraceElement traceElement = stackTrace[i];
				String fileName = traceElement.getFileName();
				int lineNumber = traceElement.getLineNumber();
				String methodName = traceElement.getMethodName();
				String className = traceElement.getClassName();
				sb.append(String.format("%s\t%s[%d].%s \n", className,
						fileName, lineNumber, methodName));
			}
			sb.append(String.format("\n%s", e.getMessage()));
			Writer stringWriter = new StringWriter();
			PrintWriter pw = new PrintWriter(stringWriter);
			e.printStackTrace(pw);
			pw.flush();
			pw.close();

			sb.append("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
			sb.append(stringWriter.toString());
		}
		return sb.toString();
	}

}
