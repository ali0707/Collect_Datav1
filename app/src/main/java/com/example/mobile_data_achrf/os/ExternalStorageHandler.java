/*
 hello 007
 */
package com.example.mobile_data_achrf.os;

import java.io.File;

import com.example.mobile_data_achrf.ForensicsException;
import com.example.mobile_data_achrf.R;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ExternalStorageHandler {
	
	public static boolean isMounted() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	public File prepareStorageLocation( Context context, String subFolder ) throws ForensicsException {
		validateSDCardIsReady();
		return getForensicsFile(context, getFileForExternalStorage(context), subFolder );
		
	}

	private File getForensicsFile(Context context, File esd, String subFolder) throws ForensicsException {
		String subDir = context.getString(R.string.forensics_subdir) + "/" + subFolder;
		File forensicsDir = new File(esd, subDir);
		if (forensicsDir == null || forensicsDir.isFile() || (! forensicsDir.exists() && ! forensicsDir.mkdirs())) {
			// Report problem accessing sub-directory
			throw new ForensicsException(context.getString(R.string.cant_access_subdirectory));			
		}
		return forensicsDir;
	}

	public File getFileForForensicsRoot(Context ctx) throws ForensicsException {
		return new File( getFileForExternalStorage(ctx), ctx.getString(R.string.forensics_subdir) );
	}
	
	public File getFileForExternalStorage(Context context) throws ForensicsException {
		File esd = Environment.getExternalStorageDirectory();
		if (esd == null || ! esd.exists() || ! esd.isDirectory()) {
			throw new ForensicsException(context.getString(R.string.no_external_drive));
		}
		return esd;
	}

	private void validateSDCardIsReady() throws ForensicsException {
		String storageState = Environment.getExternalStorageState();
		Log.d(this.getClass().getName(), "Storage state: " + storageState );
		if ( ! Environment.MEDIA_MOUNTED.equals(storageState) ) {
			throw new ForensicsException("Storage State: " + storageState);
		}
	}
	
    public boolean checkFsWritable(Context context ) {
        // Create a temporary file to see whether a volume is really writeable.
        // It's important not to put it in the root directory which may have a
        // limit on the number of files.
    	
        try {
        	File directory = getFileForForensicsRoot(context);
            if (!directory.isDirectory()) {
	            try {
					boolean mkdirs = directory.mkdirs();
					if (!mkdirs) {
						return false;
					}
				} catch (Throwable t) {
					Log.e("ExternalStorageHandler", "checking for r/w access", t);
		        	throw t;
				}
	        }
            File f = new File(directory, ".dat");
            // Remove stale file if any
            if (f.exists()) {
                f.delete();
            }
            if (!f.createNewFile()) {
                return false;
            }
            f.delete();
            return true;
        } catch (Throwable t) {
            Log.e("ExternalStorageHandler", "checking for r/w access", t);
        	return false;
        }
    }
    
	
}
