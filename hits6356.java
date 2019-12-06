/* Dhruvinkumar Desai cs610 6356 prp */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dhruvin
 */
public class hits6356 {

    public static void main(String[] args) {
        
        int iterations = 0;
        double error_rate = 0;
        double initial_value = 0;
        int no_of_nodes = 0,no_of_edge = 0;
        boolean graph_edges[][] = null;
        /*
            Information on graph_edges[][]
            We will consider starting node to be the one in rows and end node to the one in edge( i.e. graph_edges[1][4]
            means there is a directed edge from node 1 --> node 4 in the given graph. And if it is than than entry in
            graph_edges will ne true else for no edge between node the value will be false.
        */
        double auth_value[] = null, hub_value[] = null;
        
        // Fetching arguments..
        try{
            iterations = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException err){
            System.out.println("\n Not a number: Please provide a valid number as iteration argument!!!\n\n");
            err.printStackTrace();
        }
        
        // Deciding error rate if '-'ve iteration value.
        if(iterations < 0){
            error_rate = (double)Math.pow(10.0, iterations);
            
        }
        else if(iterations == 0){
            // Default error rate.
            error_rate = (double)(Math.pow(10.0, -5));
        }
        else{
            // Iterations is a positive integer. Do nothing.
        }
        
        // Fetching file data and populating graph.
        File data_file = new File(args[2]);
        try{
            BufferedReader dfbr = new BufferedReader(new FileReader(data_file));
            
            // Fetching Graph meta-data.
            String temp_line[] = dfbr.readLine().split(" ");
            //temp_line = temp_line.split(" ");
            no_of_nodes = Integer.parseInt(temp_line[0]);
            no_of_edge = Integer.parseInt(temp_line[1]);
            
            // Initializing Graph;
            graph_edges = new boolean[no_of_nodes][no_of_nodes];
            
            // Populating the edge information in graph.
            for(int i = 0; i < no_of_edge; i++){
                temp_line = dfbr.readLine().split(" ");
                graph_edges[Integer.parseInt(temp_line[0])][Integer.parseInt(temp_line[1])] = true;
                
            }
            
            // Closing file.
            dfbr.close();
        }
        catch (FileNotFoundException err) {
            Logger.getLogger(hits6356.class.getName()).log(Level.SEVERE, null, err);
            err.printStackTrace();
        }
        catch (IOException err) {
            Logger.getLogger(hits6356.class.getName()).log(Level.SEVERE, null, err);
            err.printStackTrace();
        }
        catch(Exception err){
            System.out.println("\n\nERROR: Error while parsing data file. Not able to generate full graph.\n\n");
            err.printStackTrace();
        }
        
        // Fetching Data file complete.
        
        // Fetching Initial Values.
        if(no_of_nodes > 10){
            initial_value = (double)(1.0/no_of_nodes);
            error_rate = (double)(Math.pow(10.0, -5));
            iterations = 0;   
        }
        else{
            int temp_initial = 0;
            try{
                temp_initial = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException err){
                System.out.println("\n Not a number: Please provide a valid number as iteration argument!!!\n\n");
                err.printStackTrace();
            }
            
            switch(temp_initial){
                case 0:
                    // Set all to 0.
                    initial_value = 0;
                    break;
                case 1:
                    //set all to 1.
                    initial_value = 1;
                    break;

                case -1:
                    // set all to 1/N.
                    initial_value = (double)(1.0/no_of_nodes);
                    break;

                case -2:
                    // Set all to 1/n^-2.
                    initial_value = (double)(1.0/Math.pow(no_of_nodes, -2));
                    break;
                default:
                    // Setting all to default 1 value.
                    System.out.println("\nInvalid initial value provided. Going further with default value(1).\n\n");
                    initial_value = 1;
                    break;
            }
        
            
        } 
        // Initial Value Fetched.
        
        // Initializing auth_value and hub_value.
        auth_value = new double[no_of_nodes];
        hub_value = new double[no_of_nodes];
        // Setting initial value to auth and hub values.
        for(int i = 0; i < no_of_nodes; i++){
            auth_value[i] = initial_value;
            hub_value[i] = initial_value;
        }
        
        if(iterations > 0){
            // Iterations > 0.
            // Algorithm runs till iterations.
            if(no_of_nodes <=10){
                // Printing base case.
                System.out.printf("Base : %d :", 0);
                for(int i = 0; i < no_of_nodes; i++){
                    System.out.printf("A/H[ %d]=%.7f/%.7f ", i, auth_value[i], hub_value[i]);
                }
                System.out.print("\n");
            }
            for(int i = 1; i <= iterations; i++){
                // Iteration start.
                double normalizing_value = 0;
                // Authority value update.
                for(int node = 0; node < no_of_nodes; node++){
                    double temp_auth = 0;
                    for(int inlink = 0; inlink < no_of_nodes; inlink++){
                        if(graph_edges[inlink][node]){
                            temp_auth += hub_value[inlink];
                        }
                    }
                    auth_value[node] = temp_auth;
                    normalizing_value += (temp_auth*temp_auth);
                }
                
                normalizing_value = (double)Math.sqrt(normalizing_value);
                // Normalizing authorative value for all documents.
                for(int j = 0; j < no_of_nodes; j++){
                    auth_value[j] /= normalizing_value;
                }
                // Resetting normalizing_value
                normalizing_value = 0;
                // Hub value update.
                
                for(int node = 0; node < no_of_nodes; node++){
                    double temp_hub = 0;
                    for(int outlink = 0; outlink < no_of_nodes; outlink++){
                        if(graph_edges[node][outlink]){
                            temp_hub += auth_value[outlink];
                        }
                    }
                    hub_value[node] = temp_hub;
                    normalizing_value += (temp_hub*temp_hub);
                }
                
                normalizing_value = (double)Math.sqrt(normalizing_value);
                // Normalizing authorative value for all documents.
                for(int j = 0; j < no_of_nodes; j++){
                    hub_value[j] /= normalizing_value;
                }
                // Iteration ends.
                
                // Printing iteration computed results.
                if(no_of_nodes <=10){
                    // Printing base case.
                    System.out.printf("Iter : %d :", i);
                    for(int k = 0; k < no_of_nodes; k++){
                        System.out.printf("A/H[ %d]=%.7f/%.7f ", k, auth_value[k], hub_value[k]);
                    }
                    System.out.print("\n");
                }
            }
            
            if(no_of_nodes > 10){
                System.out.printf("Iter : %d\n", iterations);
                for(int k = 0; k < no_of_nodes; k++){
                    System.out.printf("A/H[ %d]=%.7f/%.7f\n", k, auth_value[k], hub_value[k]);
                }
                System.out.print("\n");
            }
        }
        else{
            // Iterations <= 0
            // Algorithm runs till error rate is not achieved.
            if(no_of_nodes <=10){
                // Printing base case.
                System.out.printf("Base : %d :", 0);
                for(int i = 0; i < no_of_nodes; i++){
                    System.out.printf("A/H[ %d]=%.7f/%.7f ", i, auth_value[i], hub_value[i]);
                }
                System.out.print("\n");
            }
            /////////////////
            boolean LOOP_EXIT_FLAG = false,ERROR_IN_RANGE = true;
            int count = 0;
            while(!LOOP_EXIT_FLAG){
                // Iteration start.
                count++;
                // Resetting error range flag.
                ERROR_IN_RANGE = true;
                double normalizing_value = 0;
                // Authority value update.
                double temp_auth_value[] = auth_value.clone(), temp_hub_value[] = hub_value.clone();
                
                for(int node = 0; node < no_of_nodes; node++){
                    double temp_auth = 0;
                    for(int inlink = 0; inlink < no_of_nodes; inlink++){
                        if(graph_edges[inlink][node]){
                            temp_auth += hub_value[inlink];
                        }
                    }
                    auth_value[node] = temp_auth;
                    normalizing_value += (temp_auth*temp_auth);
                }
                
                normalizing_value = (double)Math.sqrt(normalizing_value);
                // Normalizing authorative value for all documents.
                for(int j = 0; j < no_of_nodes; j++){
                    auth_value[j] /= normalizing_value;
                    
                    // Checking for error in range.
                    if(ERROR_IN_RANGE){
                        if(Math.abs(auth_value[j] - temp_auth_value[j]) > error_rate){
                            ERROR_IN_RANGE = false;
                        }
                    }
                }
                // Resetting normalizing_value
                normalizing_value = 0;
                
                // Hub value update.
                for(int node = 0; node < no_of_nodes; node++){
                    double temp_hub = 0;
                    for(int outlink = 0; outlink < no_of_nodes; outlink++){
                        if(graph_edges[node][outlink]){
                            temp_hub += auth_value[outlink];
                        }
                    }
                    hub_value[node] = temp_hub;
                    normalizing_value += (temp_hub*temp_hub);
                }
                
                normalizing_value = (double)Math.sqrt(normalizing_value);
                // Normalizing authorative value for all documents.
                for(int j = 0; j < no_of_nodes; j++){
                    hub_value[j] /= normalizing_value;
                    
                    // Checking for error in range.
                    if(ERROR_IN_RANGE){
                        if(Math.abs(hub_value[j] - temp_hub_value[j]) > error_rate){
                            ERROR_IN_RANGE = false;
                        }
                    }
                }
                // Iteration ends.
                
                // Printing iteration computed results.
                if(no_of_nodes <=10){
                    // Printing base case.
                    System.out.printf("Iter : %d :", count);
                    for(int k = 0; k < no_of_nodes; k++){
                        System.out.printf("A/H[ %d]=%.7f/%.7f ", k, auth_value[k], hub_value[k]);
                    }
                    System.out.print("\n");
                }
                
                if(ERROR_IN_RANGE){
                    LOOP_EXIT_FLAG = true;
                }
            }
            
            if(no_of_nodes > 10){
                System.out.printf("Iter : %d\n", count);
                for(int k = 0; k < no_of_nodes; k++){
                    System.out.printf("A/H[ %d]=%.7f/%.7f\n", k, auth_value[k], hub_value[k]);
                }
                System.out.print("\n");
            }
            /////////////////
            
        }
    }
    
}
