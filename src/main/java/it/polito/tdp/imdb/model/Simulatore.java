package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Simulatore {
	private Model model=new Model();
	//stato del mondo
	List<Actor>possibili;//=new ArrayList(model.getAttoriCombo());
	//input
	int n;
	//par. sim.
	int giorno;
	private double SCELTA_CASUALE=0.6;
	private double PAUSA=0.9;
	//output
	int pause;
	//List<Actor>intervistati;   meglio una mappa,altrimenti perdo i giorni di pausa nei controlli
	Map<Integer,Actor>intervistati;
	public Simulatore(int n,Set<Actor> attori) {
		this.n=n;
		//possibili=attori;
		possibili=new ArrayList();
		for(Actor a:attori) {
			possibili.add(a);
		}
		
	}
	//carico eventi
	public void caricaEventi() {
		//double casuale=Math.random()*SCELTA_CASUALE;
		//double prendiUnaPausa=Math.random()*PAUSA;
	}
//inizializzo simulatore
	public void init() {
		//intervistati=new ArrayList<>();
		intervistati=new HashMap<>();
		pause=0;
		giorno=1;
	}
	//simulazione
	public void run() {
		while(giorno<=n) {
			double casuale=Math.random();//*SCELTA_CASUALE;
			double prendiUnaPausa=Math.random();//*PAUSA;
			if(giorno==1) {
				//estrazione casuale
				Random random = new Random();
		      /*  int index = random.nextInt(possibili.size());
		        intervistati.add(possibili.get(index));
		        possibili.remove(possibili.get(index));*/
		        //fine estrazione casuale
				//metodo 2
		        Actor a = possibili.get(random.nextInt(possibili.size()));
		        possibili.remove(a);
		        intervistati.put(giorno,a);
		        System.out.println("giorno "+giorno+" scelto attore "+a.getLastName()+"\n");
		        giorno++;
		        continue;
		        }
			if(giorno>3 && intervistati.containsKey(giorno-1) && intervistati.containsKey(giorno-2) ) {
				if(intervistati.get(giorno-1).getGender().equals(intervistati.get(giorno-2).getGender())) {
					if(prendiUnaPausa<0.9/*Math.random()*PAUSA*/) {  //o così
						pause++;
						System.out.println("giorno "+giorno+" PAUSA \n");
						giorno++;
						continue;
					}
				}
			}
			if(casuale<0.6/*Math.random()*SCELTA_CASUALE*/) {//così?
				Random random = new Random();
				Actor a = possibili.get(random.nextInt(possibili.size()));
		        possibili.remove(a);
		        intervistati.put(giorno,a);
		        System.out.println("giorno "+giorno+" scelto attore "+a.getLastName()+"\n");
		        giorno++;
		        continue;
			}
			else {
				//deve scegliere il precedente se c'è
					if(intervistati.containsKey(giorno-1)) {
						Actor a=model.consigliato(intervistati.get(giorno-1));
						if(!(possibili.contains(a))||a==null) {
							//se non ha consigliati o è già stato intervistato=>casuale
							Random random = new Random();
							Actor a2 = possibili.get(random.nextInt(possibili.size()));
					        possibili.remove(a2);
					        intervistati.put(giorno,a2);
					        System.out.println("giorno "+giorno+" scelto attore "+a2.getLastName()+"\n");
					        giorno++;
					        continue;
							}
						else {//c'è
							possibili.remove(a);
							intervistati.put(giorno,a);
							System.out.println("giorno "+giorno+" CONSIGLIATO attore :  "+a.getLastName()+"\n");
					        giorno++;
					        continue;
							}
						}
					}
					/*else {//altrimenti casuale
						Random random = new Random();
						Actor a = possibili.get(random.nextInt(possibili.size()));
				        possibili.remove(a);
				        intervistati.put(giorno,a);
				        System.out.println("giorno "+giorno+" scelto attore "+a.getLastName()+"\n");
				        giorno++;
				        continue;
					}*/
			}
	}
	}

