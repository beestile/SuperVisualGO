<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

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
<!-- 
Listë°ì¤ê° íë ë¤ì´ê°ì¼ í¨. ì íì command, http ëê° 
 -->
<body>
	<div class="container">
		<br /> <br />				
		<form class="form-horizontal" name="schedulerForm" id="schedulerForm">
			
			<div class="form-group">
				<label class="control-label col-sm-2">Task 설정 : </label>
				<div class="col-sm-10">
					<select id="pType"  name="pType" class="form-control">
						<option value="command" selected="selected">command</option>
						<option value="http" >http</option>
					</select>
				</div>
			</div>
			
			<div class="form-group">
				<label class="control-label col-sm-2" id="pathLable">프로그램 경로 : </label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="progPath"  placeholder="...">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">파라미터 : </label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="parameter" placeholder="...">
				</div>
			</div>
			
			<div id="methodGroup" class="form-group">
				<label class="control-label col-sm-2">http method : </label>
				<div class="col-sm-10">
					<select id="httpMethod"  name="method" class="form-control">
						<option value="get" selected="selected">get</option>
						<option value="post" >post</option>
					</select>
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function() {

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