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

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.text.DateFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Document;

import org.xBaseJ.DBF;
import org.xBaseJ.awt.dbfFileFilter;
import org.xBaseJ.fields.Field;
import org.xBaseJ.fields.MemoField;


public class dbfViewer extends JFrame implements ActionListener, WindowListener, ListSelectionListener, DocumentListener, Printable

{

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private boolean startEnabled = false;

    int editingRow = -1;

    Vector<String> names;
    String fname = null;

    JTable table;
    JMenuBar menuBar;
    JMenu menuFile, menuView, menuSearch;
    JMenuItem menuPrint;
    JMenuItem menuSave;
    JMenuItem menuOpen;
    JMenuItem menuExit;
    JMenuItem horizontalView;
    JMenuItem startFind, findNext, findPrev;

    JSplitPane splitPane;
    JScrollPane tableScrollPane, recordScrollPane;


    dbfTableModel tableModel;
    dbfViewerRecordPanel dbfrp = null;
    private String lastDirectory = "./.";

    private String searchText = "";
    private boolean searchFields[] = null;

    private int orient = JSplitPane.HORIZONTAL_SPLIT;
    
    boolean fileChanged = false;

    public dbfViewer(String arg) {

        fname = arg;

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuBar.add(menuFile);
        menuOpen = new JMenuItem("Open..");
        menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuPrint = new JMenuItem("Print");
        menuPrint.setEnabled(startEnabled);
        menuPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuSave = new JMenuItem("Save..");
        menuSave.setEnabled(startEnabled);
        menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuExit = new JMenuItem("Exit..");
        menuSave.setEnabled(startEnabled);
        menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        menuFile.add(menuOpen);
        menuFile.add(menuPrint);
        menuFile.add(menuSave);
        menuFile.addSeparator();
        menuFile.add(menuExit);
        menuView = new JMenu("View");
        menuBar.add(menuView);
        horizontalView = new JMenuItem("Vertical");
        menuView.add(horizontalView);
        menuSearch = new JMenu("Search");
        menuBar.add(menuSearch);
        startFind = new JMenuItem("Find");
        startFind.setEnabled(startEnabled);
        startFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        findNext = new JMenuItem("Find Next");
        findNext.setEnabled(false);
        findNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F3)));
        findPrev = new JMenuItem("Find Prev");
        findPrev.setEnabled(false);
        findPrev.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.SHIFT_MASK));
        menuSearch.add(startFind);
        menuSearch.add(findNext);
        menuSearch.add(findPrev);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);


        menuOpen.addActionListener(this);
        menuPrint.addActionListener(this);
        menuSave.addActionListener(this);
        menuExit.addActionListener(this);
        startFind.addActionListener(this);

        findNext.addActionListener(this);
        findPrev.addActionListener(this);
        horizontalView.addActionListener(this);
        addWindowListener(this);

        if (arg == null) {
           tableModel = new dbfTableModel();
           setTitle("org.xBaseJ Version: "+ DBF.xBaseJVersion);
        }
        else {

           tableModel = new dbfTableModel(arg, this);
           File fil = new File(arg);
           lastDirectory = fil.getPath();
           setTitle("org.xBaseJ Version: "+ DBF.xBaseJVersion + " " + arg);
           startEnabled = true;
           tableSetUpToGo();
        }




    }

    public void tableSetUpToGo() {
                table = new JTable(tableModel);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.getSelectionModel().addListSelectionListener(this);
                tableScrollPane = new JScrollPane(table);

                try{dbfrp = new dbfViewerRecordPanel(this, tableModel);}
                catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
					}
                dbfrp.goTo(1);

    		    menuPrint.setEnabled(true);
    		    menuSave.setEnabled(true);
    		    startFind.setEnabled(true);
    		    findNext.setEnabled(false);
    		    findPrev.setEnabled(false);

    	    	recordScrollPane = new JScrollPane(dbfrp);
    	     	Dimension min = new Dimension(200,150);
    	    	tableScrollPane.setMinimumSize(min);
                recordScrollPane.setMinimumSize(min);

                splitPane = new JSplitPane(orient,
                                      tableScrollPane, recordScrollPane);

            	min = new Dimension(400,300);
         		splitPane.setMinimumSize(min);
                getContentPane().add(splitPane);
                splitPane.setDividerLocation(200);
                table.revalidate();
                table.repaint();
                validate();
                editingRow = 0;

                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
    public void actionPerformed(ActionEvent ae) {

       if (ae.getSource() == menuOpen)
          {
    	   
    	   		if (fileChangedTestDontContinue()) {
    	   			return;
    	   		}
                JFileChooser jfc = new JFileChooser(new File(lastDirectory +"/*.dbf"));
                jfc.addChoosableFileFilter(new dbfFileFilter());
                jfc.showOpenDialog(this);
                File fil = jfc.getSelectedFile();
                if (fil == null)
                    return;

                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                lastDirectory = fil.getPath();
                fname = fil.getAbsolutePath();
                orient = splitPane.getOrientation();
                getContentPane().remove(splitPane);

                setTitle(fname);

                tableModel = new dbfTableModel(fname, this);

                tableSetUpToGo();

                fileChanged = false;
                return;
           }

       if (ae.getSource() == menuPrint)
         {
              PrinterJob pj=PrinterJob.getPrinterJob();

              pj.setPrintable(dbfViewer.this);
              pj.printDialog();

                try{
                    pj.print();
                }catch (Exception PrintException) {
					PrintException.printStackTrace();}

              return;
         }

       if (ae.getSource() == menuSave) {
           this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
           TableCellEditor editor = table.getCellEditor();
           if (editor != null)
              editor.stopCellEditing();
           tableModel.save();
           this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           fileChanged = false;
       }
       
       if (ae.getSource() == menuExit)
       {

	   		if (fileChangedTestDontContinue()) {
	   			return;
	   		}
	   		
            System.exit(0);
        }


       if (ae.getSource() == horizontalView) {
		   if (horizontalView.getText().compareTo("Vertical") == 0) {
                splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                horizontalView.setText("Horizontal");
	         }
           else  {
                splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                horizontalView.setText("Vertical");
		     }
	       splitPane.resetToPreferredSizes();
	       this.pack();
       }


      if (ae.getSource() == startFind) {
		   if (searchFields == null)
		      {
				  searchFields = new boolean[tableModel.getColumnCount()-1];
			  }

		   new dbfViewerSearchDialog(this, tableModel, searchText, searchFields).setVisible(true);
	   }

      if (ae.getSource() == findNext) {

	    String colText;

	    int row = table.getEditingRow()+1;


	    outRow: for (; row < tableModel.getRowCount(); row++)
	     {
	       for (int col=0; col < tableModel.getColumnCount()-1; col++)
	         {


				if (searchFields[col] == false)
				  continue;

	            colText = (String) tableModel.getValueAt(row,col);
	            if (colText.toUpperCase().indexOf(searchText.toUpperCase()) > -1)
	              {
	               table.setRowSelectionInterval( row, row);
                   table.scrollRectToVisible(table.getCellRect(row, col, false));
	               table.setEditingRow(row);
	               editingRow = row;
	               break outRow;
			   }
		   }
	    }
	    if (editingRow != row)
	      Toolkit.getDefaultToolkit().beep();

	   }

      if (ae.getSource() == findPrev) {

	    String colText;

	    int row = table.getEditingRow()-1;

	    outRow: for (; row >  -1; row--)
	     {
	       for (int col=0; col < tableModel.getColumnCount()-1; col++)
	         {
				if (searchFields[col] == false)
				  continue;

	            colText = (String) tableModel.getValueAt(row,col);
	            if (colText.toUpperCase().indexOf(searchText.toUpperCase()) > -1)
	              {
	               table.setRowSelectionInterval( row, row);
                   table.scrollRectToVisible(table.getCellRect(row, col, false));
	               table.setEditingRow( row);
	               editingRow = row;
	               break outRow;
			   }
		   }
	    }
	    if (editingRow != row)
	      Toolkit.getDefaultToolkit().beep();

	   }



      this.repaint();

  }

    private boolean fileChangedTestDontContinue() {
		if (fileChanged == false)
			return false;
		
		int dialogButton = JOptionPane.OK_CANCEL_OPTION;
		
		return JOptionPane.showConfirmDialog(null, "Data has changed. Ok to continue. Cancel to return.", "Confirm", dialogButton) == JOptionPane.CANCEL_OPTION;
		
	}

	JTableHeader tableHeader;
    int [] subTableSplit = null;
    boolean pageinfoCalculated=false;
    int totalNumPages=0;
    int prevPageIndex = 0;
    int subPageIndex = 0;
    int subTableSplitSize = 0;
    double tableHeightOnFullPage, headerHeight;
    double pageWidth, pageHeight;
    int fontHeight, fontDesent;
    double tableHeight, rowHeight;

   public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {

        Graphics2D g2=(Graphics2D)g;
        if(!pageinfoCalculated) {
                getPageInfo(g, pageFormat);
        }

        g2.setColor(Color.black);
        if(pageIndex>=totalNumPages) {
                return Printable.NO_SUCH_PAGE;
        }
        if (prevPageIndex != pageIndex) {
                subPageIndex++;
                if( subPageIndex == subTableSplitSize -1) {
                        subPageIndex=0;
                }
        }

        g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int rowIndex = pageIndex/ (subTableSplitSize -1);

        printTablePart(g2, pageFormat, rowIndex, subPageIndex);
        prevPageIndex= pageIndex;
        return Printable.PAGE_EXISTS;
        }


 public void getPageInfo(Graphics g, PageFormat pageFormat) {

        subTableSplit = null;
        subTableSplitSize = 0;
        subPageIndex = 0;
        prevPageIndex = 0;

        fontHeight=g.getFontMetrics().getHeight();
        fontDesent=g.getFontMetrics().getDescent();

        tableHeader = table.getTableHeader();

        headerHeight = tableHeader.getHeight() + table.getRowMargin();

        pageHeight = pageFormat.getImageableHeight();
        pageWidth =  pageFormat.getImageableWidth();


        tableHeight = table.getSize().getHeight();
        rowHeight = table.getRowHeight() + table.getRowMargin();

        tableHeightOnFullPage = (int)(pageHeight - headerHeight - fontHeight*2);
        tableHeightOnFullPage = tableHeightOnFullPage/rowHeight * rowHeight;

        TableColumnModel tableColumnModel = tableHeader.getColumnModel();
        int columns = tableColumnModel.getColumnCount();
        int columnMargin = tableColumnModel.getColumnMargin();

        int [] temp = new int[columns];
        int columnIndex = 0;
        temp[0] = 0;
        int columnWidth;
        int length = 0;
        subTableSplitSize = 0;
        while ( columnIndex < columns ) {

           columnWidth = tableColumnModel.getColumn(columnIndex).getWidth();

           if ( length + columnWidth + columnMargin > pageWidth ) {
              temp[subTableSplitSize+1] = temp[subTableSplitSize] + length;
              length = columnWidth;
              subTableSplitSize++;
            }
            else {
               length += columnWidth + columnMargin;
            }
            columnIndex++;
        } //while

        if ( length > 0 )  {  // if are more columns left, part page
           temp[subTableSplitSize+1] = temp[subTableSplitSize] + length;
           subTableSplitSize++;
        }

        subTableSplitSize++;
        subTableSplit = new int[subTableSplitSize];
        for ( int i=0; i < subTableSplitSize; i++ ) {
           subTableSplit[i]= temp[i];
        }
        totalNumPages = (int)(tableHeight/tableHeightOnFullPage);
        if ( tableHeight%tableHeightOnFullPage >= rowHeight ) { // at least 1 more row left
            totalNumPages++;
        }

        totalNumPages *= (subTableSplitSize-1);
        pageinfoCalculated = true;
    }

    public void printTablePart(Graphics2D g2, PageFormat pageFormat, int rowIndex, int columnIndex) {

        String pageNumber = "Page: "+(rowIndex+1);
        if ( subTableSplitSize > 1 ) {
                pageNumber += "-" + (columnIndex+1);
        }

        int pageLeft = subTableSplit[columnIndex];
        int pageRight = subTableSplit[columnIndex + 1];

        int pageWidth =  pageRight-pageLeft;
        // page number message
        g2.drawString(pageNumber,  pageWidth/2-35, (int)(pageHeight - fontHeight));

        double clipHeight = Math.min(tableHeightOnFullPage, tableHeight - rowIndex*tableHeightOnFullPage);

        g2.translate(-subTableSplit[columnIndex], 0);
        g2.setClip(pageLeft ,0, pageWidth, (int)headerHeight);

        tableHeader.paint(g2);   // draw the header on every page
        g2.translate(0, headerHeight);
        g2.translate(0,  -tableHeightOnFullPage*rowIndex);

        // cut table image and draw on the page

        g2.setClip(pageLeft, (int)tableHeightOnFullPage*rowIndex, pageWidth, (int)clipHeight);
        table.paint(g2);

        double pageTop =  tableHeightOnFullPage*rowIndex - headerHeight;
        g2.drawRect(pageLeft, (int)pageTop, pageWidth, (int)(clipHeight+ headerHeight));
    }


 public void searchSet(String inText, boolean[] inFields)
 {
    searchText = inText;
    searchFields = inFields;

    String colText;

    int row = table.getEditingRow();
    if (row < 0) row = 1;

    for (; row < tableModel.getRowCount(); row++)
     {
       for (int col=0; col < tableModel.getColumnCount()-1; col++)
         {
			if (searchFields[col] == false)
			  continue;

            colText = (String) tableModel.getValueAt(row,col);
            if (colText.toUpperCase().indexOf(searchText.toUpperCase()) > -1)
              {
               table.setRowSelectionInterval( row, row);
               editingRow = row;
               table.scrollRectToVisible(table.getCellRect(row, col, false));
               table.setEditingRow(row);
               findNext.setEnabled(true);
               findPrev.setEnabled(true);
               return;
		   }
	   }
   }
   Toolkit.getDefaultToolkit().beep();

 }


 public static void main(String[] args) {

        dbfViewer frame;

        if (args.length == 0) frame = new dbfViewer(null);
        else  frame = new dbfViewer(args[0]);

        frame.setSize(888, 1111);
        frame.setVisible(true);
    }


 public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
 public void windowOpened(java.awt.event.WindowEvent we) {}
 public void windowClosed(java.awt.event.WindowEvent we) {System.exit(0);}
 public void windowIconified(java.awt.event.WindowEvent we) {}
 public void windowDeiconified(java.awt.event.WindowEvent we) {}
 public void windowActivated(java.awt.event.WindowEvent we) {}
 public void windowDeactivated(java.awt.event.WindowEvent we) {}

 public void valueChanged(ListSelectionEvent e) {
	 ListSelectionModel lsm = (ListSelectionModel) e.getSource();
	 if (lsm.isSelectionEmpty()) ;
	 else
	 {
	    dbfrp.goTo(lsm.getMinSelectionIndex()+1);
	    editingRow = lsm.getMinSelectionIndex();
	}
 }


 public void changedUpdate(DocumentEvent de)
 {
 }
 public void insertUpdate(DocumentEvent de) { updateTable(de); }
 public void removeUpdate(DocumentEvent de) { updateTable(de); }


 public void updateTable(DocumentEvent de)
 {
	 Document doc = (Document) de.getDocument();
	 int col = Integer.parseInt((String) doc.getProperty("name"));
	 int len = doc.getLength();
	 if (editingRow == -1)
	    editingRow = 0;
	 try {
    	 String text = doc.getText(0,len);
         tableModel.setValueAt(text, editingRow, col);
         tableModel.fireTableCellUpdated(editingRow, col);
		}
	catch (javax.swing.text.BadLocationException ble)
	  	{
		  ble.printStackTrace();
		}
 }

}


class dbfTableModel extends AbstractTableModel
    {
     /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	DBF currentDBF;
     int inRow;
     String columnName[];
     int columnCount;
     int rowCount;
     DateFormat df;
     Object fld[][];
     Boolean deleted[];
     dbfViewer parent;

    public dbfTableModel(String tableName, dbfViewer inParent)
      {
       try {
        currentDBF = new DBF(tableName);
        df = DateFormat.getDateInstance(DateFormat.SHORT);
        parent = inParent;
        getData();
       }
       catch( Exception e ) {
		   e.printStackTrace();
		   }
      }

    public dbfTableModel()
      {
          columnCount = 0;
          rowCount = 0;
      }
    public void save()
      {
        String inString = "not set yet";
        try {
        for (int r = 0; r < rowCount; r++)
        {
          currentDBF.gotoRecord(r+1);
          for (int k = 0; k < columnCount-1; k++)
             {
		      inString = "Field " + k;
		      if (fld[r][k] instanceof String)
                  currentDBF.getField(k+1).put((String)fld[r][k]);
              else
		      if (fld[r][k] instanceof Boolean)
		        { Boolean bf = (Boolean) fld[r][k];
                  currentDBF.getField(k+1).put(bf.booleanValue()?"T":"F");
			    }
             }
          Boolean b = deleted[r];
          if (b.booleanValue())
              currentDBF.delete();
          else
              currentDBF.undelete();
          currentDBF.update();
          } // for each record
         
        } // try
        catch (Exception e) {
			  e.printStackTrace();
              JOptionPane.showMessageDialog(parent, inString + " caused an exception: " + e.getMessage(), "Error Setting Field" , JOptionPane.ERROR_MESSAGE);
         }
      }

    public  String getColumnName(int c) {
		return columnName[c];}

    public  void setValueAt(Object in, int r, int  c)
          {
    	
    	 

            if (c==columnCount-1) {
               deleted[r] = (Boolean) in;
               return;
              }


             try {

                if (currentDBF.getField(c+1).getType() == 'L')
                    fld[r][c] = (Boolean) in;
                else
                    fld[r][c] = (String) in;
                }

                /*catch (java.text.ParseException pe) {pe.printStackTrace();
                    JOptionPane.showMessageDialog(parent, inString + " is not the correct format for date fields.", "Invalid Date Field", JOptionPane.ERROR_MESSAGE);
                }
                catch (java.lang.NumberFormatException nfe) {nfe.printStackTrace();
                    JOptionPane.showMessageDialog(parent, inString + " is not the correct format for numeric fields.", "Invalid Floating Numeric Field", JOptionPane.ERROR_MESSAGE);
                }
                */
                catch (Exception e) {e.printStackTrace();
                    JOptionPane.showMessageDialog(parent, "row: " + r + " col: " + c + " " + in + " caused an exception: " + e.getMessage(), "Error Setting Field", JOptionPane.ERROR_MESSAGE);
                }
           }

     public  Object getValueAt(int r, int  c)
          {
            if (c == columnCount-1)
             {
              return deleted[r];
		    }

            return fld[r][c];
           }


   @SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int c){
	   return getValueAt(0,c).getClass();
	   }

   public Field getColumnField(int c) throws Exception {return currentDBF.getField(c+1);}

   public  int getRowCount() { return rowCount; }

   public  int getColumnCount() { return columnCount; }

   public boolean isCellEditable(int r, int c) {return false;}


   public  void getData() throws Exception
      {
        int r, k;
        rowCount = currentDBF.getRecordCount();
        columnCount = currentDBF.getFieldCount()+ 1;
        columnName = new String[columnCount];
        columnName[columnCount-1] = "Deleted";
        for (k = 1; k< columnCount; k++)
           {
           columnName[k-1] = currentDBF.getField(k).getName();
           }

        deleted = new Boolean[rowCount];
        fld = new Object[rowCount][columnCount];

        try {
        for (r = 0; r < rowCount; r++)
        {
          currentDBF.gotoRecord(r+1);
          for (k = 0; k < columnCount-1; k++)
             {
              if (currentDBF.getField(k+1).getType() == 'L')
                fld[r][k] = Boolean.valueOf(currentDBF.getField(k+1).get().compareTo("T")==0);
              else
                fld[r][k] = currentDBF.getField(k+1).get();
             }
          deleted[r] = Boolean.valueOf(currentDBF.deleted());
        }
        }

        catch (Exception e1) {
			e1.printStackTrace();
			}

      }
}

class dbfViewerRecordPanel extends JPanel implements ActionListener

  {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	dbfViewer parent;
    JPanel p;
    JScrollPane sp;
    JPanel viewPane;
    GridBagLayout gb;
    GridBagConstraints gbc;
    JLabel crl, trl;
    JCheckBox delCB;
    JButton Prev, Next, Add,  Update, Clear;
    Vector<Component> fldObjects;

    JLabel l;
    JTextField t;
    Field f;
    JButton b;
    JCheckBox c;
    int currentRow;
    dbfTableModel tableModel;



    public dbfViewerRecordPanel(dbfViewer inParent, dbfTableModel indbfTableModel) throws Exception
      {

         sp = new JScrollPane(this);
         parent = inParent;
         tableModel = indbfTableModel;
         setupDBFields();
     	class KeyDispatcher implements KeyEventDispatcher {

     		 
     		public boolean dispatchKeyEvent(KeyEvent e) {
     			parent.fileChanged = true;
     			return false;
     		}
         	
         }
         KeyDispatcher keyDispatcher = new KeyDispatcher();
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyDispatcher);


      }

    public void setupDBFields()  throws Exception
       {
         setLayout(null);
         Dimension dimView =  sp.getSize();
         int height = 0, width = 50;
         removeAll();


         gb = new GridBagLayout();
         gbc = new  GridBagConstraints();
         setLayout(gb);

         int i;
         if (tableModel.getColumnCount()>0) {
            fldObjects = new Vector<Component>(tableModel.getColumnCount()-1);
            Field f;
            for (i = 0; i < tableModel.getColumnCount()-1; i++)
               {

                  f = tableModel.getColumnField(i);
                  if (f.getType() == 'M' || f.getType() == 'P' ){
                     b = new JButton(f.getName());
                     b.setActionCommand("M"+i);
                     b.addActionListener(this);
                     addComponent(this, b, 1, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                     fldObjects.addElement(b);
                      }
                  else if (f.getType() == 'L') {
                     c = new JCheckBox(f.getName(), true);
                     c.setActionCommand("L"+i);
                     c.addActionListener(this);
                     addComponent(this, c, 1, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                     fldObjects.addElement(c);
                      }
                  else {
                     l = new JLabel(f.getName(), Label.RIGHT);
                     addComponent(this,  l, 0, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                     int ln = f.getLength();
                     if (ln > 100) ln = 100;
                     t = new JTextField(f.getName(), ln);
                     t.setName(f.getName());
                     t.setActionCommand("T"+i);
                     t.getDocument().addDocumentListener(parent);
                     t.getDocument().putProperty("name", ""+i);
                     t.setEditable(true);
                     if (width < ln*10)
                     	width=ln*10;
                     addComponent(this, t, 1, i, ln, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                     fldObjects.addElement(t);
                     }
                   height += 10;
               }

           c = new JCheckBox("Deleted");
           c.setActionCommand("L"+i);
           c.addActionListener(this);
           addComponent(this, c, 1, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
           fldObjects.addElement(c);
           height += 10;

	    }
        dimView.setSize(width+150, height+150);
        sp.setSize(dimView);
   }

 public void actionPerformed(ActionEvent e) {
	 int col = Integer.parseInt(e.getActionCommand().substring(1));


	 switch (e.getActionCommand().charAt(0))
	 {
		 case 'M':
             parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
             this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        	 new dbfTableRecordMemoDialog(parent, this, tableModel, parent.editingRow, col).setVisible(true);
		     break;
		 case 'L':
		     c = (JCheckBox) fldObjects.elementAt(col);
		     tableModel.setValueAt(Boolean.valueOf(c.isSelected()), parent.editingRow, col);
		     tableModel.fireTableCellUpdated(parent.editingRow, col);
		     break;
		/* case 'T':
		     t = (JTextField) fldObjects.elementAt(col);
		     tableModel.setValueAt(t.getText(), parent.editingRow, col);
		     tableModel.fireTableCellUpdated(here, col+1);
		     break;

        */
	 }

 }


 public void goTo(int where)
    {
		parent.editingRow = where-1;
        try {
        if (where < 1) return;
        if (where > tableModel.getRowCount()) return;

         Field f;

         JCheckBox c;
         JTextField t;
         int i;

         for (i = 0; i < tableModel.getColumnCount()-1; i++)
            {
               f = tableModel.getColumnField(i);
               if (f  instanceof MemoField) {
                  }
               else
               if (f.getType() == 'L') {
                  c  = (JCheckBox) fldObjects.elementAt(i);
                  Boolean bln = (Boolean)tableModel.getValueAt(where-1, i);
                  c.setSelected(bln.booleanValue());
                  }
               else {
                  t = (JTextField)  fldObjects.elementAt(i);
                  t.setText((String) tableModel.getValueAt(where-1, i));
                  }
            }

            c  = (JCheckBox) fldObjects.elementAt(i);
            Boolean bln = (Boolean)tableModel.getValueAt(where-1, i);
            c.setSelected(bln.booleanValue());

         } // try

         catch (Exception e1) {
			 e1.printStackTrace();
			 System.exit(2);
			 }
        }


public static void addComponent (Container container, Component component,
    int gridx, int gridy, int gridwidth, int gridheight, int fill,
    int anchor) throws AWTException {
    LayoutManager lm = container.getLayout();
    if (!(lm instanceof GridBagLayout)) {
        throw new AWTException ("Invalid layout" + lm);
    } else {
        GridBagConstraints gbc = new GridBagConstraints ();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = fill;
        gbc.anchor = anchor;
        ((GridBagLayout)lm).setConstraints(component, gbc);
        container.add (component);
    }
}

}


class dbfTableRecordMemoDialog extends JDialog implements ActionListener, WindowListener
  {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public JButton Okay = new JButton("Okay");
    public JButton Cancel = new JButton("Cancel");
    public JTextArea text = new JTextArea();
    public int row, col;
    dbfTableModel tableModel;
    dbfViewer parent;
    JPanel fr;

    public dbfTableRecordMemoDialog(dbfViewer inParent, JPanel inFr, dbfTableModel indbfTableModel, int inRow, int inCol)
      {
         super(inParent, "Memo Field", true);
         parent = inParent;
         fr = inFr;
         addWindowListener(this);
         tableModel = indbfTableModel;
         row = inRow;
         col = inCol;
         try {
            Field f = indbfTableModel.getColumnField(inCol);
            setTitle(f.getName());
		}
		 catch (Exception e1) {System.err.println("Failed to get column name");}

         text.setText((String) tableModel.getValueAt(row, col));
         JScrollPane jsp = new JScrollPane(text);
         jsp.setPreferredSize(new Dimension(230,160));
         jsp.setBorder(BorderFactory.createEtchedBorder());
         this.getContentPane().add("Center", jsp);

         JPanel p =  new JPanel();
         p.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
         p.add(Okay);
         Okay.addActionListener(this);
         this.getContentPane().add("East", p);
         p.add(Cancel);
         Cancel.addActionListener(this);
         this.getContentPane().add("West", p);
         this.pack();
         
     	class KeyDispatcher implements KeyEventDispatcher {

     		 
     		public boolean dispatchKeyEvent(KeyEvent e) {
     			parent.fileChanged = true;
     			return false;
     		}
         	
         }
         KeyDispatcher keyDispatcher = new KeyDispatcher();
         KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyDispatcher);
      }

     public void actionPerformed(ActionEvent event)
     {
        if (event.getSource() == Cancel) {
           setVisible(false);
           dispose();
           parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           fr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           return;
          }

        if (event.getSource() == Okay)
          {
             setVisible(false);
             tableModel.setValueAt(text.getText(),row, col);
		     tableModel.fireTableCellUpdated(row, col);
             parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
             fr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
             dispose();
          }
       }


    public void windowClosing(WindowEvent event) {
            dispose();
            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            fr.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           }


    public void windowClosed(WindowEvent event) { }

    public void windowDeiconified(WindowEvent event) { }

    public void windowIconified(WindowEvent event) { }

    public void windowActivated(WindowEvent event) { }

    public void windowDeactivated(WindowEvent event) { }

    public void windowOpened(WindowEvent event) { }
    
 

}




class dbfViewerSearchDialog extends JDialog implements ActionListener, WindowListener
  {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public JButton Okay = new JButton("Okay");
    public JButton Cancel = new JButton("Cancel");
    public JTextField text = new JTextField();
    public JCheckBox jcb[];
    dbfViewer parent;
    dbfTableModel model;

    public dbfViewerSearchDialog(dbfViewer  inParent, dbfTableModel inModel, String inText, boolean inFields[])
      {
         super(inParent, "Find Data", true);
         parent = inParent;
         model = inModel;


         addWindowListener(this);
         setTitle("Search Fields");

         JPanel p =  new JPanel();
         p.setLayout(new GridLayout(0,1));

         text.setText(inText);
         p.add(text);

         jcb = new JCheckBox[inModel.getColumnCount()-1];
         for (int i = 0; i < inModel.getColumnCount()-1; i++)
           {
			   jcb[i] = new JCheckBox(inModel.getColumnName(i));
			   if (i < inFields.length)
			      jcb[i].setSelected(inFields[i]);
			   p.add(jcb[i]);
		  }

         this.getContentPane().add("Center", p);

         JPanel p2 = new JPanel();
         p2.setLayout(new GridLayout(1,0));
         p2.add(Okay);
         Okay.addActionListener(this);
         getRootPane().setDefaultButton(Okay);
         p2.add(Cancel);
         Cancel.addActionListener(this);

         this.getContentPane().add("South", p2);
         this.pack();
      }

     public void actionPerformed(ActionEvent event)
     {
        if (event.getSource() == Cancel) {
           setVisible(false);
           dispose();
           parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           return;
          }

        if (event.getSource() == Okay)
          {

			 String s = text.getText().trim();

			 if (s.length() < 1)
			    {
                    JOptionPane.showMessageDialog(parent, "Enter Search Text",
                             "Enter Search Text", JOptionPane.ERROR_MESSAGE);
					return;
				}
			 int i;
	         for ( i = 0; i < model.getColumnCount()-1; i++)
	           {
				   if (jcb[i].isSelected())
				     break;
			  }

			 if (i ==  model.getColumnCount()-1)
			  {
					   JOptionPane.showMessageDialog(parent, "Select one or more fields to search in",
                             "Select A Field", JOptionPane.ERROR_MESSAGE);
    			   return;
		      }

             boolean setfields[] = new boolean[model.getColumnCount()-1];

	         for ( i = 0; i < model.getColumnCount()-1; i++)
	           {
				   setfields[i] = jcb[i].isSelected();
			  }

             setVisible(false);
             parent.searchSet(s, setfields);
             parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
             dispose();
          }
       }


    public void windowClosing(WindowEvent event) {
            dispose();
            parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
           }


    public void windowClosed(WindowEvent event) { }

    public void windowDeiconified(WindowEvent event) { }

    public void windowIconified(WindowEvent event) { }

    public void windowActivated(WindowEvent event) { }

    public void windowDeactivated(WindowEvent event) { }

    public void windowOpened(WindowEvent event) { }

}


