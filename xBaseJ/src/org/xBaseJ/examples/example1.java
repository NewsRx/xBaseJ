/**
 * 
 */
package org.xBaseJ.examples;

/**
 * @author joseph mcverry
 *
 */



import org.xBaseJ.DBF;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.NumField;

public class example1 {


	public static void main(String args[]){


		try{
			//Create a new dbf file
			DBF aDB=new DBF("class.dbf",true);

			//Create the fields

			CharField classId = new CharField("classId",9);            
			CharField className = new CharField("className",25);         
			CharField teacherId = new CharField("teacherId",9);          
			CharField daysMeet = new CharField("daysMeet",7);           
			CharField timeMeet =new CharField("timeMeet",4);           
			NumField credits = new NumField("credits",2, 0);   
			LogicalField UnderGrad = new LogicalField("UnderGrad");         


			//Add field definitions to database
			aDB.addField(classId);
			aDB.addField(className);
			aDB.addField(teacherId);
			aDB.addField(daysMeet);
			aDB.addField(timeMeet);
			aDB.addField(credits);
			aDB.addField(UnderGrad);

			aDB.createIndex("classId.ndx","classId",true,true);     //  true - delete ndx, true - unique index, 
			aDB.createIndex("TchrClass.ndx","teacherID+classId", true, false);     //true - delete NDX,  false - unique index, 
			System.out.println("index created");

			classId.put("JAVA10100");
			className.put("Introduction to JAVA");
			teacherId.put("120120120");
			daysMeet.put("NYNYNYN");
			timeMeet.put("0800");
			credits.put(3);
			UnderGrad.put(true);

			aDB.write();

			classId.put("JAVA10200");
			className.put("Intermediate JAVA");
			teacherId.put("300020000");
			daysMeet.put("NYNYNYN");
			timeMeet.put("0930");
			credits.put(3);
			UnderGrad.put(true);

			aDB.write();

			classId.put("JAVA501");
			className.put("JAVA And Abstract Algebra");
			teacherId.put("120120120");
			daysMeet.put("NNYNYNN");
			timeMeet.put("0930");
			credits.put(6);
			UnderGrad.put(false);

			aDB.write();


		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
