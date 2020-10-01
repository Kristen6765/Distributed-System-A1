// -------------------------------
// adapted from Kevin T. Manley
// CSE 593
// -------------------------------

package Server.RMI;

import Server.Interface.*;

import java.util.*;
import java.rmi.RemoteException;
import java.io.*;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;


import Server.Common.*;
import java.rmi.server.UnicastRemoteObject;

public class RMIMiddleware extends ResourceManager
{
	
    private static String m_serverName ="Middleware";
    private static String m_rmiPrefix = "group_24_";

    private static int middleware_port = 3024;
    private static int server_port_car = 3124; 
    private static int server_port_room = 3224;
    private static int server_port_flight = 3324;

    private static IResourceManager flightRM = null;
    private static IResourceManager carRM = null;
    private static IResourceManager roomRM  = null;

    private Queue<Integer> customerIdx;

	protected String m_name = "";
	protected RMHashMap m_data = new RMHashMap();

    private static String s_serverName = "Server";
	//TODO: ADD YOUR GROUP NUMBER TO COMPLETE
	private static String s_rmiPrefix = "group_24_";

	public static void main(String args[])
	{
		if (args.length > 0)
		{
			s_serverName = args[3];
		}
			
		// Create the RMI server entry
		try {
			// Create a new Server object
			RMIMiddleware server = new RMIMiddleware(s_serverName);

			// Dynamically generate the stub (client proxy)
			IResourceManager resourceManager = (IResourceManager)UnicastRemoteObject.exportObject(server, 0);

			// Bind the remote object's stub in the registry
			Registry l_registry;
			try {
				l_registry = LocateRegistry.createRegistry(3024);
			} catch (RemoteException e) {
				l_registry = LocateRegistry.getRegistry(3024);
			}
			final Registry registry = l_registry;
			registry.rebind(s_rmiPrefix + s_serverName, resourceManager);

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						registry.unbind(s_rmiPrefix + s_serverName);
						System.out.println("'" + s_serverName + "' resource manager unbound");
					}
					catch(Exception e) {
						System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
						e.printStackTrace();
					}
				}
			});                                       
			System.out.println("'" + s_serverName + "' resource manager server ready and bound to '" + s_rmiPrefix + s_serverName + "'");
		}
		catch (Exception e) {
			System.err.println((char)27 + "[31;1mServer exception: " + (char)27 + "[0mUncaught exception");
			e.printStackTrace();
			System.exit(1);
		}

		// Create and install a security manager
		if (System.getSecurityManager() == null)
		{
			System.setSecurityManager(new SecurityManager());
		}
	}


	public RMIMiddleware(String p_name)
	{
		super(p_name);
		m_name = p_name;
	
				try {
					Registry carRegistry = LocateRegistry.getRegistry("localhost", server_port_car);
                    carRM = (IResourceManager) carRegistry.lookup(m_rmiPrefix + "Cars");
                   // System.out.println("Connected to '" +"Cars"  + "' server [" + args[1] + ":" + server_port + "/" + s_rmiPrefix + name + "]");
                    if (carRM == null)
                        throw new AssertionError();

					Registry roomRegistry = LocateRegistry.getRegistry("localhost", server_port_room);
                    roomRM = (IResourceManager) roomRegistry.lookup(m_rmiPrefix + "Rooms");
                   // System.out.println("Connected to '" +"Cars"  + "' server [" + args[1] + ":" + server_port + "/" + s_rmiPrefix + name + "]");
                    if (roomRM == null)
                        throw new AssertionError();
	
					Registry flightRegistry = LocateRegistry.getRegistry("localhost", server_port_flight);
                    flightRM = (IResourceManager) flightRegistry.lookup(m_rmiPrefix + "Flights");
                   // System.out.println("Connected to '" +"Cars"  + "' server [" + args[1] + ":" + server_port + "/" + s_rmiPrefix + name + "]");
                    if (flightRM == null)
                        throw new AssertionError();
				//  Registry flightRegistry = LocateRegistry.getRegistry("localhost", server_port_flight);
                //     flightRM = (IResourceManager) flightRegistry.lookup(m_rmiPrefix + "Resources");
                //    // System.out.println("Connected to '" +"Cars"  + "' server [" + args[1] + ":" + server_port + "/" + s_rmiPrefix + name + "]");
                //     if (carRM == null)
                //         throw new AssertionError();
				}
				catch (NotBoundException|RemoteException e) {
					System.out.println(e);
				}
	}


	// // Reads a data item
	// protected RMItem readData(int xid, String key)
	// {
	// 	synchronized(m_data) {
	// 		RMItem item = m_data.get(key);
	// 		if (item != null) {
	// 			return (RMItem)item.clone();
	// 		}
	// 		return null;
	// 	}
	// }

	// // Writes a data item
	// protected void writeData(int xid, String key, RMItem value)
	// {
	// 	synchronized(m_data) {
	// 		m_data.put(key, value);
	// 	}
	// }

	// // Remove the item out of storage
	// protected void removeData(int xid, String key)
	// {
	// 	synchronized(m_data) {
	// 		m_data.remove(key);
	// 	}
	// }

	// // Deletes the encar item
	// protected boolean deleteItem(int xid, String key)
	// {
	// 	Trace.info("RM::deleteItem(" + xid + ", " + key + ") called");
	// 	ReservableItem curObj = (ReservableItem)readData(xid, key);
	// 	// Check if there is such an item in the storage
	// 	if (curObj == null)
	// 	{
	// 		Trace.warn("RM::deleteItem(" + xid + ", " + key + ") failed--item doesn't exist");
	// 		return false;
	// 	}
	// 	else
	// 	{
	// 		if (curObj.getReserved() == 0)
	// 		{
	// 			removeData(xid, curObj.getKey());
	// 			Trace.info("RM::deleteItem(" + xid + ", " + key + ") item deleted");
	// 			return true;
	// 		}
	// 		else
	// 		{
	// 			Trace.info("RM::deleteItem(" + xid + ", " + key + ") item can't be deleted because some customers have reserved it");
	// 			return false;
	// 		}
	// 	}
	// }

	// // Query the number of available seats/rooms/cars
	// protected int queryNum(int xid, String key)
	// {
	// 	Trace.info("RM::queryNum(" + xid + ", " + key + ") called");
	// 	ReservableItem curObj = (ReservableItem)readData(xid, key);
	// 	int value = 0;  
	// 	if (curObj != null)
	// 	{
	// 		value = curObj.getCount();
	// 	}
	// 	Trace.info("RM::queryNum(" + xid + ", " + key + ") returns count=" + value);
	// 	return value;
	// }    

	// // Query the price of an item
	// protected int queryPrice(int xid, String key)
	// {
	// 	Trace.info("RM::queryPrice(" + xid + ", " + key + ") called");
	// 	ReservableItem curObj = (ReservableItem)readData(xid, key);
	// 	int value = 0; 
	// 	if (curObj != null)
	// 	{
	// 		value = curObj.getPrice();
	// 	}
	// 	Trace.info("RM::queryPrice(" + xid + ", " + key + ") returns cost=$" + value);
	// 	return value;        
	// }

	// // Reserve an item
	// protected boolean reserveItem(int xid, int customerID, String key, String location)
	// {
	// 	Trace.info("RM::reserveItem(" + xid + ", customer=" + customerID + ", " + key + ", " + location + ") called" );        
	// 	// Read customer object if it exists (and read lock it)
	// 	Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
	// 	if (customer == null)
	// 	{
	// 		Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ")  failed--customer doesn't exist");
	// 		return false;
	// 	} 

	// 	// Check if the item is available
	// 	ReservableItem item = (ReservableItem)readData(xid, key);
	// 	if (item == null)
	// 	{
	// 		Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ") failed--item doesn't exist");
	// 		return false;
	// 	}
	// 	else if (item.getCount() == 0)
	// 	{
	// 		Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ") failed--No more items");
	// 		return false;
	// 	}
	// 	else
	// 	{            
	// 		customer.reserve(key, location, item.getPrice());        
	// 		writeData(xid, customer.getKey(), customer);

	// 		// Decrease the number of available items in the storage
	// 		item.setCount(item.getCount() - 1);
	// 		item.setReserved(item.getReserved() + 1);
	// 		writeData(xid, item.getKey(), item);

	// 		Trace.info("RM::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ") succeeded");
	// 		return true;
	// 	}        
	// }
	@Override
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice)
		throws RemoteException {
		return flightRM.addFlight(id, flightNum, flightSeats, flightPrice);
	}

	
	@Override
	public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
		return carRM.addCars(id, location, numCars, price);
	}

	@Override
 	public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
    return roomRM.addRooms(id, location, numRooms, price);
  	}

	
	@Override
	public boolean deleteFlight(int id, int flightNum) throws RemoteException {
		return flightRM.deleteFlight(id, flightNum);
	}

	@Override
	public boolean deleteCars(int id, String location) throws RemoteException {
		return carRM.deleteCars(id, location);
	}

	@Override
	public boolean deleteRooms(int id, String location) throws RemoteException {
		return roomRM.deleteRooms(id, location);
	}

@Override
  public int queryFlight(int id, int flightNumber) throws RemoteException {
    return flightRM.queryFlight(id, flightNumber);
  }

  @Override
  public int queryCars(int id, String location) throws RemoteException {
    return carRM.queryCars(id, location);
  }

  @Override
  public int queryRooms(int id, String location) throws RemoteException {
    return roomRM.queryRooms(id, location);
  }

  @Override
  public String queryCustomerInfo(int id, int customerID) throws RemoteException {
    return flightRM.queryCustomerInfo(id, customerID)
        + carRM.queryCustomerInfo(id, customerID).split("/n", 2)[1] + roomRM.queryCustomerInfo(id, customerID).split("/n", 2)[1];
  }

  @Override
  public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
    return flightRM.queryFlightPrice(id, flightNumber);
  }

  @Override
  public int queryCarsPrice(int id, String location) throws RemoteException {
    return carRM.queryCarsPrice(id, location);
  }

  @Override
  public int queryRoomsPrice(int id, String location) throws RemoteException {
    return roomRM.queryRoomsPrice(id, location);
  }

  @Override
  public int newCustomer(int id) throws RemoteException {
    int cid = super.newCustomer(id);
    flightRM.newCustomer(id, cid);
    carRM.newCustomer(id, cid);
    roomRM.newCustomer(id, cid);
    //int cid = Collections.max(customerIdx);
    //this.newCustomer(id, cid);
    return cid;
  }

  @Override
  public boolean newCustomer(int id, int cid) throws RemoteException {
    //this.customerIdx.add(cid);
    return super.newCustomer(id, cid) && flightRM.newCustomer(id, cid) && carRM.newCustomer(id, cid) && roomRM.newCustomer(id, cid);
  }

  @Override
  public boolean deleteCustomer(int xid, int customerID) throws RemoteException{
	  return carRM.deleteCustomer(xid, customerID) && roomRM.deleteCustomer(xid,customerID) 
	  && flightRM.deleteCustomer(xid, customerID);
  }

  @Override
  public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException {
    return flightRM.reserveFlight(id, customerID, flightNumber);
  }

  @Override
  public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
    return carRM.reserveCar(id, customerID, location);
  }

  @Override
  public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {
    return roomRM.reserveRoom(id, customerID, location);
  }


  @Override
  public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location,
      boolean car, boolean room) throws RemoteException {
    	boolean res = true;
		for (String fn:flightNumbers) res &=reserveFlight(id,customerID,Integer.parseInt(fn));
		if (car) res &= reserveCar(id, customerID, location);
		if (room) res &= reserveRoom(id, customerID, location);
		return res; // return False if any of the above failed
  }

  

	// // Returns the number of empty seats in this flight
	// public int queryFlight(int xid, int flightNum) throws RemoteException
	// {
	// 	return queryNum(xid, Flight.getKey(flightNum));
	// }

	// // Returns the number of cars available at a location
	// public int queryCars(int xid, String location) throws RemoteException
	// {
	// 	return queryNum(xid, Car.getKey(location));
	// }

	// // Returns the amount of rooms available at a location
	// public int queryRooms(int xid, String location) throws RemoteException
	// {
	// 	return queryNum(xid, Room.getKey(location));
	// }

	// // Returns price of a seat in this flight
	// public int queryFlightPrice(int xid, int flightNum) throws RemoteException
	// {
	// 	return queryPrice(xid, Flight.getKey(flightNum));
	// }

	// // Returns price of cars at this location
	// public int queryCarsPrice(int xid, String location) throws RemoteException
	// {
	// 	return queryPrice(xid, Car.getKey(location));
	// }

	// // Returns room price at this location
	// public int queryRoomsPrice(int xid, String location) throws RemoteException
	// {
	// 	return queryPrice(xid, Room.getKey(location));
	// }

	// public String queryCustomerInfo(int xid, int customerID) throws RemoteException
	// {
	// 	Trace.info("RM::queryCustomerInfo(" + xid + ", " + customerID + ") called");
	// 	Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
	// 	if (customer == null)
	// 	{
	// 		Trace.warn("RM::queryCustomerInfo(" + xid + ", " + customerID + ") failed--customer doesn't exist");
	// 		// NOTE: don't change this--WC counts on this value indicating a customer does not exist...
	// 		return "";
	// 	}
	// 	else
	// 	{
	// 		Trace.info("RM::queryCustomerInfo(" + xid + ", " + customerID + ")");
	// 		System.out.println(customer.getBill());
	// 		return customer.getBill();
	// 	}
	// }

	// public int newCustomer(int xid) throws RemoteException
	// {
    //     	Trace.info("RM::newCustomer(" + xid + ") called");
	// 	// Generate a globally unique ID for the new customer
	// 	int cid = Integer.parseInt(String.valueOf(xid) +
	// 		String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
	// 		String.valueOf(Math.round(Math.random() * 100 + 1)));
	// 	Customer customer = new Customer(cid);
	// 	writeData(xid, customer.getKey(), customer);
	// 	Trace.info("RM::newCustomer(" + cid + ") returns ID=" + cid);
	// 	return cid;
	// }

	// public boolean newCustomer(int xid, int customerID) throws RemoteException
	// {
	// 	Trace.info("RM::newCustomer(" + xid + ", " + customerID + ") called");
	// 	Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
	// 	if (customer == null)
	// 	{
	// 		customer = new Customer(customerID);
	// 		writeData(xid, customer.getKey(), customer);
	// 		Trace.info("RM::newCustomer(" + xid + ", " + customerID + ") created a new customer");
	// 		return true;
	// 	}
	// 	else
	// 	{
	// 		Trace.info("INFO: RM::newCustomer(" + xid + ", " + customerID + ") failed--customer already exists");
	// 		return false;
	// 	}
	// }

	// public boolean deleteCustomer(int xid, int customerID) throws RemoteException
	// {
	// 	Trace.info("RM::deleteCustomer(" + xid + ", " + customerID + ") called");
	// 	Customer customer = (Customer)readData(xid, Customer.getKey(customerID));
	// 	if (customer == null)
	// 	{
	// 		Trace.warn("RM::deleteCustomer(" + xid + ", " + customerID + ") failed--customer doesn't exist");
	// 		return false;
	// 	}
	// 	else
	// 	{            
	// 		// Increase the reserved numbers of all reservable items which the customer reserved. 
 	// 		RMHashMap reservations = customer.getReservations();
	// 		for (String reservedKey : reservations.keySet())
	// 		{        
	// 			ReservedItem reserveditem = customer.getReservedItem(reservedKey);
	// 			Trace.info("RM::deleteCustomer(" + xid + ", " + customerID + ") has reserved " + reserveditem.getKey() + " " +  reserveditem.getCount() +  " times");
	// 			ReservableItem item  = (ReservableItem)readData(xid, reserveditem.getKey());
	// 			Trace.info("RM::deleteCustomer(" + xid + ", " + customerID + ") has reserved " + reserveditem.getKey() + " which is reserved " +  item.getReserved() +  " times and is still available " + item.getCount() + " times");
	// 			item.setReserved(item.getReserved() - reserveditem.getCount());
	// 			item.setCount(item.getCount() + reserveditem.getCount());
	// 			writeData(xid, item.getKey(), item);
	// 		}

	// 		// Remove the customer from the storage
	// 		removeData(xid, customer.getKey());
	// 		Trace.info("RM::deleteCustomer(" + xid + ", " + customerID + ") succeeded");
	// 		return true;
	// 	}
	// }

	// // Adds flight reservation to this customer
	// public boolean reserveFlight(int xid, int customerID, int flightNum) throws RemoteException
	// {
	// 	return reserveItem(xid, customerID, Flight.getKey(flightNum), String.valueOf(flightNum));
	// }

	// // Adds car reservation to this customer
	// public boolean reserveCar(int xid, int customerID, String location) throws RemoteException
	// {
	// 	return reserveItem(xid, customerID, Car.getKey(location), location);
	// }

	// // Adds room reservation to this customer
	// public boolean reserveRoom(int xid, int customerID, String location) throws RemoteException
	// {
	// 	return reserveItem(xid, customerID, Room.getKey(location), location);
	// }

	// // Reserve bundle 
	// public boolean bundle(int xid, int customerId, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException
	// {
	// 	return false;
	// }

	public String getName() throws RemoteException
	{
		return m_name;
	}
}
 




