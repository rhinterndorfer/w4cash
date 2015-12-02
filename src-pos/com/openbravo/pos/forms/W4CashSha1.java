package com.openbravo.pos.forms;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class W4CashSha1 {

	/**
	 * Calculate sha1 hash of whole input stream fis and returns the hash as
	 * string.
	 * 
	 * @param fis
	 *            Input stream to calculate sha for.
	 * @return Returns the sha hash of input stream as string.
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public String calcSha(InputStream fis, String extension) throws NoSuchAlgorithmException, IOException {
		StringBuffer sha = new StringBuffer("");
		if (fis != null) {
			MessageDigest md;

			md = MessageDigest.getInstance("SHA1");
			byte[] dataBytes = new byte[1024];

			int nread = 0;

			while ((nread = fis.read(dataBytes)) != -1) {
				md.update(dataBytes, 0, nread);
			}
			md.update(extension.getBytes());

			byte[] mdbytes = md.digest();

			// convert the byte to hex format
			for (int i = 0; i < mdbytes.length; i++) {
				sha.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			}
		}

		return sha.toString();
	}

	/**
	 * Reads the content of first input stream, calculates the sha hash and
	 * validates it with string in shafile stream.
	 * 
	 * @param contentfile
	 *            Input stream to validate content for.
	 * @param shafile
	 *            Input stream with string representation of first sha file.
	 * @return True if validation was successfully, otherwise false.
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public boolean validateSha(InputStream contentfile, InputStream shafile, String extension)
			throws NoSuchAlgorithmException, IOException {

		// now read sha file
		String sb = this.calcSha(contentfile, extension);
		int nread = 0;

		StringBuffer shab = new StringBuffer("");
		while ((nread = shafile.read()) != -1) {
			shab.append((char) nread);
		}

		if (sb.compareTo(shab.toString().toLowerCase()) == 0)
			return true;
		return false;
	}

	/**
	 * validates contentfile with sha file by sha1 algorithm. If sha1 of
	 * contentfile and content of sha1 file confirm, then return true, otherwise
	 * false
	 * 
	 * @param contentfile
	 * @param shafile
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public boolean validateSha(String contentfile, String shafile) throws NoSuchAlgorithmException, IOException {
		boolean valid = false;
		File file = new File(contentfile);
		File sha = new File(shafile);
		if (!file.exists()) {
			return false;
		}
		if (!sha.exists()) {
			return false;
		}

		FileInputStream ifile = null;
		FileInputStream ishafile = null;

		ifile = new FileInputStream(file);
		ishafile = new FileInputStream(sha);
		valid = this.validateSha(ifile, ishafile, "" + readCreationDate(file));

		if (ifile != null) {
			try {
				ifile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (ishafile != null) {
			try {
				ishafile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return valid;
	}

	public static long readCreationDate(File file) throws IOException {
		Path p = Paths.get(file.toURI());
		BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();
		return view.lastModifiedTime().toMillis();
	}
}
