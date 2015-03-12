import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

public class DifferenceCreator {

	private static JSONObject jsonFile1ObjU;
	private static JSONObject jsonFile1ObjV;
	private static JSONObject jsonFile2ObjU;
	private static JSONObject jsonFile2ObjV;

	public static void main(String args[]){

		try {
			createJSONObj();
			createBasicJSONFiles();
			createDifferenceScalarJSONFile();
			//only uncomment if it works
			//createDifferenceJSONFile(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//this creates the basic files to be used as the model and actual data
	public static void createBasicJSONFiles() throws IOException, JSONException
	{
		//change filePath location to where you want the files to be stored
		String filePath1 = "Quotaero Data/current-wind_model-surface-level-gfs-1.0.json";
		String filePath2 = "Quotaero Data/current-wind_actual-surface-level-gfs-1.0.json";

		//formats to how nullschool code needs it to be
		FileWriter fw = new FileWriter(new File(filePath1));
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
		System.out.println("File1 basic JSON done in " + filePath1);

		//formats to how nullschool code needs it to be
		fw = new FileWriter(new File(filePath2));
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
		System.out.println("File2 basic JSON done in " + filePath2);
	}

	//creates a scalar file to work as the "difference layer"
	//we make it into a temperature file since the functionality of that
	//type of file does not move and shows a noticeable difference
	public static void createDifferenceScalarJSONFile() throws IOException, JSONException
	{
		//change the filePath to change where the output file is stored
		String filePath = "Quotaero Data/current-temp-surface-level-gfs-1.0.json";
		FileWriter fw = new FileWriter(new File(filePath));
		String data1 = jsonFile1ObjU.get("data").toString();
		String data2 = jsonFile2ObjU.get("data").toString();
		String data3 = jsonFile1ObjV.get("data").toString();
		String data4 = jsonFile2ObjV.get("data").toString();

		//the header for this is a specific one
		//this should be changed in the future to account for different dates
		Scanner header = new Scanner(new File("Quotaero Data/temperature_header.txt"));

		//some brackets need to be removed for consistency
		data1 = data1.replaceAll("\\[|\\]", "");
		data2 = data2.replaceAll("\\[|\\]", "");
		data3 = data3.replaceAll("\\[|\\]", "");
		data4 = data4.replaceAll("\\[|\\]", "");


		Scanner s1 = new Scanner(data1);
		Scanner s2 = new Scanner(data2);
		Scanner s3 = new Scanner(data3);
		Scanner s4 = new Scanner(data4);

		//this will let us go one by one through each data point
		s1.useDelimiter(",");
		s2.useDelimiter(",");
		s3.useDelimiter(",");
		s4.useDelimiter(",");

		//if formatting is not used, the numbers have a lot of trailing information
		//this also cuts down on the total file size
		DecimalFormat numberFormat = new DecimalFormat("#.00");

		float number = 0;
		float number2 = 0;
		fw.write("[{\"header\":");
		while(header.hasNext())
			fw.write(header.next());
		fw.write(",\"data\":[");

		//gets the value from 
		//abs( sqrt(d1^2 + d3^2) - sqrt(d2^2 + d4^2))
		for(int i = 0; i < 65160; i++)
		{
			double d1 = s1.nextFloat();
			double d2 = s2.nextFloat();
			double d3 = s3.nextFloat();
			double d4 = s4.nextFloat();

			number = (float) Math.sqrt((d1 * d1) + (d3 * d3));
			number2 = (float) Math.sqrt((d2 * d2) + (d4 * d4));
			double tempnum = Math.abs(number - number2);

			//if the number is less than one, the temperature file cannot display it properly
			//the number will also never be less than 0 since it is an absolute value
			if (tempnum < 1.0)
				fw.write("1.00");
			else 
				fw.write(numberFormat.format(tempnum));

			//this is here to not write the last comma
			if(i != 65159)
				fw.write(",");
		}

		//ends the file
		fw.write("]}]");

		System.out.println("Scalar File Created in " + filePath);

		s1.close();
		s2.close();
		s3.close();
		s4.close();
		header.close();
		fw.close();
	}

	//currently this does not work
	//we couldn't find out why it wouldn't work with the null school code
	//don't run and replace the current difference file until this works
	public static void createDifferenceJSONFile() throws IOException, JSONException
	{
		//change the filePath to change where the file is stored
		String filePath = "Quotaero Data/current-wind_difference-surface-level-gfs-1.0.json";
		FileWriter fw = new FileWriter(new File(filePath));
		
		//U component
		String data1 = jsonFile1ObjU.get("data").toString();
		String data2 = jsonFile2ObjU.get("data").toString();

		data1 = data1.replaceAll("\\[|\\]", "");
		data2 = data2.replaceAll("\\[|\\]", "");

		Scanner s1 = new Scanner(data1);
		Scanner s2 = new Scanner(data2);

		//used to go line by line
		s1.useDelimiter(",");
		s2.useDelimiter(",");

		//if formatting is not used, the file size can increase significantly
		//this also allows for consistency 
		DecimalFormat numberFormat = new DecimalFormat("#.00");

		float number = 0;
		fw.write("[{\"header\":");
		fw.write(jsonFile2ObjU.get("header").toString());
		fw.write(",\"data\":[");

		for(int i = 0; i < 65160; i++)
		{
			number = s1.nextFloat() - s2.nextFloat();

			//the files must have a 0 in front of the decimal
			//if not, it thinks that it's trying to run some kind of method instead
			if (number < 1.0 && number >= 0)
				fw.write("0");
			else if (number < 0 && number > -1)
				fw.write("-0");
			fw.write(numberFormat.format(Math.abs(number)));
			
			//used to not write the last comma
			if(i != 65159)
				fw.write(",");
		}

		//ends the first section and begins the appending of the second section
		//U data on top, V data on bottom
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

			//the files must have a 0 in front of the decimal
			//if not, it thinks that it's trying to run some kind of method instead
			if (number < 1.0 && number >= 0)
				fw.write("0");
			else if (number < 0 && number > -1)
				fw.write("-0");
			fw.write(numberFormat.format(Math.abs(number)));
			
			//used to not write the last comma
			if(i != 65159)
				fw.write(",");
		}

		fw.write("]}]");

		System.out.println("Difference File Created in "+ filePath);

		s1.close();
		s2.close();
		fw.close();
	}


	public static void createJSONObj() throws FileNotFoundException, JSONException
	{
		//files be the u data and the v data for both files

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

		System.out.println("File 1: " + miniJSONModel.get("parameterNumberName"));

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


		System.out.println("File 1: " + miniJSONModel.get("parameterNumberName"));

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

		System.out.println("File 2: " + miniJSONModel.get("parameterNumberName"));

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


		System.out.println("File 2: " + miniJSONModel.get("parameterNumberName"));
		System.out.println("File 2 extracted");
		s1.close();
	}

}
