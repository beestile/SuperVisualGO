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
<script src="${pageContext.request.contextPath}/assets/js/bootstrap/bootstrap.min.js"></script>

<style type="text/css">
body {
	font-size: 12px;
	font-size: 1 rem;
	font-family: âNanum Brush Scriptâ, Helvetica, Arial, sans-serif;
	text-rendering: optimizeLegibility;
	color: #444;
}
</style>

</head>
<body>
	<div class="container">
		<br /> <br />				
		<form class="form-horizontal" name="schedulerForm" id="schedulerForm">
			
			<div class="form-group">
				<label class="control-label col-sm-2">Task 설정: </label>
				<div class="col-sm-10">
					<select id="pType"  name="pType" class="form-control">
						<option value="command">command</option>
						<option value="http" >http</option>
					</select>
				</div>
			</div>
			
			<div class="form-group">
				<label class="control-label col-sm-2" id="pathLable">프로그램 경로 : </label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="progPath" name="progPath"  placeholder="...">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">파라미터 : </label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="parameter" name="parameter" placeholder="...">
				</div>
			</div>
			
			<div id="methodGroup" class="form-group">
				<label class="control-label col-sm-2">http method : </label>
				<div class="col-sm-10">
					<select id="httpMethod"  name="method" class="form-control">
						<option value="get"  >get</option>
						<option value="post" >post</option>
					</select>
				</div>
			</div>
			<input type="text" class="form-control" style="display:none" name="targetListId" id="targetListId">
			<input type="text" class="form-control" style="display:none" name="targetListName" id="targetListName">
			<input type="text" class="form-control" style="display:none" name="groupId" id="groupId">
			<input type="text" class="form-control" style="display:none" name="jobId" id="jobId">
			<input type="text" class="form-control" style="display:none" name="stepId" id="stepId">
		</form>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function() {

	// 2016.03.16 Add... 
	var parameters = decodeURI(location.search.substring(1));
	var targetInfo;
    if (parameters.length > 1) {
    	targetInfo = parameters.split("stepInfo=")[1];
	};
	
	var infos = targetInfo.split(",");
	//$('select[name=pType]:option[value=' + infos[7] + ']').attr("selected", true);	
	
	$('#targetListId').val(infos[0]);
	$('#targetListName').val(infos[1]);
	$('#groupId').val(infos[2]);
	$('#jobId').val(infos[3]);
	$('#stepId').val(infos[4]);
	
	$("#progPath").val(infos[5]);
	$("#parameter").val(infos[6]);
	
	$("#pType").val(infos[7]);
	if(infos[7] === "http"){
		$("#httpMethod").val(infos[8]);
	}
	
	var pType = $("#pType option:selected").val();
	if(pType === 'command'){
		$("#methodGroup").hide();
	}
		
	$('#pType').change(function(){
		
		var pType = $("#pType option:selected").val();
		
		if(pType === 'command'){
			$("#methodGroup").hide();
			$("#pathLable").text("프로그램경로 : ");
		}else if(pType === 'http'){
			$("#methodGroup").show();
			$("#pathLable").text("URL : ");
		}
	});
});
</script>
</html>