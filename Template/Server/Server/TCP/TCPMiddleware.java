package Server.TCP;

import java.io.IOException;
import java.net.*;
import java.util.Vector;

import Server.Common.*;
import Server.Interface.*;

public class TCPMiddleware extends ResourceManager {
	private static String m_serverName = "Middleware";
	private static String m_rmiPrefix = "group_24_";
	private static String server_host = "localhost";

	private static ServerSocket serverSocket = null;
	private static TCPMiddleware socketMiddleware = null;
	private static int middleware_port = 3024;
	private static int server_port_car = 3124;
	private static int server_port_room = 3224;
	private static int server_port_flight = 3324;

	private static ClientSocket flightRM = null;
	private static ClientSocket carRM = null;
	private static ClientSocket roomRM = null;

	private static String s_serverName = "Server";
	// TODO: ADD YOUR GROUP NUMBER TO COMPLETE
	private static String s_rmiPrefix = "group_24_";

	public static void main(String[] args) {
		if (args.length > 0) {
			s_serverName = args[3];
		}

		try {
			socketMiddleware = new TCPMiddleware(s_serverName);

			// Middleware must not block when it is waiting for the ResourceManagers to
			// execute a request.
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						flightRM.stopClient();
						carRM.stopClient();
						roomRM.stopClient();
						serverSocket.close();
						System.out.println("'" + s_serverName + "' resource manager unbound");
					} catch (Exception e) {
						System.err.println(
								(char) 27 + "[31;1mMiddleware exception: " + (char) 27 + "[0mUncaught exception" + e.toString());
						e.printStackTrace();
					}
				}
			});

			System.out.println((char) 27 + "[31;1mMiddleware starting to get input...");
			while (true) {
				serverSocket = new ServerSocket(middleware_port);
				ServerSocketThread sthread = new ServerSocketThread(serverSocket.accept(), socketMiddleware);
				sthread.start();
			}
		} catch (Exception e) {
			System.err.println((char) 27 + "[31;1mMiddleware exception: " + (char) 27 + "[0mUncaught exception " + e.toString());
		}
	}

	public TCPMiddleware(String p_name) {
		super(p_name);
		flightRM = new ClientSocket(server_host, server_port_flight);
		carRM = new ClientSocket(server_host, server_port_car);
		roomRM = new ClientSocket(server_host, server_port_room);

		flightRM.connect();
		carRM.connect();
		roomRM.connect();
	}

	public String executeCommand(Command cmd, Vector<String> arguments, String message) {
		try {
			switch (cmd) {
			case AddFlight: {
				try {
					synchronized (flightRM) {
	                    try {
	                        String res = flightRM.process(message);
	                        if (res.equals(""))throw new IOException();
	                        return res;
	                    } catch (IOException e) {
	                    	flightRM.connect();
	                        return flightRM.process(message);
	                    }
	                }
					
				} catch (Exception e) {
					return "Failed to execute command: AddFlight";
				}
			}
			case AddCars: {
				try {
					synchronized (carRM) {
	                    try {
	                        String res = carRM.process(message);
	                        if (res.equals("")) throw new IOException();
	                        return res;
	                    } catch (IOException e) {
	                    	carRM.connect();
	                        return carRM.process(message);
	                    }
	                }
					
				} catch (Exception e) {
					return "Failed to execute command: AddCars";
				}
			}
			case AddRooms: {
				try {
					synchronized (roomRM) {
	                    try {
	                        String res = roomRM.process(message);
	                        if (res.equals("")) throw new IOException();
	                        return res;
	                    } catch (IOException e) {
	                    	roomRM.connect();
	                        return roomRM.process(message);
	                    }
	                }
					
				} catch (Exception e) {
					return "Failed to execute command: AddCars";
				}
			}
			case AddCustomer: {
				// TODO: handle this case
				
				break;
			}
			case AddCustomerID: {
				// TODO: handle this case
				break;
			}
			case DeleteFlight: {
				try {
					synchronized (flightRM) {
	                    try {
	                        String res = flightRM.process(message);
	                        if (res.equals("")) throw new IOException();
	                        return res;
	                    } catch (IOException e) {
	                    	flightRM.connect();
	                        return flightRM.process(message);
	                    }
	                }
				} catch (Exception e) {
					return "Failed to execute command: DeleteFlight";
				}
			}
			case DeleteCars: {
				try {
					synchronized (carRM) {
	                    try {
	                        String res = carRM.process(message);
	                        if (res.equals("")) throw new IOException();
	                        return res;
	                    } catch (IOException e) {
	                    	carRM.connect();
	                        return carRM.process(message);
	                    }
	                }
				} catch (Exception e) {
					return "Failed to execute command: DeleteCars";
				}
			}
			case DeleteRooms: {
				try {
					synchronized (roomRM) {
	                    try {
	                        String res = roomRM.process(message);
	                        if (res.equals("")) throw new IOException();
	                        return res;
	                    } catch (IOException e) {
	                    	roomRM.connect();
	                        return roomRM.process(message);
	                    }
	                }
				} catch (Exception e) {
					return "Failed to execute command: DeleteRooms";
				}
			}
			case DeleteCustomer: {
				// handle this case;
				break;
			}
			case QueryFlight: {
				try {
                    String res = flightRM.process(message);
                    if (res.equals("")) throw new IOException();
                    return res;
                } catch (IOException e) {
                	flightRM.connect();
                    return flightRM.process(message);
                }
			}
			case QueryCars: {
				try {
                    String res = carRM.process(message);
                    if (res.equals("")) throw new IOException();
                    return res;
                } catch (IOException e) {
                	carRM.connect();
                    return carRM.process(message);
                }
			}
			case QueryRooms: {
				try {
                    String res = roomRM.process(message);
                    if (res.equals("")) throw new IOException();
                    return res;
                } catch (IOException e) {
                	roomRM.connect();
                    return roomRM.process(message);
                }
			}
			case QueryCustomer: {
				// TODO: handle this case
				break;
			}
			case QueryFlightPrice: {
				try {
                    String res = flightRM.process(message);
                    if (res.equals("")) throw new IOException();
                    return res;
                } catch (IOException e) {
                	flightRM.connect();
                    return flightRM.process(message);
                }
			}
			case QueryCarsPrice: {
				try {
                    String res = carRM.process(message);
                    if (res.equals("")) throw new IOException();
                    return res;
                } catch (IOException e) {
                	carRM.connect();
                    return carRM.process(message);
                }
			}
			case QueryRoomsPrice: {
				try {
                    String res = roomRM.process(message);
                    if (res.equals("")) throw new IOException();
                    return res;
                } catch (IOException e) {
                	roomRM.connect();
                    return roomRM.process(message);
                }
			}
			case ReserveFlight: {
				break;
			}
			case ReserveCar: {
				break;
			}
			case ReserveRoom: {
				break;
			}
			case Bundle: {
				// TODO: implement bundle
				// if (arguments.size() < 7) {
				// System.err.println((char)27 + "[31;1mCommand exception: " + (char)27 +
				// "[0mBundle command expects at least 7 arguments. Location \"help\" or
				// \"help,<CommandName>\"");
				// break;
				// }

				// System.out.println("Reserving an bundle [xid=" + arguments.elementAt(1) +
				// "]");
				// System.out.println("-Customer ID: " + arguments.elementAt(2));
				// for (int i = 0; i < arguments.size() - 6; ++i)
				// {
				// System.out.println("-Flight Number: " + arguments.elementAt(3+i));
				// }
				// System.out.println("-Location for Car/Room: " +
				// arguments.elementAt(arguments.size()-3));
				// System.out.println("-Book Car: " + arguments.elementAt(arguments.size()-2));
				// System.out.println("-Book Room: " + arguments.elementAt(arguments.size()-1));

				// int id = toInt(arguments.elementAt(1));
				// int customerID = toInt(arguments.elementAt(2));
				// Vector<String> flightNumbers = new Vector<String>();
				// for (int i = 0; i < arguments.size() - 6; ++i)
				// {
				// flightNumbers.addElement(arguments.elementAt(3+i));
				// }
				// String location = arguments.elementAt(arguments.size()-3);
				// boolean car = toBoolean(arguments.elementAt(arguments.size()-2));
				// boolean room = toBoolean(arguments.elementAt(arguments.size()-1));

				// if (m_resourceManager.bundle(id, customerID, flightNumbers, location, car,
				// room)) {
				// System.out.println("Bundle Reserved");
				// } else {
				// System.out.println("Bundle could not be reserved");
				// }
				break;
			}
			
			}
		} catch (Exception e) {
			System.out.println((char)27 + "[31;1mMiddleware exception: " + (char)27 + "[0mUncaught exception " + e.toString());
			e.printStackTrace();
//			System.exit(1);
		}
		return "";
	}
}

