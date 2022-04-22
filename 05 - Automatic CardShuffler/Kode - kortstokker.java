import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.motor.*;
import lejos.hardware.Brick;
import lejos.robotics.Color;
import lejos.hardware.Sound;
import lejos.hardware.Keys;
import lejos.hardware.sensor.NXTTouchSensor;
import java.util.Random;

public class KortstokkerBETA {
	public static void main(String[] arg) throws Exception {

		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // 1
		Port s2 = brick.getPort("S2"); // 2
		Port s3 = brick.getPort("S3"); // 3
		Port s4 = brick.getPort("S4"); // OK

		// Definerer trykksensorer
		SampleProvider trykkSensor1 = new EV3TouchSensor(s1);
		float[] trykkSample1 = new float[trykkSensor1.sampleSize()];
		SampleProvider trykkSensor2 = new EV3TouchSensor(s2);
		float[] trykkSample2 = new float[trykkSensor2.sampleSize()];
		SampleProvider trykkSensor3 = new NXTTouchSensor(s3);
		float[] trykkSample3 = new float[trykkSensor3.sampleSize()];
		SampleProvider trykkSensorOK = new NXTTouchSensor(s4);
		float[] trykkSampleOK = new float[trykkSensorOK.sampleSize()];

		int gjennomlop = 0;
		Random random = new Random();
		boolean pa = true; // "Skrur på" roboten
		boolean vent = true; // blir brukt hver gang programmet venter pa input fra brukeren
		int avslutt = 0;

		PA: // dersom bruker avslutter kaller vi på dette stedet med "break PA;" for å
			// forhindre at resten av programmet kjøres før programmet avslutter
		while (pa) {
			System.out.println("			\n" + "Legg kortene i	\n" + "anvist posisjon	\n" + "			\n"
					+ "Deretter trykk OK	\n" + "Hold OK = skru av\n" + "			");

			while (vent) {
				trykkSensorOK.fetchSample(trykkSampleOK, 0);
				if (trykkSampleOK[0] > 0) {
					vent = false;
				}
			}

			while (trykkSampleOK[0] > 0) { // dersom bruker holder inne knappen
				avslutt++;
				trykkSensorOK.fetchSample(trykkSampleOK, 0);
				Thread.sleep(50); // kontrolerer hastigheten avslutt økes

				if (avslutt > 25) { // bruker har holdt inne knappen lenge nok, maskinen skrus av
					Sound.twoBeeps();
					pa = false;
					break PA; // Bryter ut av alle loopene og avslutter programmet
				}
			}

			if (gjennomlop == 0) { // dersom bruker vil bruke samme antall stokkinger som forrige gang, vil
									// programmet hoppe over alt av valg/oppsett

				// Valg av modus for stokkingen
				int modus = 1;
				System.out.println("				\n" + "Choose mode:	\n" + "				\n" + "1: Random		\n"
						+ "2: Custom		\n" + "3: Tid BETA		\n" + "				");
				vent = true;
				while (vent) {
					trykkSensor1.fetchSample(trykkSample1, 0);
					trykkSensor2.fetchSample(trykkSample2, 0);
					trykkSensor3.fetchSample(trykkSample3, 0);
					trykkSensorOK.fetchSample(trykkSampleOK, 0);
					Thread.sleep(300); // registrer kun trykk på knappene hvert 3/10 sekund, for at bruker ikke trykker
										// flere ganger ved et uhell

					if (trykkSampleOK[0] > 0) {
						vent = false; // bekreft valg
					} else if (trykkSample1[0] > 0) {
						modus = 1; // Random
						System.out.println("\nChoose mode:\n1:	 Random\n2: Custom\n3: Tid BETA\n\n\n");
					} else if (trykkSample2[0] > 0) {
						modus = 2; // Custom
						System.out.println("\nChoose mode:\n1: Random\n2: 	Custom\n3: Tid BETA\n\n\n");
					} else if (trykkSample3[0] > 0) { // Endre til valg for tid f.eks. stokke i 3 minutter = 7
														// gjennomløp.
						modus = 3; // Tid
						System.out.println("\nChoose mode:\n1: Random\n2: Custom\n3:	Tid BETA\n\n\n");
					}
				}

				// Valg av hvor godt stokket kortstokken skal bli via valgt modus
				gjennomlop = 1; // standard antall stokk = 1, skal ikke være mulig å endre til noe mindre enn 1
								// da dette ikke har noen hensikt
				vent = true;
				switch (modus) {
					case 1: // Random
						System.out.println("				\n" + "Velg Stokkegrad:	\n" + "1: Lett 			\n"
								+ "2: Middels		\n" + "3: Godt			\n" + "				\n" + "				");

						while (vent) {
							Thread.sleep(300); // registrer kun trykk på knappene hvert 3/10 sekund, for at bruker ikke
												// trykker flere ganger ved et uhell
							trykkSensor1.fetchSample(trykkSample1, 0);
							trykkSensor2.fetchSample(trykkSample2, 0);
							trykkSensor3.fetchSample(trykkSample3, 0);
							trykkSensorOK.fetchSample(trykkSampleOK, 0);

							if (trykkSampleOK[0] > 0) {
								vent = false; // bekreft valg
							} else if (trykkSample1[0] > 0) {
								gjennomlop = 1 + random.nextInt(3);
								System.out.println("				\n" + "Velg Stokkegrad:	\n"
										+ "1: 		Lett 		\n" + "2: Middels		\n" + "3: Godt			\n"
										+ "				\n" + "				");
							} else if (trykkSample2[0] > 0) {
								gjennomlop = 3 + random.nextInt(3);
								System.out.println("				\n" + "Velg Stokkegrad:	\n" + "1: Lett 			\n"
										+ "2: 		Middels	\n" + "3: Godt			\n" + "				\n"
										+ "				");
							} else if (trykkSample3[0] > 0) {
								gjennomlop = 5 + random.nextInt(3);
								System.out.println("				\n" + "Velg Stokkegrad:	\n" + "1: Lett 			\n"
										+ "2: Middels		\n" + "3: 		Godt	\n" + "				\n"
										+ "				");
							}
						}
						break;

					case 2: // Custom (velg antall stokkinger selv);
						System.out.println("						\n" + "Velg stokkinger:			\n"
								+ "1: -						\n" + "2: +					\n" + "3: 1					\n"
								+ "Stokkinger: " + gjennomlop + "\n" + "						");
						while (vent) {
							trykkSensor1.fetchSample(trykkSample1, 0);
							trykkSensor2.fetchSample(trykkSample2, 0);
							trykkSensor3.fetchSample(trykkSample3, 0);
							trykkSensorOK.fetchSample(trykkSampleOK, 0);
							Thread.sleep(300);

							if (trykkSampleOK[0] > 0) {
								vent = false; // bekreft
							} else if (trykkSample1[0] > 0) {
								gjennomlop -= 1;
								if (gjennomlop < 1) { // kan ikke senke antall stokkinger til mindre enn 1
									gjennomlop = 1;
								}
							} else if (trykkSample2[0] > 0) {
								gjennomlop += 1;
							} else if (trykkSample3[0] > 0) {
								gjennomlop = 1;
							}

							if (trykkSample1[0] > 0 || trykkSample2[0] > 0 || trykkSample3[0] > 0) { // oppdaterer
																										// brukeren på
																										// gjeldende
																										// antall
																										// gjennomløp
																										// når bruker
																										// trykker på en
																										// knapp.
								System.out.println("						\n" + "Velg stokkinger:			\n"
										+ "1: -						\n" + "2: +					\n"
										+ "3: 1					\n" + "Stokkinger: " + gjennomlop + "\n"
										+ "						");
							}
						}
						break;

					case 3: // Bruker vil velge en tid
						int minutter = 1;
						int sekunder = 0;
						System.out.println("									\n"
								+ "Velg onsket tid:						\n" + "1: -30sek							\n"
								+ "2: +30sek							\n" + "3: +1min							\n"
								+ "Tid " + minutter + "min " + sekunder + "sek	\n"
								+ "									");
						while (vent) {
							trykkSensor1.fetchSample(trykkSample1, 0);
							trykkSensor2.fetchSample(trykkSample2, 0);
							trykkSensor3.fetchSample(trykkSample3, 0);
							trykkSensorOK.fetchSample(trykkSampleOK, 0);
							Thread.sleep(300);

							if (trykkSampleOK[0] > 0) {
								vent = false; // bekreft valgt tid
							} else if (trykkSample1[0] > 0) { // - 30sek
								sekunder -= 30;
								if (sekunder < 0) { // konverterer 60 sekunder om til 1 minutt
									sekunder = 30;
									minutter--;
								}
								if (minutter <= 0 && sekunder < 30) { // Minste tiden som er mulig å velge er 30
																		// sekunder
									sekunder = 30;
									minutter = 0;
								}
							} else if (trykkSample2[0] > 0) { // + 30 sek
								sekunder += 30;
								if (sekunder == 60) {
									sekunder = 0;
									minutter++;
								}
							} else if (trykkSample3[0] > 0) { // + 1 min
								minutter += 1;
							}
							if (trykkSample1[0] > 0 || trykkSample2[0] > 0 || trykkSample3[0] > 0) { // oppdaterer
																										// brukeren på
																										// gjeldende tid
																										// når bruker
																										// trykker på en
																										// knapp.
								System.out.println("									\n"
										+ "Velg onsket tid:						\n"
										+ "1: -30sek							\n"
										+ "2: +30sek							\n"
										+ "3: +1min							\n" + "Tid " + minutter + "min " + sekunder
										+ "sek	\n" + "									");
							}
						}
						int stokkeTid = 20; // roboten bruker 20 sekunder på å stokke 1 gang.
						int tid = minutter * 60 + sekunder; // +10 gjør at roboten i teorien vil være ferdig a stokke på
															// angitt tid +-10 sekunder, med stokketid 20 sek
						gjennomlop = tid / stokkeTid;
						break;
				}
			}

			boolean opp = false;
			int heisSakte = 25;
			int heisFort = 400;
			int splitter = 10000;
			int samler = 420;
			int sideHjelp = 65;

			// Selve stokkingen, vil stokke like mange ganger som verdien til "gjennomlop"
			for (int i = 0; i < gjennomlop; i++) {
				int gjenstar = gjennomlop - i;
				System.out.println("							\n" + "Antall stokk: " + gjennomlop + "	\n"
						+ "							\n" + "Stokkinger som 				\n" + "gjenstar: " + gjenstar
						+ "		\n" + "           						");

				// Heis opp
				if (i != 0) { // heisen er allerede i toppposisjon ved første gjennomlop

					// løft heisen og splitt bunken i 2
					Motor.A.setSpeed(80);
					Motor.C.setSpeed(sideHjelp);
					Motor.D.setSpeed(sideHjelp);
					Motor.A.forward();
					Motor.D.forward();
					Motor.C.forward();
					Thread.sleep(2580);

					Motor.A.setSpeed(heisSakte);
					Motor.B.setSpeed(splitter);
					Motor.C.stop();
					Motor.D.stop();

					for (int j = 0; j < 30; j++) {
						Motor.A.stop();
						if (j % 2 == 0) {
							Motor.B.backward();
							Thread.sleep(160);
						} else {
							Motor.B.forward();
							Thread.sleep(160);
						}
						Motor.B.stop();
						Motor.A.forward();
						Thread.sleep(100);
						trykkSensorOK.fetchSample(trykkSampleOK, 0);
						if (trykkSampleOK[0] > 0) { // bruker har muligheten til å avslutte programmet tidlig
							break;
						}
					}
					Motor.B.stop();
					Motor.C.stop();
					Motor.D.stop();
					opp = false;

				}

				// Heis ned
				Motor.A.setSpeed(heisFort);
				Motor.A.backward();
				Thread.sleep(850);

				Motor.A.stop();
				opp = true;

				trykkSensorOK.fetchSample(trykkSampleOK, 0);
				if (trykkSampleOK[0] > 0) {
					break;
				}

				// Samler kortene pa plattformen
				Motor.C.setSpeed(samler);
				Motor.C.backward();
				Thread.sleep(100);

				Motor.D.setSpeed(samler);
				Motor.D.backward();
				Thread.sleep(7000);

				Motor.D.stop();
				Motor.C.stop();
				Thread.sleep(1000);

				trykkSensorOK.fetchSample(trykkSampleOK, 0);
				if (trykkSampleOK[0] > 0) {
					break;
				}

			} // ferdig med a stokke

			if (opp) {
				Motor.A.setSpeed(heisFort);
				Motor.C.setSpeed(sideHjelp * 4);
				Motor.D.setSpeed(sideHjelp * 4);
				Motor.A.forward();
				Motor.C.forward();
				Motor.D.forward();
				Thread.sleep(700);

				Motor.A.stop();
				Motor.C.stop();
				Motor.D.stop();
				opp = false;

				trykkSensorOK.fetchSample(trykkSampleOK, 0);
				if (trykkSampleOK[0] > 0) {
					break;
				}
			}
			System.out.println("\n\n\nFerdig stokket\n\n");
			Sound.twoBeeps();
			Thread.sleep(1000);

			System.out.println("				\n" + "Onsker du a 		\n" + "bruke dette 		\n"
					+ "oppsettet		\n" + "neste gang?		\n" + "1: Ja			\n" + "2: Nei			\n"
					+ "OK: Skru av		");
			vent = true;
			while (vent) {
				trykkSensor1.fetchSample(trykkSample1, 0);
				trykkSensor2.fetchSample(trykkSample2, 0);
				trykkSensorOK.fetchSample(trykkSampleOK, 0);

				if (trykkSample1[0] > 0) {
					System.out.println("\n\n\nProgram lagret: \n" + gjennomlop + " gjennomlop");
					Thread.sleep(2000);
					vent = false;
				} else if (trykkSample2[0] > 0) {
					gjennomlop = 0;
					System.out.println("\n\n\nStarter pa nytt\n\n");
					Thread.sleep(2000);
					vent = false;
				} else if (trykkSampleOK[0] > 0) {
					pa = false;
					vent = false;
					Sound.twoBeeps();
				}
			}

		} // while(pa)
		System.out.println("\n\n\nAVSLUTTER\n\n");
		Thread.sleep(1000);
	}
}
