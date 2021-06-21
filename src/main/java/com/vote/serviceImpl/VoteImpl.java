package com.vote.serviceImpl;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vote.models.Condidate;
import com.vote.models.Votant;
import com.vote.service.Vote;
import com.vote.utils.HibernateUtil;

public class VoteImpl extends UnicastRemoteObject implements Vote {

	public VoteImpl() throws RemoteException {
		super();

		// TODO Auto-generated constructor stub
	}

	public List<Condidate> resultatVote() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Query q = session.createQuery("From Condidate");
		List<Condidate> resultList = q.list();
		session.close();
		return resultList;
	}

	public HashMap<Long, String> condidates() {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Query q = session.createQuery("From Condidate");
		List<Condidate> resultList = q.list();
		session.close();
		if (resultList.size() == 0) {
			return null;
		}
		HashMap<Long, String> condidat = new HashMap<Long, String>();
		for (Condidate c : resultList) {

			condidat.put(c.getId(), c.getNom());
		}

		return condidat;
	}

	public Votant signUp(String nom, String prenom, int age, String email, byte[] publicKey) throws RemoteException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("select votant from Votant as votant where votant.email = :email");
		query.setString("email", email);
		List result = query.list();
		if (result.size() == 0) {
			Votant votant = new Votant(nom, prenom, age, email, publicKey);
			votant = session.find(Votant.class, session.save(votant));
			session.close();
			return votant;

		}
		session.close();
		return null;
	}

	@SuppressWarnings("deprecation")
	public int voter(byte[] signatureMessage, PublicKey publicKey, String identifiantCondidat, Long idVotant)
			throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		// TODO Auto-generated method stub
		Signature signature = Signature.getInstance("SHA256WithDSA");
		signature.initVerify(publicKey);
		signature.update(identifiantCondidat.getBytes());
		boolean verified = signature.verify(signatureMessage);
		if (!verified) {
			return -1;
		} else {
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			String hql = "FROM Votant E WHERE E.id = :id";
			Query query = session.createQuery(hql);
			query.setParameter("id", idVotant);
			List<Votant> result = query.list();
			System.out.println(result.get(0));
			Votant votant = result.get(0);
			System.out.println(votant.getNom());
			if (votant.isVoter()) {
				session.close();
				return -2;
			} else {
				Condidate condidat = session.find(Condidate.class, Long.parseLong(identifiantCondidat));
				condidat.setNbrVotes(condidat.getNbrVotes() + 1);
				session.saveOrUpdate(condidat);
				votant.setVoter(true);
				session.saveOrUpdate(votant);
				session.getTransaction().commit();
				session.close();
				return 0;
			}
		}
	}
	public byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}
	public String findCondidatById(Long id) throws RemoteException {
		// TODO Auto-generated method stub
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Condidate condidat = session.find(Condidate.class, id);
		if (condidat != null) {
			session.close();
			return condidat.getNom();

		}
		session.close();
		return null;
	}

}
