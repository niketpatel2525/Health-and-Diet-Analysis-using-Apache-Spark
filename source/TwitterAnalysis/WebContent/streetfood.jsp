<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
<script src="https://www.amcharts.com/lib/3/funnel.js"></script>
<script
	src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
<link rel="stylesheet"
	href="https://www.amcharts.com/lib/3/plugins/export/export.css"
	type="text/css" media="all" />
<script src="https://www.amcharts.com/lib/3/themes/light.js"></script>
<style>
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
		
	<%Map<String, Integer> data = (Map<String, Integer>) request.getAttribute("streetfood");%>
	
		var chart = AmCharts.makeChart("chartdiv", {
			"type" : "funnel",
			"theme" : "light",
			"dataProvider" : [
	<%for (Map.Entry<String, Integer> d : data.entrySet()) {%>
		{
			"title":"<%=d.getKey()%>",
			"value":<%=d.getValue()%>
		},
		<%}%>
			],
			"balloon" : {
				"fixedPosition" : true
			},
			"valueField" : "value",
			"titleField" : "title",
			"marginRight" : 240,
			"marginLeft" : 50,
			"startX" : -500,
			"depth3D" : 100,
			"angle" : 40,
			"outlineAlpha" : 1,
			"outlineColor" : "#FFFFFF",
			"outlineThickness" : 2,
			"labelPosition" : "right",
			"balloonText" : "[[title]]: [[value]]n[[description]]",
			"export" : {
				"enabled" : true
			}
		});
	</script>
</body>
</html>