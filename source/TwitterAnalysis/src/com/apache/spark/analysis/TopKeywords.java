package com.apache.spark.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
 * Servlet implementation class TopKeywords
 */
public class TopKeywords extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TopKeywords() {
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
			String outputFile = getServletContext().getRealPath("/") + "/word.csv";
			File f = new File(outputFile);
			BufferedWriter bw;
			FileWriter fw = null;
			SparkConf sparkConf = new SparkConf().setAppName("Keywords").setMaster("local")
					.set("spark.driver.allowMultipleContexts", "true");
			JavaSparkContext ctx = new JavaSparkContext(sparkConf);
			SQLContext sc = new SQLContext(ctx);

			DataFrame d = sc.read().json(inputFile);
			d.registerTempTable("tweets");
			DataFrame data;
			data = sc.sql("select text from tweets where place.country='United States'");
			JavaRDD<String> words = data.toJavaRDD().flatMap(new FlatMapFunction<Row, String>() {

				@Override
				public Iterable<String> call(Row row) throws Exception {
					// TODO Auto-generated method stub
					String s = "";
					List<String> keywords = Arrays.asList("food", "health", "gym", "workout", "lifestyle", "diet",
							"pizza", "sandwich", "pasta", "salad", "vegetarian", "vegan", "non vegetarian", "health",
							"society", "weight loss", "fat", "nutrition", "fitness", "fruit", "yoga", "exercise",
							"Chicken", "Mutton", "beef", "fish", "salads", "juices", "streetfood", "street food",
							"Indian food", "Italian food", "Mexican food", "Chinese food", "Thai food", "American food",
							"Japanese food", "aloo chat", "aloochat", "momos", "samosa", "vadapav", "vada pav", "dosa",
							"pani puri", "panipuri", "bhelpuri", "bhel puri", "gymdiet", "gym diet", "protein");

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
			bw.append("word,count\n");

			Map<String, Integer> resultData = reducedCounts.collectAsMap();
			for (Map.Entry<String, Integer> da : resultData.entrySet()) {
				if (da.getKey().trim().length() > 0)
					bw.append(da.getKey() + "," + da.getValue() + "\n");
			}
			bw.flush();
			bw.close();
			ctx.stop();

			response.sendRedirect("top_keyword.jsp");
		} else

		{
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
