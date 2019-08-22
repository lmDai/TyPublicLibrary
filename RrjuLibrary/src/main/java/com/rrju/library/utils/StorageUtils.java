package com.rrju.library.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 存储工具
 * 
 */
public class StorageUtils {
	/**
	 * 当前存储路径
	 */
	private static String internalSDStorage = null;
	/**
	 * 存放实的路径的集合
	 */
	private static ArrayList<String> mMounts;
	/**
	 * 判断路径是否已被切换
	 */
	private static boolean isSwitching;

	static {
		getAllStorageLocations();
	}

	/**
	 * 当前是否使用内置存储
	 * 
	 * @return
	 */
	public static boolean useInternalStorage() {
		return getStorageDirectory().equals(internalSDStorage);
	}

	/**
	 * @return True if the external storage is available. False otherwise.
	 */
	public static boolean isAvailable(boolean requireWriteAccess) {
		return isAvailable(getStorageDirectory(), requireWriteAccess);
	}

	private static boolean isAvailable(String strSdcardPath,
                                       boolean requireWriteAccess) {
		if (strSdcardPath.equals(internalSDStorage)) {
			if (requireWriteAccess) {
				boolean writable = checkFsWritable(strSdcardPath);
				return writable;
			} else {
				File root = new File(strSdcardPath);
				return root.exists() && root.isDirectory() && root.canWrite();
			}
		} else {
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				if (requireWriteAccess) {
					boolean writable = checkFsWritable(strSdcardPath);
					return writable;
				} else {
					return true;
				}
			} else if (!requireWriteAccess
					&& Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
				return true;
			}
			return false;
		}
	}

	private static boolean checkFsWritable(String storageDirectory) {
		// Create a temporary file to see whether a volume is really writeable.
		// It's important not to put it in the root directory which may have a
		// limit on the number of files.
		String directoryName = storageDirectory.toString() + "/xueyi";
		File directory = new File(directoryName);
		if (!directory.isDirectory()) {
			if (!directory.mkdirs()) {
				return false;
			}
		}
		File f = new File(directoryName, ".probe");
		try {
			// Remove stale file if any
			if (f.exists()) {
				f.delete();
			}
			if (!f.createNewFile()) {
				return false;
			}
			f.delete();
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	/**
	 * 获取存储目录
	 * 
	 * @return
	 */
	public static String getStorageDirectory() {
		// if (TextUtils.isEmpty(internalSDStorage)) {
		return Environment.getExternalStorageDirectory().getPath() + "/";
		// } else {
		// return internalSDStorage;
		// }
	}

	/**
	 * 获取存储路径是否已被切换
	 * 
	 * @return
	 */
	public static boolean isSwitching() {
		return isSwitching;
	}

	/**
	 * 设置存储路径是否已被切换
	 * 
	 * @param isSwitching
	 */
	public static void setSwitching(boolean isSwitching) {
		StorageUtils.isSwitching = isSwitching;
	}

	/**
	 * 获取实在的存储路径集合
	 * 
	 * @return
	 */
	public static ArrayList<String> getmMounts() {
		return mMounts;
	}

	/**
	 * @return A map of all storage locations available
	 */
	@SuppressWarnings("resource")
	public static void getAllStorageLocations() {
		mMounts = new ArrayList<String>(10);
		List<String> mVold = new ArrayList<String>(10);
		mMounts.add("/mnt/sdcard");
		mVold.add("/mnt/sdcard");
		try {
			File mountFile = new File("/proc/mounts");
			if (mountFile.exists()) {
				Scanner scanner = new Scanner(mountFile);
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					if (line.startsWith("/dev/block/vold/")
							|| line.startsWith("/dev/fuse")) {
						String[] lineElements = line.split(" ");
						String element = lineElements[1];

						// don't add the default mount path
						// it's already in the list.
						if (!element.equals("/mnt/sdcard"))
							mMounts.add(element);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			File voldFile = new File("/system/etc/vold.fstab");
			if (voldFile.exists()) {
				Scanner scanner = new Scanner(voldFile);
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					if (line.startsWith("dev_mount")) {
						String[] lineElements = line.split(" ");
						String element = lineElements[2];

						if (element.contains(":"))
							element = element
									.substring(0, element.indexOf(":"));
						if (!element.equals("/mnt/sdcard"))
							mVold.add(element);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < mMounts.size(); i++) {
			String mount = mMounts.get(i);
			if (!mVold.contains(mount))
				mMounts.remove(i--);
		}
		mVold.clear();
		mVold = null;
		if (mMounts.size() > 1) {
			mMounts.remove(0);
			for (int i = 0; i < mMounts.size(); i++) {
				if (mMounts.size() > 1
						&& !CheckSDSize.getSDIsThanCurrentSize(mMounts.get(i))) {
					continue;
				}
				File root = new File(mMounts.get(i));
				if (root.exists() && root.isDirectory() && root.canWrite()) {
					setSwitching(!mMounts.get(i)
							.equals(Environment.getExternalStorageDirectory()
									.getPath()));// 设置是否已被切换
					internalSDStorage = mMounts.get(i).endsWith("/") ? mMounts
							.get(i) : mMounts.get(i) + "/";
					break;
				}
			}
		}
	}

	/**
	 * 获取外置存储root
	 * 
	 * @param type
	 * @return
	 */
	public static File getStoragePublicDirectory(String type) {
		return new File(getStorageDirectory(), type);
	}
}
