package com.jdbc.hotel;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HotelReservationSystem {
	

	private static final String url="jdbc:mysql://localhost:3306/hotel_db";
	private static final String username="root";
	private static final String password="anitadhawanak47";

	public static void main(String[] args) throws SQLException {
		
	try {
		Class.forName("com.mysql.cj.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	Connection con = DriverManager.getConnection(url,username,password);
    boolean exit=true;
    while(exit) {
	System.out.println();
	System.out.println("Hotel Management System");
	Scanner sc = new Scanner(System.in);
	System.out.println("1.Reserve a room");
	System.out.println("2.View Reservations");
	System.out.println("3.Get Room Number");
	System.out.println("4.Update Reservations");
	System.out.println("5.Delete Reservations");
	System.out.println("Exit(0)");
	System.out.println("Chose an options");
	int choice = sc.nextInt();
	
		switch(choice) {
		case 1:
			reserveRoom(con,sc);
			break;
		case 2:
		viewReservations(con);
			break;
		case 3:
			getRoomNumber(con,sc);
			break;
		case 4:
			updateReservation(con,sc);
			break;
		case 5:
			deleteReservation(con,sc);
			break;
		case 0:
			System.out.println("thank you");
			exit=false;
			break;
		default :
			System.err.println("PLEASE ENTER VALID CHOICE");
		}

	}

}
	public static void reserveRoom(Connection con ,Scanner sc) {
		try {
		System.out.println("Enter Guest name");
		String guestName = sc.next();
		System.out.println("Enter the room number");
		String roomNumber = sc.next();
		System.out.println("Enter the contact number");
		String contactNumber = sc.next();
		
		String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                "VALUES ('" + guestName + "', '" + roomNumber + "', '" + contactNumber + "')";
		
		 try (Statement stmt = con.createStatement()) {
	            // Properly format the SQL query string
	            

	            int rows = stmt.executeUpdate(sql);

	            if (rows > 0) {
	                System.out.println("Reservation Successful");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

}
	public static void viewReservations(Connection con) throws SQLException {
		
		String sql="select reservation_id, guest_name , room_number ,contact_number , reservation_date from reservations ";
		try (Statement stmt = con.createStatement(); 
				ResultSet rs = stmt.executeQuery(sql)){
			System.out.println("current Reservation");
			
			
			while(rs.next()) {
				int rs_id = rs.getInt("reservation_id");
				String gs = rs.getString("guest_name");
				int rn    = rs.getInt("room_number");
				String cn = rs.getString("contact_number");
				String rd = rs.getString("reservation_date").toString();
				System.out.println("Reservation Id :"+rs_id);
				System.out.println("Guest Name     :"+gs);
				System.out.println("Room Number    :"+rn);
				System.out.println("Contect Number :"+cn);
				System.out.println("Resevation Date:"+rd);
				System.out.println("-------------------->");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void getRoomNumber(Connection con, Scanner sc) {
	    try {
	        System.out.println("Enter the ID");
	        int reservationId = sc.nextInt();
	        System.out.println("Enter Guest name");
	        String guestName = sc.next();

	        String sql = "SELECT room_number FROM reservations " +
	        "WHERE reservation_id = " + reservationId 
	        + " AND guest_name = '" + guestName + "'";
	        try (Statement stmt = con.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            if (rs.next()) {
	                int roomNumber = rs.getInt("room_number");
	                System.out.println("Reservation number for Reservation id " + reservationId + " and guest is " + guestName + " is " + roomNumber);
	            } else {
	                System.out.println("No matching reservation found for Reservation id " + reservationId + " and guest " + guestName);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

    private static void updateReservation(Connection connection,Scanner scanner ) {
    	try {
    		System.out.println("Enter Reservation ID to Update :");
    		int reservationId =  scanner.nextInt();
    		scanner.nextLine();
    		
    		if(!resrervationExists(connection , reservationId)) {
    			
    			System.err.println("Reservation Not Found For Give Id");
    			return;
    		}
    		
    		System.out.println("Enter The Guest Name");
    		String newGuestName = scanner.nextLine();
    		
    		System.out.println("Enter The Room Number ");
    		int newRoomNumber = scanner.nextInt();
    		
    		System.out.println("Enter The Contect Number ");
    		String newContectNumber = scanner.next();
    		
    		String sql = "UPDATE reservations SET guest_name = '"+ newGuestName + " ' , " +
    		"room_number = " + newRoomNumber + " , " +
    		"contact_number = '" + newContectNumber + "' "+
    		"WHERE reservation_id = " + reservationId;
    		
    		try(Statement statement = connection.createStatement()){
    			int Rows = statement.executeUpdate(sql);
    
    		
    		if(Rows>0) {
    			System.out.println("Reservation Update Successfully ");
    		}

    	}
    }catch(Exception e) {
		e.printStackTrace();
       }
    }
    
    private static void deleteReservation(Connection connection, Scanner scanner) {
    	try {
    		System.out.println("Enter Reservation Id To Delete :");
    		
    		int reservation_id = scanner.nextInt();
    		
    		int reservationId = 0;
			if(!resrervationExists(connection , reservationId)) {
			
			System.err.println("Reservation Not Found For Give Id");
			return;
		}
    		String sql = "DELETE FROM reservations WHERE reservation_id = "+ reservation_id;
    	    		
    	    		try(Statement statement = connection.createStatement()){
    	    			int Rows = statement.executeUpdate(sql);
    	    
    	    		
    	    		if(Rows>0) {
    	    			System.out.println("Reservation Deleted Successfully ");
    	    		}

    	    	}	
    	}catch(Exception e) {
    		e.printStackTrace();
        }
    }
    
    public static boolean resrervationExists(Connection connection , int reservationId) {
    	try {
    		String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = "+ reservationId;
    		
    		try(Statement statement = connection.createStatement();
    			ResultSet resultSet = statement.executeQuery(sql)){
    				
    				return resultSet.next();
    			}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		return false;
        }
    }
    
}
