package com.vote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vote.models.Condidate;
import com.vote.models.Votant;
import com.vote.service.Vote;

public class Client {

	public static void main(String[] args)
			throws RemoteException, MalformedURLException, NotBoundException, NoSuchAlgorithmException {
		Vote stub = (Vote) Naming.lookup("vote");
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		byte[] bytes = keyPair.getPublic().getEncoded();
		Votant votant = null;
		while (true) {
			System.out.println("**************************************************************************");
			System.out.println("\t\t\t\tGestion de Vote");
			System.out.println("**************************************************************************");
			System.out.println("Votre Menu ...");
			System.out.println(" 1.Inscription \n 2.Liste des condidats \n 3.Voter \n 4.Résultat \n 5.Exit");
			System.out.println("\tEntrer Votre choix");
			try {
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);
				String s1 = br.readLine();
				int input1 = Integer.parseInt(s1);
				switch (input1) {
				case 1:
					System.out.println("Enter Votre nom:");
					String nom = br.readLine();
					System.out.println("Enter Votre prenom:");
					String prenom = br.readLine();
					System.out.println("Enter Votre age:");
					String age = br.readLine();
					System.out.println("Enter Votre Email:");
					String email = br.readLine();

//					Votant votant = new Votant(nom, prenom, Integer.valueOf(age), email,
//							keyPair.getPublic().toString());
					votant = stub.signUp(nom, prenom, Integer.valueOf(age), email, bytes);
					if (votant != null) {
						System.out.println("**********************");
						System.out.println("Vous êtes incscrit");
						System.out.println("**********************");
					} else {
						System.out.println("User already exists");
					}
					System.out.println("Cliquez sur entrer pour continuer");
					try {
						System.in.read();
					} catch (Exception e) {
					}
					break;
				case 2:
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					System.out.println("\t\t\t\tListe des condidats");
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
					System.out.println("| identifiant | nom");
					HashMap<Long, String> listDesCondidats = stub.condidates();
					for (Map.Entry<Long, String> entry : listDesCondidats.entrySet()) {
						System.out.println("| " + entry.getKey() + " | " + entry.getValue());
					}
					System.out.println("Cliquez sur entrer pour continuer");
					try {
						System.in.read();
					} catch (Exception e) {
					}
					break;
				case 3:
					if (votant != null) {

						System.out
								.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						System.out.println("Veuillez taper l'identifiant du condidat pour voter");
						System.out
								.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						String identifiant = br.readLine();
						String nomCondidat = stub.findCondidatById(Long.parseLong(identifiant));
						if (nomCondidat == null) {
							System.out.println("condidat inexistant");
						} else {
							Signature signature = Signature.getInstance("SHA256WithDSA");
							signature.initSign(keyPair.getPrivate(), secureRandom);
							signature.update(identifiant.getBytes());
							byte[] digitalSignature = signature.sign();
							int result = stub.voter(digitalSignature, keyPair.getPublic(), identifiant, votant.getId());
							if (result == -1) {
								System.out.println("Votre singature n'a pas été vérifié");

							} else if (result == -2) {
								System.out.println("vous ne pouvez pas voter 2 fois");
							} else if (result == 0) {
								System.out.println("Votre vote a été bien enregistré");
							} else {
								System.out.println("Erreur Serveur");
							}
							System.out.println("Cliquez sur entrer pour continuer");
							try {
								System.in.read();
							} catch (Exception e) {
							}
						}
					} else {
						System.out.println("il faut être inscrit pour voter ");
					}
					System.out.println("Cliquez sur entrer pour continuer");
					try {
						System.in.read();
					} catch (Exception e) {
					}
					break;
				case 4:
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("Résultat du vote");
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					List<Condidate> condidats = stub.resultatVote();
					System.out.println("-----------------------------------------------------");
					System.out.println("nom     |    nombre de vote");
					for (Condidate condidate : condidats) {
						System.out.println(condidate.getNom() + "   |  " + condidate.getNbrVotes());
					}
					break;
				case 5:
					System.out.println("**********************");
					System.out.println("à bientôt...");
					System.out.println("**********************");
					System.exit(0);
					break;
				default:
					break;
				}
				System.out.println(input1);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
