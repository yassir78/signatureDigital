package com.vote.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vote.models.Condidate;
import com.vote.models.Votant;

public interface Vote extends Remote {

	public Votant signUp(String nom, String prenom, int age, String email, byte[] publicKey) throws RemoteException;

	public List<Condidate> resultatVote() throws RemoteException;

	public HashMap<Long, String> condidates() throws RemoteException;

	public int voter(byte[] signatureMessage, PublicKey publicKey, String identifiantCondidat,Long idVotant) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

	public String findCondidatById(Long id) throws RemoteException;

}
