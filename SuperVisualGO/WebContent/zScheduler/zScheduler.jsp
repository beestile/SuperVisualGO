<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="euc-kr">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ZettaSoft - Scheduler</title>
<link href="${pageContext.request.contextPath}/assets/css/bootstrap/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jquery-ui-smoothness.min.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/plugin/datatables/DataTables/css/dataTables.jqueryui.min.css">

<script src="${pageContext.request.contextPath}/assets/js/jquery-2.1.4.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jquery-ui.min.css">
<script src="${pageContext.request.contextPath}/assets/js/jquery-ui.min.js"></script>


<script src="${pageContext.request.contextPath}/plugin/datatables/DataTables/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/DataTables/js/dataTables.jqueryui.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap/bootstrap.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/plugin/jspanel/jquery.jspanel.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/jspanel/jquery.jspanel.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/plugin/avgrund/style/avgrund.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/avgrund/jquery.avgrund.min.js"></script>


</head>
<style type="text/css">
body {
	font-size: 12px;
	font-family: ‘Nanum Brush Script’, Helvetica, Arial, sans-serif;
	text-rendering: optimizeLegibility;
	color: #444;
}

td.details-control {
	background:
		url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABGdBTUEAANbY1E9YMgAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAAMDSURBVHjarFXdS5NRGH/eufyK2ZZWvqKiyZQQpQ9hNKVIHZLzpq7Ey8SbQhG66R+IPqCuCqKLrsK8kbCMhcOwNdimglZ24WSVHyxzVjqZQ9+P0/Mcz1upE7vwjB/nnOfjt3Oej/NKkHqYEGmIA4h0saahITYQiljr2x2lFHszIgthQeQgDgpSEGQJRByxikgiVARLdSoiy0QcRVR2dHRc8fv9nmg0OqvrukagNclIRzbCNjPFwbiATlWAcPT39z9VFGWD7TJIRzZkK3y2kEriSvmyLJ+LRCIfySmpJZk3Nsiuf+pmLaGLrDnYxLonO9mr7wMsoSY4MdmSD/oeExySJBJAsSoOBoN3HQ5H07KyDI+/PoI3S0M8OGTEpM1I0VR7uA6ull6D3PQ8CAQCHqfTeQPFMxRXI5O2rq6uhvb29k4NNOlO+DYMx4bRH386gv0DXYeZ5AxE1iJw4Ug9FBcWl8VisYnR0dFZSpJJEB5qbW29JEmS6d2SD3wxH2gaUmsqqLoG3roh8NYO8T1mB1TUjf0Yg7f4p+TT1tZ2WdzSbBBml5eXn6SAeqKvQVWRTFdBUdFZVf9kjuRch4QKknu+ebi8oqKCfLMpjmZRtOlWqzWXlFPxKXRQ8LISBFyBLaXgq/fz2ek9y+fPq1/4bLFYrEYDmLfXD8WMTrazsv4OVVN5qtaVjc0ywWsbOrPRTvF4/JfNZsuTM2SYW53nKT01cJrP4y3j3NjYi7xDQU4Bl6PvT9FFmkn05Vo4HJ4gpSvfxeO2GS+VJ8AYioghnZDWjXIjl09PT38gDjIxCFd6enr6sCz05sJmqLJWcSIOdDzRV8nBsy5kdosdWorcVEp6b2/vc9HfSppxh1AoFHe73faSopKyM3k1EF4J49XnttSizvgOqm3VcKvmJsjZMoyMjAxibz9Bjph4LFK33mJykT2YfMgaXrrY8Wd2Voo4/6Ke3Xt/n0UT0e2tl2+03n49Dlm7vTg7nq+FhYV5g4jWez1f//vAZgj9+l4PrLTfn4DfAgwAXP8AAdHdgRsAAAAASUVORK5CYII=')
		no-repeat center;
	cursor: pointer;
}

tr.details td.details-control {
	background:
		url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAACNiR0NAAAABGdBTUEAANbY1E9YMgAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAALbSURBVHjarFVNTFNBEJ4tFVuw0gaj1Jhe5OdgJV5Iaw+CGAMKIdET4Sgh4SIHDcYLN07oiZOGqyFcjOECISEhwRpoGxLExAMVAi9NadIgWNLW8t7bdWa7L0Ap6oFtvsy+3Zlvd2ZnpnYoP2yICsQFRKWa0zARhwhdzXmpob3km6k1J8KFuIyoVqSgyLKIDOIAkUcYCFHuVkTmQFxF3BoYGHgWDodnk8mkxjk3CTSnNdojHaXrULanyOhW1xGB6enpD7quH4ozBu2RDukqmxOkTLlU5/V6721sbHwjI57Pi9z8vNgdHhY7PT0i1d0tdl+8FNmZGcGzWUlMumSDttcUB2PqAShWvuXl5bFAINDB9/chMzEBuYWF4rGMgsSACQECTRyhENQMDkJFbS0sLS3NhkKh16i1TXG1XtIzNDT0oL+//zmYJtt7Mwa5xc+Al0AmDlwKJKMfrunaNhibm+Bsa4MbPt/NdDq9GovFNHokmyKs6e3tfcIYs+XDYciGvwA3TQnT5EXJaW6qdQ65lRU8dBHIpq+v76ny0m4RVjU2Nt4h7w7m5qRhKY4OOALp0mhqaiLbKoqjXSVtpdvtrqXNfDwuldE3qMcblBs/WlulLGxtSelyudxWAZQmtkx90zTgb8M0DPlQTNeLaYJuH68UWU6ZTGbP4/FcqfB64XciIZ/2e/CuSixVCMeErAJvnfxG25+qikybqsvc+vr6qrx+e7uKkXEEQ8WNcxXPorx0v10SxuPxNeIgLovw1+Tk5EfKZ3dHBzj8/qIxt0gUkUWMh1TW14Pn8SNKIz41NfVJ1bd+IrGj0ejblpaWhwVNA210FA6iEfSPFV203EZnq5ubwTcyAs6GBohEInPBYPAVbmiKtHzpHabTIvnuvVjr6hIx/20R9fvF185OkRgfF4WdndLSq7NK77yag/OsjnOqfaVSqYRFRPN/tS/2nw32otov/KvBsvP+C/gjwAC23ACdhngbNwAAAABJRU5ErkJggg==')
		no-repeat center;
}
</style>

<body>
	<div class="btn-group btn-block">
		<button type="button" onclick="javascript:addGroup();" class="btn  btn-primary" style="width: 25%">그룹 추가</button>
		<button type="button" onclick="javascript:doExport();" class="btn  btn-primary" style="width: 25%">Export/Import</button>
		<button type="button" onclick="javascript:addProcess();" class="btn  btn-primary" style="width: 25%">프로세스에서 추가</button>
		<button type="button" onclick="javascript:reLoad();" class="btn  btn-primary" style="width: 25%">ReLoad</button>
	</div>
	<div id="zv" class="display" width="100%" cellspacing="0" width="100%" style="margin-right: 5px"></div>
</body>


<script laguage="javascript">
/**
 * [entry point]
 * window.onload 함수는 웹페이지의 로딩이 끝나는 시점에 실행되는 함수이다.
 * jQuery의 $(document).ready(function(){}); 과 동일.
 * window.onload의 단점
 * 	- 외부 라이브러리에서 onload가 이미 선언되어 있을 경우
 * 	- <body onload="">와 같은 attribute가 설정이 되어 있는 경우에는 attribute의 onload=""만 작동
 * */
window.onload = function() {
		
//	jQuery.ajax({ // ajax 사용
//        type: "POST", // post 타입 전송
//        url: "${pageContext.request.contextPath}/schedule/reLoad.do",
//        dataType: "json", // json타입 데이터 전송...
//        success: function(msg) { // 콜백 성공 응답시 실행
//        	console.info(JSON.stringify(msg));
//        },
//        error: function(request, status, error) { // Ajax 전송 에러 발생시 실행.
//            msgBox("request:" + request.responseText + "\n error:" + error);
//        },
//        //data: sendInfo // 전송 파라미터, 현재 컨트롤러에서 여기 있는 데이터를 까서 쓰고 있지는 않음...
//    });
	
	
    jQuery.ajax({ // ajax 사용
        type: "POST", // post 타입 전송
        url: "${pageContext.request.contextPath}/schedule/load.do",
        dataType: "json", // json타입 데이터 전송...
        success: function(msg) { // 콜백 성공 응답시 실행
        	dataSet = msg; // msg : 컨트롤러에서 List<TargetList>가 넘어옴.
        	showSchedule("table");
        },
        error: function(request, status, error) { // Ajax 전송 에러 발생시 실행.
            msgBox("request:" + request.responseText + "\n error:" + error);
        },
        //data: sendInfo // 전송 파라미터, 현재 컨트롤러에서 여기 있는 데이터를 까서 쓰고 있지는 않음...
    });
 };
  
 // 메인 테이블 생성
function showSchedule(tableName){
	// html table 객체 생성 
	jQuery("#zv").html('<table id="' + tableName + '" class="display compact" width="100%" cellspacing="0" width="100%" padding-right="10px"></table>');
	
    var columns = new Array(); // 배열 객체 생성
    columns.push({ // 'dataTable'의 'columns'파라미터에 들어갈 값을 셋팅
    	"class": "details-control",
        "orderable": false,
        "data": null,
        "defaultContent": "",
        "width": 50,
        "createdCell": function (td, cellData, rowData, row, col) {
            if (rowData.level > 0) {
                td.className = td.className + ' level-' + rowData[3];
            }
        }
	});
    
    var firstObj = dataSet[0];
    var row = Object.keys(firstObj);
    for (var i = 0; i < row.length; i++) {
    	if(row[i] != "targets" && row[i] != "uniqueId"){
    		if(row[i] === "triggerId" || row[i] === "jobId" ){
	            var model = {
	            	title: row[i], "visible": false // 화면에서 안보이게하는 설정
	            };
	            columns.push(model); //'dataTable'의 'columns'파라미터에 들어갈 값을 셋팅
    		}else{
	            var model = {
		           	title: row[i]
		        };
		        columns.push(model); // 'dataTable'의 'columns'파라미터에 들어갈 값을 셋팅    			
    		}
    	}
    }
    /*
     * // 'dataTable'의 'columns'파라미터에 들어갈 값을 셋팅, 버튼도 추가할 수 있음.
     * */
    columns.push({title:"동작","defaultContent": "<button type='button' name='group' class='btn btn-info btn-sm' id='action'>실행/중지</button>"});
    columns.push({title:"삭제","defaultContent": "<button type='button' name='group' class='btn btn-danger btn-sm' id='delete'>삭제</button>"});
    columns.push({title:"변경","defaultContent": "<button type='button' name='group' class='btn btn-primary btn-sm' id='update'>변경</button>"});
    columns.push({title:"추가","defaultContent": "<button type='button' name='group' class='btn btn-primary btn-sm' id='addTarget'>추가</button>"});
    
    var currData = [];
    for(var i in dataSet){
    	var data = [];
    	for(var r in row){
    		if(row[r] != "targets"){
    			data.push(dataSet[i][row[r]]); // 테이블의 필드안에 들어갈 값 추가 (필드명은 위에서 구성되었음, title: row[i])
    		}
    		if(row[r] === "status"){
    			var status = dataSet[i][row[r]];
    			if(status === "stop"){
    				data.push("<button type='button' name='group' class='btn btn-info btn-sm' id='action'>실행</button>"); 
    			}else if(status === "run"){
    				data.push("<button type='button' name='group' class='btn btn-warning btn-sm' id='action'>중지</button>");
    			}        			
    		}
    	}
    	currData.push(data);
    }
    
    var dataTable = jQuery("#" + tableName); // HTML table객체에 table ID를 통하여 dataTable을 적용한다.
    dataTable.height(window.innerHight * 0.95);
    
	var dt = dataTable.DataTable({
			//"dom": '<"top"flp>rt<"bottom"><"clear">',
			"dom": '<"top">rt<"bottom"flp><"clear">',
   		info:false,
   		"data": currData,
        "columns": columns
	});
	
    var displayed = new Set([]);
    // 하위 테이블을 보여주는 코드
    dataTable.find('tbody').on('click', 'tr td:first-child', function () {
    	var tr = $(this).closest('tr');
        var row = dt.row(tr);
        
        console.info(dt);
        
        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('details');
        }
        else {
            // Open this row
            var data = row.data();
            var id = data[0]; // 에러발생...
            var html  = '<table id="' + id + '" class="display compact" width="100%" cellspacing="0" width="100%" padding-right="10px"></table>';
            row.child(html).show();
            showSubTable(id);
            tr.addClass('details');
        }
    });
    
    // 테이블안의 번튼들에 기능 부여
    dataTable.find('tbody').on('click', 'button[name="group"]', function () {	
        var data = dt.row( $(this).parents('tr') ).data();
        var part = this.id; // 클릭이벤트가 발생한 버튼객체의 id를 가지고 옴
        
        console.info("part :: ", part);
        if(part === 'action'){ // 버튼객체의 id가 'action'인 경우
        	console.info("data[10] :: ", data[10]);
	        if(data[10] === 'stop'){  // 스케줄의 status가 'stop'인 경우
	        	
	        	jQuery.ajax({ // ajax 호출
					type : "GET", // get 타입 전송
					url : "${pageContext.request.contextPath}/start/selectedOne.do", // 전송 url
					dataType : "json", // json 형태
					success : function(msg) { // 콜백 성공 응답시 실행
						if(msg == true){
							msgBox("실행되었습니다.");
							window.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
						}else{
							msgBox("실패했습니다.");
						}							
					},
					error : function(request, status, error) { // Ajax 전송 에러 발생시 실행 
						msgBox("error:" + error);
					},
					data: {uniqueId:data[0]} // 전송 파라미터, data[0]에 담겨있는 스케줄의 uniqueId를 꺼내서 전송.
				});	     
	        }else if(data[10] === 'run'){ // 스케줄의 status가 run'인 경우
	        	jQuery.ajax({ // ajax 호출
					type : "GET", // get 타입 전송
					url : "${pageContext.request.contextPath}/stop/selectedOne.do", // 전송 url 
					dataType : "json", // json 형태
					success : function(msg) { // 콜백 성공 응답시 실행
						if(msg == true){
							msgBox("중지되었습니다.");
							window.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
						}else{
							msgBox("실패했습니다.");
						}							
					},
					error : function(request, status, error) { // Ajax 전송 에러 발생시 실행 
						msgBox("error:" + error);
					},
					data: {uniqueId:data[0]} // 전송파라미터 , data[0]에 담겨있는 스케줄의 uniqueId를 꺼내서 전송.
				});	 
	        } 
        }else if(part === 'delete'){ // 버튼객체의 id가 'delete'인 경우
    		jQuery.ajax({ // ajax 호출
				type : "POST", // post 타입 전송
				url : "${pageContext.request.contextPath}/delete/selectedOne.do", // 전송 url
				dataType : "json", // json 형태
				success : function(msg) { // 콜백 성공 응답시 실행
					if(msg == true){
						msgBox("삭제되었습니다.");
						window.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
					}else{
						msgBox("실패했습니다.");
					}						
				},
				error : function(request, status, error) { // Ajax 전송 에러 발생시 실행
					msgBox("error:" + error);
				},
				data: {uniqueId:data[0]} // 전송파라미터 , data[0]에 담겨있는 스케줄의 uniqueId를 꺼내서 전송. 
			});	     
        }else if(part === 'addTarget'){ // 버튼객체의 id가 'addTarget'인 경우
        	addTarget(data[0], data[2], data[4], data[5]); // uniqueId, scheduleName, groupId, jobId
        }else if(part === 'update'){
        	updateGruop(data);
        };	
    });  
};

// 하위테이블 생성.
function showSubTable(id){    	
	var curDatat = null;
	for(var i in dataSet){
		if(dataSet[i].uniqueId == id){ // dataSet은 공유하고 있음.
			curData  = dataSet[i].targets; // TargetList의 targets를 꺼내서 하위테이블의 데이터로 사용
		}
	}
	
	var columns = new Array();
    var firstObj = curData[0];
    var row = Object.keys(firstObj);
    for (var i = 0; i < row.length; i++) {
    	if(row[i] != "targets"){
    		if(row[i] === "triggerId" || row[i] === "jobId" || row[i] === "targetListId" || row[i] === "targetListName"){
	            var model = {
	            	title: row[i], "visible": false // 화면에서 안보이게하는 설정
	            };
	            columns.push(model);
    		}else{
	            var model = {
	            	title: row[i]
	            };
	            columns.push(model);       			
    		}
    	}
    }
    
    /*
     * // 'dataTable'의 'columns'파라미터에 들어갈 값을 셋팅, 버튼도 추가할 수 있음.
     * */
    columns.push({title:"삭제","defaultContent": "<button type='button' name='target'  class='btn btn-sm' id='deleteTarget'>삭제</button>"});
    columns.push({title:"변경","defaultContent": "<button type='button' name='target'  class='btn btn-sm' id='updateTarget'>변경</button>"});
    
    var dataTable = [];
    for(var i in curData){
    	var data = [];
    	for(var r in row){
    		if(row[r] != "targets"){
    			data.push(curData[i][row[r]]);
    		}
    	}
    	dataTable.push(data);
    }
    
    var tables = jQuery("#" + id); // html객체 생성. 여기에 dataTable을 적용함.
    tables.height(window.innerHight * 0.95);
    
	var dt = tables.DataTable({
			"dom": '<"top">rt<"bottom"><"clear">',
   		info:false,
   		"data": dataTable,
        "columns": columns
	});
	
	console.info("subHtml", jQuery("#" + id).html()); // 로그
	
    // 테이블안의 번튼들에 기능 부여
	tables.find('tbody').on('click', 'button[name="target"]', function () {	
        var data = dt.row( $(this).parents('tr') ).data();
        var part = this.id; // 클릭이벤트가 발생한 버튼객체의 id를 가지고 옴
        
        if(part === 'deleteTarget'){;
        	jQuery.ajax({ // ajax 호출
				type : "POST", // post 타입 전송
				url : "${pageContext.request.contextPath}/deleteStep/selectedOne.do", // 전송 url
				dataType : "json", // json 형태
				success : function(msg) { // 콜백 성공 응답시 실행
					if(msg == true){
						msgBox("삭제되었습니다.");
						window.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
					}else{
						msgBox("실패했습니다.");
					}						
				},
				error : function(request, status, error) { // Ajax 전송 에러 발생시 실행
					msgBox("error:" + error);
				},
				data: {
					targetListId:data[0],
					stepId:data[4]			
				} // 전송파라미터 , data[0]에 담겨있는 스케줄의 uniqueId를 꺼내서 전송. 
			});	     	        	

        }else if(part === 'updateTarget'){
        	updateTarget(data);
        }
        
    });
	   	
};     

/**
 * 그룹추가, TargetList의 기본정보 추가
 * zScheduler.html의 '그룹 추가' 버튼을 눌렀을 경우 실행.
 * */
function addGroup(){
	//jsPanel에 사용할 'toolbarFooter' 생성
	var arr = [
	     {	// 저장 버튼과 그 기능에 해당하는 코드
            item: '<button type="button" class="btn btn-info pull-right">저장</button>',
            event: "click",
            btnclass: "btn-sm",
            
            callback: function(event) { // 저장버튼에 click 이벤트가 발생할 경우 아래의 코드 실행
            	
             	var iframe = document.getElementById('if_addGroup');
				var innerDoc = (iframe.contentDocument) ? iframe.contentDocument : iframe.contentWindow.document;
				var ulObj = innerDoc.getElementById("schedulerForm");
				
				/*
				 * .serializeArray() 함수는 W3C 의 표준 규칙인 successful controls 을 따릅니다. 요소는 disabled이 아니어야 하고 반드시 name 속성을 지녀야 합니다. type='file' 요소는 포함되지 않습니다.
				 * 폼 요소를 names와 values 배열로 인코딩합니다.
				 * .serializeArray() 함수는 JSON 문자열 형태로 JavaScript 배열 객체를 만듭니다.					 * 
				 * */
				var formData = jQuery(ulObj).serializeArray(); // form에서 데이터를 가지고 옴

				// ...iframe내의 ajax form 벨리데이션 방법을 모르겠으므로 이렇게 수작업으로 함...향후에는 일반적인 방법으로 수정해야함...
				var validation = true;
				if(formData[0].value.length <1) validation = false;
				if(formData[1].value.length <1) validation = false;
				if(formData[2].value.length <1) validation = false;
				if(formData[3].value.length <1) validation = false;
				 
				if(validation){ // validation에 통과(true)할 경우 아래 코드 실행
					
					// data객체 생성. formData에서 데이터를 꺼내서 data객체의 변수에 대입.
					var data = {
					  "scheduleName": formData[0].value,
					  "cycleCode": formData[1].value,
					  "date" : formData[2].value,
					  "cycleTime": formData[3].value,					  
					  "useYN": "yes",
					  "status": "stop"
					}; 
					
					var sendInfo = {
						/*
						 * JSON.parse : string 개체를 json 개체로 변환
						 * JSON.stringfy : json 개체를  string 개체로 변환
						 * 'JSON'이라는 개체는 jquery에서 만들어주는 개체가 아니라 브라우저에서 지원해주는 개체 
						 * (IE8+, Firefox 3.1+, Chrome, Opera 10, Safari 4)
						 * */
						content : JSON.stringify(data) // ajax로 데이터를 전송하기 위해
					};
				
					jQuery.ajax({ // ajax 사용
						type : "POST",  // post 타입 전송
						url : "${pageContext.request.contextPath}/schedule/addGroup.do", // 전송 url
						dataType : "json",
						success : function(msg) { // 콜백 성공 응답시 실행
							if(msg == true){
								msgBox("저장되었습니다.");
								document.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
							}else{
								msgBox("실패했습니다.");
							}							
						},
						error : function(request, status, error) { // Ajax 전송 에러 발생시 실행
							msgBox("error:" + error);
						},
						// 전송 파라미터
						data: sendInfo // 서버로는 sendInfo안에 있는 content가 날아 가는데... ? 'sendInfo' 안에 있는 변수들이 서버로 날아간다? 복수개의 변수가 필요할 수 잇으므로 sendInfo로 묶는다?
					});
				}else{
					msgBox("모든 항목에 값을 넣으세요...");
				}
            }
        },            
        {
        	// 취소 버튼과 그 기능에 해당하는 코드
            item: '<button type="button" id="btn_cancle" class="btn btn-default">취소</button>',
            event: "click",
            btnclass: "btn-sm",
            callback: function(event) {
                event.data.close()
            }
        }];
		
		/*
		 * jsPanel : a jQuery plugin to create multifunctional floating panels
		 * */
        jQuery.jsPanel({
            controls: {
                buttons: 'none'
            },
            toolbarFooter: arr, // ★★★★★★★★★★★
            paneltype: {
                type: 'modal',
                mode: 'default'
            },
            size: {
                width: 500,
                height: window.innerHeight * 0.8
            },
            theme: 'medium',
            title: "그룹관리",
            iframe: {
            	id : "if_addGroup",
            	src : "${pageContext.request.contextPath}/zScheduler/groupEdit.jsp?uniqueId=_New",
                style: {
                    "display": "none",
                    "border": "10px solid transparent"
                },
                width: '100%',
                height: '100%'
            },
            callback: function(panel) {
                jQuery("iframe", panel).load(function(e) {
                    jQuery(e.target).fadeIn(200);
                });
            }
        });  
};

function updateGruop(targetInfo){
	//jsPanel에 사용할 'toolbarFooter' 생성
	var arr = [
	     {	// 저장 버튼과 그 기능에 해당하는 코드
            item: '<button type="button" class="btn btn-info pull-right">변경</button>',
            event: "click",
            btnclass: "btn-sm",
            
            callback: function(event) { // 저장버튼에 click 이벤트가 발생할 경우 아래의 코드 실행
            	
             	var iframe = document.getElementById('if_addGroup');
				var innerDoc = (iframe.contentDocument) ? iframe.contentDocument : iframe.contentWindow.document;
				var ulObj = innerDoc.getElementById("schedulerForm");
				
				/*
				 * .serializeArray() 함수는 W3C 의 표준 규칙인 successful controls 을 따릅니다. 요소는 disabled이 아니어야 하고 반드시 name 속성을 지녀야 합니다. type='file' 요소는 포함되지 않습니다.
				 * 폼 요소를 names와 values 배열로 인코딩합니다.
				 * .serializeArray() 함수는 JSON 문자열 형태로 JavaScript 배열 객체를 만듭니다.					 * 
				 * */
				var formData = jQuery(ulObj).serializeArray(); // form에서 데이터를 가지고 옴

				// ...iframe내의 ajax form 벨리데이션 방법을 모르겠으므로 이렇게 수작업으로 함...향후에는 일반적인 방법으로 수정해야함...
				var validation = true;
				if(formData[0].value.length <1) validation = false;
				if(formData[1].value.length <1) validation = false;
				if(formData[2].value.length <1) validation = false;
				if(formData[3].value.length <1) validation = false;
				 
				if(validation){ // validation에 통과(true)할 경우 아래 코드 실행
					
					// data객체 생성. formData에서 데이터를 꺼내서 data객체의 변수에 대입.
					var data = {
					  "scheduleName": formData[0].value,
					  "cycleCode": formData[1].value,
					  "date" : formData[2].value,
					  "cycleTime": formData[3].value,					  
					  "uniqueId" : formData[4].value
					}; 
					
					var sendInfo = {
						/*
						 * JSON.parse : string 개체를 json 개체로 변환
						 * JSON.stringfy : json 개체를  string 개체로 변환
						 * 'JSON'이라는 개체는 jquery에서 만들어주는 개체가 아니라 브라우저에서 지원해주는 개체 
						 * (IE8+, Firefox 3.1+, Chrome, Opera 10, Safari 4)
						 * */
						content : JSON.stringify(data) // ajax로 데이터를 전송하기 위해
					};
				
					jQuery.ajax({ // ajax 사용
						type : "POST",  // post 타입 전송
						url : "${pageContext.request.contextPath}/schedule/updateGroup.do", // 전송 url
						dataType : "json",
						success : function(msg) { // 콜백 성공 응답시 실행
							if(msg == true){
								msgBox("변경되었습니다.");
								document.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
							}else{
								msgBox("실패했습니다.");
							}							
						},
						error : function(request, status, error) { // Ajax 전송 에러 발생시 실행
							msgBox("error:" + error);
						},
						// 전송 파라미터
						data: sendInfo // 서버로는 sendInfo안에 있는 content가 날아 가는데... ? 'sendInfo' 안에 있는 변수들이 서버로 날아간다? 복수개의 변수가 필요할 수 잇으므로 sendInfo로 묶는다?
					});
				}else{
					msgBox("모든 항목에 값을 넣으세요...");
				}
            }
        },           
        {
        	// 취소 버튼과 그 기능에 해당하는 코드
            item: '<button type="button" id="btn_cancle" class="btn btn-default">취소</button>',
            event: "click",
            btnclass: "btn-sm",
            callback: function(event) {
                event.data.close()
            }
        }];   	
	
    jQuery.jsPanel({
    	content : "<p style='padding:10px;'>Just a piece of sample text ...</p>",
    	controls: {
            buttons: 'none'
        },
        toolbarFooter: arr, // ★★★★★★★★★★★
        paneltype: {
            type: 'modal',
            mode: 'default'
        },
        size: {
            width: 500,
            height: window.innerHeight * 0.8
        },
        theme: 'medium',
        title: "그룹관리",
        iframe: {
        	id : "if_addGroup",
        	src : encodeURI("${pageContext.request.contextPath}/zScheduler/groupUpdate.jsp?targetInfo=" + targetInfo),
        	contentType: "application/x-www-form-urlencoded; charset=UTF-8",  
            style: {
                "display": "none",
                "border": "10px solid transparent"
            },
            content : 'asdasdasdasdas',
            width: '100%',
            height: '100%'
        },
        callback: function(panel) {
            jQuery("iframe", panel).load(function(e) {
                jQuery(e.target).fadeIn(200);
            });
        }
    });  
}

/*
 * Tartget정보(step) 추가. 실직적으로 스케줄러가 실행하는 프로그램, URL 등...에 해당하는 정보.
 * 화면에서 '추가'버튼을 누르면 동작하는 함수
 * '추가'버튼을 생성하는 코드는 본 JS파일안에 'function showSchedule(tableName)'안에 있음
 * */
function addTarget(uniqueId, scheduleName, groupId, jobId){
	//jsPanel에 사용할 'toolbarFooter' 생성
	var arr = [
	     {
	    	// 저장 버튼과 그 기능에 해당하는 코드.
            item: '<button type="button" class="btn btn-info pull-right">저장</button>',
            event: "click",
            btnclass: "btn-sm",
            
            callback: function(event) { // 저장버튼에 click 이벤트가 발생할 경우 아래의 코드 실행
            	
            	var iframe = document.getElementById('if_addTarget');
				var innerDoc = (iframe.contentDocument) ? iframe.contentDocument : iframe.contentWindow.document;
				var ulObj = innerDoc.getElementById("schedulerForm"); // schedulerForm이라는 이름은 바꿀 필요가 있을듯... 
				
				// form 데이터를 배열로 변환
				var formData = jQuery(ulObj).serializeArray();
				
				// iframe내의 ajax form 벨리데이션 방법을 모르겠으므로 이렇게 수작업으로 함...향후에는 일반적인 방법으로 수정해야함...
				var validation = true;
				if(formData[0].value.length <1) validation = false;
				if(formData[1].value.length <1) validation = false;
				
				var data = {
				  "targetListId": uniqueId,
				  "targetListName": scheduleName,
			      "groupId": groupId,
			      "jobId": jobId,
			      "pType" : formData[0].value, // command 또는 http
			      "progPath": formData[1].value, // 프로그램 경로
			      "parameter": formData[2].value, // 프로그램에 필요한 파라미터
			      "method": formData[3].value // http일경우 get이냐 post이냐				       
				};
				
				var sendInfo = {
					// json 형태의 데이터를 String 데이터로 변환.
					//content : encodeURIComponent(JSON.stringify(data))
					content : JSON.stringify(data)
				};
				
				if(validation){
					jQuery.ajax({ // ajax 사용
						type : "POST", // post 타입 전송
						url : "${pageContext.request.contextPath}/schedule/addTarget.do", // 전송 url
						dataType : "json",
						success : function(msg) { // 콜백 성공 응답시 실행
							if(msg == true){
								msgBox("저장되었습니다.");
								document.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
							}else{
								msgBox("실패했습니다.");
							}								
						},
						error : function(request, status, error) { // Ajax 전송 에러 발생시 실행
							msgBox("error:" + error);							
						},
						data: sendInfo // 전송 파라미터 서버에서 content 벨류를 꺼내서 씀
					});
				}else{
					msgBox("모든 항목에 값을 넣으세요...");
				}
            }
        },
      
        {
        	// 취소 버튼과 그 기능에 해당하는 코드 
            item: '<button type="button" id="btn_cancle" class="btn btn-default">취소</button>',
            event: "click",
            btnclass: "btn-sm",
            callback: function(event) {
                event.data.close()
            }
        }];

		/*
		 * jsPanel : a jQuery plugin to create multifunctional floating panels
		 * */
        jQuery.jsPanel({
            controls: {
                buttons: 'none'
            },
            toolbarFooter: arr, // ★★★★★★★★★★★
            paneltype: {
                type: 'modal',
                mode: 'default'
            },
            size: {
            	width: 600, //참조) width: window.innerWidth * 0.5,
            	height: window.innerHeight * 0.8
            },
            theme: 'medium',
            title: "task관리",
            iframe: {
            	id : "if_addTarget",
            	src : "${pageContext.request.contextPath}/zScheduler/targetEdit.jsp?uniqueId=_New",
                style: {
                    "display": "none",
                    "border": "10px solid transparent"
                },
                width: '100%',
                height: '100%'
            },
            callback: function(panel) {
                jQuery("iframe", panel).load(function(e) {
                    jQuery(e.target).fadeIn(200);
                });
            }
        });
};

function updateTarget(stepInfo){
	//jsPanel에 사용할 'toolbarFooter' 생성
	var arr = [
	     {
	    	// 저장 버튼과 그 기능에 해당하는 코드.
            item: '<button type="button" class="btn btn-info pull-right">변경</button>',
            event: "click",
            btnclass: "btn-sm",
            
            callback: function(event) { // 저장버튼에 click 이벤트가 발생할 경우 아래의 코드 실행
            	
            	var iframe = document.getElementById('if_addTarget');
				var innerDoc = (iframe.contentDocument) ? iframe.contentDocument : iframe.contentWindow.document;
				var ulObj = innerDoc.getElementById("schedulerForm"); // schedulerForm이라는 이름은 바꿀 필요가 있을듯... 
				
				// form 데이터를 배열로 변환
				var formData = jQuery(ulObj).serializeArray();
				
				// iframe내의 ajax form 벨리데이션 방법을 모르겠으므로 이렇게 수작업으로 함...향후에는 일반적인 방법으로 수정해야함...
				var validation = true;
				if(formData[0].value.length <1) validation = false;
				if(formData[1].value.length <1) validation = false;
				
				var data = {
			      "pType" : formData[0].value, // command 또는 http
			      "progPath": formData[1].value, // 프로그램 경로
			      "parameter": formData[2].value, // 프로그램에 필요한 파라미터
			      "method": formData[3].value, // http일경우 get이냐 post이냐
			      "targetListId" : formData[4].value,
			      "targetListName" : formData[5].value,
			      "groupId" : formData[6].value,
			      "jobId" : formData[7].value,			      
			      "stepId" : formData[8].value
				};
				
				var sendInfo = {
					// json 형태의 데이터를 String 데이터로 변환.
					//content : encodeURIComponent(JSON.stringify(data))
					content : JSON.stringify(data)
				};
				
				if(validation){
					jQuery.ajax({ // ajax 사용
						type : "POST", // post 타입 전송
						url : "${pageContext.request.contextPath}/schedule/updateTarget.do", // 전송 url
						dataType : "json",
						success : function(msg) { // 콜백 성공 응답시 실행
							if(msg == true){
								msgBox("저장되었습니다.");
								document.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
							}else{
								msgBox("실패했습니다.");
							}								
						},
						error : function(request, status, error) { // Ajax 전송 에러 발생시 실행
							msgBox("error:" + error);							
						},
						data: sendInfo // 전송 파라미터 서버에서 content 벨류를 꺼내서 씀
					});
				}else{
					msgBox("모든 항목에 값을 넣으세요...");
				}
            }
        },
      
        {
        	// 취소 버튼과 그 기능에 해당하는 코드 
            item: '<button type="button" id="btn_cancle" class="btn btn-default">취소</button>',
            event: "click",
            btnclass: "btn-sm",
            callback: function(event) {
                event.data.close()
            }
        }];

		/*
		 * jsPanel : a jQuery plugin to create multifunctional floating panels
		 * */
        jQuery.jsPanel({
            controls: {
                buttons: 'none'
            },
            toolbarFooter: arr, // ★★★★★★★★★★★
            paneltype: {
                type: 'modal',
                mode: 'default'
            },
            size: {
            	width: 600, //참조) width: window.innerWidth * 0.5,
            	height: 300 //참조) height: window.innerHeight * 0.5
            },
            theme: 'medium',
            title: "task관리",
            iframe: {
            	id : "if_addTarget",
            	src : encodeURI("${pageContext.request.contextPath}/zScheduler/targetUpdate.jsp?stepInfo=" + stepInfo),
                style: {
                    "display": "none",
                    "border": "10px solid transparent"
                },
                width: '100%',
                height: '100%'
            },
            callback: function(panel) {
                jQuery("iframe", panel).load(function(e) {
                    jQuery(e.target).fadeIn(200);
                });
            }
        });
};    


/*
 * - 스케줄정보를 일괄로 편집하는 용도로 사용하기 위해
 * - 스케줄링 정보들을 하나의 텍스트 파일로 만들어서 서버에 저장 
 * */
function doExport(){

	var arr = [
    {
    	// 취소 버튼과 그 기능에 해당하는 코드 
        item: '<button type="button" id="btn_cancle" class="btn btn-default">취소</button>',
        event: "click",
        btnclass: "btn-sm",
        callback: function(event) {
            event.data.close()
        }
    }];

	/*
	 * jsPanel : a jQuery plugin to create multifunctional floating panels
	 * */
    jQuery.jsPanel({
        controls: {
            buttons: 'none'
        },
        toolbarFooter: arr, // ★★★★★★★★★★★
        paneltype: {
            type: 'modal',
            mode: 'default'
        },
        size: {
        	width: window.innerWidth * 0.8,
        	height: window.innerHeight * 0.8
        },
        theme: 'medium',
        title: "Export/Import",
        iframe: {
        	id : "if_addTarget",
        	src : "${pageContext.request.contextPath}/zScheduler/zScheduleManager.jsp",
            style: {
                "display": "none",
                "border": "10px solid transparent"
            },
            width: '100%',
            height: '100%'
        },
        callback: function(panel) {
            jQuery("iframe", panel).load(function(e) {
                jQuery(e.target).fadeIn(200);
            });
        }
    });			   		
};

function addProcess(){
	msgBox("프로세스 선택해서 추가");	    	
}

function reLoad(){
    jQuery.ajax({ // ajax 사용
        type: "POST", // post 타입 전송
        url: "${pageContext.request.contextPath}/schedule/reLoad.do",
        dataType: "json", // json타입 데이터 전송...
        success: function(msg) { // 콜백 성공 응답시 실행
			if(msg == true){
				document.location.reload(); // window객체의 location프로퍼티의 reload()함수 : 현재 표시된 페이지를 다시불러오는데 사용.
				//$("<div>"+  "Reload성공."+ "</div>").dialog();
				msgBox("Reload성공.", "test");
			}else{
				msgBox("Reload실패.");
				//$("<div>"+  "Reload실패."+ "</div>").dialog();
			}		
        },
        error: function(request, status, error) { // Ajax 전송 에러 발생시 실행.
            msgBox("request:" + request.responseText + "\n error:" + error);
        },
    });
}

function msgBox(text){
	
	alert(text);
	
	$("<div>"+  text + "</div>").avgrund({
			width: 380, // max is 640px
			height: 280, // max is 350px
			showClose: false, // switch to 'true' for enabling close button
			showCloseText: '', // type your text for close button
			closeByEscape: true, // enables closing popup by 'Esc'..
			closeByDocument: true, // ..and by clicking document itself
			holderClass: '', // lets you name custom class for popin holder..
			overlayClass: '', // ..and overlay block
			enableStackAnimation: false, // another animation type
			onBlurContainer: '', // enables blur filter for specified block
			openOnEvent: true, // set to 'false' to init on load
			setEvent: 'click', // use your event like 'mouseover', 'touchmove', etc.
//			onLoad: function (elem) { ... }, // set custom call before popin is inited..
//			onUnload: function (elem) { ... }, // ..and after it was closed
			template: 'Your content goes here..' // or function (elem) { ... }
		});
	
	//.easyconfirm(
//	$("<div>"+  text + "</div>").dialog({    
//			modal: true,
//		    draggable: false,
//		    resizable: false,
//		    position: ['center', 'top'],
//		    show: 'blind',
//		    hide: 'blind',
//		    width: 400,
//		    dialogClass: 'ui-dialog-osx',
//		    buttons: {
//		        "I've read and understand this": function() {
//		            $(this).dialog("close");
//		        }
//		    }
//	});
//	$("<div>"+  text + "</div>").dialog({
//	    modal: true,
//	    buttons: {
//	        "Yes": function () { 
//	            callbackFunctionTrue();
//	            $(this).dialog('close'); 
//	         },
//	        "No": function () { 
//	            callbackFunctionFalse();
//	            $(this).dialog('close'); 
//	         }
//	    },
//	    close: function(event, ui){
//	       alert("something");
//	    }
//	});
	
//	count = 0;
//	$("<div>"+  text + "</div>").dialog({
//	     resizable: false,
//	      height:140,
//	      modal: true,
//	      buttons: {
//	        "Delete all items": function() {
//	          $( this ).dialog( "close" );
//	        },
//	        Cancel: function() {
//	          $( this ).dialog( "close" );
//	        }
//	      }
//	});
//	function proceed(){
//	    if (count != 10){
//	        $("#dialog-confirm").dialog('open');    
//	    }else{
//	        $('body').append("<div>Yes was selected " + count + " times!</div>");
//	    }
//	}    	
}

</script>
</html>