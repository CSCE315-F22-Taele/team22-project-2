
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
//import java.awt.event.*;
import javax.swing.*;

/* Inventory_view displays the inventory using java swing elements and jdbc queries to the database.
 * @author Asger Schelde Larsen
 * 
 */
public class inventory_view implements ActionListener{

    static String[][] input_data;

    public static void main(String[] args) {
        // create a new frame
        JFrame f = new JFrame("inventory GUI");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));


        String[][] data = getData();
        
        String columns[] = {"food_id", "food_name", "current_count", "max_count", "sell_price"};

        JTable table = new JTable(data,columns);
        table.setBounds(30,40,200,300);          
        JScrollPane sp=new JScrollPane(table);    
        f.getContentPane().add(sp);

        JPanel p = new JPanel(new BorderLayout());

        input_data = new String[1][5];
        JTable input = new JTable(input_data,columns);
        input.setBounds(30,40,200,300);
        p.add(input, BorderLayout.PAGE_START);

        JPanel pButtons = new JPanel(new FlowLayout());
        JButton addItem = new JButton("Add Item");
        JButton updateItem = new JButton("Update Item");
        JButton deleteItem = new JButton("Delete Item");

        inventory_view iv = new inventory_view();
        addItem.addActionListener(iv);
        updateItem.addActionListener(iv);
        deleteItem.addActionListener(iv);

        pButtons.add(addItem);
        pButtons.add(updateItem);
        pButtons.add(deleteItem);

        p.add(pButtons, BorderLayout.CENTER);
        
        f.getContentPane().add(p);
        f.setSize(300,400);
        f.pack();
        f.setVisible(true);       
        
    }

/* getData() sends a jdbc query to the database to get the inventory data and return it
 * @author Asger Schelde Larsen
 * @param none
 * @return {ArrayList<String[]>} data
 */

    static String[][] getData() {
        String[][] data = new String[0][0];
        Connection conn = null;
        String teamNumber = "22";
        String sectionNumber = "913";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber ;
        String dbConnectionString = "jdbc.;postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        //Connecting to the database
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_913_22",
               "csce315_913_larsen", "633001563");
          } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
          }

        System.out.println("Opened database successfully");

        try{
            //create a statement object
            Statement stmt = conn.createStatement();

            String sqlStatement = "SELECT count(food_id) AS count FROM inventory;";

            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();
            int length = Integer.parseInt(result.getString("count"));
            data = new String[length][5];

            sqlStatement = "SELECT * FROM inventory;";
            //send statement to DBMS
            result = stmt.executeQuery(sqlStatement);
            
            int entry_nr = 0;
            while (result.next()) {
                data[entry_nr][0] = result.getString("food_id")+"\n";
                data[entry_nr][1] = result.getString("food_name")+"\n";
                data[entry_nr][2] = result.getString("current_count")+"\n";
                data[entry_nr][3] = result.getString("max_count")+"\n";
                data[entry_nr][4] = result.getString("sell_price")+"\n";
                entry_nr++;
            }
            conn.close();
        } catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error accessing Database.");
        }
        return data;
    }

    // if button is pressed
    public void actionPerformed(ActionEvent e)
    {
        Connection conn = null;
        String teamNumber = "22";
        String sectionNumber = "913";
        String dbName = "csce315_" + sectionNumber + "_" + teamNumber ;
        String dbConnectionString = "jdbc.;postgresql://csce-315-db.engr.tamu.edu/" + dbName;

        //Connecting to the database
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315_913_22",
               "csce315_913_larsen", "633001563");
        } catch (Exception exception) {
            exception.printStackTrace();
            System.err.println(exception.getClass().getName()+": "+exception.getMessage());
            System.exit(0);
        }
        
        String s = e.getActionCommand();
        if (s.equals("Add Item")) {
            try {
                Statement stmt = conn.createStatement();

                String sqlStatement = "INSERT INTO inventory VALUES (" + input_data[0][0] + ",'" + input_data[0][1] + "'," + input_data[0][2] + "," + input_data[0][3] + "," + input_data[0][4] + ");";
                System.out.println(sqlStatement);
                //send statement to DBMS
                stmt.executeUpdate(sqlStatement);
            } catch (Exception exception) {
                exception.printStackTrace();
                System.err.println(exception.getClass().getName()+": "+exception.getMessage());
                System.exit(0);
            }
        }
    }
}