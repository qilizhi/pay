package com.heepay;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 
 * <p>
 * <b>BaseAction</b> 是基Action类，定义一些基本操作流程, 由于时间原因暂时不采取加密方式，后期加上。
 * </p>
 */
public abstract class BaseAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(BaseAction.class);

	/**
	 * 读取客户端传过来的数据
	 * 
	 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
	 * @date 2016年4月26日 下午7:41:28
	 * @return JSON Object
	 */
	public JSONObject getRequestData() {
		HttpServletRequest request = ServletActionContext.getRequest();

		int len = request.getContentLength();
		byte[] reqData = new byte[len];

		// 读取post过来的data
		InputStream is = null;

		try {
			is = request.getInputStream();
			is.read(reqData);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (null != is) {
					is.close();
					is = null;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				logger.error(e2);
			}
		}

		JSONObject json = null;
		try {
			String requestStr = new String(reqData, "UTF-8");
			logger.info("request:{" + requestStr + "}");
			json = JSONObject.fromObject(requestStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json;
	}

	
	
	protected void writeSuccess() {
		writeBack("success");
	}

	protected void writeSuccess(String msg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("msg", msg);
		writeBack(JSONObject.fromObject(map).toString());
	}

	protected void writeError(String msg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		map.put("msg", msg);
		writeBack(JSONObject.fromObject(map).toString());
	}
	
	/**
	 * 将处理结果送回客户端
	 * 
	 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
	 * @date 2016年4月26日 下午7:41:59
	 * @param json
	 */
	public void writeBack(String str) {
		logger.info(str); // 打印返回信息
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/plain;charset=utf-8");

		OutputStream out = null;

		try {
			byte[] bytes = str.getBytes("UTF-8");

			out = response.getOutputStream();
			out.write(bytes);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			try {
				if (null != out) {
					out.close();
					out = null;
				}
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
	}
}