<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ZettaSoft sheetDemo</title>
<link href="/zPortal/plugin/jspanel/jquery.jspanel.min.css" rel="stylesheet">
<link href="/zPortal/assets/css/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
<LINK type="text/css" rel="stylesheet" href="/zPortal/assets/css/jquery-ui.css" />

<script src="${pageContext.request.contextPath}/assets/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/DataTables/js/dataTables.jqueryui.js"></script>
<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/assets/js/mobile-detect.min.js"></SCRIPT>
<script src="${pageContext.request.contextPath}/assets/js/jquery.ui.touch-punch.min.js"></script>

<script src="${pageContext.request.contextPath}/plugin/jspanel/jquery.jspanel.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/zetta/zSettings.js"></script>

<link rel="stylesheet" href="/zPortal/assets/css/bootstrap/bootstrap.min.css">

<script src="${pageContext.request.contextPath}/assets/js/js-xlsx/shim.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/js-xlsx/jszip.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/js-xlsx/xlsx.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/js-xlsx/ods.js"></script>


<link rel="stylesheet" href="/zPortal/assets/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/zPortal/plugin/datatables/DataTables/css/dataTables.bootstrap.min.css">
<link rel="stylesheet" href="/zPortal/plugin/datatables/DataTables/css/dataTables.jqueryui.min.css">
<link rel="stylesheet" href="/zPortal/plugin/datatables/Buttons/css/buttons.dataTables.min.css">    

<script src="${pageContext.request.contextPath}/plugin/datatables/DataTables/js/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/bootstrap/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/DataTables/js/dataTables.bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/Buttons/js/dataTables.buttons.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/JSZip/jszip.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/pdfmake/pdfmake.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/pdfmake/vfs_fonts.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/Buttons/js/buttons.html5.min.js"></script>
<script src="${pageContext.request.contextPath}/plugin/datatables/Buttons/js/buttons.print.js"></script></head>

<style>
#drop{
	border:2px dashed #bbb;
	-moz-border-radius:5px;
	-webkit-border-radius:5px;
	border-radius:5px;
	padding:25px;
	text-align:center;
	font:20pt bold,"Vollkorn";
	color:#bbb;
	width:98%;
}

#text {
	border: 1px dashed #bbb;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	padding: 25px;
	font: 10pt bold, "Vollkorn";
	color: #bbb;
	width: 98%;
}

#b64data{
	width:100%;
}
</style>
</head>
<body>
	<div>
		<div id="drop">XLSX / XLS / XML 파일을 끌어다 놓으세요.</div>
		<textarea id="text" placeholder="layout json "></textarea>
		<input id="title"></input>
		<button onclick="javascript:addProcess();" id="comma">process실행</button>
		<button onclick="javascript:textToJson();" id="comma">JSON</button>
		<button id="saveBtn">저장</button>
		<button id="loadBtn">로드</button>
	</div>
	
	<div id="zv"></div>
</body>


	<script>

jQuery("#saveBtn").on("click", function() {
	saveServer();
});

jQuery("#loadBtn").on("click", function() {
	loadServer();
});

var tableData = [];
var scheduleInfos = null;

var X = XLSX;
var XW = {
	/* worker message */
	msg: 'xlsx',
	/* worker scripts */
	rABS: '/zPortal/assets/js/js-xlsx/xlsxworker2.js',
	norABS: '/zPortal/assets/js/js-xlsx/xlsxworker1.js',
	noxfer: '/zPortal/assets/js/js-xlsx//xlsxworker.js'
};

var rABS = typeof FileReader !== "undefined" && typeof FileReader.prototype !== "undefined" && typeof FileReader.prototype.readAsBinaryString !== "undefined";
var use_worker = typeof Worker !== 'undefined';
var transferable = use_worker;

var wtf_mode = false;

function fixdata(data) {
	var o = "", l = 0, w = 10240;
	for(; l<data.byteLength/w; ++l) o+=String.fromCharCode.apply(null,new Uint8Array(data.slice(l*w,l*w+w)));
	o+=String.fromCharCode.apply(null, new Uint8Array(data.slice(l*w)));
	return o;
}

function ab2str(data) {
	var o = "", l = 0, w = 10240;
	for(; l<data.byteLength/w; ++l) o+=String.fromCharCode.apply(null,new Uint16Array(data.slice(l*w,l*w+w)));
	o+=String.fromCharCode.apply(null, new Uint16Array(data.slice(l*w)));
	return o;
}

function s2ab(s) {
	var b = new ArrayBuffer(s.length*2), v = new Uint16Array(b);
	for (var i=0; i != s.length; ++i) v[i] = s.charCodeAt(i);
	return [v, b];
}

function xw_noxfer(data, cb) {
	var worker = new Worker(XW.noxfer);
	worker.onmessage = function(e) {
		switch(e.data.t) {
			case 'ready': break;
			case 'e': console.error(e.data.d); break;
			case XW.msg: cb(JSON.parse(e.data.d)); break;
		}
	};
	var arr = rABS ? data : btoa(fixdata(data));
	worker.postMessage({d:arr,b:rABS});
}

function xw_xfer(data, cb) {
	var worker = new Worker(rABS ? XW.rABS : XW.norABS);
	worker.onmessage = function(e) {
		switch(e.data.t) {
			case 'ready': break;
			case 'e': console.error(e.data.d); break;
			default: xx=ab2str(e.data).replace(/\n/g,"\\n").replace(/\r/g,"\\r"); console.log("done"); cb(JSON.parse(xx)); break;
		}
	};
	if(rABS) {
		var val = s2ab(data);
		worker.postMessage(val[1], [val[1]]);
	} else {
		worker.postMessage(data, [data]);
	}
}

function xw(data, cb) {
	//transferable = document.getElementsByName("xferable")[0].checked;
	transferable = false;
	if(transferable) xw_xfer(data, cb);
	else xw_noxfer(data, cb);
}

function get_radio_value( radioName ) {
	var radios = document.getElementsByName( radioName );
	for( var i = 0; i < radios.length; i++ ) {
		if( radios[i].checked || radios.length === 1 ) {
			return radios[i].value;
		}
	}
}

function to_json(workbook) {
	var result = [];
	workbook.SheetNames.forEach(function(sheetName) {
		var roa = X.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
		if(roa.length > 0){
			result.push({tableName:sheetName, data:convertSimpleJson(roa)});
		}
	});
	return result;
}

function to_simple_json(workbook) {
	var result = [];
	workbook.SheetNames.forEach(function(sheetName) {
		var roa = X.utils.sheet_to_simple_json(workbook.Sheets[sheetName]);
		//top.zPortal.log("info","roa",roa);
		if(roa.length > 0){
			var obj = JSON.parse("[" + roa + "]");
			var output = {SHEET: sheetName, data:obj};
			result.push(output);
		}
	});
	return result;
}

function to_csv(workbook) {
	var result = [];
	workbook.SheetNames.forEach(function(sheetName) {
		var csv = X.utils.sheet_to_csv(workbook.Sheets[sheetName]);
		if(csv.length > 0){
			result.push("SHEET: " + sheetName);
			result.push("");
			result.push(csv);
		}
	});
	return result.join("\n");
}

function to_formulae(workbook) {
	var result = [];
	workbook.SheetNames.forEach(function(sheetName) {
		var formulae = X.utils.get_formulae(workbook.Sheets[sheetName]);
		if(formulae.length > 0){
			result.push("SHEET: " + sheetName);
			result.push("");
			result.push(formulae.join("\n"));
		}
	});
	return result.join("\n");
}

var tarea = document.getElementById('b64data');
function b64it() {
	if(typeof console !== 'undefined') console.log("onload", new Date());
	var wb = X.read(tarea.value, {type: 'base64',WTF:wtf_mode});
	process_wb(wb);
}

function process_wb(wb) {
	var output = "";
// 	switch(get_radio_value("format")) {
// 		case "json":
// 			output = JSON.stringify(to_json(wb), 2, 2);
// 			break;
// 		case "form":
// 			output = to_formulae(wb);
// 			break;
// 		case "simpleJson":
// 			output = to_simple_json(wb);
// 			break;
// 		default:
// 		output = to_csv(wb);
// 	}
	//output = JSON.stringify(to_simple_json(wb), 2, 2);
	tableData = to_json(wb);
// 	if(out.innerText === undefined) out.textContent = output;
// 	else out.innerText = output;
// 	if(typeof console !== 'undefined') console.log("output", new Date());
	jQuery("#zv").empty();
	showTabs(tableData);
}

var drop = document.getElementById('drop');
var dropFileName = "";
function handleDrop(e) {
	
	processShow("ë¶ìì¤ìëë¤.", 3000);
	
	e.stopPropagation();
	e.preventDefault();
	rABS = false;
	use_worker = true;
	var files = e.dataTransfer.files;
	var f = files[0];
	{
		var reader = new FileReader();
		var name = f.name;
		dropFileName = name;
		reader.onload = function(e) {
			if(typeof console !== 'undefined') console.log("onload", new Date(), rABS, use_worker);
			var data = e.target.result;
			if(use_worker) {
				xw(data, process_wb);
			} else {
				var wb;
				if(rABS) {
					wb = X.read(data, {type: 'binary'});
				} else {
				var arr = fixdata(data);
					wb = X.read(btoa(arr), {type: 'base64'});
				}
				process_wb(wb);
			}
		};
		if(rABS) reader.readAsBinaryString(f);
		else reader.readAsArrayBuffer(f);
	}
}

function handleDragover(e) {
	e.stopPropagation();
	e.preventDefault();
	e.dataTransfer.dropEffect = 'copy';
}

if(drop.addEventListener) {
	drop.addEventListener('dragenter', handleDragover, false);
	drop.addEventListener('dragover', handleDragover, false);
	drop.addEventListener('drop', handleDrop, false);
}


function convertSimpleJson(obj){
	if(obj.length == 0) return null;
	
	//top.zPortal.log("info","result", JSON.stringify(obj));
	var result = new Array();
	var keys = Object.keys(obj[0]);
	result.push(keys);
	for(var index in obj){
		var row = [];
		for(var key in obj[index]){
			row.push(obj[index][key].trim());
		}
		result.push(row);
	}
	
	//top.zPortal.log("info","result", JSON.stringify(result));
	return result;
}

function saveServer(){
	makeInfos();
	
	if(tableData.length <= 0){
		alert("ì ì¥í  ë´ì©ì´ ììµëë¤.");
		return;
	}	
	
	for(var i in scheduleInfos){
		jQuery.ajax({
			type : "POST",
			url : "/zPortal/modelManager/save.do",
			dataType : "json",
			success : function(msg) {
				//if(i == scheduleInfos.length -1) alert("ì ì¥ëììµëë¤."); // ìì í´ì¼í¨ forë¬¸ ëëë§í¼ alertì´ ë°ìí¨...
			},
			error : function(request, status, error) {
				alert("request:" + request.responseText + "\n error:" + error);
			},
			data : {
				fileName : "zPublisher\\data",
				objectKey : scheduleInfos[i].uniqueId,
				content : JSON.stringify(scheduleInfos[i])
			}
		});
   	}
	alert("ì ì¥ëììµëë¤."); // ììê» ìì ëë©´ ì­ì ...
}

function loadServer(){
	
	jQuery.ajax({
		type : "GET",
		url : "${pageContext.request.contextPath}/data/doExport.do",
		dataType : "json",
		success : function(msg) {
			scheduleInfos = msg;
			
			makeTable();
			jQuery("#zv").empty();
			showTabs(tableData);
		},
		error : function(request, status, error) {
			alert("request:" + request.responseText + "\n error:" + error);
		}
	});
}


function showTabs(tableData){	
	var tab = '<div><ul class="nav nav-tabs" role="tablist"></ul><div class="tab-content"></div></div>';
    jQuery("#zv").append(tab);
    for (var index = 0 ; index < tableData.length ; index++) {
        var table = tableData[index];
        top.zPortal.log("info",table.tableName);
        addTab(table.data, table.tableName, index);
    }
}

function addTab(dataSet, tableName, index){
    	var li = '';
    	var contentHtml = '';
    	if(index == 0){
    		li = '<li class="active"><a href="#' + tableName + '" data-toggle="tab">' + tableName + '</a></li>';
    		contentHtml = '<div class="tab-pane active" id="' + tableName + '"><table class="table table-striped table-bordered" width="100%" cellspacing="0" width="100%" id="table_' + index + '" ></table></div>';
    	}else{
    		li = '<li><a href="#' + tableName + '" data-toggle="tab">' + tableName + '</a></li>';
    		contentHtml = '<div class="tab-pane" id="' + tableName + '"><table class="table table-striped table-bordered" width="100%" cellspacing="0" width="100%" id="table_' + index + '" ></table></div>';
    	}
        jQuery(".nav-tabs").append(li);
        jQuery(".tab-content").append(contentHtml);
        
        var dataTable = dataSet.slice(0);
        
    	var row = dataTable[0];
        var columns = new Array();

        for (var i = 0; i < row.length; i++) {
            var model = {
            	title: row[i]
            };
            columns.push(model);
        }
        dataTable.splice(0, 1);

        var tables = jQuery("#table_" + index );
        tables.height(window.innerHight * 0.95);
        tables.DataTable( {
            data: dataTable,
            columns: columns,
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'collection',
                    text: 'ë¤ì´ë¡ë',
                    autoClose: true,
                    buttons: [
                        'copy',                              
						{
			                extend: 'csv',
			                title: tableName
			            },
			            {
			                extend: 'excel',
			                title: tableName
			            },
			            {
			                extend: 'pdf',
			                title: tableName
			            },
			            'print'
                    ]
                }
            ]
        });
    }
    
    
    function addProcess(){
    	
    	makeInfos();
    	
	 	var processId = jQuery("#title").val();
		
	 	if(processId.length <= 0) {
	 		alert("process idë¥¼ ìë ¥íì¸ì");
	 		return;
	 	}
	 	
	 	jQuery.ajax({
			type : "POST",
			url : "/zPortal/modelManager/load.do",
			dataType : "json",
			success : function(msg) {
				var main = msg;
				if(main[0] == "notFound"){
					alert("ìëª»ë process idìëë¤.");
	 				return;
				}
				
// 				try{
					
					var someDate = new Date();
					var dateFormated = someDate.toISOString().substr(0,10);

					var d = new Date;
					var dformat = d.getFullYear() + "-" + 
						    ("00" + (d.getMonth() + 1)).slice(-2) + "-" + 
						    ("00" + d.getDate()).slice(-2) + " " + 
						    ("00" + d.getHours()).slice(-2) + ":" + 
						    ("00" + d.getMinutes()).slice(-2) + ":" + 
						    ("00" + d.getSeconds()).slice(-2);
					var cformat = d.getFullYear() + "" + 
						    ("00" + d.getDate()).slice(-2) + "" + 
						    ("00" + (d.getMonth() + 1)).slice(-2) +
						    ("00" + d.getHours()).slice(-2) + "" + 
						    ("00" + d.getMinutes()).slice(-2);
					var cDate = ("00" + (d.getMonth() + 1)).slice(-2) + "/" +
						    ("00" + d.getDate()).slice(-2) + "/" + 
						    d.getFullYear();
					
					var node = top.zPortal.page.fullMenu.findNode(top.zPortal.page.fullMenu, processId);
								
					var group = { "targets": [],
						"uniqueId": dateFormated + "_" + processId,
						"createdAt": dformat,
						"scheduleName": node.data.text,
						"triggerId": "GRP_" + processId,
						"groupId": "GRP_" + processId,
						"jobId": "JOB_" + processId,
						"cycleCode": "daily",
						"cycleTime": cformat,
						"date": cDate,
						"useYN": "yes",
						"status": "stop"
					};
					
					//mainìì toê° ìë ê°ì²´ë¥¼ ì»ì´ë¸ë¤.
					var scheduleObj = [];
					for(var n in main.nodeDataArray){
						var founded = false;
						for(var l in main.linkDataArray){
							if(main.nodeDataArray[n].key == main.linkDataArray[l].from){
								founded = true;
							}
						}
						
						if(!founded){
							scheduleObj.push(main.nodeDataArray[n]);
						}
					}
					
					
					
					for(var i in scheduleObj){
						console.info("scheduleObj[i]", scheduleObj[i]);
						
						var target = { "targetListId": null,"targetListName": null,"groupId": null,"jobId": null,"stepId": null,"progPath": null,"parameter": null, "pType":null, "method":null};
			  			target.targetListId = dateFormated + "_" + processId;
			  			target.targetListName = scheduleObj[i].text;
			  			target.groupId = "GRP_" + processId;
			  			target.jobId = "JOB_" + processId;
			  			target.stepId = "STP_" + scheduleObj[i].key;
			  			target.progPath = "http://localhost:8080/zPortal/zData/zDataProcessing.html";
			  			target.parameter = "fileName=process//" + processId + "&objectKey=" + scheduleObj[i].key;
			  			target.pType = "http";
			  			target.method = "get";
						group.targets.push(target);
					}
			 		
					scheduleInfos.push(group);
			 		makeTable();
				  	jQuery("#zv").empty();
					showTabs(tableData);
				
// 			 	}catch(e){
// 			 		alert("íìì´ ë§ì§ ììµëë¤." + e);
// 			 	}
				
			},
			error : function(request, status, error) {
				alert("request:" + request.responseText + "\n error:" + error);
			},
			data : {fileName:"process//" + processId, objectKey:"main"}
		});
	 	
    }
    
    
    
    function textToJson(type){
    	makeInfos();
    	
	 	var text = jQuery("#text").val();
		
	 	try{
	 		var layouts = JSON.parse(text);
	 		var id = "layout" + layouts.length;
	 		
	 		var founded = false;
	 		for(var i in scheduleInfos){
	 			if(scheduleInfos[i].id == id){
	 				scheduleInfos[i].layouts.push(layouts);
	 				founded = true;
	 			}
	 		}
	 		
	 		if(!founded){
	 			var infos = {id:id ,layouts:[layouts]};
	 			scheduleInfos.push(infos);
	 		}
	 		
	 		makeTable();
		  	jQuery("#zv").empty();
			showTabs(tableData);
		
	 	}catch(e){
	 		alert("íìì´ ë§ì§ ììµëë¤." + e);
	 	}
	}
    
    function makeTable(){
    	//scheduleInfosë¡ tableì ë§ë¤ì.
    	//group
    	tableData = [];
    	var table = {tableName:"group" ,data:[]};    
   		var header = ["uniqueId", "createdAt", "scheduleName", "triggerId", "groupId", "jobId", "cycleCode", "cycleTime", "date", "useYN", "status"];
   		table.data.push(header);
   		
   		console.info("scheduleInfos", scheduleInfos);
    		
    	for(var i in scheduleInfos){
   			var row = [];
   			row.push(scheduleInfos[i].uniqueId);
   			row.push(scheduleInfos[i].createdAt); 
   			row.push(scheduleInfos[i].scheduleName); 
   			row.push(scheduleInfos[i].triggerId); 
   			row.push(scheduleInfos[i].groupId); 
   			row.push(scheduleInfos[i].jobId); 
   			row.push(scheduleInfos[i].cycleCode); 
   			row.push(scheduleInfos[i].cycleTime); 
   			if(scheduleInfos[i].date){
   				row.push(scheduleInfos[i].date); 
   			}else{
   				row.push("none"); 
   			}
   			
   			row.push(scheduleInfos[i].useYN); 
   			row.push(scheduleInfos[i].status); 
   			table.data.push(row);
    	}
    	tableData.push(table);
    	
    	//targets
    	table = {tableName:"targets" ,data:[]};
   		var header = ["targetListId", "targetListName", "groupId", "jobId", "stepId", "progPath", "parameter", "pType", "method"];
   		table.data.push(header);
    		
    	for(var i in scheduleInfos){
    		for(var l in scheduleInfos[i].targets){
	   			var row = [];
	   			row.push(scheduleInfos[i].targets[l].targetListId);
	   			row.push(scheduleInfos[i].targets[l].targetListName); 
	   			row.push(scheduleInfos[i].targets[l].groupId); 
	   			row.push(scheduleInfos[i].targets[l].jobId); 
	   			row.push(scheduleInfos[i].targets[l].stepId); 
	   			row.push(scheduleInfos[i].targets[l].progPath); 
	   			row.push(scheduleInfos[i].targets[l].parameter); 
	   			if(scheduleInfos[i].targets[l].pType){
		   			row.push(scheduleInfos[i].targets[l].pType); 
		   			
	   			}else{
	   				row.push("none"); 
	   			}
	   			if(scheduleInfos[i].targets[l].method){
		   			row.push(scheduleInfos[i].targets[l].method); 
		   			
	   			}else{
	   				row.push("none"); 
	   			}
	   			
	   			table.data.push(row);
    		}
    	}
    	tableData.push(table);   	
    }
    
    function makeInfos(){
    	//tableDataë¡ scheduleInfosë¥¼ ë§ë¤ì.
    	top.zPortal.log("info",tableData);
    	
    	scheduleInfos = [];
  		
  		var format = "{";
		for(var h in tableData[0].data[0]){
			format += "\"" + tableData[0].data[0][h] + "\":"  + h + ","; 
		}
		format = format.substring(0, format.length - 1) + "}";
		
		var header = JSON.parse(format);
		top.zPortal.log("info","header", header);
		
		var data = tableData[0].data.slice(0);
	   	data.splice(0,1);
  		for(var i in data){
  			var group = { "targets": [],"uniqueId": null,"createdAt": null,"scheduleName": null,"triggerId": null,"groupId": null,"jobId": null,"cycleCode": null,"cycleTime": null, "date":null, "useYN": null,"status": null};
  			group.uniqueId = data[i][header['uniqueId']];
  			group.createdAt = data[i][header['createdAt']];
  			group.scheduleName = data[i][header['scheduleName']];
  			group.triggerId = data[i][header['triggerId']];
  			group.groupId = data[i][header['groupId']];
  			group.jobId = data[i][header['jobId']];
  			group.cycleCode = data[i][header['cycleCode']];
  			group.cycleTime = data[i][header['cycleTime']];
  			group.date = data[i][header['date']];
  			group.useYN = data[i][header['useYN']];
  			group.status = data[i][header['status']];
  			scheduleInfos.push(group);
  		}
  		
  		//targets
  		format = "{";
		for(var h in tableData[1].data[0]){
			format += "\"" + tableData[1].data[0][h] + "\":"  + h + ","; 
		}
		format = format.substring(0, format.length - 1) + "}";
		
		header = JSON.parse(format);
		data = tableData[1].data.slice(0);
		data.splice(0,1);
  		for(var i in data){
  			var target = { "targetListId": null,"targetListName": null,"groupId": null,"jobId": null,"stepId": null,"progPath": null,"parameter": null, "pType":null, "method":null};
  			target.targetListId = data[i][header['targetListId']];
  			target.targetListName = data[i][header['targetListName']];
  			target.groupId = data[i][header['groupId']];
  			target.jobId = data[i][header['jobId']];
  			target.stepId = data[i][header['stepId']];
  			target.progPath = data[i][header['progPath']];
  			target.parameter = data[i][header['parameter']];
  			target.pType = data[i][header['pType']];
  			target.method = data[i][header['method']];
  			
			for(var s in scheduleInfos){
				if(scheduleInfos[s].uniqueId == target.targetListId){
					scheduleInfos[s].targets.push(target);
					break;
				}
			}
  		}
    }

</script>
</html>
