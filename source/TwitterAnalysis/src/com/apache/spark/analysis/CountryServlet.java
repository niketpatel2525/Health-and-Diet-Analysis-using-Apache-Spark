package com.apache.spark.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import scala.Tuple2;

/**
 * Servlet implementation class CountryServlet
 */
public class CountryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CountryServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// TODO Auto-generated method stub

		String header = request.getHeader("referer");
		if (header != null) {
			String inputFile = getServletContext().getRealPath("/") + "twitter.json";
			String outputFile = getServletContext().getRealPath("/") + "flare.csv";

			File f = new File(outputFile);
			BufferedWriter bw;
			FileWriter fw = null;

			SparkConf sparkConf = new SparkConf().setAppName("Country Servlet").setMaster("local")
					.set("spark.driver.allowMultipleContexts", "true");

			SparkContext ctx = new SparkContext(sparkConf);
			SQLContext sc = new SQLContext(ctx);

			DataFrame df = sc.read().json(inputFile);
			df.registerTempTable("tweets");

			DataFrame data = sc.sql("select place.country from tweets");

			JavaPairRDD<String, Integer> ones = data.toJavaRDD().mapToPair(new PairFunction<Row, String, Integer>() {

				@Override
				public Tuple2<String, Integer> call(Row r) throws Exception {
					// TODO Auto-generated method stub
					String i = r.getString(0);

					return new Tuple2<String, Integer>(i, 1);
				}
			});

			JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {

				@Override
				public Integer call(Integer i1, Integer i2) throws Exception {
					// TODO Auto-generated method stub
					return i1 + i2;
				}

			});
			JavaPairRDD<String, Integer> reduced = counts
					.mapToPair(new PairFunction<Tuple2<String, Integer>, String, Integer>() {
						@Override
						public Tuple2<String, Integer> call(Tuple2<String, Integer> stringIntegerTuple2)
								throws Exception {

							if (stringIntegerTuple2._2() >= 200)
								return new Tuple2<>(stringIntegerTuple2._1(), stringIntegerTuple2._2());
							else
								return new Tuple2<>("others", stringIntegerTuple2._2());

						}
					});

			JavaPairRDD<String, Integer> countsReduced = reduced
					.reduceByKey(new Function2<Integer, Integer, Integer>() {
						@Override
						public Integer call(Integer i1, Integer i2) {

							return i1 + i2;
						}

					});
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				fw = new FileWriter(f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bw = new BufferedWriter(fw);

			try {
				bw.write("id,value\n");
				Pattern p = Pattern.compile("[^a-zA-Z]");
				Map<String, Integer> resultData = countsReduced.collectAsMap();
				for (Map.Entry<String, Integer> a : resultData.entrySet()) {
					if (a.getKey() != null) {
						Matcher m = p.matcher(a.getKey());
						System.out.println(a.getKey() + "==>" + m.find());

						if (!m.find()) {
							bw.append(a.getKey() + "," + a.getValue() + "\n");
						}
					}

				}
				bw.flush();
				bw.close();
				ctx.stop();
				response.sendRedirect("country_about_health.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					response.sendRedirect("error.jsp");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

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

}
