package com.zetta.portal.ctl.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.zetta.common.utils.zLogger;

@Controller
public class DownloadView extends AbstractView {

	public zLogger logger = new zLogger(getClass());

	public void Download() {
		setContentType("application/download; utf-8");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		File file = (File) model.get("downloadFile");
		
		if(file == null) {
			response.getWriter().println("Sorry! Create Excel File Failed.");
			return;
		}
		logger.info("DownloadView --> file.getPath() : " + file.getPath());
		logger.info("DownloadView --> file.getName() : " + file.getName());

		// String fileName = (String) model.get("fileNm");

		response.setContentType(getContentType());
		response.setContentLength((int) file.length());

		String userAgent = request.getHeader("User-Agent");

		boolean ie = userAgent.indexOf("MSIE") > -1;

		String fileName = null;
		fileName = (String) model.get("fileNm");
		String header = getBrowser(request);

		if (header.contains("MSIE")) {

			String docName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

			response.setHeader("Content-Disposition", "attachment;filename=" + docName + ";");

		} else if (header.contains("Firefox")) {

			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

			response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");

		} else if (header.contains("Opera")) {

			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

			response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");

		} else if (header.contains("Chrome")) {

			String docName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

			response.setHeader("Content-Disposition", "attachment; filename=\"" + docName + "\"");

		}

		response.setHeader("Content-Type", "application/octet-stream");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		OutputStream out = response.getOutputStream();

		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch (IOException e) {
			System.out.print("IOException");
		} catch (Exception e) {
			System.out.print("Exceptoin");
		} finally {
			fis.close();
			out.flush();
		} // try end;

	}// render() end;

	private String getBrowser(HttpServletRequest request) {

		String header = request.getHeader("User-Agent");

		if (header.contains("MSIE")) {

			return "MSIE";

		} else if (header.contains("Chrome")) {

			return "Chrome";

		} else if (header.contains("Opera")) {

			return "Opera";

		}

		return "Firefox";

	}

}