package com.company;

import java.util.Scanner;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Main me = new Main();
        me.sim();
    }

    public void doIt() {
        AQueue<String> q = new AQueue<String>();

        q.add("String 1");
        q.add("String 2");
        q.add("String 3");
        System.out.println(q);

        System.out.println("Removing " + q.remove());

        System.out.println("Queue is now: " + q);


    }

    public void sim() {
        int timeToLand;			    // Length of time to land
        int timeToDepart;			// time to depart
        int averageArrival;		    // average arrival time
        int averageDeparture;		// average departure time
        int maxTimeInArrivalQueue;	// max time a plane can be in q
        int simulationTime;		    // max time in simulation
        int systemTime = 0;		    // current time in simulation
        int nextArrival;			// when will the next arrival be
        int nextDeparture;			// when will the next departure be
        int nextArrivalQueued;		// when will the next queued arrival be
        int nextDepartureQueued;	// next queued departure
        Plane removee;			    // item being removed from queue
        Plane tmp;				    // tmp item
        int crashed = 0;			// crashed count
        int landed;			        // landed count
        int departed;			    // departed count
        boolean somebodyLanding;	// is somebody landing?
        boolean somebodyDeparting;	// is someone departing?
        int totalDepartureTime = 0;	// Total time of departures
        int totalArrivalTime = 0;	// total time of arrivals

        String inputString;

        Random rand = new Random();

        Scanner kbd = new Scanner(System.in);
        AQueue<Plane> arrivals = new AQueue<Plane>(10000);			// Queue of arrivals
        AQueue<Plane> departures = new AQueue<Plane>(10000);		// Queue of departures

        // Print out the banner and read the input.
        System.out.println( "Welcome to the airport simulation\n");
        System.out.println( "Please enter the following information.");
        System.out.print( "Time in minutes to Land         : ");
        inputString = kbd.nextLine();
        timeToLand = Integer.parseInt(inputString);
        System.out.print( "Time in minutes to Takeoff      : ");
        inputString = kbd.nextLine();
        timeToDepart = Integer.parseInt(inputString);
        System.out.print( "Average time between arrivals   : ");
        inputString = kbd.nextLine();
        averageArrival = Integer.parseInt(inputString);
        System.out.print( "Average time between departures : ");
        inputString = kbd.nextLine();
        averageDeparture = Integer.parseInt(inputString);
        System.out.print( "Maximum time in arrival queue   : ");
        inputString = kbd.nextLine();
        maxTimeInArrivalQueue = Integer.parseInt(inputString);
        System.out.print("Number of minutes in Simulation : ");
        inputString = kbd.nextLine();
        simulationTime = Integer.parseInt(inputString);

        // Compute some preliminary data
        nextArrivalQueued = systemTime + rand.nextInt((int)(averageArrival*2));
        nextDepartureQueued = systemTime + rand.nextInt((int)(averageDeparture*2));
        nextArrival = timeToLand;
        nextDeparture = timeToDepart;

        // initialize some data
        somebodyLanding = false;
        somebodyDeparting = false;
        departed = 0;
        landed = 0;

        // Run the simulation
        for (systemTime = 0; systemTime < simulationTime; systemTime++) {

            // See if we need to add anyone to a queue
            if (systemTime == nextArrivalQueued) {
                tmp = new Plane();
                tmp.ttl = maxTimeInArrivalQueue;
                tmp.qtime = systemTime;
                arrivals.add(tmp);
                nextArrivalQueued = systemTime + rand.nextInt(averageArrival*2) + 1;
            }
            // departures
            if (systemTime == nextDepartureQueued) {
                tmp = new Plane();
                tmp.ttl = 0;
                tmp.qtime = systemTime;
                departures.add(tmp);
                nextDepartureQueued = systemTime + rand.nextInt(averageDeparture*2) + 1;
            }

            // See if we can remove anyone from a queue.
            if (!somebodyDeparting) {
                if (somebodyLanding) {
                    // Somebody is landing, see if they are done
                    if (systemTime >= (nextArrival + timeToLand)) {
                        somebodyLanding = false;
                        nextArrival = systemTime + timeToLand;
                    }
                } else if (arrivals.size() > 0) {
                    // Nobody is currently landing, allow someone to land
                    // if there is a plane in the queue
                    removee = arrivals.remove();
                    somebodyLanding = true;
                    if (removee.ttl <= 0) {
                        // oops, took too long to land
                        crashed += 1;
                    } else {
                        // they made it on the ground
                        landed += 1;
                        totalArrivalTime += (systemTime - removee.qtime);
                    }
                }
            }

            // Handle departures.

            if (!somebodyLanding) {
                if (somebodyDeparting == true) {
                    // If someone is already departing deal with them
                    if (systemTime >= (nextDeparture + timeToLand)) {
                        somebodyDeparting = false;
                        nextDeparture = systemTime + timeToDepart;
                    }
                } else if (departures.size() > 0) {
                    // as long as there are no landings in progress we can depart
                    removee = departures.remove();
                    totalDepartureTime += (systemTime - removee.qtime);
                    somebodyDeparting = true;
                    departed++;
                }
            }

            // Decrement the time to live for each queue element in the
            // arrivals queue.
            arrivals.queue_dec_ttl();
        }

        // Simulation is over, print out the statistics
        System.out.println( "Simulation finished!\n");
        System.out.println( "Simulation ran                  : " + simulationTime
                + " Minutes\n");
        System.out.println( "Planes Departed                 : " + departed);
        System.out.println( "Planes Landed                   : " + landed);
        System.out.println( "Planes Crashed                  : " + crashed);
        if (departed > 0)
            System.out.println( "Average Time in Departure Queue : " + (float)
                    ((float)totalDepartureTime / departed));
        if (landed > 0)
            System.out.println( "Average Time in Arrival Queue   : " + (float)
                    ((float)totalArrivalTime / landed));
        System.out.println( "Total Arrivals Still in Queue   : " + arrivals.size());
        System.out.println( "Total Departures Still in Queue : " + departures.size());
        System.out.println( "Total departure time is         : " + totalDepartureTime);

        kbd.close();

    }

}
