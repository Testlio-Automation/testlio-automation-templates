package com.testlio.lib.utility.file;

import com.testlio.lib.pagefactory.MobileScreen;

import java.awt.geom.Point2D;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static com.testlio.lib.utility.helpers.ExecutionHelper.sleepInSeconds;
import static com.testlio.lib.utility.recognition.ImageRecognitionUtility.getCoords;
import static com.testlio.lib.utility.recognition.ImageRecognitionUtility.getFalsePoint;

public class FileUtil {

    private static final int MAX_SCROLL_COUNT = 10;

    private FileUtil() {}

	public static FileSearchResult findByImageByScrollingDown(MobileScreen mobileScreen, String directory,
                                                              String targetFileName) {
		return findByImage(mobileScreen, true, directory, targetFileName);
	}

	public static FileSearchResult findByImageByScrollingLeft(MobileScreen mobileScreen, String directory,
			String targetFileName) {
		return findByImage(mobileScreen, false, directory, targetFileName);
	}

	private static FileSearchResult findByImage(MobileScreen mobileScreen, boolean scrollDown, String directory,
			String targetFileName) {
		String targetImgPath = getFoundFilePath(directory, targetFileName);
		FileSearchResult result = new FileSearchResult(false, null);

		if (!targetImgPath.isEmpty()) {
			int scrollCount = 0;

			while (scrollCount < MAX_SCROLL_COUNT) {
				sleepInSeconds(2);
				Point2D point = getCoords(mobileScreen.getDriver(), targetImgPath);
				if (point.equals(getFalsePoint())) {
					scrollDownOrLeft(mobileScreen, scrollDown);
					scrollCount++;
				} else {
					result.setFileFound(true);
					result.setPoint(point);
					return result;
				}
			}
		}
		return result;
	}

	private static void scrollDownOrLeft(MobileScreen mobileScreen, boolean scrollDown) {
		if (scrollDown) {
			mobileScreen.scrollDownScrollView();
		} else {
			mobileScreen.scrollRightScrollView();
		}
	}

	public static String getFoundFilePath(String directory, String targetFileName) {
		targetFileName = targetFileName.toLowerCase();
		Optional<File> optionalFile = getFileIfExistsInDirectory(directory, targetFileName);

		return optionalFile.map(File::getAbsolutePath).orElse("");
	}

	private static Optional<File> getFileIfExistsInDirectory(String directory, String targetFileName) {
		List<File> fileListInDirectory = getFileListInDirectory(directory);

		return fileListInDirectory.stream().filter(i -> i.getName().contains(targetFileName)).findAny();
	}

	private static List<File> getFileListInDirectory(String directoryPath) {
		URL fileUrl = FileUtil.class.getResource(directoryPath);

		if (fileUrl == null) {
			return emptyList();
		}

		File file = new File(fileUrl.getFile());
		if (!file.exists()) {
			return emptyList();
		}
		return asList(requireNonNull(file.listFiles()));
	}
}
