package com.zetta.zScheduler.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zetta.dataSource.model.dataSource.zDB;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CommonUtils {

	public zLogger logger = new zLogger(getClass());

	static CommonConfig config = new CommonConfig();
	private Connection con = null;

	public CommonUtils() {
	}

	public void getconnection(zDB db) {
		try {
			logger.info("getconnection conName" + db.getConName());
			logger.info("getconnection getDriverName" + db.getDriverName());
			logger.info("getconnection getConnectionURL" + db.getConnectionURL());
			Class.forName(db.getDriverName());
			con = DriverManager.getConnection(db.getConnectionURL(), db.getUserId(), db.getPassWd());
		} catch (Exception e) {
			logger.info("getconnection error: " + e.getMessage());
			con = null;
		}
	}

	public List<String[]> getSimpleArrayWithQuery(String strQuery, int sampleSize) {
		logger.info("getSimpleArrayWithQuery  start");
		logger.info("strQuery :" + strQuery);
		List<String[]> results = new ArrayList<String[]>();
		Statement stmt = null;
		ResultSet rs = null;

		strQuery = strQuery.replace("&lt;", "<");
		strQuery = strQuery.replace("&gt;", ">");

		try {
			// Establish the connection.
			stmt = con.createStatement();
			rs = stmt.executeQuery(strQuery);

			ResultSetMetaData metaData = rs.getMetaData();
			String[] header = new String[metaData.getColumnCount()];
			while (rs.next()) {
				if (header[0] == null) {
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						header[i - 1] = metaData.getColumnName(i);
					}
					results.add(header);
				}

				String[] result = new String[metaData.getColumnCount()];
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					result[i - 1] = rs.getString(i);
				}
				results.add(result);

				if (sampleSize != -1) {
					if (results.size() > sampleSize) {
						break;
					}
				}
			}
			return results;
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}

	public String getJSonRecordWithQuery(String strQuery, int maxSize) {
		logger.info("getJSonRecordWithQuery  start");
		logger.info("strQuery :" + strQuery);
		List<HashMap> results = new ArrayList<HashMap>();
		Statement stmt = null;
		ResultSet rs = null;

		strQuery = strQuery.replace("&lt;", "<");
		strQuery = strQuery.replace("&gt;", ">");

		try {
			// Establish the connection.
			stmt = con.createStatement();
			rs = stmt.executeQuery(strQuery);

			ResultSetMetaData metaData = rs.getMetaData();
			while (rs.next()) {
				HashMap result = new HashMap<>();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					result.put(metaData.getColumnName(i), rs.getString(i));
				}
				results.add(result);

				if (maxSize != -1) {
					if (results.size() > maxSize) {
						break;
					}
				}
			}

			logger.info("getJSonRecordWithQuery  results.size:" + results.size());

			Gson gson = new GsonBuilder().create();
			return gson.toJson(results);

		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error(e.getMessage());
			return "[\"notFound\"]";
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}

	public String getSimpleJSonRecordWithQuery(String strQuery, int sampleSize) {
		logger.info("getSimpleJSonRecordWithQuery  start");
		logger.info("strQuery :" + strQuery);
		List<String[]> results = new ArrayList<String[]>();
		Statement stmt = null;
		ResultSet rs = null;

		strQuery = strQuery.replace("&lt;", "<");
		strQuery = strQuery.replace("&gt;", ">");

		try {
			// Establish the connection.
			stmt = con.createStatement();
			rs = stmt.executeQuery(strQuery);

			ResultSetMetaData metaData = rs.getMetaData();
			String[] header = new String[metaData.getColumnCount()];
			while (rs.next()) {
				if (header[0] == null) {
					for (int i = 1; i <= metaData.getColumnCount(); i++) {
						header[i - 1] = metaData.getColumnName(i);
					}
					results.add(header);
				}

				String[] result = new String[metaData.getColumnCount()];
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					result[i - 1] = rs.getString(i);
				}
				results.add(result);

				if (sampleSize != -1) {
					if (results.size() > sampleSize) {
						break;
					}
				}
			}
			Gson gson = new GsonBuilder().create();
			return gson.toJson(results);
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error(e.getMessage());
			return "[\"notFound\"]";
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}

	public String get64baseDecode(String sourcestr) {
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			return new String(decoder.decodeBuffer(sourcestr));
		} catch (Exception e) {
			return "";
		}
	}

	public static String get64baseEncode(String sourcestr) {
		try {
			BASE64Encoder encoder = new BASE64Encoder();
			return new String(encoder.encode(sourcestr.getBytes()));
		} catch (Exception e) {
			return "";
		}
	}

	public String getSessionUserID(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		String userId = (String) session.getAttribute("User_ID");

		if (userId == null)
			userId = "";

		return userId;
	}

	public void setSessionUserID(HttpServletRequest request, String user_id) {
		HttpSession session = request.getSession(true);

		session.setAttribute("User_ID", user_id);
	}

	public static String getValueFormatting(String value, String defaultstr, String formatstr) {
		try {
			java.text.NumberFormat formatter = new java.text.DecimalFormat(formatstr);
			return formatter.format(Double.valueOf(value));
		} catch (Exception e) {
			return defaultstr;
		}
	}

	/**
	 * http request의 param안에 값이 있는지 없는지 체크한다.
	 * 
	 * @param request
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(HttpServletRequest request, String param) {
		if (request.getParameter(param) == null || request.getParameter(param).equals("")) {
			return true;
		}
		return false;
	}

	public static String htmlNewLine(String input) {

		StringReader stringReader = new StringReader(input);
		BufferedReader reader = new BufferedReader(stringReader);
		StringBuffer ret = new StringBuffer(input.length() + 200);
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				ret.append(line).append("<br/>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret.toString();

	}

	public static void download(HttpServletRequest request, HttpServletResponse response, String path)
			throws Exception {

		String fileName = request.getParameter("file");
		String fileName2 = URLEncoder.encode(fileName, "UTF-8");
		File file = new File(path, fileName);
		if (!file.exists()) {
			throw new FileNotFoundException("실패.");
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Transfer-Encoding:", "base64");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName2 + ";");
			response.setContentLength((int) file.length());
			byte b[] = new byte[1024];
			int numRead = 0;
			out = response.getOutputStream();
			while ((numRead = in.read(b)) != -1) {
				out.write(b, 0, numRead);
			}

			out.flush();
			return;

		} finally {
			in.close();
			out.close();
		}
	}

	/*
	* 
	*/
	public static void deleteFiles(String[] fileNames, String path) {
		for (int i = 0; i < fileNames.length; i++) {
			// System.out.println(path + fileNames[i]);
			File file = new File(path, fileNames[i]);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	public static String null2void(String str, String defaultstr) {
		if (str == null)
			return defaultstr;
		return str;
	}

	// 필터를 분리해낸다. 반복 ; , 필드 분리 # , 값분리:
	public String makeStr(String arg, String defaultquery1) {
		String strresult = "", strquery = "";
		String[] aryFilters, aryFilter, arysql;
		int i, j;

		arysql = arg.split("[\u003B]"); // ;
		for (j = 0; j < arysql.length; j++) {
			strquery = defaultquery1;
			aryFilters = arysql[j].split("[\u0023]"); // #
			for (i = 0; i < aryFilters.length; i++) {
				aryFilter = aryFilters[i].split("[\u003A]"); // :
				if (aryFilter.length == 2)
					strquery = strquery.replace(aryFilter[0], aryFilter[1]);
				else if (aryFilter.length == 1)
					strquery = strquery.replace(aryFilter[0], "");
				else
					strquery = "";
			}
			strresult += strquery;
		}

		return strresult;
	}

	// 필터를 분리해낸다. 반복 ; , 필드 분리 # , 값분리:
	public Statement makeStatement(Statement statment, String arg, String defaultquery1) {
		String[] aryFilters, aryFilter, arysql;
		int i, j, k;

		arysql = arg.split("[\u003B]");
		for (j = 0; j < arysql.length; j++) {
			// 쿼리도 ; 를 기준으로 나눈다. statement를 만들어야 한다..
			String[] strquery = defaultquery1.split("[\u003B]");
			aryFilters = arysql[j].split("[\u0023]"); // #
			for (i = 0; i < aryFilters.length; i++) {
				aryFilter = aryFilters[i].split("[\u003A]"); // :

				if (aryFilter.length == 2) {
					for (k = 0; k < strquery.length; k++)
						strquery[k] = strquery[k].replace(aryFilter[0], aryFilter[1]);
				} else if (aryFilter.length == 1) {
					for (k = 0; k < strquery.length; k++)
						strquery[k] = strquery[k].replace(aryFilter[0], "");
				} else {
					for (k = 0; k < strquery.length; k++)
						strquery[k] = "";
				}

			}

			try { // 일괄처리시 마지막에 ; 붙어있을경우 그뒤에것을 하나의 객체로 분리 .. 주것등을 그객체 실행시키면서
					// 에러남..(원인이유 추후파악)
				for (k = 0; k < strquery.length; k++) {
					if (strquery[k].length() > 10)
						statment.addBatch(strquery[k].replace(";", " "));
				}
			} catch (Exception e) {
				System.out.println("Errors in MakeStatement: " + e.getMessage());
				return null;
			}
		}

		return statment;
	}

	public static String getSelectedDropDownList(String tValue, String dropList) {
		return dropList.replace("value=\"" + tValue + "\"", "value=\"" + tValue + "\" selected=true");
	}

	public boolean queryExec(String strQuery) {
		logger.info("queryExec  start");
		logger.info("strQuery :" + strQuery);
		Statement stmt = null;
		boolean result = false;

		strQuery = strQuery.replace("&lt;", "<");
		strQuery = strQuery.replace("&gt;", ">");

		try {
			// Establish the connection.
			stmt = con.createStatement();
			result = stmt.execute(strQuery);

			return true;

		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}

	public boolean bulkInsertQueryExec(String tableName, List<String[]> data, String dbName) {
		logger.info("bulkInsertQueryExec  start");
		Statement stmt = null;
		boolean result = false;

		String[] header = data.get(0);

		try {

			stmt = con.createStatement();
			con.setAutoCommit(false);

			// 첫행은 해더이다.
			for (int index = 1; index < data.size(); index++) {
				String[] row = data.get(index);

				String strQuery = "insert into " + dbName + "." + tableName + "(";
				for (int i = 0; i < header.length; i++) {
					strQuery += header[i] + ",";
				}
				strQuery = strQuery.substring(0, strQuery.length() - 1);
				strQuery += ")";
				strQuery += "  values(";

				for (int i = 0; i < row.length; i++) {
					strQuery += "'" + row[i] + "',";
				}

				strQuery = strQuery.substring(0, strQuery.length() - 1);
				strQuery += ");";

				strQuery = strQuery.replace("&lt;", "<");
				strQuery = strQuery.replace("&gt;", ">");

				logger.info(strQuery);
				stmt.addBatch(strQuery);
			}
			stmt.executeBatch();

			con.commit();
			return result;
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}

	public int getCountData(String strQuery, int i) {
		logger.info("getSingleData  start");
		logger.info("strQuery :" + strQuery);
		Statement stmt = null;
		ResultSet rs = null;

		strQuery = strQuery.replace("&lt;", "<");
		strQuery = strQuery.replace("&gt;", ">");

		try {
			// Establish the connection.
			stmt = con.createStatement();
			rs = stmt.executeQuery(strQuery);
			int returnValue = 0;
			if(rs.next()) {  
				returnValue = rs.getInt(1); 
			 } 
			logger.info("return value --> " + returnValue);

			return returnValue;
		}
		// Handle any errors that may have occurred.
		catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (Exception e) {
				}
		}
	}
}
