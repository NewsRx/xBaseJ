/**
 * 
 */
package org.xBaseJ.examples;

import org.xBaseJ.DBF;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.NumField;

/**
 * @author joseph mcverry
 *
 */
public class example4 {

    public static void main(String args[]){

            String dow[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    
            try{
                    //Open dbf file
                    DBF classDB=new DBF("class.dbf");
                    
                    //Define fields
                    CharField classId  = (CharField) classDB.getField("classId");      
                    CharField name  = (CharField) classDB.getField("className");    
                    CharField teacher = (CharField) classDB.getField("teacherId");    
                    CharField time = (CharField) classDB.getField("timeMeet");     
                    NumField credits = (NumField) classDB.getField("credits");      
                    LogicalField underGrad  = (LogicalField) classDB.getField("UnderGrad");    
                    CharField daysMeet  = (CharField) classDB.getField("daysMeet");     

                    DBF teacherDB=new DBF("teacher.dbf");
		            teacherDB.useIndex("teacher.ndx");     
                    
                    //Define fields
                    CharField teacherName = (CharField) teacherDB.getField("teacherNm");    

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
                                 teacherDB.find(teacher.get()); 
                                    // this is not perfect for two reasons
                                    // first,  not catching record not found exception
                                    // second,  dbase logic requires returning a record with an equal or greater than key value
                                 System.out.println("   Taught by: " + teacherName.get());
                            } // end if undergrad test
                       }
                    
            }catch(Exception e){
                    e.printStackTrace();
            }
    }
}
                    
