import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONObject;

public class Sandbox {

	private static JSONObject jsonFile1ObjU;
	private static JSONObject jsonFile1ObjV;
	private static JSONObject jsonFile2ObjU;
	private static JSONObject jsonFile2ObjV;
	private static int numberPoints;

	public static void main(String args[]){

		try {
			createJSONObj();
			createBasicJSONFiles();
			createDifferenceJSONFile();
			createDifferenceScalarJSONFile();
			//			doWork();
			//						subtract();
			//vectorToScalar();
			//			createTemp();
			//countLength();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createBasicJSONFiles() throws IOException
	{
		//formats it to how nullschool code needs it to be
		FileWriter fw = new FileWriter(new File("Quotaero Data/current-temp-surface-level-gfs-1.0.json"));
		fw.write("[{\"header\":");
		fw.write(jsonFile1ObjU.get("header").toString());
		fw.write(",\"data\":");
		fw.write(jsonFile1ObjU.get("data").toString());
		fw.write("},");
		fw.write("{\"header\":");
		fw.write(jsonFile1ObjV.get("header").toString());
		fw.write(",\"data\":");
		fw.write(jsonFile1ObjV.get("data").toString());
		fw.write("}]");
		fw.close();
		System.out.println("File1 done");

		fw = new FileWriter(new File("Quotaero Data/current-wind_actual-surface-level-gfs-1.0.json"));
		fw.write("[{\"header\":");
		fw.write(jsonFile2ObjU.get("header").toString());
		fw.write(",\"data\":");
		fw.write(jsonFile2ObjU.get("data").toString());
		fw.write("},");
		fw.write("{\"header\":");
		fw.write(jsonFile2ObjV.get("header").toString());
		fw.write(",\"data\":");
		fw.write(jsonFile2ObjV.get("data").toString());
		fw.write("}]");
		fw.close();
		System.out.println("File2 done");
	}
	
	public static void createDifferenceScalarJSONFile() throws IOException
	{
		
		FileWriter fw = new FileWriter(new File("Quotaero Data/current-wind_difference-surface-level-gfs-1.0.json"));
		String data1 = jsonFile1ObjU.get("data").toString();
		String data2 = jsonFile2ObjU.get("data").toString();
		String data3 = jsonFile1ObjV.get("data").toString();
		String data4 = jsonFile2ObjV.get("data").toString();

		data1 = data1.replaceAll("\\[|\\]", "");
		data2 = data2.replaceAll("\\[|\\]", "");
		data3 = data3.replaceAll("\\[|\\]", "");
		data4 = data4.replaceAll("\\[|\\]", "");
		

		Scanner s1 = new Scanner(data1);
		Scanner s2 = new Scanner(data2);
		Scanner s3 = new Scanner(data3);
		Scanner s4 = new Scanner(data4);

		s1.useDelimiter(",");
		s2.useDelimiter(",");
		s3.useDelimiter(",");
		s4.useDelimiter(",");
		
		DecimalFormat numberFormat = new DecimalFormat("#.00");

		float number = 0;
		float number2 = 0;
		fw.write("[{\"header\":");
		fw.write(jsonFile2ObjU.get("header").toString());
		fw.write(",\"data\":[");

		for(int i = 0; i < 65160; i++)
		{
			
			double d1 = s1.nextFloat();
			double d2 = s2.nextFloat();
			double d3 = s3.nextFloat();
			double d4 = s4.nextFloat();
			
			number = (float) Math.sqrt((d1 * d1) + (d3 * d3));
			number2 = (float) Math.sqrt((d2 * d2) + (d4 * d4));
			double tempnum = Math.abs(number - number2);

			fw.write(numberFormat.format(tempnum));
			
			if(s1.hasNext())
				fw.write(",");
		}

		fw.write("]}],");
		
		System.out.println("Scalar File Created");

		s1.close();
		s2.close();
		s3.close();
		s4.close();
		fw.close();
		

	}

	public static void createDifferenceJSONFile() throws IOException
	{
		FileWriter fw = new FileWriter(new File("Quotaero Data/current-wind_difference-surface-level-gfs-1.0.json"));
		String data1 = jsonFile1ObjU.get("data").toString();
		String data2 = jsonFile2ObjU.get("data").toString();

		data1 = data1.replaceAll("\\[|\\]", "");
		data2 = data2.replaceAll("\\[|"
				+ "\\]", "");
		//		data1 = data1.replaceFirst("\\]", "");
		//		data2 = data2.replaceFirst("\\]", "");

		Scanner s1 = new Scanner(data1);
		Scanner s2 = new Scanner(data2);

		s1.useDelimiter(",");
		s2.useDelimiter(",");

		DecimalFormat numberFormat = new DecimalFormat("#.00");

		float number = 0;
		fw.write("[{\"header\":");
		fw.write(jsonFile2ObjU.get("header").toString());
		fw.write(",\"data\":[");

		for(int i = 0; i < 65160; i++)
		{
			number = s1.nextFloat() - s2.nextFloat();

			if (number < 1.0 && number >= 0)
				fw.write("0");
			else if (number < 0 && number > -1)
				fw.write("-0");
			fw.write(numberFormat.format(Math.abs(number)));
			if(s1.hasNext())
				fw.write(",");
		}

		fw.write("]},");
		fw.write("{\"header\":");
		fw.write(jsonFile2ObjV.get("header").toString());
		fw.write(",\"data\":[");

		s1.close();
		s2.close();

		//V component
		data1 = jsonFile1ObjV.get("data").toString();
		data2 = jsonFile2ObjV.get("data").toString();

		data1 = data1.replaceAll("\\[|\\]", "");
		data2 = data2.replaceAll("\\[|\\]", "");

		s1 = new Scanner(data1);
		s2 = new Scanner(data2);

		s1.useDelimiter(",");
		s2.useDelimiter(",");


		for(int i = 0; i < 65160; i++)
		{
			number = s1.nextFloat() - s2.nextFloat();

			if (number < 1.0 && number >= 0)
				fw.write("0");
			else if (number < 0 && number > -1)
				fw.write("-0");
			fw.write(numberFormat.format(Math.abs(number)));
			if(s1.hasNext())
				fw.write(",");
		}

		fw.write("]}]");

		System.out.println("Difference File Created");

		s1.close();
		s2.close();
		fw.close();
		//		
		//		data1 = f1.nextLine().split(",");
		//		data2 = f2.nextLine().split(",");
		//
		//
		//		number = new Float(data1[0]) - new Float(data2[0]);
		//		pw.print(numberFormat.format(number));
		//
		//		for(int i = 0; i < 65159; i++)
		//		{
		//			//System.out.println(new Float(data1[i]) - new Float(data2[i]));
		//			number = new Float(data1[i]) - new Float(data2[i]);
		//			pw.print(",");
		//			if (number < 1.0 && number >= 0)
		//				pw.print("0");
		//			else if (number < 0 && number > -1)
		//				pw.print("-0");
		//			pw.print(numberFormat.format(Math.abs(number)));
		//		}
		//
		//		pw.print(f3.nextLine());
		//
		//		f1.close();
		//		f2.close();
		//		f3.close();
		//		f4.close();
		//		pw.close();
	}


	public static void createJSONObj() throws FileNotFoundException
	{

		//First file
		//creates first json object from u-grd model file1
		Scanner s1 = new Scanner(new File("Quotaero Data/u-grd1.json"));

		String json = "";
		s1.useDelimiter("]}");

		json += s1.next();
		json += "]}";

		//for some reason the original file is in an array, this removes the array
		json = json.replaceFirst("\\[", "");
		jsonFile1ObjU = new JSONObject(json);
		JSONObject miniJSONModel = new JSONObject(jsonFile1ObjU.get("header").toString());

		System.out.println(miniJSONModel.get("parameterNumberName"));
		numberPoints = (int)miniJSONModel.get("numberPoints");
		System.out.println(numberPoints);

		s1.close();


		//creates second json object from v-grd model file1
		json = "";
		s1 = new Scanner(new File("Quotaero Data/v-grd1.json"));
		s1.useDelimiter("]}");

		json += s1.next();
		json += "]}";
		json = json.replaceFirst("\\[", "");
		jsonFile1ObjV = new JSONObject(json);
		miniJSONModel = new JSONObject(jsonFile1ObjV.get("header").toString());


		System.out.println(miniJSONModel.get("parameterNumberName"));
		numberPoints = (int)miniJSONModel.get("numberPoints");
		System.out.println(numberPoints);

		System.out.println("File 1 extracted");
		s1.close();


		//Second File
		//creates first json object from u-grd model file2
		s1 = new Scanner(new File("Quotaero Data/u-grd2.json"));

		json = "";
		s1.useDelimiter("]}");

		json += s1.next();
		json += "]}";

		//for some reason the original file is in an array, this removes the array
		json = json.replaceFirst("\\[", "");
		jsonFile2ObjU = new JSONObject(json);
		miniJSONModel = new JSONObject(jsonFile2ObjU.get("header").toString());

		System.out.println(miniJSONModel.get("parameterNumberName"));
		numberPoints = (int)miniJSONModel.get("numberPoints");
		System.out.println(numberPoints);

		s1.close();


		//creates second json object from v-grd model file2
		json = "";
		s1 = new Scanner(new File("Quotaero Data/v-grd2.json"));
		s1.useDelimiter("]}");

		json += s1.next();
		json += "]}";
		json = json.replaceFirst("\\[", "");
		jsonFile2ObjV = new JSONObject(json);
		miniJSONModel = new JSONObject(jsonFile2ObjV.get("header").toString());


		System.out.println(miniJSONModel.get("parameterNumberName"));
		numberPoints = (int)miniJSONModel.get("numberPoints");
		System.out.println(numberPoints);
		System.out.println("File 2 extracted");
		s1.close();


		//		s1 = new Scanner(new File("current-wind_actual-surface-level-gfs-1.0.json"));
		//		s1.useDelimiter("}}");
		//		json = "";
		//
		//		//creates first json object from actual file
		//		json += s1.next() + "}}";
		//		json = json.replaceFirst("\\[", "");
		//		System.out.println(json.substring(0,100));
		//		jsonFile1ObjU = new JSONObject(json);
		//		System.out.println(jsonFile1ObjU.get("header"));
		//		JSONObject miniJSONActual = new JSONObject(jsonFile1ObjU.get("header").toString());
		//		numberPoints = (int)miniJSONActual.get("numberPoints");
		//
		//		System.out.println(numberPoints);
		//		
		//		//creates second json object from actual file
		//		json = "";
		//		System.out.println(json.substring(0,100));
		//		json += s1.next();
		//		json += "}}";
		//		json = json.replaceFirst(",", "");
		//		jsonFile1ObjV = new JSONObject(json);
		//		//		jsonFile1[1] = (s1.next() + "}}").replaceFirst("//[", "");
		//		System.out.println(jsonFile1ObjV.get("header"));
		//		s1.close();

	}

	public static void doWork()
	{
		System.out.println(jsonFile1ObjU.getJSONArray("data").toString());
		System.out.println(jsonFile1ObjV.getJSONArray("data").toString());
		System.out.println(jsonFile2ObjU.getJSONArray("data").toString());
		System.out.println(jsonFile2ObjV.getJSONArray("data").toString());
	}


	public static void countLength() throws FileNotFoundException{
		Scanner f1 = new Scanner(new File("tosplit"));
		String[] data1 = f1.nextLine().split(",");
		PrintWriter pw = new PrintWriter(new File("temperature_files"));
		pw.write(data1[0]);
		double min = 99999, max = 0;
		for(int i = 1; i < 65160; i++)
		{
			pw.write("," + data1[i]);
			if (Double.parseDouble(data1[i]) < min)
				min = Double.parseDouble(data1[i]);
			if (Double.parseDouble(data1[i]) > max)
				max = Double.parseDouble(data1[i]);
		}
		System.out.println("Min is" + min + " max is " + max);
		System.out.println(data1.length);
	}

	public static void createTemp() throws FileNotFoundException{
		PrintWriter pw = new PrintWriter(new File("temperature_files"));
		pw.write("293");
		for(int i = 0; i < 65159; i++)
		{
			Random rand = new Random();

			// nextInt is normally exclusive of the top value,
			// so add 1 to make it inclusive
			int randomNum = rand.nextInt((314 - 226) + 1) + 226;
			//String randomNum = "0.3";
			pw.write("," + randomNum);
		}
		pw.close();
	}

	public static void vectorToScalar() throws FileNotFoundException{
		Scanner f1 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/model1.txt"));
		Scanner f2 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/actual1.txt"));
		Scanner f3 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/model2.txt"));
		Scanner f4 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/actual2.txt"));
		PrintWriter pw = new PrintWriter(new File("current-temp-surface-level-gfs-1.0.json"));
		String[] data1 = f1.nextLine().split(",");
		String[] data3 = f1.nextLine().split(",");

		String[] data2 = f2.nextLine().split(",");
		String[] data4 = f2.nextLine().split(",");

		System.out.println("File 1 Length" + data1.length + " " + data3.length);
		System.out.println("File 2 Length" + data2.length + " " + data4.length);
		DecimalFormat numberFormat = new DecimalFormat("#.00");


		double number = Math.sqrt((Double.parseDouble(data1[0]) * Double.parseDouble(data1[0])) + (Double.parseDouble(data3[0]) * Double.parseDouble(data3[0])));
		double number2 = Math.sqrt((Double.parseDouble(data2[0]) * Double.parseDouble(data2[0])) + (Double.parseDouble(data4[0]) * Double.parseDouble(data4[0])));

		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((320 - 230) + 1) + 320;
		double min =999999;
		double max = 0;
		double sum = 0;
		pw.print(f3.nextLine() + numberFormat.format(Math.abs(number - number2) + 1));

		for(int i = 0; i < 65159; i++)
		{
			//System.out.println(new Float(data1[i]) - new Float(data2[i]));
			number = Math.sqrt((Double.parseDouble(data1[i]) * Double.parseDouble(data1[i])) + (Double.parseDouble(data3[i]) * Double.parseDouble(data3[i])));
			number2 = Math.sqrt((Double.parseDouble(data2[i]) * Double.parseDouble(data2[i])) + (Double.parseDouble(data4[i]) * Double.parseDouble(data4[i])));
			double tempnum = Math.abs(number - number2) + 1;
			System.out.println(tempnum);
			//randomNum = rand.nextInt((320 - 230) + 1) + 320;
			pw.print("," + numberFormat.format(tempnum));
			sum += tempnum;
			if (tempnum < min)
				min = tempnum;
			if (tempnum > max)
				max = tempnum;
		}
		System.out.println("Min is" + min + " max is " + max + " average is " + sum/65160);


		pw.print(f3.nextLine());

		f1.close();
		f2.close();
		f3.close();
		f4.close();
		pw.close();
	}

	public static void subtract() throws FileNotFoundException{


		Scanner f1 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/model1.txt"));
		Scanner f2 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/actual1.txt"));
		Scanner f3 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/model2.txt"));
		Scanner f4 = new Scanner(new File("/Users/jeffs5s/Documents/workspace - new/Sandbox/src/actual2.txt"));
		PrintWriter pw = new PrintWriter(new File("(try)current-wind_difference-surface-level-gfs-1.0.json"));
		String[] data1 = f1.nextLine().split(",");
		String[] data2 = f2.nextLine().split(",");
		System.out.println(data1.length);
		System.out.println(data2.length);
		DecimalFormat numberFormat = new DecimalFormat("#.00");

		float number = new Float(data1[0]) - new Float(data2[0]);
		pw.print(f3.nextLine() + numberFormat.format(number));

		for(int i = 1; i < 65159; i++)
		{
			//System.out.println(new Float(data1[i]) - new Float(data2[i]));
			number = new Float(data1[i]) - new Float(data2[i]);

			pw.print(",");
			if (number < 1.0 && number >= 0)
				pw.print("0");
			else if (number < 0 && number > -1)
				pw.print("-0");
			pw.print(numberFormat.format(Math.abs(number)));
		}

		pw.print(f3.nextLine());
		pw.print(f3.nextLine());
		data1 = f1.nextLine().split(",");
		data2 = f2.nextLine().split(",");


		number = new Float(data1[0]) - new Float(data2[0]);
		pw.print(numberFormat.format(number));

		for(int i = 0; i < 65159; i++)
		{
			//System.out.println(new Float(data1[i]) - new Float(data2[i]));
			number = new Float(data1[i]) - new Float(data2[i]);
			pw.print(",");
			if (number < 1.0 && number >= 0)
				pw.print("0");
			else if (number < 0 && number > -1)
				pw.print("-0");
			pw.print(numberFormat.format(Math.abs(number)));
		}

		pw.print(f3.nextLine());

		f1.close();
		f2.close();
		f3.close();
		f4.close();
		pw.close();
		System.out.println("Subtract Done");
	}
}
