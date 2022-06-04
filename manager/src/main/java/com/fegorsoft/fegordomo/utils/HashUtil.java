package com.fegorsoft.fegordomo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public final class HashUtil {

	private HashUtil() {
		throw new IllegalStateException("Hashes and Conversion Util class");
	}

	/**
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] convertToBytes(Object object) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(object);
			return bos.toByteArray();
		}
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
		try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInput in = new ObjectInputStream(bis)) {
			return in.readObject();
		}
	}

	/**
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();

		for (byte b : bytes) {
			result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
		}

		return result.toString();
	}

	/**
	 * @param s
	 * @return
	 */
	public static byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}

		return data;
	}
}
