package personnelmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import net.proteanit.sql.DbUtils;

public class PersonnelManager extends JFrame{
    
    Connection con;

    JFrame frame ;
    JPanel pane;
    JButton add;
    JLabel employeeIdLabel;
    JTextField employeeIdField;
    JLabel firstLabel;
    JTextField firstField;
    JLabel positionLabel;
    JTextField positionField;
    JLabel hrPayRateLabel;
    JTextField hrPayRateField;
    JButton showDB;
    
    JFrame frame1;
    JPanel pane1;
    JTable table;
    
    
    public PersonnelManager(){
        
        connectDB();
        buildPage();
        
        
        
    }
    
    public void buildPage(){
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(400,300);
        
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints a = new GridBagConstraints();
        frame.add(pane);
        
        a.anchor = GridBagConstraints.LINE_START;
        a.insets = new Insets(10,10,10,10);
        
        employeeIdLabel = new JLabel("Employee ID: ");
        a.gridx = 0;
        a.gridy = 0;
        pane.add(employeeIdLabel,a);
        
        employeeIdField = new JTextField();
        employeeIdField.setHorizontalAlignment(JTextField.CENTER);
        employeeIdField.setEditable(false);
        a.gridx++;
        a.ipadx = 50;
        pane.add(employeeIdField,a);
        
        firstLabel = new JLabel("Name: ");
        a.gridx--;
        a.gridy++;
        a.ipadx = 0;
        pane.add(firstLabel,a);
        
        firstField = new JTextField();
        a.gridx++;
        a.ipadx = 100;
        firstField.setHorizontalAlignment(JTextField.CENTER);
        pane.add(firstField,a);
        
        positionLabel = new JLabel("Position: ");
        a.gridx--;
        a.gridy++;
        a.ipadx = 0;
        pane.add(positionLabel,a);
        
        positionField = new JTextField();
        a.gridx++;
        a.ipadx = 100;
        positionField.setHorizontalAlignment(JTextField.CENTER);
        pane.add(positionField,a);
        
        hrPayRateLabel = new JLabel("Hourly Pay Rate: ");
        a.gridx--;
        a.gridy++;
        a.ipadx = 0;
        pane.add(hrPayRateLabel,a);
        
        hrPayRateField = new JTextField();
        a.gridx++;
        a.ipadx = 100;
        hrPayRateField.setHorizontalAlignment(JTextField.CENTER);
        pane.add(hrPayRateField,a);
        
        add = new JButton("Add Employee");
        a.gridx--;
        a.gridy++;
        a.ipadx = 0;
        pane.add(add,a);
        
        showDB = new JButton("Employee Directory");
        a.gridx++;
        pane.add(showDB,a);
        
        
        String idString = String.valueOf(findEmpId());
        employeeIdField.setText(idString);
        employeeIdField.setBackground(Color.WHITE);
        
        add.addActionListener(new addEmployee());
        showDB.addActionListener(new addEmployee());
        

       
        
        frame.pack();
    }
    
    public void buildDirectory(){
        frame1 = new JFrame();
        frame1.setDefaultCloseOperation(HIDE_ON_CLOSE);
        frame1.setVisible(false);
       
        JPanel pane1 = new JPanel(new BorderLayout());
        frame1.add(pane1);
        
        table = new JTable();
        
        JScrollPane js = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pane1.add(js,BorderLayout.CENTER);
        
        try{
            String query = "Select employeeID AS ID, Name, Position, hrPayRate as PayRate from personnel.employee";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch(Exception e){
            
        }
        
        frame1.pack();
        
        
    }
    
    public class addEmployee implements ActionListener{
        public void actionPerformed(ActionEvent e){

            if(e.getSource() == add){
            int empId = Integer.parseInt(employeeIdField.getText());
            String name = firstField.getText();
            String position = positionField.getText();
            int hrPayRate = Integer.parseInt(hrPayRateField.getText());
            
            
            
            try{
                String query = "Insert Into employee values(?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(query);
                
                
                
                ps.setInt(1,empId);
                ps.setString(2,name);
                ps.setString(3,position);
                ps.setInt(4,hrPayRate);
                
                
                
                
                
                ps.executeUpdate();
                
                System.out.println("Check");
                
                firstField.setText("");
                positionField.setText("");
                hrPayRateField.setText("");
                String idString = String.valueOf(findEmpId());
                employeeIdField.setText(idString);
                
                JOptionPane.showMessageDialog(null, name + " has been added to the database.");
            }
            catch(Exception a){
                JOptionPane.showMessageDialog(null,"Did not fill out information correctly.");
                System.out.println("ERROR2");
                a.getStackTrace();
            }
            }
            else if(e.getSource() == showDB){
                        buildDirectory();
                        frame1.setVisible(true);
            }
        }
}
    
    public void connectDB(){
            String user = "root";
             String pass = "root";
       try
       {
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/personnel",user,pass);
        System.out.println("Connected");
       }
       catch(Exception e)
       {
          System.out.println("ERROR1");
          e.printStackTrace();
       }
        
        }
    
    public int findEmpId(){
        int empId = 0;
        try{
            String query = "Select max(employeeID) AS maxID from personnel.employee";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                empId = rs.getInt("maxID");
            }
            
        }
        catch(Exception e){
            e.getStackTrace();
            System.out.println("ERROR3");
        }
        empId++;
        return empId;
        
    }
     
    public static void main(String[] args) {
       new PersonnelManager();
    }
    
}