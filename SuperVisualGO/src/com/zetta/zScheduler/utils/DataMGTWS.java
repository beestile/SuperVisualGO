package com.zetta.zScheduler.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.dom4j.Element;
import org.springframework.security.core.Authentication;

public class DataMGTWS implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6068431806005024005L;

	public zLogger logger = new zLogger(getClass());

	public DataMGTWS() {
	}

//	
//	/**
//	 * 메소드명: GetSQLHOUSE 설명: 전달받은 인덱스를 가진 sql을 반환한다. 반환되는 sql은 decoding된다.
//	 * return값: index
//	 */
//	public String getSqlHouse(String index) {
//		CommonUtils objcommon = new CommonUtils();
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//
//			// Create and execute an SQL statement that returns some data.
//			String SQL = "select sql from kebl_sqlhouse_java where num=" + index;
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(SQL);
//
//			rs.next();
//
//			return objcommon.get64baseDecode(rs.getString(1));
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return e.getMessage();
//		} finally {
//			if (rs != null)
//				try {
//					rs.close();
//				} catch (Exception e) {
//				}
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
//
//	/**
//	 * 메소드명: Get 설명: 전달받은 sql을 이용하여 row형태의 xml을 생성한다. return값: index
//	 */
//	public String getXmlRecordWithQuery(String StrQuery) {
//		CommonUtils objcommon = new CommonUtils();
//
//		String XMLOutputString = "<KEBL>";
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		StrQuery = StrQuery.replace("&lt;", "<");
//		StrQuery = StrQuery.replace("&gt;", ">");
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(StrQuery);
//
//			ResultSetMetaData metaData = rs.getMetaData();
//			while (rs.next()) {
//				XMLOutputString += "<row ";
//				for (int i = 1; i <= metaData.getColumnCount(); i++) {
//					XMLOutputString += metaData.getColumnName(i) + "=\"" + rs.getString(i) + "\" ";
//				}
//				XMLOutputString += " />";
//			}
//			// XMLOutputString = XMLOutputString.replace("&", "&");
//			return XMLOutputString + "</KEBL>";
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return e.getMessage();
//		} finally {
//			if (rs != null)
//				try {
//					rs.close();
//				} catch (Exception e) {
//				}
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
//
//	public boolean isExecuteDB(String StrQuery) {
//		CommonUtils objcommon = new CommonUtils();
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//			stmt = con.createStatement();
//
//			// ";" 을 기준으로 sql을 잘라내서 batch로 업데이트하자.
//			stmt.execute(StrQuery);
//			return true;
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return false;
//		} finally {
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
//
//	public boolean isExecuteDBWithBatch(String arg, String defaultquery1) {
//		CommonUtils objcommon = new CommonUtils();
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//			stmt = con.createStatement();
//
//			stmt = new CommonUtils().makeStatement(stmt, arg, defaultquery1);
//			stmt.executeBatch();
//			return true;
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return false;
//		} finally {
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
//
//	/**
//	 * 메소드명: getDeleteYn 설명: 전달받은 인덱스를 가진 sql을 실행하여 반환되는 값이 N 이면 삭제 불가, Y이면 삭제
//	 * 가능 return값: Y, N
//	 */
//	public String getDeleteYn(String index) {
//		CommonUtils objcommon = new CommonUtils();
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//
//			// Create and execute an SQL statement that returns some data.
//			String SQL = index;
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(SQL);
//
//			rs.next();
//
//			return rs.getString(1);
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return e.getMessage();
//		} finally {
//			if (rs != null)
//				try {
//					rs.close();
//				} catch (Exception e) {
//				}
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
//
//	public static void main(String args[]) {
//
//		System.out.println("11==>" + "\r\n111\r\n".replaceAll("\r", "").replaceAll("\n", ""));
//	}
//
//	public String getOneRecordWithQuery(String StrQuery) {
//		CommonUtils objcommon = new CommonUtils();
//
//		String outputString = "";
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		StrQuery = StrQuery.replace("&lt;", "<");
//		StrQuery = StrQuery.replace("&gt;", ">");
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(StrQuery);
//
//			if(rs == null) return "";
//			
//			while (rs.next()) {
//				return rs.getString(1);
//			}
//			
//			return rs.getString(0);
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return "";
//		} finally {
//			if (rs != null)
//				try {
//					rs.close();
//				} catch (Exception e) {
//				}
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
//
//	public String getSqlHouseXML(String queryName) {
////		API oApi = new API();
////		Element securityViews = (Element) oApi.getElementWithXmlFile(CommonUtils.getConfig().getRealPath() + "//Query//sqlHouse.xml", queryName);
////		return securityViews.getText();
//		return null;
//	}
//
//	public String getJSonRecordWithQuery(String StrQuery) {
//		CommonUtils objcommon = new CommonUtils();
//
//		String XMLOutputString = "{ \"rows\" : [";
//
//		// Declare the JDBC objects.
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//
//		StrQuery = StrQuery.replace("&lt;", "<");
//		StrQuery = StrQuery.replace("&gt;", ">");
//
//		try {
//			// Establish the connection.
//			con = objcommon.getconnection();
//
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(StrQuery);
//
//			ResultSetMetaData metaData = rs.getMetaData();
//			while (rs.next()) {
//				XMLOutputString += "{ \"cell\" : [";
//				for (int i = 1; i <= metaData.getColumnCount(); i++) {
//					XMLOutputString += "\"" + rs.getString(i) + "\",";
//				}
//				//마지막 ,(콤마) 제거
//				XMLOutputString = XMLOutputString.substring(0, XMLOutputString.length()-1);
//				XMLOutputString += " ]},";
//			}
//			//마지막 ,(콤마) 제거
//			XMLOutputString = XMLOutputString.substring(0, XMLOutputString.length()-1);
//			return XMLOutputString + "]}";
//		}
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			logger.error("error", e);
//			return e.getMessage();
//		} finally {
//			if (rs != null)
//				try {
//					rs.close();
//				} catch (Exception e) {
//				}
//			if (stmt != null)
//				try {
//					stmt.close();
//				} catch (Exception e) {
//				}
//			if (con != null)
//				try {
//					con.close();
//				} catch (Exception e) {
//				}
//		}
//	}
}