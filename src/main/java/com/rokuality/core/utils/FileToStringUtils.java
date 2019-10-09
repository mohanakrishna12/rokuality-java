package com.rokuality.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import com.rokuality.core.driver.By;
import com.rokuality.core.exceptions.NoSuchElementException;

public class FileToStringUtils {

	private static final String BY_IMAGE = "By.Image: ";

	public By prepareLocator(By by) {
		By preparedBy = null;

		// image file locator
		if (by != null && by.toString().startsWith(BY_IMAGE)) {
			File elementFile = null;
			try {
				elementFile = new File(by.toString().split(BY_IMAGE)[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new NoSuchElementException("Your locator is not valid! The image locator "
						+ "must be a valid path to an existing image file OR a valid URL to an imge file.");
			}

			if (elementFile.exists() && elementFile.isFile()) {
				String encodedFileLocator = convertFileToBase64String(elementFile);
				preparedBy = By.Image(encodedFileLocator);
				return preparedBy;
			}
		}

		// image url OR text
		return by;
	}

	// TODO - custom ArtifactNotCreatedException
	public File convertToFile(String source, String extension) {
		String property = "java.io.tmpdir";
		String tempDir = System.getProperty(property);
		if (tempDir == null) {
			throw new RuntimeException("Unable to find temp dir during artifact creation!");
		}

		File rokualityTempDir = new File(tempDir + File.separator + "com.rokuality");
		if (!rokualityTempDir.exists()) {
			boolean dirCreated = rokualityTempDir.mkdir();
			if (!dirCreated) {
				throw new RuntimeException("Failed to create rokuality temp dir during artifact creation!");
			}
		}

		byte[] fileData = Base64.getDecoder().decode(source);

		File artifactFile = new File(
				rokualityTempDir.getAbsolutePath() + File.separator + UUID.randomUUID().toString() + extension);

		FileOutputStream fileOutputStream = null;
		boolean fileCreated = false;
		try {
			fileCreated = artifactFile.createNewFile();
			if (fileCreated) {
				fileOutputStream = new FileOutputStream(artifactFile);
				fileOutputStream.write(fileData);
				fileCreated = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		if (fileCreated) {
			return artifactFile;
		}

		return null;
	}

	public String convertFileToBase64String(File file) {
		byte[] fileData = null;
		Path filePath = Paths.get(file.getAbsolutePath());
		try {
			fileData = Files.readAllBytes(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (fileData != null) {
			return Base64.getEncoder().encodeToString(fileData);
		}

		return null;
	}

}
