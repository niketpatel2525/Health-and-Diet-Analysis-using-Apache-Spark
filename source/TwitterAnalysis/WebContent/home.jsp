<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">


<!--Import Google Icon Font-->
<link href="http://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<!--Import materialize.css-->
<link type="text/css" rel="stylesheet" href="css/materialize.min.css"
	media="screen,projection" />

<link href="css/custom.css" rel="stylesheet">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="jumbotron text-center">
			<p class="page-title">Health and Diet Analysis using Twitter Data</p>
			<hr>
		</div>

	</div>


	<div class="container">

		<div class="row">
			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Health Tweets</span>
						<p>This analysis gives analysis for the tweets that related
							with Health issues. Analysis is represented using Bubble Chart,
							provided by D3.js visualization javascript.</p>
					</div>
					<div class="card-action">
						<form action="country" target="_blank">
							<input type="hidden" value="country" name="process"> <input
								type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>

					</div>
				</div>
			</div>


			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Trending on Twitter</span>
						<p>This analysis gives buzzing words on twitter. Analysis is
							represented using Word Cloud Chart, provided by D3.js
							visualization javascript.</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="keywords">
							<input type="hidden" value="top_keywords" name="process">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>

					</div>
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Cuisine Trends</span>
						<p>This analysis finds trend for different kind of Cuisines in
							the world and represent it using pie chcart provided by
							Amchart.js</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="cuisines">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>
					</div>
				</div>
			</div>


			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Flu affected states in USA</span>
						<p>This will process the tweets and find unhealthy people
							across the United States grouped by States and represend data
							using USA map provided by HighChart.js</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="flu">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>

					</div>
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Street Food Trends</span>
						<p>This Analysis process twitter data find people preference
							for street food and visualize street food based on their
							preference using funnel chart provided by HighChart.js.</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="streetfood">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>

					</div>
				</div>
			</div>


			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Gym Diet Trends in United States</span>
						<p>This analysis finds twitter trends for people preference of
							Gym Diet using tweets and represent data using pie chart provided
							by Amchart.js Javascript</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="gymdiet">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>
					</div>
				</div>
			</div>

		</div>
		<div class="row">
			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Retweets (Health and Fitness)</span>
						<p>This analysis finds retweeted tweets and process the data
							to find ratio of FitnessFreaks to FoodLovers. This analysis is
							represented by half pie chart provided by Amchart.js.</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="retweets">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>

					</div>
				</div>
			</div>


			<div class="col s12 m6">
				<div class="card blue-grey darken-1" id="card">
					<div class="card-content white-text">
						<span class="card-title">Workout Trends</span>
						<p>This analysi finds different kind of exercise prfered by
							people to be fit. This analysis is represented by pie chart
							provided by Amchart.js.</p>
					</div>
					<div class="card-action">
						<form target="_blank"  action="workout">
							<input type="submit" value="Start Analysis"
								class="btn btn-block btn-lg btn-link" />
						</form>

					</div>
				</div>
			</div>

		</div>

	</div>
	<!--Import jQuery before materialize.js-->
	<script type="text/javascript"
		src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
	<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>