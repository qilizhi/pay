package com.util;

import java.util.LinkedList;

import javax.swing.plaf.SliderUI;

import org.apache.log4j.Logger;

import com.ekapay.util.DataHelper;

/**
 * 失败请求队列
 */
@SuppressWarnings("unused")
public class QueueManager {
	private static Logger logger = Logger.getLogger(QueueManager.class);

//	private long SLEEP_TIME = 3000;// 5分钟
	private long SLEEP_TIME = 300000;// 5分钟

	public LinkedList<String> urlQueue = new LinkedList<String>();
	protected QueueThread queueThread = null;
	protected static QueueManager self = null;

	public static QueueManager getInstance() {
		if (self == null) {
			synchronized (QueueManager.class) {
				if (self == null) {
					self = new QueueManager();
					self.setQueueThread();
				}
			}
		}
		return self;
	}

	public synchronized void setQueueThread() {
		queueThread = new QueueThread();
		queueThread.start();
	}

	public void addUrl(String url) {
		addUrl(url, false);
	}
	
	public void addUrl(String url, boolean first) {
		synchronized (urlQueue) {
			if (first) {
				urlQueue.addFirst(url);
			} else {
				urlQueue.add(url);
			}
			urlQueue.notify();
		}
	}

	public String deUrl() throws InterruptedException {
		synchronized (urlQueue) {
			while (urlQueue.size() <= 0) {
				urlQueue.wait();
			}
			return (String) urlQueue.remove(0);
		}
	}

	/**
	 * TODO 队列线程
	 */
	public class QueueThread implements Runnable {
		private Thread t;
		boolean bRunning = true;

		public QueueThread() {

		}

		public void start() {
			bRunning = true;
			t = new Thread(this, "Thread-Queue");
			t.start();
		}

		public void stop() {
			bRunning = false;
			t.interrupt();
		}

		public void run() {
			while (bRunning) {

				try {
					String url = deUrl();
					
					if (StringUtils.notEmpty(url)) {
						logger.info("QueueThread重发url,url:{" + url + "}");
						
						String[] split = url.split("\\?");
						if (split.length == 2 && StringUtils.notEmpty(split[0]) && StringUtils.notEmpty(split[1])) {
							RewireThread rewireThread = new RewireThread(split[0], split[1]);
							rewireThread.start();
						}
					}
				} catch (InterruptedException e) {
					logger.error("QueueThread重发url失败", e);
				}
			}
		}
	}

	/**
	 * TODO 重发线程
	 */
	private class RewireThread implements Runnable {
		private Thread t;
		private String url;
		private String postData;

		public RewireThread(String url, String postData) {
			this.url = url;
			this.postData = postData;
		}

		public void start() {
			t = new Thread(this,"Thread-Rewire");
			t.start();
		}

		public void stop() {
			t.interrupt();
		}

		@SuppressWarnings("static-access")
		public void run() {
			try {
				logger.info("RewireThread线程编号【"+Thread.currentThread().getId()+"】休眠 " + SLEEP_TIME + " 毫秒");
				t.sleep(SLEEP_TIME);
				String result = DataHelper.rewirePostUrl(url, postData);
				if (result == null) {// 还是连不上则继续加入请求队列
					addUrl(url + "?" + postData);
				}
			} catch (InterruptedException e) {
				logger.error("RewireThread执行重发失败", e);
			}

		}
	}
	
	public static void main(String[] args) {
		QueueManager.getInstance().addUrl("http://localhost:8080/api.action?url=1");
		QueueManager.getInstance().addUrl("http://localhost:8081/api.action?url=2");
	}
}
