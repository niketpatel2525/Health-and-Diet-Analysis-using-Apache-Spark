<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="https://www.amcharts.com/lib/3/ammap.js"></script>
<script src="https://www.amcharts.com/lib/3/maps/js/usaLow.js"></script>
<script
	src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
<link rel="stylesheet"
	href="https://www.amcharts.com/lib/3/plugins/export/export.css"
	type="text/css" media="all" />
<script src="https://www.amcharts.com/lib/3/themes/none.js"></script>
<style type="text/css">
#chartdiv {
	width: 100%;
	height: 500px;
}
</style>
</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-8 col-md-offset-2">
					<div id="chartdiv"></div>
				</div>
			</div>
		</div>
	</div>
	<script>
		
	<%Map<String, Integer> data = (Map<String, Integer>) request.getAttribute("flu");%>
		var map = AmCharts.makeChart("chartdiv", {
			"type" : "map",
			"theme" : "none",
			"colorSteps" : 10,

			"dataProvider" : {
				"map" : "usaLow",
				"areas" : [
					<%for (Map.Entry<String, Integer> d : data.entrySet()) {%>
					{
					"id" : "US-<%=d.getKey()%>",
					"value" : <%=d.getValue()%>
					}, 
					<%}%> ]
			},

			"areasSettings" : {
				"autoZoom" : true
			},

			"valueLegend" : {
				"right" : 10,
				"minValue" : "low flu wave",
				"maxValue" : "high flu wave"
			},

			"export" : {
				"enabled" : true
			}

		});
	</script>
</body>
</html>