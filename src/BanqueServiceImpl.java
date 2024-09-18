import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class BanqueServiceImpl extends UnicastRemoteObject implements BanqueService {
    private Map<String, Double>[] comptesArray;
    private Map<String, Integer> accountIndexMap; // To remember the index for each account

    public BanqueServiceImpl() throws RemoteException {
        int numberOfAccounts = 10;
        comptesArray = new HashMap[numberOfAccounts];
        accountIndexMap = new HashMap<>();
        for (int i = 0; i < comptesArray.length; i++) {
            comptesArray[i] = new HashMap<>();
        }
    }

    @Override
    public void creerCompte(String id, double somme) throws RemoteException {
        int availableIndex = getAvailableIndex();
        if (availableIndex != -1) {
            comptesArray[availableIndex].put(id, somme);
            accountIndexMap.put(id, availableIndex);
        } else {
            throw new RemoteException("No available account slots.");
        }
    }

    @Override
    public void ajouter(String id, double somme) throws RemoteException {
        if (accountIndexMap.containsKey(id)) {
            int accountIndex = accountIndexMap.get(id);
            comptesArray[accountIndex].put(id, comptesArray[accountIndex].get(id) + somme);
        } else {
            throw new RemoteException("Compte non trouvé.");
        }
    }

    @Override
    public void retirer(String id, double somme) throws RemoteException {
        if (accountIndexMap.containsKey(id)) {
            int accountIndex = accountIndexMap.get(id);
            if (comptesArray[accountIndex].containsKey(id)) {
                double solde = comptesArray[accountIndex].get(id);
                if (solde >= somme) {
                    comptesArray[accountIndex].put(id, solde - somme);
                } else {
                    throw new RemoteException("Solde insuffisant.");
                }
            }
        } else {
            throw new RemoteException("Compte non trouvé.");
        }
    }

    @Override
    public double consulterSolde(String id) throws RemoteException {
        if (accountIndexMap.containsKey(id)) {
            int accountIndex = accountIndexMap.get(id);
            if (comptesArray[accountIndex].containsKey(id)) {
                return comptesArray[accountIndex].get(id);
            }
        }
        throw new RemoteException("Compte non trouvé.");
    }

    @Override
    public void transfererSolde(String id_C, String id_D, double somme) throws RemoteException {
        retirer(id_C, somme);
        ajouter(id_D, somme);
    }

    private int getAvailableIndex() {
        for (int i = 0; i < comptesArray.length; i++) {
            if (comptesArray[i].isEmpty()) {
                return i;
            }
        }
        return -1; // Indicates no available index
    }
}
