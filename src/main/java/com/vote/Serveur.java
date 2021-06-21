package com.vote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.hibernate.Session;

import com.vote.models.Condidate;
import com.vote.service.Vote;
import com.vote.serviceImpl.VoteImpl;
import com.vote.utils.HibernateUtil;

/**
 * Hello world!
 *
 */
public class Serveur {
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		Vote skeleton = new VoteImpl();
		LocateRegistry.createRegistry(1099);
		Naming.rebind("vote", skeleton);
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Condidate cd1 = new Condidate("yassir");
		session.save(cd1);
		Condidate cd2 = new Condidate("amadou");
		session.save(cd2);
		session.getTransaction().commit();
		session.close();
		System.out.println("Server registry is ready");

	}
}
