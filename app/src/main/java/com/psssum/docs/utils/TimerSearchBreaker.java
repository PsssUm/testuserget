package com.psssum.docs.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.Timer;
import java.util.TimerTask;

public class TimerSearchBreaker {
	
	public interface ISearchTask {
		public void searchUpdate(String query);
	}
	
	private Context act;
	private ISearchTask searchTask;
	private Timer timer;
	
	public TimerSearchBreaker(Context act, ISearchTask searchTask){
		this.act = act;
		this.searchTask = searchTask;
	}
	
	public void run(String query){
		if(timer != null){
			timer.cancel();
		}
		timer = new Timer();
		TimerTask updateBall = new UpdateSearchTask(query);
		timer.schedule(updateBall, 300);
	}

	
	class UpdateSearchTask extends TimerTask {
		String newText;
		public UpdateSearchTask(String newText) {
			this.newText = newText;
		}

		public void run() {
            newText = (newText.equals("")) ? null : newText;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    searchTask.searchUpdate(newText);
                }
            });
		}
	}
}
