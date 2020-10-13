/*
 007
 */

package com.example.mobile_data_achrf.content;

import android.database.Cursor;

import com.example.mobile_data_achrf.logs.DebugLogger;



public class LargeContentManager {
	
	public static final int BLOCKSIZE = 2000;
	

	public static long[] buildIdBlocks(Cursor idsOnlyCursor) {
		
		
		idsOnlyCursor.moveToFirst();
		long[] idList = new long[ idsOnlyCursor.getCount() ];
		int i = 0;
		do {
			idList[i++] = idsOnlyCursor.getLong(0);
		}while(idsOnlyCursor.moveToNext());
		
		
		return idList;
	}

	public static long[][] parseIds(long[] idList, int chunkSize) {
		
		if ( idList == null || idList.length == 0 ) return null;
		
		final int divisor   = ( idList.length / chunkSize );
		final int remainder = ( idList.length % chunkSize );
		final boolean hasRemainder = ( remainder != 0);
		
		DebugLogger.d("AndroidForensics", "divisor = " + divisor);
		DebugLogger.d("AndroidForensics", "remainder = " + remainder);
		DebugLogger.d("AndroidForensics", "last is = " + idList[idList.length-1]);
		
		final int numChunks = ( divisor + ( hasRemainder ? 1 : 0 ) );
		long[][] idChunks = new long[numChunks][2];
		
		for ( int i = 1; i <= numChunks; i ++) {
			final int previousIndex = i-1;
			final int minIdIndex = ( i * chunkSize ) - chunkSize;
			final int maxIdIndex = ( i == numChunks ? remainder + ( previousIndex * chunkSize ) : ( i * chunkSize ) ) - 1;
			
			DebugLogger.d("AndroidForensics", "minIdIndex = " + minIdIndex );
			DebugLogger.d("AndroidForensics", "maxIdIndex = " + maxIdIndex );
			
			idChunks[previousIndex][0] = idList[ minIdIndex ];
			idChunks[previousIndex][1] = idList[ maxIdIndex ];
		}
		
		return idChunks;
	}
	
	
	
}
