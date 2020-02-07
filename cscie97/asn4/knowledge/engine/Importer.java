/*
 * Importer
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

/**
 * This class reads in a file of knowledge graph triples and adds these to the
 * knowledge graph (see assignment design document for more details).
 *
 * @author Roland L. Galibert
 * @version 1.0
 */
public class Importer {

    /**
     * Constructor.
     */
    public Importer() {
        
    }
    
    /**
     * Opens a file of knowledge graph triples, parses each input line (i.e.
     * triple) and adds valid triples to the knowledge graph.
     *
     * @param fileName Name of triple input file.
     */
    public void importTripleFile(String fileName) throws ImportException, IOException {

        /*
         * Check for valid import file name
         */
        if (!isValidFileName(fileName)) {
            throw new ImportException("Triple input file '" + fileName + "' invalid");
        } else {

            /*
             * Open and process import file
             */
            KnowledgeGraph kg = KnowledgeGraph.getInstance();
            BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
            String line = null;
            int lineNumber = 0;
            while ((line = inputStream.readLine()) != null) {
                try {
                    lineNumber++;

                    /*
                     * inputParms[0] = subject
                     * inputParms[1] = predicate
                     * inputParms[2] = object
                     */
                    String[] inputParms = parseLine(line);
                    kg.importTriple(inputParms[0], inputParms[1], inputParms[2]);
                } catch (ImportException ie) {
                    System.out.println("Triple file " + fileName + " line number " + lineNumber + ": " + ie.getMessage());
                }
            }
        }
    }

    /*
     * This method parses a triple file input line (i.e. a triple) and stores
     * the elements as follows:
     *   inputParms[0] = subject
     *   inputParms[1] = predicate
     *   inputParms[2] = object
     * 
     * Method throws an ImportException in the case of malformed input. 
     * 
     * See assignment design document for more details as well as Node and
     * Predicate classes for well-formed regular expressions for those elements.
     */
    private String[] parseLine(String newLine) throws ImportException {
        
        /*
         * Split line (note that in a properly formed input line, the
         * terminating period will still be included in inputParms[2] (object)
         * after this point and will have to be deleted later)
         */
        String[] inputParms = newLine.split("[ \t]+");

        /*
         * Use pattern matching to uncover malformed elements
         */
        if (inputParms.length != 3) {
            throw new ImportException("Malformed input: Incorrect number of elements");
        } else if (!inputParms[0].matches(Node.regex)) {
            throw new ImportException("Malformed input: Bad subject");
        } else if (!inputParms[1].matches(Predicate.regex)) {
            throw new ImportException("Malformed input: Bad predicate");
        } else if (!inputParms[2].endsWith(".")) {
            throw new ImportException("Malformed input: Missing .");
        } else if (!inputParms[2].substring(0, inputParms[2].length() - 1).matches(Node.regex)) {
            throw new ImportException("Malformed input: Bad object");
        } else {

            /*
             * Input line is properly formed, remove . from inputParms[2] (i.e. object)
             */
            inputParms[2] = inputParms[2].substring(0, inputParms[2].length() - 1);
            return inputParms;
        }
    }

    /**
     * This method indicates whether a given triple input file name is valid.
     * 
     * @param fileName Name of triple input file.
     * @return True if triple input file name is valid, false otherwise.
     */
    public boolean isValidFileName(String fileName) {
        return fileName.endsWith(".nt");
    }
}
