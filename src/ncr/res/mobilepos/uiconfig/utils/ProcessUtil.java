package ncr.res.mobilepos.uiconfig.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ncr.res.mobilepos.helper.StringUtility;

public class ProcessUtil {

	public static boolean processExectue(String pCommand, int pTryCount, long pTimeout) {

		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		int tryCount = 0;
		boolean retFlg = false;

		try {
			if (StringUtility.isNullOrEmpty(pCommand)) {
				return retFlg;
			}

			if (pTryCount < 0) {
				return retFlg;
			}

			if (pTimeout < 0) {
				return retFlg;
			}

			do {
				tryCount++;
				process = runtime.exec(pCommand);

				processInputReader(process.getInputStream(), "CMD : ");

				if (process.waitFor() == 0) {
					retFlg = true;
					process.destroy();

					return retFlg;
				} else {
					Thread.sleep(pTimeout);
				}

			} while (tryCount < pTryCount);

			if (tryCount >= pTryCount) {
				process.destroy();
			}
		} catch (Exception e) {
		} finally {
			if (process != null) {
				process.destroy();
			}
		}

		retFlg = false;
		return retFlg;
	}

	public static void processInputReader(InputStream pInputStream, String pMessage) {

		String line = null;
		InputStreamReader input = null;
		BufferedReader reader = null;

		try {
			input = new InputStreamReader(pInputStream, StaticParameter.code_MS932);
			reader = new BufferedReader(input);

			while ((line = reader.readLine()) != null) {
			}
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException io) {
			}
		}

	}
}
