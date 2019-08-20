package com.zetta.zScheduler.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zetta.dataSource.model.dataSource.DataSet;
import com.zetta.dataSource.model.dataSource.zDB;
import com.zetta.dataSource.model.spreadJS.CellInfo;
import com.zetta.dataSource.model.spreadJS.MapInfo;
import com.zetta.dataSource.model.spreadJS.SpreadColumnInfo;
import com.zetta.dataSource.model.spreadJS.SpreadData;
import com.zetta.zScheduler.model.SystemInfo;	

public class DBHandler {

	public zLogger logger = new zLogger(getClass());
	public static String schema = null;
	public static zDB db = null;
	public List<String[]> tablesInfo = null;
	public CommonConfig config = new CommonConfig();
	public DataUtils dataUtils = new DataUtils(config.getProperties("ZWORKINGROOT"), config.getProperties("QVXROOT"));
	public List<String[]> dbTables = null;
	public int dataSize  = -1;
	
	enum TABLEINDEX{TABLE_ID,TABLE_NM,COLUMN_ID,COLUMN_NM,TYPE,LENGTH,NULL_YN,KEY_YN,DEFAULT_VALUE,COMMENT, USE_YN};
	enum DBTABLEINDEX{TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,DATETIME_PRECISION,IS_NULLABLE};	
	enum TYPEINFOINDEX{KIND,TABLE_NAME,COLUMN_NAME,MAPPING_NAME,SAVE_INFO,CONNECT_INFO,FIGURE,KEY};	
	
	public DBHandler(String schema, List<String[]> tablesInfo) throws IOException {
		this.schema = schema;

		try {
			this.db = dataUtils.getDB(this.schema);
		} catch (IOException e) {
			//logger.info("생성자 에러:" + e.getMessage());
			this.db = null;
			return;
		}
		
		if(tablesInfo == null){
			//시스템에 설정되어있는 테이블정보를 로드함.
			this.tablesInfo = dataUtils.getJsonTable("process\\_DBTables", "data_" + schema, "json").get(0).getData();
		}else{
			this.tablesInfo  = tablesInfo;
		}
		
		//DB테이블정보가져오기
		String resultJson = dataUtils.getDbSimpleData(db.getTableListQuery(), db, dataSize);
		dbTables = dataUtils.getJsonArray(resultJson);
	}
	
	//동기화한다.
	public boolean dbSync(){
		if(db == null) return false;
		
		//테이블 하나씩 작업을 진행하자.
		List<String[]> tableInfo = new ArrayList<String[]>();
		String prevTableName = "";
		for( int i=1; i<tablesInfo.size();i++){
			String[] row = tablesInfo.get(i);
			if(i==1) prevTableName = row[TABLEINDEX.TABLE_ID.ordinal()];

			if(prevTableName.equals(row[TABLEINDEX.TABLE_ID.ordinal()])){
				tableInfo.add(row);				
				if(i + 1 == tablesInfo.size()) dbSyncProcessing(tableInfo);
			}
			else{				
				dbSyncProcessing(tableInfo);
				
				tableInfo.clear();
				tableInfo.add(row);
				
				int nextIndex = i + 1;
				if(nextIndex!=tablesInfo.size()) prevTableName = tablesInfo.get(nextIndex)[TABLEINDEX.TABLE_ID.ordinal()];				
			}
		}
		return true;
	}	
	
	//테이블을 비교하여 삭제한다. 이건 어떻게 쓸지는 다시 생각해봐야할것같다.
	public boolean dbDropSync() {
		boolean result = false;
//		result = dropTable(tableInfo.get(0)[TABLEINDEX.TABLE_ID.ordinal()]);
//		if(!result){
//			return false;
//		}	
		return false;
	}
	
	
	public boolean dbSyncProcessing(List<String[]> tableInfo){
		//DBTables과 정보비교
		boolean result = false;
		String tableName = tableInfo.get(0)[TABLEINDEX.TABLE_ID.ordinal()];
		
		if(!isExistTableInfo(tableName)){
			result = createTable(tableInfo);
			if(!result){
				//logger.info("전달된 정보가 이상합니다. 특수문자 혹은 DB에 맞게 설정되어 있는지 확인하세요. :: " + tableName);
			}
		}
		
		result = alterTable(tableInfo);
		if(!result){
			return false;
		}
			
		return true;
	}
	
	
	//해당 테이블명이 DB에 테이블로 존재하는지 확인한다.
	public boolean isExistTableInfo(String tableName){
		for(int i = 0; i < dbTables.size(); i++){
			if(dbTables.get(i)[DBTABLEINDEX.TABLE_NAME.ordinal()].toUpperCase().equals(tableName.toUpperCase())) {
				//logger.info("테이블 존재함::" + tableName);
				return true;
			}
		}
		
		//logger.info("테이블 없음::" + tableName);
		return false;
	}
	
	//테이블 비교하여 Create하는 Function
	public boolean createTable(List<String[]> tableInfo){
		//logger.info("createTable started!!");
		
		String result = "create table " + tableInfo.get(0)[TABLEINDEX.TABLE_ID.ordinal()] + "(\n";

		for(int i = 0; i < tableInfo.size(); i++){
			result += tableInfo.get(i)[TABLEINDEX.COLUMN_ID.ordinal()] + 
					this.getTypeStr(tableInfo.get(i)[TABLEINDEX.TYPE.ordinal()], tableInfo.get(i)[TABLEINDEX.LENGTH.ordinal()]) +
					this.getNotNullStr(tableInfo.get(i)[TABLEINDEX.NULL_YN.ordinal()]) + 
					this.getDefaultValueStr(tableInfo.get(i)[TABLEINDEX.DEFAULT_VALUE.ordinal()]) + ", \n";
		}
		result = result.substring(0, result.length()- 3);
		result +=");\n";
		
		boolean isSuccess = dataUtils.queryExec(result, db);
		//logger.info("createTable result : " + isSuccess);
		return isSuccess;
	}
	
	//테이블 비교하여 업데이트 하는 Function
	public boolean alterTable(List<String[]> tableInfo){
		//logger.info("alterTable started!!");
		return true;
	}
	
	//테이블 비교하여 Delete하는 Function
	public boolean dropTable(String tableId){
		
		return true;
	}
	
	
	public String getTypeStr(String type, String length){
		String resultStr  = "";
		if(type.equals("datetime")){
			resultStr  = type;
		}else{
			resultStr  = type + " (" + length + ")";
		}
		return " " + resultStr + " ";
	}
	public String getNotNullStr(String notNull){
		String resultStr  = "";
		if(notNull.equals("N")){
			resultStr  = "NOT NULL";
		}
		return " " + resultStr + " ";
	}
	public String getDefaultValueStr(String defaultValue){
		String resultStr  = "";
		if(defaultValue.length() < 1) return "";
		
		if(defaultValue.equals("AUTO_INCREMENT")){
			resultStr  = "AUTO_INCREMENT";
		}else if(defaultValue.equals("NOW")){
			resultStr  = "default NOW()";
		}else{
			resultStr  = "default '" + defaultValue + "'";	
		}
		return " " + resultStr + " ";
	}

	//엑셀데이터를 테이블에 넣기
	public boolean tableSync(List<DataSet> tables) {

		//1. 레이아웃 확인
		for(int i = 0 ; i < tables.size(); i++){
			String tableName = tables.get(i).getTableName();
			
			List<String[]> tableInfo = getTableInfo(tableName);
			
			if(tableInfo == null) {
				//logger.info("DB에 없는 테이블입니다. 테이블명  : " + tableName);
				return false;
			}
		
			boolean success  = deleteInsertTable(tables.get(i), tableInfo);
			if(!success){
				//logger.info("테이블변경에 실패했습니다. 테이블명  : " + tableName);
				return false;
			}
			
		}
		return false;
	}

	public List<String[]> getTableInfo(String tableName){
		List<String[]> tableInfo  = new ArrayList<String[]>();
		boolean founded = false;
		for(int i = 0 ; i < tablesInfo.size(); i++){
			if(tableName.equals(tablesInfo.get(i)[TABLEINDEX.TABLE_NM.ordinal()])){
				tableInfo.add(tablesInfo.get(i));
				founded = true;
			}
		}		
		
		if (founded) return tableInfo;
		else return null;
	}
	
	
	//transaction 어쩔건지??
	private boolean deleteInsertTable(DataSet dataSet, List<String[]> tableInfo) {
		String deleteQuery = " delete from " + tableInfo.get(0)[TABLEINDEX.TABLE_ID.ordinal()];
		String insertFields = getInsertFields(dataSet.getData().get(0), tableInfo);
		dataUtils.queryExec(deleteQuery, db);
		for(int i = 1 ; i<dataSet.getData().size(); i++){
			String insertQuery = " insert into " + tableInfo.get(0)[TABLEINDEX.TABLE_ID.ordinal()];
			insertQuery += " (" + insertFields + ") \n";
			insertQuery += " values(" + getInsertValues(dataSet.getData().get(i)) + "); \n";
			
			//logger.info("insertQuery : " + insertQuery);
			dataUtils.queryExec(insertQuery, db);
		}
		return true;
	}

	private String getInsertValues(String[] row) {
		String result  = "";
		for(int i=0; i<row.length;i++){
			result += "'" + row[i] + "',";
		}
		result = result.substring(0, result.length()- 1);
		
		return result;
	}

	private String getInsertFields(String[] header, List<String[]> tableInfo) {
		String result  = "";
		for(int i=0; i<header.length;i++){
			boolean founded = false;			
			String columnName = header[i];
			
			for(int t=0;t<tableInfo.size();t++){
				if(columnName.equals(tableInfo.get(t)[TABLEINDEX.COMMENT.ordinal()])){
					founded = true;
					result += tableInfo.get(t)[TABLEINDEX.COLUMN_ID.ordinal()] + ",";
				}
			}			
			
			if(!founded){
				//logger.info("해당 이름의 필드가 테이블에 존재하지 않습니다.: 필드명::" + columnName);
				return null;
			}
		}
		result = result.substring(0, result.length()- 1);
		
		return result;
	}
	
	//데이터의 업데이트는 delete후 insert이다.
	public boolean deleteMapInfoDB(ArrayList<MapInfo> mapInfoList) {		
		
		Map<String, String> scanInfo = new HashMap<String, String>();
		scanInfo = this.getTableScanInfo(mapInfoList);

		MapInfo workInfo = this.getWorkId(mapInfoList);
		//테이블별로 Insert하자.
		for(String key : scanInfo.keySet()){
			String insertStr = "delete from " + this.getTableId(key) + " where " + this.getColumnId(key, workInfo.getColumnName()) + "='" + workInfo.getRealValue() + "';";
			boolean success = dataUtils.queryExec(insertStr, db);
			if(success){
				//logger.info("성공 SQL:" + insertStr);
			}
			else{
				//logger.info("실패 SQL:" + insertStr);
			}
		}
		
		return true;
	}

	private MapInfo getWorkId(ArrayList<MapInfo> mapInfoList) {
		//단일입력 key 를 찾으면됨
		for(MapInfo mapInfo: mapInfoList){
			if(mapInfo.getType().equals("단일입력") && mapInfo.getKey().equals("YES")){
				return mapInfo;
			}
		}
		
		return null;
	}

	//mapping 정보를 가지고 DB에 인서트한다.
	//20170105 김창영부장
	public boolean insertMapInfoDB(ArrayList<MapInfo> mapInfoList, List<String[]> systemInfo) {
		//단일 다중 insert 여부 스캔
		Map<String, String> scanInfo = new HashMap<String, String>();
		scanInfo = this.getTableScanInfo(mapInfoList);

		//테이블별로 Insert하자.
		for(String key : scanInfo.keySet()){
			//logger.info("tableName:" + key + "  saveMode::" + scanInfo.get(key));

			//현작업 테이블
			ArrayList<MapInfo> currentWork = new ArrayList<MapInfo>();
			for(MapInfo mapInfo : mapInfoList){
				if(mapInfo.getTableName() != null){					
					if(mapInfo.getTableName().equals(key)){
						currentWork.add(mapInfo);
					}else {
						//하나의 필드를 여러군데 테이블에서 저장할수도 있다. 
						String[] saveInfo = mapInfo.getSaveInfo().split(",");
						for(String info: saveInfo){
							if(info.equals(key)){
								//매핑에 테이블명이 있을경우.
								if(mapInfo.getRealValue() != null){
									currentWork.add(mapInfo);
								}
							}
						}
					}
				}
			}
			
			if(scanInfo.get(key).equals("Single")){	
				String insertStr = "insert into ";
				insertStr += this.getTableId(key) + "(" + this.getMapFields(key, currentWork, systemInfo) + ") values(" + this.getMapValues(currentWork, systemInfo) + ");";
				
				//logger.info("단일입력 만들어진 SQL:" + insertStr);
				
				boolean success = dataUtils.queryExec(insertStr, db);
				if(success){
					//logger.info("성공 SQL:" + insertStr);
				}
				else{
					//logger.info("실패 SQL:" + insertStr);
				}
				
			}else{
				String inputInfoStr = "";
				Map<String, String>  inputInfo = new HashMap<String, String>();
				Map<String, MapInfo>  rowInputInfo = new HashMap<String, MapInfo>();
				for(MapInfo mapInfo : currentWork){
					
					if(mapInfo.getType().equals("단일입력")){
						//logger.info(mapInfo.getType() + "-->" + mapInfo.getTableName() + "," + mapInfo.getConnectInfo());
						if(!inputInfo.containsKey(mapInfo.getColumnName())){
							inputInfoStr += this.getColumnId(mapInfo.getConnectInfo(), mapInfo.getColumnName()) + ",";
							inputInfo.put(mapInfo.getColumnName(), mapInfo.getRealValue());
						}
					}
					
					if(mapInfo.getType().equals("참조저장")){
						//logger.info(mapInfo.getType() + "-->" + mapInfo.getTableName() + "," + mapInfo.getConnectInfo());
						if(!inputInfo.containsKey(mapInfo.getColumnName())){
							inputInfoStr += this.getColumnId(mapInfo.getConnectInfo(), mapInfo.getColumnName()) + ",";
							inputInfo.put(mapInfo.getColumnName(), "ref");
						}
					}
					
					if(mapInfo.getType().equals("세로해더입력")){
						//logger.info(mapInfo.getType() + "-->" + mapInfo.getTableName() + "," + mapInfo.getConnectInfo());
						if(!inputInfo.containsKey(mapInfo.getColumnName())){
							inputInfoStr += this.getColumnId(mapInfo.getTableName(), mapInfo.getColumnName()) + ",";
							inputInfo.put(mapInfo.getColumnName(), "key");
						}
					}
					
					if(mapInfo.getType().equals("세로입력")){
						//logger.info(mapInfo.getType() + "-->" + mapInfo.getTableName() + "," + mapInfo.getConnectInfo());
						if(!inputInfo.containsKey(mapInfo.getColumnName())){
							inputInfoStr += this.getColumnId(mapInfo.getTableName(), mapInfo.getColumnName()) + ",";
							inputInfo.put(mapInfo.getColumnName(), "value");
						}
					}
					
					if(mapInfo.getType().equals("가로입력")){
						for(MapInfo rowSearch : currentWork){
							if(mapInfo.getType().equals("가로입력")){
								if(!rowInputInfo.containsKey(mapInfo.getRow() + "," + mapInfo.getCol())){
									if(mapInfo.getRow() == rowSearch.getRow() && mapInfo.getCol() == rowSearch.getCol()){
										if(rowSearch.getRealValue() != null){
											if(rowSearch.getRealValue().length() > 1){
												inputInfoStr += this.getColumnId(mapInfo.getTableName(), mapInfo.getColumnName()) + ",";
												inputInfo.put(mapInfo.getColumnName(), "value");
												
												//logger.info("가로입력 mapInfo.getColumnName() ->" +mapInfo.getColumnName());
												//logger.info("가로입력 rowSearch.getColumnName() ->" +rowSearch.getColumnName());
												//logger.info("가로입력 rowSearch.realvalue() ->" +rowSearch.getRealValue());
												//logger.info("  rowInputInfo key ->" + mapInfo.getRow() + "," + mapInfo.getCol());
												rowInputInfo.put(mapInfo.getRow() + "," + mapInfo.getCol(), rowSearch);
											}
										}
									}
								}
							}							
						}
					}
					
				}
				//logger.info("inputInfo -->" + inputInfo);
				
				//세로해더정보
				Map<Integer, String>  headerInfo = new HashMap<Integer, String>();
				int headerRow = 0;
				for(MapInfo mapInfo : currentWork){
					if(mapInfo.getType().equals("세로해더입력")){
						headerInfo.put(mapInfo.getCol(), mapInfo.getRealValue());
						headerRow = mapInfo.getRow();
					}
				}
				
				//logger.info("headerInfo -->" + headerInfo);
				
				for(MapInfo mapInfo : currentWork){
					if(mapInfo.getType().equals("세로입력")){
						if(key.equals(mapInfo.getTableName())){
							String insertStr = "insert into ";
							String multiMapFields = this.multiMapFields(mapInfo.getTableName(), inputInfo);
							
							String multiMapValues = this.getMultiMapValues(inputInfo, headerInfo, mapInfo, currentWork, headerRow);
							
							insertStr += this.getTableId(key) + "(" + multiMapFields + ") values(" + multiMapValues + ");";
							//logger.info("다중입력 만들어진 SQL:" + insertStr);
							boolean success = dataUtils.queryExec(insertStr, db);
							if(success){
								//logger.info("성공 SQL:" + insertStr);
							}
							else{
								//logger.info("실패 SQL:" + insertStr);
							}
						}
					}else if(mapInfo.getType().equals("가로입력") && mapInfo.getKey().equals("YES")){
						if(key.equals(mapInfo.getTableName())){
							String insertStr = "insert into ";
							String multiMapFields = this.multiMapFields(mapInfo.getTableName(), inputInfo);
							
							String multiMapValues = this.getMultiMapValues(inputInfo, headerInfo, mapInfo, currentWork, headerRow, rowInputInfo);
							
							insertStr += this.getTableId(key) + "(" + multiMapFields + ") values(" + multiMapValues + ");";
							//logger.info("다중입력 만들어진 SQL:" + insertStr);
							boolean success = dataUtils.queryExec(insertStr, db);
							if(success){
								//logger.info("성공 SQL:" + insertStr);
							}
							else{
								//logger.info("실패 SQL:" + insertStr);
							}
						}
						
					}else{
						//logger.info("세로해더입력 라인은 제외함");
						//logger.info("mapInfo.getType() : " + mapInfo.getType());
					}
				}
			}		
			
		}
		
		return true;
	}
	
	private String multiMapFields(String tableName, Map<String, String> inputInfo) {
		String result = "";
		
		for(String key : inputInfo.keySet()){
			result += this.getColumnId(tableName, key) + ",";
		}
		
		result = result.substring(0, result.length() - 1);
		
		return result;
	}

	//이게 좀 어렵네.. 상황에따라 많이 다를듯.
	//inputInfo순서대로 key값과 ref값, value를 넣어주는데 mapInfo에서 찾아서 넣어준다.
	private String getMultiMapValues(Map<String, String> inputInfo, Map<Integer, String> headerInfo, MapInfo mapInfo, ArrayList<MapInfo> currentWork, int headerRow) {
		String result = "";
		
		//우선 입력할 row 와 col을 알아내고 해달 row에 맞는  ref를 구하고 해당  col에 맞는  key를 구하자.
		int col = mapInfo.getCol();
		int row = mapInfo.getRow();
		
		//해더가 있는 열은 제외
		ArrayList<MapInfo> mapInfoRow = new ArrayList<MapInfo>();	
		for(MapInfo extract : currentWork){
			if(extract.getRow() == row){
				mapInfoRow.add(extract);
			}
		}
		
		
		for(String key : inputInfo.keySet()){			
			if(inputInfo.get(key).equals("key")){
				result += "'" + headerInfo.get(col) + "',";
			}else if(inputInfo.get(key).equals("ref")){
				for(MapInfo ref : mapInfoRow){
					if(ref.getColumnName().equals(key)){
						result += "'" + ref.getRealValue() + "',";
					}
				}
			}else if(inputInfo.get(key).equals("value")){
				result += "'" + mapInfo.getRealValue() + "',";
			}else {
				result += "'" + inputInfo.get(key) + "',";
			}
				 
		}
		
		result = result.substring(0, result.length() - 1);

		return result;
	}
	
	//가로입력 케이스
	private String getMultiMapValues(Map<String, String> inputInfo, Map<Integer, String> headerInfo, MapInfo mapInfo, ArrayList<MapInfo> currentWork, int headerRow, Map<String, MapInfo> rowInputInfo) {
		String result = "";
		
		//우선 입력할 row 와 col을 알아내고 해달 row에 맞는  ref를 구하고 해당  col에 맞는  key를 구하자.
		int col = mapInfo.getCol();
		int row = mapInfo.getRow();
		
		//해더가 있는 열은 제외
		ArrayList<MapInfo> mapInfoRow = new ArrayList<MapInfo>();	
		for(MapInfo extract : currentWork){
			if(extract.getRow() == row){
				mapInfoRow.add(extract);
			}
		}
		
		for(String key : inputInfo.keySet()){
			if(inputInfo.get(key).equals("key")){
				result += "'" + headerInfo.get(col) + "',";
			}else if(inputInfo.get(key).equals("ref")){
				for(MapInfo ref : mapInfoRow){
					if(ref.getColumnName().equals(key)){
						result += "'" + ref.getRealValue() + "',";
					}
				}
			}else if(inputInfo.get(key).equals("value")){
				//logger.info("key:: " + key);
				
				boolean isNotFound = true;
				for(String rowCol : rowInputInfo.keySet()){
					int rowNum = Integer.parseInt(rowCol.split(",")[0]);
					if(mapInfo.getRow() ==  rowNum && rowInputInfo.get(rowCol).getMappingName().equals(key)){
						isNotFound = false;
						result += "'" + rowInputInfo.get(rowCol).getRealValue() + "',";
					}
				}
				
				if(isNotFound){
					result += "'',";
				}
				
			}else {
				result += "'" + inputInfo.get(key) + "',";
			}
				 
		}
		
		result = result.substring(0, result.length() - 1);

		return result;
	}


	//중복으로 Insert해야 하는 테이블을 찾는다.
	private Map<String, String> getTableScanInfo(ArrayList<MapInfo> mapInfoList) {
		Map<String, String> result = new HashMap<String, String>();
		Map<String, String> scan = new HashMap<String, String>();
		for(MapInfo mapInfo : mapInfoList){	
			if(mapInfo.getTableName() != null && mapInfo.getType().indexOf("입력") > dataSize){
				if(scan.containsKey(mapInfo.getTableName() + "," + mapInfo.getColumnName())){
					scan.put(mapInfo.getTableName() + "," + mapInfo.getColumnName(), "YES");
				}else{
					scan.put(mapInfo.getTableName() + "," + mapInfo.getColumnName(), "NO");
				}
				
			}
		}
		
		for(String key: scan.keySet()){
			String tableName = key.split(",")[0];
			if(scan.get(key).equals("YES")){
				result.put(tableName, "Multiple");
			}else{
				result.put(tableName, "Single");
			}
		}
		
		//logger.info("getTableScanInfo result --> " + result);
		return result;
	}
	
	private String getColumnId(String tableName, String columnName) {
		for(String[] row : tablesInfo){
			tableName = tableName.replace("[", "").replace("]", "");
			columnName  = columnName.replace("[", "").replace("]", "");

			if(row[TABLEINDEX.TABLE_NM.ordinal()].equals(tableName) && row[TABLEINDEX.COLUMN_NM.ordinal()].equals(columnName)){
				return row[TABLEINDEX.COLUMN_ID.ordinal()];
			}
		}
		return null;
	}

	private String getTableId(String tableName) {
		tableName = tableName.replace("[", "").replace("]","");
		
		for(String[] row : tablesInfo){
			if(row[TABLEINDEX.TABLE_NM.ordinal()].equals(tableName)){
				return row[TABLEINDEX.TABLE_ID.ordinal()];
			}
		}
		return null;
	}
	
	private String getMapValues(ArrayList<MapInfo> currentWork, List<String[]> systemInfo) {
		String result = "";
		String tableName = "";
		for(MapInfo info : currentWork){
			tableName = info.getTableName();
			result += "'" + info.getRealValue() + "',";
		}
		
		
		//systemInfo 를 insert문에 추가해준다.
		if(tableName.equals(systemInfo.get(1)[0])){
			for(int i = 1; i < systemInfo.get(1).length; i++){
				result += "'" + systemInfo.get(1)[i] + "',";
			}
		}
		
		if(result.length() > 1){
			result = result.substring(0, result.length() - 1);
		}		
		
		return result;
	}

	private String getMapFields(String tableName, ArrayList<MapInfo> currentWork, List<String[]> systemInfo) {
		//logger.info("getMapFields start!");
		String result = "";
		for(MapInfo info : currentWork){
			result += this.getColumnId(tableName, info.getColumnName()) + ",";
		}
		
		//systemInfo 를 insert문에 추가해준다.
		if(tableName.equals(systemInfo.get(1)[0])){
			for(int i = 1; i < systemInfo.get(0).length; i++){
				logger.info("systemInfo.get(1)[i] -- >" + systemInfo.get(0)[i]);
				result += this.getColumnId(tableName, systemInfo.get(0)[i]) + ",";
			}
		}
		
		if(result.length() > 1){
			result = result.substring(0, result.length() - 1);
		}		
		
		return result;
	}
	
	//20170125 김창영부장
	public List<DataSet> getMapInfoDB(ArrayList<MapInfo> mapInfoList) {
		//1.테이블과 필드 식별
		//2.select문 구성
		//3.dataset으로 반환
		
		//전체적으로 스켄하여 테이블명, 컬럼명, 매핑명을 찾는다.
		List<DataSet> result = new ArrayList<DataSet>();
		Map<String, String[]> scanInfo = new HashMap<String, String[]>();
		for(MapInfo mapInfo : mapInfoList){
			if(mapInfo.getTableName() == null) continue;
			
			String mappingName = mapInfo.getMappingName();
			String[] tableColumn = {mapInfo.getTableName(), mapInfo.getColumnName(),"",""};
			
			//logger.info(mapInfo.getTableName() + "," + mapInfo.getColumnName());
			for(String[] row : tablesInfo){				
				if(mapInfo.getTableName().equals("[" + row[TABLEINDEX.TABLE_NM.ordinal()] + "]") && mapInfo.getColumnName().equals("[" + row[TABLEINDEX.COLUMN_NM.ordinal()] + "]")){
					//logger.info("row[TABLEINDEX.TABLE_ID.ordinal()]:" + row[TABLEINDEX.TABLE_ID.ordinal()]);
					tableColumn[2] = "[" + row[TABLEINDEX.TABLE_ID.ordinal()] + "]";
					//logger.info("row[TABLEINDEX.COLUMN_ID.ordinal()]:" + row[TABLEINDEX.COLUMN_ID.ordinal()]);
					tableColumn[3] = "[" + row[TABLEINDEX.COLUMN_ID.ordinal()] + "]";
				}
			}
			if(!scanInfo.containsKey(mappingName)) scanInfo.put(mappingName, tableColumn);
		}
		
		
		//logger.info("scanInfo 출력");
		for(String key : scanInfo.keySet()){
			if(scanInfo.get(key)[3].length() < 1 || scanInfo.get(key)[2].length()< 1) {
				//logger.info("key:" + key + "에서 테이블명이나 컬럼명이 이상합니다.");
				continue;
			}
			String selectStr = "select distinct " + scanInfo.get(key)[3].replace("[","").replace("]", "") + " as " + key.replace("[","").replace("]", "") + " from " + scanInfo.get(key)[2].replace("[","").replace("]", "");
			//logger.info("sql:" + selectStr);
			List<String[]> resultObj = dataUtils.getDbDataArray(selectStr, db, dataSize);
			
			List<String[]> resultList = new ArrayList<String[]>();
			for(int i = 1; i < resultObj.size(); i++){
				resultList.add(resultObj.get(i));
			}
			
			DataSet dataset = new DataSet();
			dataset.setTableName(key);
			dataset.setData(resultList);
			result.add(dataset);
		}
		
		return result;
	}

	public List<CellInfo> getAffectedData(MapInfo mapInfo, String fileName, String objectKey, ArrayList<MapInfo> mapInfoList) {
		//logger.info("getAffectedData start!");
		//1.조회할  sql을 생성한다.
		List<CellInfo> resultList = new ArrayList<CellInfo>();
		String[] keys = mapInfo.getConnectInfo().split(",");
		for(String key:keys){
			String[] info = key.split("by");
			if(info.length == 2){
				for(MapInfo targetMapInfo: mapInfoList){
					if(targetMapInfo.getMappingName().equals(info[0])){
						
						String sqlStr = "select " + this.getColumnId(targetMapInfo.getTableName(),targetMapInfo.getColumnName()) + 
								" from " + this.getTableId(targetMapInfo.getTableName()) + 
								" where " + this.getColumnId(targetMapInfo.getTableName(),mapInfo.getColumnName()) + " = '" + mapInfo.getRealValue() + "' " +
								" and " + this.getColumnId(targetMapInfo.getTableName(),targetMapInfo.getKeyValue()) + " = '" + targetMapInfo.getRealValue() + "' ";
						//logger.info("info:" + info[0] + "," + info[1]);
						//logger.info("sql:"+ sqlStr);
						CellInfo cell = new CellInfo();
						cell.setRow(targetMapInfo.getRow());
						cell.setCol(targetMapInfo.getCol());
						String result = dataUtils.getDbSingleData(sqlStr, db);
						//logger.info("result::"+ result);
						cell.setValue(result);
						resultList.add(cell);
					}
				}
			}else{
				for(MapInfo targetMapInfo: mapInfoList){
					//logger.info("getMappingName:key<-->" + targetMapInfo.getMappingName() + ":" + key);
					if(targetMapInfo.getMappingName().equals(key)){
						
						String sqlStr = "select " + this.getColumnId(targetMapInfo.getTableName(),targetMapInfo.getColumnName()) + 
								" from " + this.getTableId(targetMapInfo.getTableName()) + 
								" where " + this.getColumnId(targetMapInfo.getTableName(),mapInfo.getColumnName()) + " = '" + mapInfo.getRealValue() + "';";
						
						//logger.info("sql:"+ sqlStr);
						CellInfo cell = new CellInfo();
						cell.setRow(targetMapInfo.getRow());
						cell.setCol(targetMapInfo.getCol());
						String result = dataUtils.getDbSingleData(sqlStr, db);
						//logger.info("result::"+ result);
						cell.setValue(result);
						resultList.add(cell);
					}
				}
			}
		
		}
		// TODO Auto-generated method stub
		return resultList;
	}

	public SpreadData getSQLPagingData(String sql, int start, int count) {
		SpreadData resultData = new SpreadData();
		
		String sqlStr = "select * from (" + sql + ") as A ";
		String countSqlStr = "select count(*) as cnt from (" + sql + ") as A " + " ;";
		if(db.getDriverName().indexOf("mysql") >= 0){
			sqlStr += " LIMIT " + start + ", " + count + " ;";
		}else if(db.getDriverName().indexOf("SQLServerDriver") >= 0){
//			DECLARE @RowsPerPage INT = 10, @PageNumber INT = 6
//					SELECT SalesOrderDetailID, SalesOrderID, ProductID
//					FROM (
//					SELECT SalesOrderDetailID, SalesOrderID, ProductID,
//					ROW_NUMBER() OVER (ORDER BY SalesOrderDetailID) AS RowNum
//					FROM Sales.SalesOrderDetail ) AS SOD
//					WHERE SOD.RowNum BETWEEN ((@PageNumberdataSize)*@RowsPerPage)+1
//					AND @RowsPerPage*(@PageNumber)
			return null;
		}else if(db.getDriverName().indexOf("oracle") >= 0){
			sqlStr += " WHERE BETWEEN ROWNUM > " + start + " AND ROWNUM <= " + (start + count) + " ;";
		}else{
			return null;
		}
		
		
		int totalCount = dataUtils.getCountData(countSqlStr, db);
		resultData.setReturnCount(totalCount);
		
		String result = dataUtils.getDbData(sqlStr, db, dataSize);
		resultData.setData(result);
		
		return resultData;
	}

	public SpreadData getTablePagingData(String tableName, int start, int count) {
		SpreadData resultData = new SpreadData();
		
		String sqlStr = "select * from " + tableName ;
		String countSqlStr = "select count(*) as cnt from " + tableName + " ;";
		if(db.getDriverName().indexOf("mysql") >= 0){
			sqlStr += " LIMIT " + start + ", " + count + " ;";
		}else if(db.getDriverName().indexOf("SQLServerDriver") >= 0){
//			DECLARE @RowsPerPage INT = 10, @PageNumber INT = 6
//					SELECT SalesOrderDetailID, SalesOrderID, ProductID
//					FROM (
//					SELECT SalesOrderDetailID, SalesOrderID, ProductID,
//					ROW_NUMBER() OVER (ORDER BY SalesOrderDetailID) AS RowNum
//					FROM Sales.SalesOrderDetail ) AS SOD
//					WHERE SOD.RowNum BETWEEN ((@PageNumberdataSize)*@RowsPerPage)+1
//					AND @RowsPerPage*(@PageNumber)
			return null;
		}else if(db.getDriverName().indexOf("oracle") >= 0){
			sqlStr += " WHERE BETWEEN ROWNUM > " + start + " AND ROWNUM <= " + (start + count) + " ;";
		}else{
			return null;
		}
		
		
		int totalCount = dataUtils.getCountData(countSqlStr, db);		
		resultData.setReturnCount(totalCount);
		
		String result = dataUtils.getDbData(sqlStr, db, dataSize);
		resultData.setData(result);
		
		return resultData;
	}

	//TABLE_ID,TABLE_NM,COLUMN_ID,COLUMN_NM,TYPE,LENGTH,NULL_YN,KEY_YN,DEFAULT_VALUE,COMMENT, USE_YN
	public List<SpreadColumnInfo> getColumnInfo(String tableName) {
		//logger.info("getColumnInfo start!");
		
		List<SpreadColumnInfo> colInfos = new ArrayList<SpreadColumnInfo>();
		for(String[] rows : tablesInfo){
			if(rows[TABLEINDEX.TABLE_ID.ordinal()].equals(tableName.toUpperCase()) && rows[TABLEINDEX.USE_YN.ordinal()].equals("Y")){
				SpreadColumnInfo colInfo = new SpreadColumnInfo();
				colInfo.setId(rows[TABLEINDEX.COLUMN_ID.ordinal()]);
				if(rows[TABLEINDEX.KEY_YN.ordinal()].equals("Y")){
					colInfo.setAllowEditing(true);
				}
				colInfo.setCaption(rows[TABLEINDEX.COLUMN_NM.ordinal()]);
				colInfo.setDataField(rows[TABLEINDEX.COLUMN_ID.ordinal()]);
				switch(rows[TABLEINDEX.TYPE.ordinal()]){
					case "VARCHAR":
						colInfo.setDataType(rows[TABLEINDEX.TYPE.ordinal()]);
						break;
					case "INT":
						colInfo.setDataType("number");
						break;
					case "BOOLEAN":
						colInfo.setDataType("boolean");
						break;
					case "DATE":
						colInfo.setDataType("date");
						break;
					default:
						colInfo.setDataType(null);
				}
				colInfos.add(colInfo);
			}
		}
		
		
		return colInfos;
	}

	
	//workId로 해당 데이터를 가져온다.
	public ArrayList<MapInfo> getWorkData(String workKey, String defId) {
		//1. typeInfo, map을 읽어온다.
		List<SpreadColumnInfo> result = new ArrayList<SpreadColumnInfo>();
		List<String[]> typeInfos = this.getTypeInfos(defId);
		ArrayList<MapInfo> mapInfoList = this.getWorkMap(defId);
		//single, mulity 정보
		Map<String, String> scanInfo = this.getTableScanInfo(mapInfoList);
		//1. 사용할 데이터를 구하자.
		List<DataSet> dataSets = this.getDataTables(workKey, typeInfos);
		List<DataSet> codeDataSets = this.getCodeTables(typeInfos);
		//세로해더입력
		Map<String, Integer> mapMultiMode = new HashMap<String, Integer>(); 
		//가로입력
		Map<String, Integer> mapMultiColMode = new HashMap<String, Integer>(); 
		//세로입력을 위해
		Map<String,Map<String, Integer>> mapMultiHeader = new HashMap<String,Map<String, Integer>>(); 
		//키값을 먼저 넣는다.
		for(MapInfo mapInfo : mapInfoList){
			
			switch(mapInfo.getType()){
				case "단일입력":
					for(DataSet dataSet: dataSets){
						if(dataSet.getTableName().equals(mapInfo.getTableName())){
							logger.info("mapInfo.getTableName() ->" + mapInfo.getTableName());
							logger.info("data ->" + dataSet.getData());
							List<String[]> data =  dataSet.getData();
							for(int i = 0; i < data.get(0).length; i++){
								if(data.get(0)[i].equals(this.getColumnId(mapInfo.getTableName(), mapInfo.getColumnName()))){
									mapInfo.setRealValue(data.get(1)[i]);
								}
							}
						}
					}
					break;
				case "참조저장":
					//저장정보테이블의 동일컬럼명으로 셋팅
					if(scanInfo.get(mapInfo.getSaveInfo()).equals("Single")){
						//logger.info("Single mapInfo.getSaveInfo() ->" + mapInfo.getSaveInfo());
						for(DataSet dataSet: dataSets){
							if(dataSet.getTableName().equals(mapInfo.getSaveInfo())){
								List<String[]> data =  dataSet.getData();
								for(int i = 0; i < data.get(0).length; i++){
									if(data.get(0)[i].equals(this.getColumnId(mapInfo.getSaveInfo(), mapInfo.getColumnName()))){
										mapInfo.setRealValue(data.get(1)[i]);
										//관련 참조인경우 모두 셋팅함.
										this.setSingleRef(mapInfo, typeInfos, mapInfoList, codeDataSets);
									}
								}
							}
						}
					}else{
						//다중입력인경우 순서대로 세로입력해더 가 있으면 첫번째 값을 찾아서 그값에 해당하는 데이터만 가져오자. 헐....
						//logger.info("multi mapInfo.getSaveInfo() ->" + mapInfo.getSaveInfo());
						String workingTablename = mapInfo.getSaveInfo();
						String mappingName = mapInfo.getMappingName();
						String columnName = mapInfo.getColumnName();
						String headerName = this.getHeaderName(workingTablename, typeInfos); 
						String headerValue = this.getHeaderValue(workingTablename, typeInfos, dataSets); 

						for(DataSet dataSet: dataSets){
							if(dataSet.getTableName().equals(workingTablename)){
								List<String[]> data =  dataSet.getData();
								
								int headerIndex = 0;
								headerIndex = this.getIndex(workingTablename, columnName, data);
								
								int rowIndex = 1;
								// 세로해더입력 index
								int headerNameIndex = dataSize;
								if(headerName != null){
									headerNameIndex = getIndex(workingTablename, headerName, data);
									if(mapMultiMode.containsKey(mappingName)){
										for(int i = mapMultiMode.get(mappingName) + 1; i < data.size(); i++){
											if(data.get(i)[headerNameIndex].equals(headerValue)){
												rowIndex = i;
												break;
											}
										}
									}
								}else{
									if(mapMultiMode.containsKey(mappingName)){
										for(int i = mapMultiMode.get(mappingName) + 1; i < data.size(); i++){
											rowIndex = i;
											break;
										}
									}
								}
								mapMultiMode.put(mappingName, rowIndex);
								mapInfo.setRealValue(data.get(mapMultiMode.get(mappingName))[headerIndex]);
								this.setMultiRef(mapInfo, typeInfos, mapInfoList, codeDataSets);
							}
							
						}
					}
					break;
				case "가로입력":
					if(mapInfo.getKey().equals("YES")){
						//logger.info("multi mapInfo.getSaveInfo() ->" + mapInfo.getSaveInfo());
						String workingTablename = mapInfo.getTableName();
						String mappingName = mapInfo.getMappingName();
						String columnName = mapInfo.getColumnName();

						for(DataSet dataSet: dataSets){
							if(dataSet.getTableName().equals(workingTablename)){
								List<String[]> data =  dataSet.getData();
								
								int headerIndex = 0;
								headerIndex = this.getIndex(workingTablename, columnName, data);
								
								int rowIndex = 1;
								if(mapMultiColMode.containsKey(mappingName)){
									for(int i = mapMultiColMode.get(mappingName) + 1; i < data.size(); i++){
										rowIndex = i;
										break;
									}
								}
								mapMultiColMode.put(mappingName, rowIndex);
								mapInfo.setRealValue(data.get(rowIndex)[headerIndex]);
								//가로입력 key가 아닌것
								this.setMultiRowRef(mapInfo, mapInfoList, dataSets);
							}
						}
					}
					break;
				default:
					break;
			}
		}
		
		for(MapInfo mapInfo : mapInfoList){
			switch(mapInfo.getType()){				
				case "세로해더입력":
					//이것은 DB에서 순서대로 가져오는 수밖에 없다. 단일입력은 존재하지 않음
					String tableName = mapInfo.getTableName();
					String mappingName = mapInfo.getMappingName();
					String columnName = mapInfo.getColumnName();

					//해더가 새로 출현하면 만들어줌
					if(!mapMultiHeader.containsKey(columnName)){
						Map<String, Integer> valueMap = new HashMap<String, Integer>();
						for(DataSet dataSet: dataSets){
							if(dataSet.getTableName().equals(tableName)){
								List<String[]> data =  dataSet.getData();

								int resultIndex = this.getIndex(tableName, columnName, data);
								int colIndex = 1;
								for(int i = 1; i < data.size(); i++){
									//workKey
									String value = data.get(i)[resultIndex];
									if(!valueMap.containsKey(value)){
										valueMap.put(value, colIndex++);
									}
								}
							}
						}
						//logger.info("valueMap ---------------------->" +  valueMap);
						mapMultiHeader.put(columnName, valueMap);
					}
					
					//값을 넣어준다
					Map<String, Integer> valueMap = mapMultiHeader.get(columnName);
					int minValue  = 10000;	
					for(String key: valueMap.keySet()){
						//순서대로 하려면 0이 아닌 가장 작은것 찾기
						if(minValue > valueMap.get(key)){
							minValue = valueMap.get(key);
						}
						
					}
					for(String key: valueMap.keySet()){
						//순서대로 하려면 0이 아닌 가장 작은것 찾기
						if(valueMap.get(key) ==  minValue){
							//세로입력을 처리하자.							
							mapInfo.setRealValue(key);
							valueMap.put(key, 10000);
							
							//mapInfo에 있는 값이 세로입력해더값임.
							this.setColDBValue(mapInfo, typeInfos, mapInfoList, dataSets);
							break;
						}
					}
					
					break;
			}
		}
		
		return mapInfoList;
	}

	private void setMultiRowRef(MapInfo mapInfo, ArrayList<MapInfo> mapInfoList, List<DataSet> dataSets) {
		String tableName = mapInfo.getTableName();
		
		Map<String,String> refKeyMaps = new HashMap<String, String>();
		refKeyMaps.put(mapInfo.getColumnName(), mapInfo.getRealValue());
				
		for(MapInfo searchInfo: mapInfoList){
			if(searchInfo.getType().equals("가로입력") 
					&& searchInfo.getKey().equals("NO")
					&& searchInfo.getRow() == mapInfo.getRow() 
					&& searchInfo.getTableName().equals(tableName)){
				logger.info("setMultiRowRef --> " + tableName + "," + searchInfo.getColumnName() + "   refKeyMaps -->" + refKeyMaps);
				String value = this.getDataValue(tableName, searchInfo.getColumnName() ,refKeyMaps, dataSets);
				if(value == null) value = "";
				searchInfo.setRealValue(value);
			}
		}	
	}

	//세로입력처리, 해당 컬럼만 처리한다.
	private void setColDBValue(MapInfo mapInfo, List<String[]> typeInfos, ArrayList<MapInfo> mapInfoList, List<DataSet> dataSets) {
		String tableName = mapInfo.getTableName();
		
		Map<String,String> refKeyMaps = new HashMap<String, String>();
		refKeyMaps.put(mapInfo.getColumnName(), mapInfo.getRealValue());
		//자재명 --> 참조저장을 가져온다.
		String refKey = this.getRowKey(mapInfo, typeInfos);
		
		for(MapInfo searchInfo: mapInfoList){
			if(searchInfo.getType().equals("세로입력") && mapInfo.getCol() == searchInfo.getCol() && mapInfo.getTableName().equals(searchInfo.getTableName())){	
				String refValue = this.getRowKeyValue(searchInfo.getRow(), refKey, mapInfoList);
				refKeyMaps.put(refKey, refValue);
				String value = this.getDataValue(tableName, searchInfo.getColumnName() ,refKeyMaps, dataSets);
				if(value == null) value = "";
				searchInfo.setRealValue(value);
			}
		}	
	}
	
	//refKeyMaps에는 참조저장 컬럼명가 value가 들어있는데 모두 들어있는 row의 value를 찾아낸다.
	private String getDataValue(String tableName, String columnName, Map<String, String> refKeyMaps, List<DataSet> dataSets) {
		for(DataSet dataSet: dataSets){
			if(dataSet.getTableName().equals(tableName)){
				int resultIndex = dataSize;
				List<String[]> data =  dataSet.getData();
				for(int i = 1; i < data.size(); i++){
					int okCount = 0;
					for(String key : refKeyMaps.keySet()){
						int headerIndex = getIndex(tableName, key, data);
						if(data.get(i)[headerIndex].equals(refKeyMaps.get(key))){
							okCount++;
						}
					}
					if(okCount == refKeyMaps.size()){
						resultIndex = i;
						break;
					}
				}
				
				if(resultIndex != dataSize){
					int retultIndex = getIndex(tableName, columnName, data);					
					return data.get(resultIndex)[retultIndex];
				}
			}
		}
		return null;
	}
	
	private String getRowKey(MapInfo mapInfo, List<String[]> typeInfos) {
		String tableName = mapInfo.getTableName();

		for(String[] type: typeInfos){
			String[] saveInfo = type[TYPEINFOINDEX.SAVE_INFO.ordinal()].split(",");
			for(String saveTable: saveInfo){
				if(type[TYPEINFOINDEX.KIND.ordinal()].equals("참조저장")){
					if(saveTable.equals(tableName)){
						return type[TYPEINFOINDEX.COLUMN_NAME.ordinal()];
					}
				}
			}
		}
		return null;
	}
	
	//세로입력 데이터를 얻기위해 해당 row의 key와 value를 찾아낸다.
	private String getRowKeyValue(int rowNum, String refKey, ArrayList<MapInfo> mapInfoList) {
		for(MapInfo info: mapInfoList){
			if(info.getRow() == rowNum && info.getColumnName().equals(refKey)){
				return info.getRealValue();
			}
		}
		return null;
	}

	private int getIndex(String workingTablename, String columnName, List<String[]> data) {
		for(int i = 0; i < data.get(0).length; i++){
			if(data.get(0)[i].equals(this.getColumnId(workingTablename, columnName))){
				return i;
			}
		}
		return dataSize;
	}

	private void setMultiRef(MapInfo mapInfo, List<String[]> typeInfos, ArrayList<MapInfo> mapInfoList,
			List<DataSet> codeDataSets) {
		
		String[] connectInfos = mapInfo.getConnectInfo().split(",");
		
		String tableName = mapInfo.getTableName();
		String columnName = mapInfo.getColumnName();	
		
		
		for(String connect: connectInfos){
			String value = this.getDataRefValue(tableName, columnName, mapInfo.getRealValue(), connect, codeDataSets);
			if(value == null) break;
			for(MapInfo info: mapInfoList){
				if(info.getRow() == mapInfo.getRow() && info.getMappingName().equals(connect)){
					info.setRealValue(value);
				}
					
			}
		}
	}

	private void setSingleRef(MapInfo mapInfo, List<String[]> typeInfos, ArrayList<MapInfo> mapInfoList,
			List<DataSet> codeDataSets) {
		String[] connectInfos = mapInfo.getConnectInfo().split(",");
		
		String tableName = mapInfo.getTableName();
		String columnName = mapInfo.getColumnName();	
		
		
		for(String connect: connectInfos){
			String realName = this.getConnectColumnName(connect, typeInfos);
			if(realName == null) break;
			String value = this.getDataRefValue(tableName, columnName, mapInfo.getRealValue(), realName, codeDataSets);
			if(value == null) break;
			for(MapInfo info: mapInfoList){
				if(info.getMappingName().equals(connect)){
					//logger.info("setSingleRef --> " + info.getMappingName() + "," + connect + " value:" + value);
					info.setRealValue(value);
				}
					
			}
		}
	}

	private String getConnectColumnName(String connect, List<String[]> typeInfos) {
		for(String[] info: typeInfos){
			if(info[TYPEINFOINDEX.MAPPING_NAME.ordinal()].equals(connect)){
				return info[TYPEINFOINDEX.COLUMN_NAME.ordinal()];
			}
		}
		return null;
	}
	
	private String getDataRefValue(String tableName, String keyName, String keyValue, String searchName, List<DataSet> dataSets) {
		for(DataSet dataSet: dataSets){
			if(dataSet.getTableName().equals(tableName)){
				List<String[]> data =  dataSet.getData();
				
				int headerIndex = getIndex(tableName, keyName, data);
				int searchIndex = getIndex(tableName, searchName, data);
				
				if(headerIndex == dataSize || searchIndex == dataSize){
					return null;
				}
				for(int i = 0; i < data.size(); i++){
					if(data.get(i)[headerIndex].equals(keyValue)){
						return data.get(i)[searchIndex];
					}
				}
				
			}
		}
		return null;
	}

	//만약 세로해더입력 이 있으면 컬럼명을 얻어온다.
	private String getHeaderName(String workingTablename, List<String[]> typeInfos) {
		for(String[] type : typeInfos){
			if(type[TYPEINFOINDEX.TABLE_NAME.ordinal()].equals(workingTablename)){
				String headerName = type[TYPEINFOINDEX.COLUMN_NAME.ordinal()];
				return headerName;
			}
		}
		return null;
	}

	//만약 세로해더입력 이 있으면 컬럼의 값을 얻어온다.
	private String getHeaderValue(String workingTablename, List<String[]> typeInfos, List<DataSet> dataSets) {
		for(String[] type : typeInfos){
			if(type[TYPEINFOINDEX.TABLE_NAME.ordinal()].equals(workingTablename)){
				String headerName = type[TYPEINFOINDEX.COLUMN_NAME.ordinal()];
				for(DataSet dataSet : dataSets){
					if(dataSet.getTableName().equals(workingTablename)){
						List<String[]> data =  dataSet.getData();
						for(int i = 0; i < data.get(0).length; i++){
							if(data.get(0)[i].equals(this.getColumnId(workingTablename, headerName))){
								return data.get(1)[i];
							}
						}
					}
				}
			}
		}
		return null;
	}
	

	private List<DataSet> getCodeTables(List<String[]> typeInfos) {
		List<DataSet> result = new ArrayList<DataSet>();
		List<String> codeTables = new ArrayList<String>();
		for(String[] row : typeInfos){
			if(row[TYPEINFOINDEX.KIND.ordinal()].equals("참조저장") && row[TYPEINFOINDEX.KEY.ordinal()].equals("YES")){
				codeTables.add(row[TYPEINFOINDEX.TABLE_NAME.ordinal()]);
			}
		}
		
		for(String codeTable: codeTables){
			String selectStr = "select * from " + this.getTableId(codeTable) + ";";
			DataSet dataSet = new DataSet();
			dataSet.setTableName(codeTable);
			dataSet.setData(dataUtils.getDbDataArray(selectStr, db, dataSize));
			result.add(dataSet);
		}
		
		return result;
	}

	private String makeWhereStagement(String workKey, String tableName, String columnName) {
		String result = " where ";
		
		result += this.getColumnId(tableName, columnName) + " ='" + workKey + "'";
		
		return result;
	}

	//select 대상테이블을 가져온다.
	//typeInfo에서 단일입력이면서 Key인 필드를 찾아서 saveInfo테이블리스트
	private List<DataSet> getDataTables(String workKey, List<String[]> typeInfos) {
		List<DataSet> result = new ArrayList<DataSet>();
		List<String> tables = new ArrayList<String>();
		String keyColumnName = "";
		
		for(String[] row : typeInfos){
			String tableId = this.getTableId(row[TYPEINFOINDEX.TABLE_NAME.ordinal()]);
			if(row[TYPEINFOINDEX.KIND.ordinal()].equals("단일입력") && row[TYPEINFOINDEX.KEY.ordinal()].equals("YES")){
				//자기자신넣음.
				tables.add(row[TYPEINFOINDEX.TABLE_NAME.ordinal()]);
						
				String[] saveTables = row[TYPEINFOINDEX.SAVE_INFO.ordinal()].split(",");
				keyColumnName = row[TYPEINFOINDEX.COLUMN_NAME.ordinal()];
				for(String table : saveTables){
					tables.add(table);
				}
			}
		}
		
		for(String key : tables){
			String selectStr = "select  * from " + this.getTableId(key) + this.makeWhereStagement(workKey, key, keyColumnName) + ";";
			logger.info("getDataTables selectStr -- >" + selectStr);
			
			DataSet dataSet = new DataSet();
			dataSet.setTableName(key);
			dataSet.setData(dataUtils.getDbDataArray(selectStr, db, dataSize));
			result.add(dataSet);
		}
		
		return result;
	}

	private ArrayList<MapInfo> getWorkMap(String defId) {
		ArrayList<MapInfo> mapInfoList = null;
		try {
			String mapInfoListStr = dataUtils.getJsonFile("process\\_DBTables\\def","MAP_" + defId, "json");
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<ArrayList<MapInfo>>() {}.getType();
			mapInfoList = gson.fromJson(mapInfoListStr, listType);
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapInfoList;
	}

	private List<String[]> getTypeInfos(String defId) {
		List<String[]> result = null;
		try {
			String typeInfoStr = dataUtils.getJsonFile("process\\_DBTables\\def","TYPEINFO_" + defId, "json");
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<ArrayList<DataSet>>() {}.getType();
			List<DataSet> dataSets = gson.fromJson(typeInfoStr, listType);
			
			result = dataSets.get(0).getData();
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public List<String[]> getUserDataList(String userId, String userColumn, String tableName, String selectColumn , String defColumn) {
		String[] result = null;
		String selectStr = "select distinct  " + this.getColumnId(tableName, selectColumn) + "," + this.getColumnId(tableName, defColumn) + " from " 
						+ this.getTableId(tableName) + " where " + this.getColumnId(tableName, userColumn) + "='" + userId + "';";
		
		logger.info("getUserDataList select string -->" + selectStr);
		List<String[]> resultObj = dataUtils.getDbDataArray(selectStr, db, dataSize);
		return resultObj;
	}

	public boolean getSystemInfoSave(SystemInfo systemInfo) {
		dataUtils.saveSystemInfo(systemInfo, db);
		return true;
	}
	
	public boolean generateSampleData(String targetTable, int genCnt) {
		logger.info("generateSampleData start");
		logger.info("String targetTable, int genCnt -->" + targetTable + "," + genCnt);
		
		Map<String, String>  inputInfo = new HashMap<String, String>();
		
		for(int cnt = 0; cnt < genCnt; cnt++){
			String fields = "";
			String values = "";
			for( int i=1; i<tablesInfo.size();i++){
				String[] row = tablesInfo.get(i);
				logger.info("row[TABLEINDEX.DEFAULT_VALUE.ordinal()].trim().length() --> " + row[TABLEINDEX.DEFAULT_VALUE.ordinal()].trim().length());
				if(row[TABLEINDEX.DEFAULT_VALUE.ordinal()].trim().length() >= 1) continue;
				
				logger.info("targetTable , row[TABLEINDEX.TABLE_ID.ordinal()] --> " + targetTable + "," + row[TABLEINDEX.TABLE_ID.ordinal()]);
				if(targetTable.equals(row[TABLEINDEX.TABLE_ID.ordinal()])){
					fields += row[TABLEINDEX.COLUMN_ID.ordinal()] + ",";
					values += this.getSampleValue(row[TABLEINDEX.COLUMN_NM.ordinal()]) + ",";
				}
			}
			
			logger.info("fields --> " + fields);
			logger.info("values --> " + values);
			fields = fields.substring(0,fields.length()-1);
			values = values.substring(0,values.length()-1);
			
			String insertStr = "insert into ";
			insertStr += targetTable + "(" + fields + ") values(" + values + ");";
			
			//logger.info("단일입력 만들어진 SQL:" + insertStr);
			
			boolean success = dataUtils.queryExec(insertStr, db);
		}
		return true;
	}

	private String getSampleValue(String colName) {
		List<String[]> code = null;
		try {
			code = dataUtils.getCodeJsonArray(colName);
		} catch (IOException e) {
			logger.info(colName + "  --> 해당코드를 찾을수 없습니다.");
			return null;
		}
		
		int ran = (int) (Math.random() * ( code.size() - 0 ));
		
		return "'" + code.get(ran)[0] + "'";
	}
}
