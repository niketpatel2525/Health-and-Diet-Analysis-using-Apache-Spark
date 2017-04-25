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
 * Servlet implementation class Cusines
 */
public class Cuisines extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Cuisines() {
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
			data = sc.sql("select text from tweets where text LIKE	'%Indian%food%' or  text LIKE '%Thai%food%' or  "
					+ "text LIKE '%Italian%food%' or  text LIKE '%Mexican%food%' or  text LIKE '%Chinese%food%' or "
					+ " text LIKE '%American%food%' or  text LIKE '%Japanese%food%' or "
					+ "text LIKE	'%Indian%dish%' or  text LIKE '%Thai%dish%' or "
					+ "text LIKE '%Italian%dish%' or  text LIKE '%Mexican%dish%' or  text LIKE '%Chinese%dish%' or "
					+ " text LIKE '%American%dish%' or  text LIKE '%Japanese%dish%'");
			JavaRDD<String> words = data.toJavaRDD().flatMap(new FlatMapFunction<Row, String>() {

				@Override
				public Iterable<String> call(Row row) throws Exception {
					// TODO Auto-generated method stub
					String s = "";
					List<String> keywords = Arrays.asList("Indian", "Thai", "Italian", "Mexican", "Chinese", "American",
							"Japanese");

					for (String str : keywords) {
						if (row.getString(0).toLowerCase().contains(str.toLowerCase()))
							s += " " + str.trim();
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
			request.setAttribute("cuisines", sortedData);
			request.getRequestDispatcher("cuisines.jsp").include(request, response);
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
