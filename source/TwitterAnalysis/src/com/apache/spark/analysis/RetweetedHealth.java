package com.apache.spark.analysis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import scala.Tuple2;

/**
 * Servlet implementation class RetweetedHealth
 */
public class RetweetedHealth extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RetweetedHealth() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String header = request.getHeader("referer");
		if (header != null) {
			String inputFile = getServletContext().getRealPath("/") + "twitter.json";
			String outputFile = getServletContext().getRealPath("/") + "";

			SparkConf sparkConf = new SparkConf().setAppName("Gym Diet").setMaster("local")
					.set("spark.driver.allowMultipleContexts", "true");
			JavaSparkContext ctx = new JavaSparkContext(sparkConf);
			SQLContext sc = new SQLContext(ctx);

			DataFrame d = sc.read().json(inputFile);
			d.registerTempTable("tweets");
			DataFrame data;
			data = sc.sql("select text from tweets where quoted_status.retweet_count > 0 and text LIKE	'%gym%' or  "
					+ "text LIKE '%Gym%' or  "
					+ "text LIKE '%Diet%' or  text LIKE '%diet%' or  text LIKE '%exercise%' or "
					+ " text LIKE '%Exercise%' or  text LIKE '%Workout%' or  text LIKE '%workout%' or "
					+ " text LIKE '%Protein%' or  text LIKE '%protein%' or  text LIKE '%yoga%' or "
					+ " text LIKE '%Yoga%' or  text LIKE '%food%' or  text LIKE '%pizza%' or  text LIKE '%sandwich%' or  text LIKE '%pasta%' or "
					+ " text LIKE '%Chicken%' or  text LIKE '%Mutton%' or  text LIKE '%fish%' or  text LIKE '%protein%'");
			JavaRDD<String> words = data.toJavaRDD().flatMap(new FlatMapFunction<Row, String>() {

				@Override
				public Iterable<String> call(Row row) throws Exception {
					// TODO Auto-generated method stub
					String s = "";
					List<String> keywords_set_01 = Arrays.asList("gym", "diet", "exercise", "workout", "protein", "yoga");
					List<String> keywords_set_02 = Arrays.asList("pizza", "sandwich", "pasta", "Chicken", "Mutton", "fish","protein");
					for (String str : keywords_set_01) {
						if (row.getString(0).toLowerCase().contains(str.toLowerCase()))
							s += " " + "FitnessFreaks";
					}
					for (String str : keywords_set_02) {
						if (row.getString(0).toLowerCase().contains(str.toLowerCase()))
							s += " " + "FoodLovers";
					}
					s.trim();
					return Arrays.asList(s.split(" "));
				}
			});
			JavaPairRDD<String, Integer> counts = words.mapToPair(new PairFunction<String, String, Integer>() {
				public Tuple2<String, Integer> call(String s) {
					return new Tuple2(s, 1);
				}
			});
			JavaPairRDD<String, Integer> reducedCounts = counts.reduceByKey(new Function2<Integer, Integer, Integer>() {
				public Integer call(Integer x, Integer y) {
					return x + y;
				}
			});

			Map<String, Integer> resultData = reducedCounts.collectAsMap();
			for (Map.Entry<String, Integer> da : resultData.entrySet()) {
				if (da.getKey().trim().length() == 0)
					resultData.remove(da.getKey());
			}
			// reducedCounts.saveAsTextFile(outputFile);
			Map<String, Integer> sortedData = sortByValue(resultData);
			for (Map.Entry<String, Integer> da : sortedData.entrySet()) {
				System.out.println(da.getKey() + " " + da.getValue());
			}

			ctx.stop();
			request.setAttribute("retweets", sortedData);
			request.getRequestDispatcher("retweeted_analysis.jsp").include(request, response);
		} else {
			try {
				response.sendRedirect("unauthorized.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public static <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue(
			Map<String, Integer> unsortMap) {

		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;

	}

}
