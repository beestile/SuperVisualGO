<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>ZettaSoft Scheduler</title>

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

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jquery-ui-smoothness.min.css">

<script src="${pageContext.request.contextPath}/assets/js/jquery-2.1.4.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/jquery-ui.min.css">
<script src="${pageContext.request.contextPath}/assets/js/jquery-ui.min.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/plugin/clockpicker-gh-pages/src/clockpicker.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/clockpicker-gh-pages/src/clockpicker.js"></script>

<link rel="stylesheet" href="${pageContext.request.contextPath}/plugin/clockpicker-gh-pages/dist/jquery-clockpicker.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/plugin/clockpicker-gh-pages/dist/jquery-clockpicker.js"></script>


<body>
	<div class="container">
		<form class="form-horizontal" name="schedulerForm" id="schedulerForm">
			<div class="form-group">
				<label class="control-label col-sm-2">스케줄명 : </label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="scheduleName" id="scheduleName" placeholder="ì¤ì¼ì¤ ì´ë¦ì ìë ¥íì¸ì...">				
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">시간단위 : </label>
				<div class="col-sm-10" id="btn_radio">
					<label class="radio-inline"><input type="radio" id="cy1" name="cycleCode" value="interval">초단위간격</label> 
					<label class="radio-inline"><input type="radio" id="cy2" name="cycleCode" value="daily" >일단위</label> 
					<label class="radio-inline"><input type="radio" id="cy3" name="cycleCode" value="weekly">주단위</label> 
					<label class="radio-inline"><input type="radio" id="cy4" name="cycleCode" value="monthly">월단위</label>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">시작일 : </label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="datepicker" name="date" placeholder="...">
				</div>
			</div>
			<div id="cycleCode_interval">	
				<div class="form-group">									
					<label class="control-label col-sm-2">초단위간격 : </label>
					<div class="col-sm-10" id="content_interval">					
					</div>	
				</div>
			</div>
			<div id="cycleCode_daily">
				<div class="form-group">									
					<label class="control-label col-sm-2">일단위 : </label>					
					<div class="col-sm-10" id="content_daily">																
					</div>
				</div>
			</div>
			<div id="cycleCode_weekly">
				<div class="form-group">									
					<label class="control-label col-sm-2">주단위 : </label>					
					<div class="col-sm-10" id="content_weekly">																
					</div>
				</div>
			</div>
			<div id="cycleCode_monthly">
				<div class="form-group">									
					<label class="control-label col-sm-2">월단위 : </label>					
					<div class="col-sm-10" id="content_monthly">																	
					</div>
				</div>
			</div>
			<div>
			<input type="text" class="form-control" style="display:none" name="uniqueId" id="uniqueId">
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	// 2016.03.16 Add... 
	var parameters = decodeURI(location.search.substring(1)).split("&");
	var targetInfo;
    if (parameters.length == 1) {
    	targetInfo = parameters[0].split("=")[1];
	}	
    
    var infos = targetInfo.split(",");
	//infos = decodeURI(infos);
    
	document.getElementById('uniqueId').value = infos[0];
	document.getElementById('scheduleName').value = infos[2];		
	document.getElementById('datepicker').value = infos[1];
	$('input:radio[name=cycleCode]:input[value=' + infos[6] + ']').attr("checked", true);	
	
	$('input:radio[name="cycleCode"]').each(function() {
		if ($(this).is(":checked")) {
			$("#cycleCode_" + $(this).val()).show();
			
			if($(this).val() == 'interval'){				
				$("#content_" + $(this).val()).append('<input type="text" class="form-control" name="cycleTime" value='+ infos[7]  +'>');
			}else{
				$("#content_" + $(this).val()).append('<input type="text" class="form-control" id="timepicker" name="cycleTime" value='+ infos[7]  +'>');
				$('input:text[id="timepicker"]').each(function() {
					$(this).clockpicker({
						donetext: '선택',
					    //autoclose: true,
					    'default': 'now'
					});
				});									
			}				
		} else {
			$("#cycleCode_" + $(this).val()).hide();
		}
	});
	// 2016.03.16 End...		

	/*
	라디오버튼 선택한것만 보이도록...
	 */
	$('input:radio[name="cycleCode"]').click(function() {

		$('input:radio[name="cycleCode"]').each(function() {

			if ($(this).is(":checked")) {
				console.log("#cycleCode_" + $(this).val());
				console.log("#content_" + $(this).val());
			
				$("#cycleCode_" + $(this).val()).show();
			
				if($(this).val() === 'interval'){
					$("#content_" + $(this).val()).append('<input type="text" class="form-control" name="cycleTime" placeholder="정수값을 입력하세요...">');
				}else{
					$("#content_" + $(this).val()).append('<input type="text" class="form-control" id="timepicker" name="cycleTime" placeholder="시간을 선택하세요...">');
					$('input:text[id="timepicker"]').each(function() {
						$(this).clockpicker({
							donetext: '선택',
						    //autoclose: true,
						    'default': 'now'
						});
					});
				}
			} else {
				$("#cycleCode_" + $(this).val()).hide();
				$("#content_" + $(this).val()).empty();
			}
		});
	});
	$("#datepicker").datepicker();
});
</script>
</html>