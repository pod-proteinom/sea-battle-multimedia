
<%@ page session="false" language="java" contentType="text/html; charset=UTF-8"%>
<html>
	<head>
		<title>DWR Dev</title>
		<script type="text/javascript" src="/dwrSampleApp/dwr/engine.js"></script>
		<script type="text/javascript" src="/dwrSampleApp/dwr/util.js"></script>
		<script type="text/javascript" src="/dwrSampleApp/dwr/interface/dwrService.js"></script>
		<script>
			function getDataFromServer() {
			  dwrService.getTestBean({
			  	callback: getDataFromServerCallBack
			  });
			}
			
			function getDataFromServerCallBack(dataFromServer) {
			  alert(DWRUtil.toDescriptiveString(dataFromServer, 3));
			}
		</script>
	</head>
	<body>
		<h3>DWR/Spring and Spring MVC</h3>
		<a href="#" onclick="getDataFromServer(); return false;">Retrieve test data</a><br/>
	</body>
</html>