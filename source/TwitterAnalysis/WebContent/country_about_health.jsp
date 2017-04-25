<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="https://d3js.org/d3.v4.min.js"></script>

</head>
<body>
	<div class="container">
		<div class="jumbotron">
			<div class="row">
				<div class="col-md-8 col-md-offset-2">
					<svg width="760" height="760" font-family="sans-serif"
						font-size="10" text-anchor="middle"></svg>
				</div>
			</div>
		</div>
	</div>
	<script>
		var svg = d3.select("svg"), width = +svg.attr("width"), height = +svg
				.attr("height");

		var format = d3.format(",d");

		var color = d3.scaleOrdinal(d3.schemeCategory20c);

		var pack = d3.pack().size([ width, height ]).padding(1.5);

		d3.csv("flare.csv", function(d) {
			d.value = +d.value;
			if (d.value)
				return d;
		}, function(error, classes) {
			if (error)
				throw error;

			var root = d3.hierarchy({
				children : classes
			}).sum(function(d) {
				return d.value;
			}).each(function(d) {
				if (id = d.data.id) {
					var id, i = id.lastIndexOf(".");
					d.id = id;
					d.package = id.slice(0, i);
					d.class = id.slice(i + 1);
				}
			});

			var node = svg.selectAll(".node").data(pack(root).leaves()).enter()
					.append("g").attr("class", "node").attr("transform",
							function(d) {
								return "translate(" + d.x + "," + d.y + ")";
							});

			node.append("circle").attr("id", function(d) {
				return d.id;
			}).attr("r", function(d) {
				return d.r;
			}).style("fill", function(d) {
				return color(d.package);
			});

			node.append("clipPath").attr("id", function(d) {
				return "clip-" + d.id;
			}).append("use").attr("xlink:href", function(d) {
				return "#" + d.id;
			});

			node.append("text").attr("clip-path", function(d) {
				return "url(#clip-" + d.id + ")";
			}).selectAll("tspan").data(function(d) {
				return d.class.split(/(?=[A-Z][^A-Z])/g);
			}).enter().append("tspan").attr("x", 0).attr("y",
					function(d, i, nodes) {
						return 13 + (i - nodes.length / 2 - 0.5) * 10;
					}).text(function(d) {
				return d;
			});

			node.append("title").text(function(d) {
				return d.id + "\n" + format(d.value);
			});
		});
	</script>


	<div class="container">
		


	</div>
</body>
</html>