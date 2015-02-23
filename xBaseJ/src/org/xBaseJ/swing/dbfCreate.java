package org.xBaseJ.swing;
/**
 * xBaseJ - Java access to dBase files
 *<p>Copyright 1997-2014 - American Coders, LTD  - Raleigh NC USA
 *<p>All rights reserved
 *<p>Currently supports only dBase III format DBF, DBT and NDX files
 *<p>                        dBase IV format DBF, DBT, MDX and NDX files
*<p>American Coders, Ltd
*<br>P. O. Box 97462
*<br>Raleigh, NC  27615  USA
*<br>1-919-846-2014
*<br>http://www.americancoders.com
@author Joe McVerry, American Coders Ltd.
@Version 20140310
*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library Lesser General Public
 * License along with this library; if not, write to the Free
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *20110119  Joe McVerry (jrm)   Added static field type and CurrencyField class.
*/

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import org.xBaseJ.DBF;
import org.xBaseJ.DBFTypes;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.awt.dbfFileFilter;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.DateField;
import org.xBaseJ.fields.CurrencyField;
import org.xBaseJ.fields.Field;
import org.xBaseJ.fields.FloatField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.MemoField;
import org.xBaseJ.fields.NumField;
import org.xBaseJ.fields.PictureField;


public class dbfCreate extends JFrame implements ActionListener, WindowListener, ListSelectionListener

{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Vector<String> names;
    String fname = null;

    JTable table;
    JMenuBar menuBar;
    JMenu menuFile, menuType, menuField;
    JMenuItem menuCreate;
    JMenuItem menuOpen;

    JCheckBoxMenuItem typeIV, typeIII, typeFP;
    JMenuItem menuInsertBefore, menuInsertAfter;
    JMenuItem menuDelete;
    JScrollPane tableScrollPane;

    dbfCreateModel tableModel;
    private String lastDirectory = "./.";

    public dbfCreate() {

        setTitle("org.xBaseJ Version: " + DBF.xBaseJVersion + " Create dBase File: unnamed");

        tableModel = new dbfCreateModel();

        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(this);

        setUpColumnModel();

        tableScrollPane = new JScrollPane(table);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuBar.add(menuFile);
        menuOpen = new JMenuItem("Copy from..");
        menuCreate = new JMenuItem("Create..");
        menuFile.add(menuOpen);
        menuFile.add(menuCreate);


        menuField = new JMenu("Field");
        menuBar.add(menuField);
        menuInsertBefore = new JMenuItem("Insert before");
        menuInsertAfter = new JMenuItem("Insert after");
        menuDelete = new JMenuItem("Delete");
        menuField.add(menuInsertBefore);
        menuField.add(menuInsertAfter);
        menuField.add(menuDelete);

        menuType = new JMenu("Type: dBaseIV");
        menuBar.add(menuType);
        typeIV = new JCheckBoxMenuItem("dBaseIV", true);
        menuType.add(typeIV);
        typeIII  = new JCheckBoxMenuItem("dBaseIII", false);
        menuType.add(typeIII);
        typeFP = new JCheckBoxMenuItem("FoxPro", false);
        menuType.add(typeFP);

        getContentPane().setLayout(new GridLayout(1, 0));


      	Dimension min = new Dimension(200,150);
      	tableScrollPane.setMinimumSize(min);
        getContentPane().add(tableScrollPane);



        menuOpen.addActionListener(this);
        menuCreate.addActionListener(this);
        menuInsertBefore.addActionListener(this);
        menuInsertAfter.addActionListener(this);
        menuDelete.addActionListener(this);
        typeIV.addActionListener(this);
        typeIII.addActionListener(this);
        typeFP.addActionListener(this);

        addWindowListener(this);
    }

    public void actionPerformed(ActionEvent ae) {


       if (ae.getSource() == menuOpen)
          {
                JFileChooser jfc = new JFileChooser(new File(lastDirectory +"/*.dbf"));
                jfc.addChoosableFileFilter(new dbfFileFilter());
                jfc.showOpenDialog(this);
                File fil = jfc.getSelectedFile();
                if (fil == null)
                    {repaint(); return;}

                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                lastDirectory = fil.getPath();
                fname = fil.getAbsolutePath();
                getContentPane().remove(tableScrollPane);


                try {
		   DBF dbf = new DBF(fname);
                   tableModel = new dbfCreateModel(dbf);
                   setTitle("Create dBase File: "+fil.getName());
                    if (dbf.getVersion() == DBFTypes.DBASEIV || dbf.getVersion() == DBFTypes.DBASEIV_WITH_MEMO) {
		       menuType.setText("Type: dBaseIV");
		       typeIV.setState(true);
		       typeIII.setState(false);
		       typeFP.setState(false);
    	            }
                  else if (dbf.getVersion() == DBFTypes.DBASEIII || dbf.getVersion() == DBFTypes.DBASEIII_WITH_MEMO) {
   		       menuType.setText("Type: dBaseIII");
		       typeIV.setState(false);
		       typeIII.setState(true);
		       typeFP.setState(false);
      	          }
                 else if (dbf.getVersion() == DBFTypes.FOXPRO_WITH_MEMO) {
		       menuType.setText("Type: FoxPro");
		       typeIV.setState(false);
		       typeIII.setState(false);
		       typeFP.setState(true);
     	            }

		   }
		 catch (xBaseJException xex)
			     {
      				JOptionPane.showMessageDialog(this, xex.getMessage(), "xBaseJException Occurred", JOptionPane.ERROR_MESSAGE);
   				    repaint();
   				    return;
				 }
			    catch (IOException ioex)
			     {
      				JOptionPane.showMessageDialog(this, ioex.getMessage(), "IOException Occurred", JOptionPane.ERROR_MESSAGE);
   				    repaint();
   				    return;
				 }

                table = new JTable(tableModel);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.getSelectionModel().addListSelectionListener(this);
                setUpColumnModel();
                tableScrollPane = new JScrollPane(table);


	       	Dimension min = new Dimension(200,150);
	       	tableScrollPane.setMinimumSize(min);

                getContentPane().add(tableScrollPane);

                table.revalidate();
                table.repaint();
                validate();

                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));


           }

       if (ae.getSource() == menuCreate) {

           JFileChooser jfc = new JFileChooser(new File(lastDirectory +"/*.dbf"));
           jfc.addChoosableFileFilter(new dbfFileFilter());
           jfc.showSaveDialog(this);
           File fil = jfc.getSelectedFile();
           if (fil == null)
                {repaint(); return;}
           if (fil.exists())
             {
			   int resp = JOptionPane.showConfirmDialog(this, "File " + fil.getName() + " already exists.  Do you want to replace it?",
				          "File Already Exists",
				          JOptionPane.YES_NO_OPTION);
               if (resp == JOptionPane.YES_OPTION) ;
               else {repaint(); return;}
		   }
           this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
           lastDirectory = fil.getPath();
           fname = fil.getAbsolutePath();
           this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
           TableCellEditor editor = table.getCellEditor();
           if (editor != null)
              editor.stopCellEditing();
           DBFTypes type;
           if (typeIII.getState()) type = DBFTypes.DBASEIII;
           if (typeFP.getState()) type = DBFTypes.FOXPRO_WITH_MEMO;
           else type = DBFTypes.DBASEIV;
           try {
                tableModel.create(fil, type);
			}
  	       catch (xBaseJException xex)
  	        {
 		      JOptionPane.showMessageDialog(this, xex.getMessage(), "xBaseJException Occurred", JOptionPane.ERROR_MESSAGE);
   			  repaint();
              this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
   			  return;
  		    }
  	       catch (NumberFormatException nfex)
  	        {
      		   JOptionPane.showMessageDialog(this, nfex.getMessage(), "NumberFormatException Occurred", JOptionPane.ERROR_MESSAGE);
   			   repaint();
              this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
   			   return;
  		    }
  	       catch (IOException ioex)
  	        {
      		   JOptionPane.showMessageDialog(this, ioex.getMessage(), "IOException Occurred", JOptionPane.ERROR_MESSAGE);
   			   repaint();
              this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
   			   return;
  		    }
/*  	       catch (Exception ex)
  	        {
      		   JOptionPane.showMessageDialog(this, ex.getMessage(), "Exception Occurred", JOptionPane.ERROR_MESSAGE);
   			   repaint();
              this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
   			   return;
  		    }
*/
           this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           setTitle("Create dBase File: "+fil.getName());
       }

       if (ae.getSource() == typeIV) {
		   menuType.setText("Type: dBaseIV");
		   typeIV.setState(true);
		   typeIII.setState(false);
		   typeFP.setState(false);
	   }

       if (ae.getSource() == typeIII) {
		   menuType.setText("Type: dBaseIII");
		   typeIV.setState(false);
		   typeIII.setState(true);
		   typeFP.setState(false);
	   }

       if (ae.getSource() == typeFP) {
		   menuType.setText("Type: FoxPro");
		   typeIV.setState(false);
		   typeIII.setState(false);
		   typeFP.setState(true);
	   }

       if (ae.getSource() == menuInsertBefore) {

     	 if (tableModel.getRowCount() < 1)
    	     tableModel.insert(0);
    	 else
     	 if (table.getSelectedRow() < 0)
    	     tableModel.insert(tableModel.getRowCount());
    	 else
    	    tableModel.insert(table.getSelectedRow());

    	 table.repaint();
	   }

       if (ae.getSource() == menuInsertAfter) {

     	 if (tableModel.getRowCount() < 1)
    	     tableModel.insert(0);
    	 else
     	 if (table.getSelectedRow() < 0)
    	     tableModel.insert(tableModel.getRowCount()-1);
    	 else
    	    tableModel.insert(table.getSelectedRow()+1);

    	 table.repaint();
	   }

       if (ae.getSource() == menuDelete) {
     	 if (table.getSelectedRow() < 0);
    	 else
    	    tableModel.delete(table.getSelectedRow());
    	 table.repaint();
	   }

      this.repaint();

  }

 public void setUpColumnModel()
 {
	           JComboBox jcb = new JComboBox();
	           jcb.addItem("Char");
	           jcb.addItem("Date");
	           jcb.addItem("Float");
	           jcb.addItem("Logical");
	           jcb.addItem("Memo");
	           jcb.addItem("Num");
	           jcb.addItem("Picture");
	           jcb.addItem("Currency");
	           table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(jcb));

 }

 public static void main(String[] args) {

        dbfCreate frame;

        frame = new dbfCreate();

        frame.setSize(400, 125);
        frame.setVisible(true);
    }

 public void windowClosing(WindowEvent we) {System.exit(0);}
 public void windowOpened(java.awt.event.WindowEvent we) {}
 public void windowClosed(java.awt.event.WindowEvent we) {System.exit(0);}
 public void windowIconified(java.awt.event.WindowEvent we) {}
 public void windowDeiconified(java.awt.event.WindowEvent we) {}
 public void windowActivated(java.awt.event.WindowEvent we) {}
 public void windowDeactivated(java.awt.event.WindowEvent we) {}

 public void valueChanged(ListSelectionEvent e) {
       e.getSource();
	 /* if (lsm.isSelectionEmpty()) ;
	 else
	    dbfrp.goTo(lsm.getMinSelectionIndex()+1); */
 }

}


class dbfCreateModel extends AbstractTableModel
    {
     /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	int inRow;
     String columnName[] =
       {"Name", "Type", "Length", "Dec. Position", "Indexed", "Index Name"};
     int columnCount = 6;
     int rowCount;
     Boolean deleted[];
     JFrame parent;
     Vector<String> name;
     Vector<String> type;
     Vector<String> length;
     Vector<String> decPos;
     Vector<Boolean> indexInd;
     Vector<String> indexName;



    public dbfCreateModel(DBF dbf)
       throws xBaseJException
      {
          name = new Vector<String>();
          type = new Vector<String>();
          length = new Vector<String>();
          decPos = new Vector<String>();
          indexInd = new Vector<Boolean>();
          indexName = new Vector<String>();
          Field f;
          for (int i = 1; i <= dbf.getFieldCount(); i++)
             {
				 f = dbf.getField(i);
				 name.addElement(f.getName());
				 switch (f.getType())
				 {
					 case CharField.type:
				 	   type.addElement("Char");
				 	   break;
					 case DateField.type:
				 	   type.addElement("Date");
				 	   break;
					 case FloatField.type:
				 	   type.addElement("Float");
				 	   break;
					 case LogicalField.type:
				 	   type.addElement("Logical");
				 	   break;
					 case MemoField.type:
				 	   type.addElement("Memo");
				 	   break;
					 case NumField.type:
				 	   type.addElement("Num");
				 	   break;
					 case PictureField.type:
					 	   type.addElement("Picture");
					 	   break;
					 case CurrencyField.type:
					 	   type.addElement("Currency");
					 	   break;
				 	 default:
				 	   type.addElement("??????");
				 }

				 length.addElement(""+f.getLength());
				 decPos.addElement(""+f.getDecimalPositionCount());
				 indexInd.addElement(Boolean.valueOf(false));
				 indexName.addElement("");
			 }

	  }

    public dbfCreateModel()
      {
          rowCount = 1;
          name = new Vector<String>();
          type = new Vector<String>();
          length = new Vector<String>();
          decPos = new Vector<String>();
          indexInd = new Vector<Boolean>();
          indexName = new Vector<String>();


          name.addElement("");
          type.addElement("Char");
          length.addElement("1");
          decPos.addElement("0");
          indexInd.addElement(Boolean.valueOf(false));
          indexName.addElement("");

      }

    public void insert(int row)
      {
          name.insertElementAt("", row);
          type.insertElementAt("Char", row);
          length.insertElementAt("1", row);
          decPos.insertElementAt("0", row);
          indexInd.insertElementAt(Boolean.valueOf(false), row);
          indexName.insertElementAt("", row);
          fireTableRowsInserted(row,row);
	  }

    public void delete(int row)
      {
          name.removeElementAt(row);
          type.removeElementAt(row);
          length.removeElementAt(row);
          decPos.removeElementAt(row);
          indexInd.removeElementAt(row);
          indexName.removeElementAt(row);
          fireTableRowsDeleted(row,row);
	  }

    public void create(File fil, DBFTypes type2)
      throws xBaseJException, NumberFormatException, IOException
	  {
		  int i;
		  Field flds[] = new Field[getRowCount()];
		  String named, typed, lengths, decpoints;
		  for (i = 0; i < getRowCount(); i++)
		    {
			 named = (String) getValueAt(i, 0);
			 typed = (String) getValueAt(i, 1);
			 lengths = (String) getValueAt(i, 2);
			 decpoints = (String) getValueAt(i, 3);
 		     if (typed.compareTo("Date") == 0)
 		     {
				 DateField df = new DateField(named);
				 flds[i] = df;
			 }
			 else if (typed.compareTo("Char") == 0)
	 		     {
					 int len = Integer.parseInt(lengths);
					 CharField cf = new CharField(named, len);
					 flds[i] = cf;
				 }
			 else if (typed.compareTo("Currency") == 0)
	 		     {
					  
					 CurrencyField cf = new CurrencyField(named);
					 flds[i] = cf;
				 }
			 else if (typed.compareTo("Logical") == 0)
 		     {
				 LogicalField lf = new LogicalField(named);
				 flds[i] = lf;
			 }
			 else if (typed.compareTo("Memo") == 0)
 		     {
				 MemoField mf = new MemoField(named);
				 flds[i] = mf;
			 }
			 else if (typed.compareTo("Num") == 0)
 		     {
				 int len = Integer.parseInt(lengths);
				 int dec = Integer.parseInt(decpoints);
				 NumField nf = new NumField(named, len, dec);
				 flds[i] = nf;
			 }
			 else if (typed.compareTo("Float") == 0)
 		     {
				 int len = Integer.parseInt(lengths);
				 int dec = Integer.parseInt(decpoints);
				 FloatField ff = new FloatField(named, len, dec);
				 flds[i] = ff;
			 }
			 else if (typed.compareTo("Picture") == 0)
 		     {
				 PictureField pf = new PictureField(named);
				 flds[i] = pf;
				 type2 = DBFTypes.FOXPRO_WITH_MEMO;
			 }
		 }

		 DBF dbf = new DBF(fil.getAbsolutePath(), type2, true);

      	         dbf.addField(flds);
		 dbf.close();
      }

    public  String getColumnName(int r) {return columnName[r];}

    public  void setValueAt(Object in, int r, int  c)
          {

		switch (c)
			 {
			 case 0:
			   name.setElementAt((String) in, r);
			   break;
			 case 1:
			   type.setElementAt((String) in, r);
			   String s = (String) in;
			   if ((s.compareTo("Logical") == 0)
			     || (s.compareTo("Memo") == 0)
			     || (s.compareTo("Picture") == 0)
			     || (s.compareTo("Currency") == 0)
			     )
			     setValueAt(Boolean.valueOf(false), r, 4);
			   break;
			 case 2:
			   length.setElementAt((String) in, r);
			   break;
			 case 3:
			   decPos.setElementAt((String) in, r);
			   break;
			 case 4:
			   indexInd.setElementAt((Boolean) in, r);
			   break;
			 case 5:
		       indexName.setElementAt((String) in, r);
			   break;
		     default:
			   break;
	          }

	}


     public  Object getValueAt(int r, int  c)
          {
			switch (c)
			 {
			 case 0:
			   return name.elementAt(r);
			 case 1:
			    return type.elementAt(r);
			 case 2:
			    return length.elementAt(r);
			 case 3:
			    return decPos.elementAt(r);
			 case 4:
			    return indexInd.elementAt(r);
			 case 5:
		       return indexName.elementAt(r);
		     default:
			   return null;
		   }
          }


   @SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int c){return getValueAt(0,c).getClass();}


   public  int getRowCount() { return name.size(); }

   public  int getColumnCount() { return columnCount; }

   public boolean isCellEditable(int r, int c) {
    	    String s = (String) getValueAt(r, 1);
			switch (c)
			 {
			 case 0:
			   return true;
			 case 1:
			    return true;
			 case 2:
			    if (s.compareTo("Date") == 0)
			      return false;
			    if (s.compareTo("Memo") == 0)
			      return false;
			    if (s.compareTo("Logical") == 0)
			      return false;
			    if (s.compareTo("Picture") == 0)
				      return false;
			    if (s.compareTo("Currency") == 0)
				      return false;
			    return true;
			 case 3:
			    if (s.compareTo("Num") == 0)
			      return true;
			    if (s.compareTo("Float") == 0)
			      return true;
			    return false;
			 case 4:
			    if (s.compareTo("Memo") == 0)
			      return false;
			    if (s.compareTo("Logical") == 0)
			      return false;
			    if (s.compareTo("Picture") == 0)
			      return false;
			    if (s.compareTo("Currency") == 0)
				      return false;
			    return true;
			 case 5:
		       return isCellEditable(r, 4);
		     default:
			   return false;
		   }
		   }



}

