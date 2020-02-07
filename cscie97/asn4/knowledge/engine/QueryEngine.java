/*
 * QueryEngine
 * 
 * Version 1.0
 * 
 * September 16, 2014
 * 
 * Written by Roland L. Galibert for Harvard Extension course
 * CSCI E-97 Software Design: Principles, Models, and Patterns
 * Assignment #1.
 */
package cscie97.asn4.knowledge.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * This class executes queries over a knowledge graph. It also has the ability
 * to read in and execute a file of queries (see assignment design document for
 * more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class QueryEngine {

    private KnowledgeGraph kg = KnowledgeGraph.getInstance();

    /**
     * Constructor.
     */
    public QueryEngine() {
    }

    /**
     * Executes a single query over a knowledge graph, consisting of
     *
     * subject predicate object
     *
     * and outputs the triples in the knowledge graph that match the query. Any
     * or all of the elements may be replaced by a wild card ? See assignment
     * design document for more details as well as Node and Predicate classes
     * for well-formed regular expressions for those elements.
     *
     * @param query Knowledge graph query
     * @throws QueryEngineException
     */
    public void executeQuery(String query) throws QueryEngineException {

        /*
         * Split line inputParms[0] = subject inputParms[1] = predicate
         * inputParms[2] = object (note that in a properly formed input line,
         * the terminating period will still be included in inputParms[2]
         * (object) after this point and will have to be deleted later)
         */
        String[] queryParms = query.split("[ \t]+");

        /*
         * Use pattern matching to uncover malformed elements
         */
        if (queryParms.length != 3) {
            throw new QueryEngineException("Malformed query: Incorrect number of elements");
        } else if (!queryParms[0].matches("\\?|" + Node.regex)) {
            throw new QueryEngineException("Malformed query: Bad subject");
        } else if (!queryParms[1].matches("\\?|" + Predicate.regex)) {
            throw new QueryEngineException("Malformed query: Bad predicate");
        } else if (!queryParms[2].endsWith(".")) {
            throw new QueryEngineException("Malformed query: Missing .");
        } else if (!queryParms[2].substring(0, queryParms[2].length() - 1).matches("\\?|" + Predicate.regex)) {
            throw new QueryEngineException("Malformed query: Bad object");
        } else {

            /*
             * Input line is properly formed, remove . from inputParms[2]
             * (object).
             */
            queryParms[2] = queryParms[2].substring(0, queryParms[2].length() - 1);

            /*
             * Execute actual query.
             */
            Triple triple = kg.getTriple(kg.getNode(queryParms[0]), kg.getPredicate(queryParms[1]), kg.getNode(queryParms[2]));
            Set<Triple> tripleSet = kg.executeQuery(triple);

            /*
             * Echo query.
             */
            System.out.println(queryParms[0] + " " + queryParms[1] + " " + queryParms[2] + ".");

            /*
             * Print <null> if no triples match query, otherwise print matches.
             */
            if (tripleSet == null) {
                System.out.println("<null>");
            } else {
                Iterator<Triple> iterator = tripleSet.iterator();
                while (iterator.hasNext()) {
                    Triple setTriple = iterator.next();
                    System.out.println(setTriple.getIdentifier() + ".");
                }
            }
        }
    }

    /**
     * Reads in and executes a file of queries
     *
     * @param fileName Name of query input file.
     */
    public void executeQueryFile(String fileName) throws ImportException, IOException {

        /*
         * Check for correct import file name
         */
        if (!isValidFileName(fileName)) {
            throw new ImportException("QueryEngine.executeQueryFile(): Invalid import file name entered");
        } else {

            /*
             * Open and process import file
             */
            BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            String line = null;
            int lineNumber = 0;
            while ((line = inputStream.readLine()) != null) {
                try {
                    lineNumber++;
                    executeQuery(line);
                    System.out.println();   // Print blank line between queries
                } catch (QueryEngineException qee) {
                    System.out.println("Query file " + fileName + " line number " + lineNumber + ": " + qee.getMessage());
                }
            }
        }
    }

    /**
     * This method indicates whether a given query input file name is valid.
     * 
     * @param fileName Name of triple input file.
     * @return True if triple input file name is valid, false otherwise.
     */
    public boolean isValidFileName(String fileName) {
        return fileName.endsWith(".nt");
    }
}
