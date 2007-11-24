/*
 * $Id$
 */

package org.devtcg.rssreader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentReceiver;
import android.os.BinderNative;
import android.os.Bundle;
import android.os.Parcel;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

public class RSSReaderService extends Service implements Runnable
{
	protected boolean mRunning;
	private static final String TAG = "RSSReaderService";
	
	@Override
	protected void onCreate()
	{
		Log.d(TAG, "onCreate");
	}
	
	@Override
	protected void onStart(int startId, Bundle args)
	{
		Log.d(TAG, "onStart(" + startId + ")");
		
		Thread t = new Thread(null, this, "RSSReaderService_Service");
		t.start();
		
		mRunning = true;
	}
	
	@Override
	protected void onDestroy()
	{
		/* TODO: Do something? */
		Log.d(TAG, "onDestroy");
	}
	
	public void run()
	{
		Log.d(TAG, "Doing some work, look at me!");

        // Normally we would do some work here...  for our sample, we will
        // just sleep for 4 seconds.
        long endTime = System.currentTimeMillis() + 4*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (mBinder) {
                try {
                    mBinder.wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }
        
        Log.d(TAG, "Finished, sigh...");
        
		/* Done synchronizing, stop our service.  We will be called up again
		 * at our next scheduled interval... */
		this.stopSelf();
		
		mRunning = false;
	}
	
	@Override
	public IBinder getBinder()
	{
		return mBinder;
	}
	
	private final IRSSReaderService.Stub mBinder = new IRSSReaderService.Stub()
	{
		public int getPid()
		{
			return Process.myPid();
		}
	};
}
