package aaf.model;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

public class StorageManager {

	/**
	 * main method is for testing purposes only
	 * @param args
	 */
	public static void main(String[] args) {

		Nominator nominator = new Nominator("Brooke", "Nominator", "kellyahergert@gmail.com");
		Family family = new Family(1, nominator, "Sherlock", "Katie", "SSherlock",
				"family@fam.com", 5, "test");

		Nominator nominator2 = new Nominator("Ashley", "Nominator", "kellyahergert@gmail.com");
		Family family2 = new Family(2, nominator2, "Hagendaz", "Ice", "IceHag",
				"family@fam2.com", 7, "test2");
		
		PriorityQueue<Family> families = new PriorityQueue<Family>();
		families.add(family);
		families.add(family2);

		Sponsor sponsor = new Sponsor(1, "John", "Johnson", "kellyahergert@gmail.com", 1, 2, 3);
		Sponsor sponsor2 = new Sponsor(2, "Eric", "Erickson", "kellyahergert@gmail.com", 4, 5, 6);

		LinkedList<Sponsor> sponsors = new LinkedList<Sponsor>();
		sponsors.add(sponsor);
		sponsors.add(sponsor2);
		
		StorageManager storageMgr = new StorageManager();
		
//		storageMgr.storeFamilies(families);
//		storageMgr.storeSponsors(sponsors);
		Queue<Family> retrievedFams = storageMgr.retrieveFamilies();
		storageMgr.retrieveSponsors();
		
		for (Family fam : retrievedFams)
		{
			System.out.println("fam " + fam);
		}
		
		
//		storageMgr.deleteAllData();
	}
	
	public EntityManager getConnection()
	{
    	// pass in package name where listeners are located
        com.objectdb.Enhancer.enhance("aaf.model.*");
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(
        		"$objectdb/db/AdoptAFamily.odb");
        
        EntityManager em = emf.createEntityManager();
        
        return em;
	}
	
	/**
	 * Deletes all the tables out of the database
	 */
	public void deleteAllData()
	{
        EntityManager em = getConnection();
       try
       {
			em.getTransaction().begin();

			em.createQuery("DELETE FROM Family").executeUpdate();
			em.createQuery("DELETE FROM Sponsor").executeUpdate();
			em.createQuery("DELETE FROM Nominator").executeUpdate();
			em.createQuery("DELETE FROM Person").executeUpdate();
			
			em.getTransaction().commit();
        }
		catch (PersistenceException e)
		{
			System.out.println("No data to delete from database: " + e.getMessage());
        }
        finally 
        {
            // Close the database connection:
            if (em.getTransaction().isActive())
            {
                em.getTransaction().rollback();
            }
            em.close();
        }
	}
	
	/**
	 * retrieve all families from the database
	 * @return
	 */
	public Queue<Family> retrieveFamilies()
	{
        EntityManager em = getConnection();

		Queue<Family> families = new PriorityQueue<Family>();
		
        List<Family> familyList = em.createQuery(
                "SELECT fam FROM Family fam", Family.class).getResultList();
        

//        for (Family family : familyList)
//        {
//        	System.out.println("retrieved fam: " + family);
//        }
        
        families.addAll(familyList);

		em.close();

		return families;
	}
		
	/**
	 * retrieve all sponsors from the database
	 * @return
	 */
	public List<Sponsor> retrieveSponsors()
	{
        EntityManager em = getConnection();

        List<Sponsor> sponsorList = em.createQuery(
                "SELECT spon FROM Sponsor spon", Sponsor.class).getResultList();
        
        LinkedList<Sponsor> sponsors = new LinkedList<Sponsor>();
        sponsors.addAll(sponsorList);
        
		em.close();
        
//        for (Sponsor sponsor : sponsorList)
//        {
//        	System.out.println("retrieved spon: " + sponsor);
//        }
        
		return sponsors;
	}
	
	/**
	 * store families in the database if they dont already exist
	 * @param families
	 * @return
	 */
	public boolean storeFamilies(PriorityQueue<Family> families)
	{
        EntityManager em = getConnection();
        
        try {

			em.getTransaction().begin();
			
			for (Family family : families)
			{
				System.out.println("Adding family " + family);

				em.persist(family.getNominator());
				em.persist(family);
			}
			em.getTransaction().commit();

		}
		catch (EntityExistsException | RollbackException e)
		{
			System.out.println("problem storing families: " + e.getMessage());
			return false;
        }
        finally 
        {
            // Close the database connection:
            if (em.getTransaction().isActive())
            {
                em.getTransaction().rollback();
            }
            em.close();
        }
		
		return true;
	}
	
	/**
	 * store sponsors in the database if they dont already exist
	 * @param sponsors
	 * @return
	 */
	public boolean storeSponsors(LinkedList<Sponsor> sponsors)
	{
        EntityManager em = getConnection();
        
        try {

			em.getTransaction().begin();
			
			for (Sponsor sponsor : sponsors)
			{
				System.out.println("Adding sponsor " + sponsor);

				em.persist(sponsor);
			}
			
			em.getTransaction().commit();

		}
		catch (EntityExistsException | RollbackException e)
		{
			System.out.println("problem storing sponsors: " + e.getMessage());
			return false;
        }
        finally
        {
            // Close the database connection:
            if (em.getTransaction().isActive())
            {
                em.getTransaction().rollback();
            }
            em.close();
        }
		
		return true;
	}

	
}
