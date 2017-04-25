<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
</head>
<body>
	<div id="container"
		style="min-width: 310px; height: 400px; max-width: 600px; margin: 0 auto"></div>
	<script>

	Highcharts.chart('container', {
	    chart: {
	        plotBackgroundColor: null,
	        plotBorderWidth: 0,
	        plotShadow: false
	    },
	    title: {
	        text: 'Various<br>aspect of Health<br>with retweets',
	        align: 'center',
	        verticalAlign: 'middle',
	        y: 40
	    },
	    tooltip: {
	        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	    },
	    plotOptions: {
	        pie: {
	            dataLabels: {
	                enabled: true,
	                distance: -50,
	                style: {
	                    fontWeight: 'bold',
	                    color: 'white'
	                }
	            },
	            startAngle: -90,
	            endAngle: 90,
	            center: ['50%', '75%']
	        }
	    },
	    <%Map<String, Integer> data = (Map<String, Integer>) request.getAttribute("retweets");%>
	    series: [{
	        type: 'pie',
	        name: 'Various aspect of Health with Retweets',
	        innerSize: '50%',
	        data: [
	        	
	        	<%for (Map.Entry<String, Integer> d : data.entrySet()) {%>
	        	['<%=d.getKey()%>',<%=d.getValue()%>],
	            
	    	<%}%>
	    	 {
	                y: 0.2,
	                dataLabels: {
	                    enabled: false
	                }
	            }
	        ]
	    }]
	});

</script>
</body>
</html>