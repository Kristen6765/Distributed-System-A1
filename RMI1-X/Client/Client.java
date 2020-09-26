import java.rmi.registry.*;

public class Client
{
 public static void main(String args[])
 {
		System.setSecurityManager(new SecurityManager());
		try
		{
			Registry registry = LocateRegistry.getRegistry(args[0], Integer.parseInt(args[1]));
     	Account acc = (Account) registry.lookup("Acc");
      
			float newBal = acc.addToBlance(20.0f);
			System.out.println(newBal);
			
		} catch (Exception e)
		{ System.err.println("exception: "+e.getMessage()); }
  }
}

