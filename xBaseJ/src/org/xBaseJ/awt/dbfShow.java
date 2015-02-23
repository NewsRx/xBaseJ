package org.xBaseJ.awt;
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
 *  20110401  Joe McVerry (jrm)    Replaced instanceof call with static
 *                                 field test (eg isDateField());

*/

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.LayoutManager;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import org.xBaseJ.DBF;
import org.xBaseJ.xBaseJException;
import org.xBaseJ.fields.Field;
import org.xBaseJ.fields.LogicalField;
 
public class dbfShow extends Frame
      implements ActionListener, WindowListener, AdjustmentListener, ItemListener
  {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	Panel p;
    ScrollPane sp;
    Panel viewPane;
    GridBagLayout gb;
    GridBagConstraints gbc;
    DBF db;
    Label crl, trl;
    Scrollbar SBrecpos;
    boolean scrollBarTracking = false;
    Checkbox delCB;
    Button Prev, Next, Add,  Update, Clear;
    Vector<Component> fldObjects;
    memoDialog md;
    MenuItem opener, packer, quiter, firstRecord, nextRecord, prevRecord, lastRecord, addRecord, updateRecord, clearRecord;

    Label l;
    TextField t;
    Field f;
    Button b;
    Checkbox c;

    public dbfShow(String title, String fname) throws Exception
      {

         super(title);
         String dbname;
         addWindowListener(this);
         sp = new ScrollPane();
         viewPane = new Panel();
         sp.add(viewPane);
         this.add(sp);

         if (fname == null || fname.length() == 0)
           {
             FileDialog fd = new FileDialog(this, "dbfShow", FileDialog.LOAD);
             fd.setFile("*.DBF");
             fd.pack();
             fd.setVisible(true);
             String DBFname = fd.getFile();
             String dirname = fd.getDirectory();

            if (DBFname == null)
                System.exit(0);
            if (DBFname.length() < 1)
                   System.exit(0);
            dbname = new String(dirname + DBFname);
           }
          else dbname = new String(fname);

         MenuBar mb = new MenuBar();
         this.setMenuBar(mb);
         Menu file = new Menu("File");
         opener = new MenuItem("Open..");
         file.add(opener);
         opener.addActionListener(this);
         packer = new MenuItem("Pack");
         packer.addActionListener(this);
         file.add(packer);
         quiter = new MenuItem("Quit");
         file.add(quiter);
         quiter.addActionListener(this);
         mb.add(file);
         Menu record = new Menu("Record");
         firstRecord = new MenuItem("First");
         firstRecord.addActionListener(this);
         record.add(firstRecord);
         nextRecord = new MenuItem("Next");
         nextRecord.addActionListener(this);
         record.add(nextRecord);
         prevRecord = new MenuItem("Prev");
         prevRecord.addActionListener(this);
         record.add(prevRecord);
         lastRecord = new MenuItem("Last");
         lastRecord.addActionListener(this);
         record.add(lastRecord);
         addRecord = new MenuItem("Add");
         addRecord.addActionListener(this);
         record.add(addRecord);
         updateRecord = new MenuItem("Update");
         updateRecord.addActionListener(this);
         record.add(updateRecord);
         clearRecord = new MenuItem("Clear");
         clearRecord.addActionListener(this);
         record.add(clearRecord);
         mb.add(record);
         crl = new Label("Record" , Label.RIGHT);
         trl =  new Label(" of ", Label.LEFT);
         SBrecpos = new Scrollbar(Scrollbar.HORIZONTAL, 1, 1, 0, 0);
         SBrecpos.addAdjustmentListener(this);
         delCB = new Checkbox("Deleted");
         delCB.addItemListener(this);
         Prev = new Button("<<Prev");
         Prev.addActionListener(this);
         Next = new Button("Next>>");
         Next.addActionListener(this);
         Add = new Button("Add");
         Add.addActionListener(this);
         Update = new Button("Update");
         Update.addActionListener(this);
         Clear = new Button("Clear");
         Clear.addActionListener(this);
         setupDBFields(dbname);
         setTitle(dbname + ",   org.xBaseJ Version:" + org.xBaseJ.DBF.xBaseJVersion);
         pack();
         setVisible(true);

      }

public void setupDBFields(String dbname)  throws Exception
    {

         viewPane.setLayout(null);
         Dimension dimView =  sp.getSize();
         int height = 0, width = 50;
         viewPane.removeAll();
         db = new DBF(dbname);
         setTitle(dbname);

         gb = new GridBagLayout();
         gbc = new  GridBagConstraints();
         viewPane.setLayout(gb);

         int i, j;
         fldObjects = new Vector<Component>(db.getFieldCount());
         for (i = 1; i <= db.getFieldCount(); i++)
            {
               j = i - 1;
               f = db.getField(i);
               if (f.isMemoField() || f.isPictureField()) {
                  b = new Button(db.getField(i).getName());
                  b.addActionListener(this);
                  addComponent(viewPane, b, 1, j, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                  fldObjects.addElement(b);
                   }
               else if (f.getType() == 'L') {
                  c = new Checkbox(db.getField(i).getName(), true);
                  addComponent(viewPane, c, 1, j, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                  fldObjects.addElement(c);
                   }
               else {
                  l = new Label(db.getField(i).getName(), Label.RIGHT);
                  addComponent(viewPane,  l, 0, j, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                  int ln = f.getLength();
                  if (ln > 100) ln = 100;
                  t = new TextField(db.getField(i).getName(), ln);
                  if (width < ln*10) width=ln*10;
                  addComponent(viewPane, t, 1, j, ln, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
                  fldObjects.addElement(t);
                  t.setEditable(true);
                  }
                height += 10;
            }

         crl.setText("Record " + db.getCurrentRecordNumber());
         trl.setText(" of " + db.getRecordCount());
         SBrecpos.setMaximum(db.getRecordCount());
         addComponent(viewPane, crl, 0, i,  1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         addComponent(viewPane, trl, 1, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         i++;
         addComponent(viewPane, SBrecpos, 0, i, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         addComponent(viewPane, delCB, 2, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST );
         i++;
         addComponent(viewPane,  Prev, 0, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         addComponent(viewPane, Next, 1, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         i++;
         addComponent(viewPane, Add, 0, i,  1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         addComponent(viewPane, Update, 1, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         addComponent(viewPane, Clear, 2, i, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.EAST);
         Prev.setEnabled(false);
         prevRecord.setEnabled(false);
         if (db.getRecordCount() == 0) {
             Update.setEnabled(false);
             updateRecord.setEnabled(false);
             Next.setEnabled(false);
             nextRecord.setEnabled(false);
           }


        dimView.setSize(width+150, height+150);
        sp.setSize(dimView);

        goTo(1);
 }

    public void setFields()
     {
        Field f;
        LogicalField lf;
        Checkbox c;
        TextField t;

        int i;
        try {
         for (i = 1; i <= db.getFieldCount(); i++)
            {
               f = db.getField(i);
               if (f.isMemoField() || f.isPictureField()) {
                  }
               else
               if (f.isLogicalField()) {
                  lf = (LogicalField) f;
                  c  = (Checkbox) fldObjects.elementAt(i-1);
                  lf.put(c.getState());
                  }
               else {
                  t = (TextField)  fldObjects.elementAt(i-1);
                  f.put(t.getText());
                  }
             }
           }

        catch (Exception e1) {System.out.println(e1);}
    }

    public void addRec()
    {
        try {
         setFields();
         db.write();
         goTo(db.getRecordCount());
         delCB.setState(false);
         trl.setText(" of " + db.getRecordCount());
         } // try
       catch (Exception e1) {System.out.println(e1); System.exit(1);}
       Update.setEnabled(true);
       updateRecord.setEnabled(true);
    }

    public void updateRec()
    {

        try {
         setFields();
         db.update();
         boolean b  = delCB.getState();
         if (db.deleted() && !b)
              db.undelete();
         else if (!db.deleted() && b)
               db.delete();
         } // try
       catch (Exception e1) {System.out.println(e1); System.exit(1);}

    }

    public void goTo(int where)
    {
        try {
        if (where < 1) return;
        if (where > db.getRecordCount()) return;
        db.gotoRecord(where);
        crl.setText("Record " + db.getCurrentRecordNumber());
        delCB.setState(db.deleted());

         Field f;
         LogicalField lf;
         Checkbox c;
         TextField t;

         int i;

         for (i = 1; i <= db.getFieldCount(); i++)
            {
               f = db.getField(i);
               if (f.isMemoField()) {
                  }
               else
               if (f.getType() == 'L') {
                  lf = (LogicalField) f;
                  c  = (Checkbox) fldObjects.elementAt(i-1);
                  c.setState(lf.getBoolean());
                  }
               else {
                  t = (TextField)  fldObjects.elementAt(i-1);
                  t.setText(f.get().trim());
                  }
            }

         Next.setEnabled(! (db.getCurrentRecordNumber() == db.getRecordCount()) );
         nextRecord.setEnabled(! (db.getCurrentRecordNumber() == db.getRecordCount()) );

         Prev.setEnabled(!(db.getCurrentRecordNumber() == 1));
         prevRecord.setEnabled(!(db.getCurrentRecordNumber() == 1));

         firstRecord.setEnabled( db.getRecordCount()>0 );
         lastRecord.setEnabled( db.getRecordCount()>0 );


         SBrecpos.setValues(db.getCurrentRecordNumber(), 1, 0, db.getRecordCount());
         } // try

         catch (Exception e1) {System.out.println(e1); System.exit(2);}
        }

    public void clearFields()
    {
         Field f;

         Checkbox c;
         TextField t;

         int i;

         for (i = 1; i <= db.getFieldCount(); i++)
            {
               try
               {
                 f = db.getField(i);
                 if (f.isMemoField()) f.put("");
                 else if (f.getType() == 'L')
                  {
                    c  = (Checkbox) fldObjects.elementAt(i-1);
                    c.setState(false);
                    }
                 else {
                    t = (TextField)  fldObjects.elementAt(i-1);
                    t.setText("");
                    }
               }
               catch (Exception e1) {System.out.println(e1); System.exit(3);}
       }
    }

    public void windowClosing(WindowEvent event) {
           System.exit(0);
           }


    public void windowClosed(WindowEvent event) {
           System.exit(0);
           }


    public void windowDeiconified(WindowEvent event) { }

    public void windowIconified(WindowEvent event) { }

    public void windowActivated(WindowEvent event) { }

    public void windowDeactivated(WindowEvent event) { }

    public void windowOpened(WindowEvent event) { }

    public void itemStateChanged(ItemEvent event)
    {
    }

   public void adjustmentValueChanged(AdjustmentEvent event)
    {
        if (event.getSource() == SBrecpos) {
            if (event.getAdjustmentType() == AdjustmentEvent.TRACK)
              {
               goTo(SBrecpos.getValue());
               return;
              }
            if (event.getID() == AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED)
             {
             SBrecpos.setEnabled(false);
             goTo(SBrecpos.getValue());
             SBrecpos.setEnabled(true);
             }
        }

    }

    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() == firstRecord)
          {
             goTo(1);
             return;
          }
        if (event.getSource() == lastRecord)
          {
             goTo(db.getRecordCount());
             return;
          }
        if (event.getSource() == Next || event.getSource() == nextRecord)
          {
             if (db.getCurrentRecordNumber() < db.getRecordCount()) goTo(db.getCurrentRecordNumber()+1);
             return;
          }

        if (event.getSource() == Prev ||event.getSource() == prevRecord )
          {
             if (db.getCurrentRecordNumber() > 1) goTo(db.getCurrentRecordNumber()-1);
             return;
          }

        if (event.getSource() == Add || event.getSource() == addRecord)
          {
             addRec();
             return;
          }
        if (event.getSource() == Update || event.getSource() == updateRecord)
          {
             updateRec();
             return;
          }
        if (event.getSource() == Clear || event.getSource() == clearRecord)
          {
             clearFields();
             return;
          }

       if (event.getSource() == opener){
                FileDialog fd = new FileDialog(this, "dbfShow", FileDialog.LOAD);
                fd.setFile("*.DBF");
                fd.pack();
                fd.setVisible(true);
                String DBFname = fd.getFile();
                String dirname = fd.getDirectory();
                if (DBFname == null)
                 return;
                if (DBFname.length() < 1)
                   return;
               String dbname = new String(dirname + DBFname);
               try {setupDBFields(dbname);}
               catch (Exception e1) {System.out.println(e1); System.exit(4);}
               pack();
               setVisible(true);
               return;
             }

        if (event.getSource() == packer)
          {
               packer.setEnabled(false);
               try { db.pack(); trl.setText(" of " + db.getRecordCount());}
                catch (Exception e1) {e1.printStackTrace();}
                if (db.getRecordCount() == 0) {
                    Update.setEnabled(false);
                    Next.setEnabled(false);
                    updateRecord.setEnabled(false);
                    nextRecord.setEnabled(false);
                    SBrecpos.setValues(0, 1, 0, 0);
                  }
                else goTo(1);
                packer.setEnabled(true);
                return;
           }

        if (event.getSource() == quiter)
              {
               System.exit(0);
                return;
              }

      if (event.getSource() instanceof Button)
      {
      int i;
      Field f;
      for (i = 1; i <= db.getFieldCount(); i++)
            {
               try {
               f = db.getField(i);
               if (f.isMemoField()) {
                         if (event.getActionCommand().equals(f.getName())){
                           md = new memoDialog(this, f);
                           md.setVisible(true);
                           return;
                         }
                   }
               }
              catch (Exception e1) {System.out.println(e1);}
             }
        }

}


    public static void main(String[] args) throws Exception
    {

    if (args.length == 0)
         new dbfShow("dbfShow", null);
    else
         new dbfShow("dbfShow", args[0]);
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


class memoDialog extends Dialog implements ActionListener, WindowListener
  {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public Button Okay = new Button("Okay");
    public Button Cancel = new Button("Cancel");
    public TextArea text = new TextArea();
    Field f1;

    public memoDialog(Frame fr, Field f)
      {
         super(fr, f.getName(), true);
         addWindowListener(this);
         f1 = f;

         text.setText(f1.get());
         this.add("Center", text);

         Panel p =  new Panel();
         p.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
         p.add(Okay);
         Okay.addActionListener(this);
         this.add("East", p);
         p.add(Cancel);
         Cancel.addActionListener(this);
         this.add("West", p);
         this.pack();
      }

     public void actionPerformed(ActionEvent event)
     {
        if (event.getSource() == Cancel) {
           setVisible(false);
           dispose();
           return;
          }

        if (event.getSource() == Okay)
          {
           try
           {
             setVisible(false);
             f1.put(text.getText());
             dispose();
           }
           catch (xBaseJException e1) {System.out.println(e1);}
          }
       }


    public void windowClosing(WindowEvent event) {
           dispose();
           }


    public void windowClosed(WindowEvent event) { }

    public void windowDeiconified(WindowEvent event) { }

    public void windowIconified(WindowEvent event) { }

    public void windowActivated(WindowEvent event) { }

    public void windowDeactivated(WindowEvent event) { }

    public void windowOpened(WindowEvent event) { }

}





