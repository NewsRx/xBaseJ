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

public class example2 {

	public static void main(String args[]){

		String dow[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};


		try{
			//Open dbf file
			DBF classDB=new DBF("class.dbf");

			//Define fields
			CharField classId  = (CharField) classDB.getField("classId");      
			CharField name  = (CharField) classDB.getField("className");    
			//CharField teacher = (CharField) classDB.getField("teacherId");    
			CharField daysMeet  = (CharField) classDB.getField("daysMeet");     
			CharField time = (CharField) classDB.getField("timeMeet");     
			NumField credits = (NumField) classDB.getField("credits");      
			LogicalField underGrad  = (LogicalField) classDB.getField("UnderGrad");    

			for (int i = 1; i <= classDB.getRecordCount(); i++)
			{
				classDB.read();
				if (underGrad.getBoolean()) // just show undergrad courses
				{
					System.out.println(name.get() + " id " + classId.get());
					System.out.print("   Meets at: " + time.get() + " on ");
					for (int j = 0; j < 7; j++) 
					{  
						if (daysMeet.get().charAt(j) == 'Y') 
							System.out.print(dow[j] + " ");
					}
					System.out.println("");
					System.out.println("   Credits: " + credits.get());
				} // end if undergrad test
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

