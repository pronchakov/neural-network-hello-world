package edu.neural;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NeuralNetworkHelloWorld {
    private static final Logger log = LoggerFactory.getLogger(NeuralNetworkHelloWorld.class);

    private static final int NUMBER_OF_INPUTS = 4;
    private static final int TRIALS = 5;
    private static double learningRate = 0.2;
    private static double[] rates = new double[NUMBER_OF_INPUTS];

    public static void main(String[] args) throws Exception {
        final List<String> trainLines = IOUtils.readLines(NeuralNetworkHelloWorld.class.getClassLoader().getResourceAsStream("train"), Charset.forName("UTF-8"));
        log.debug("Read train data: {}", trainLines);

        for (int i = 0; i < TRIALS; i++) {
            for (String trainLine : trainLines) {
                int[] inputArray = getInputArray(trainLine);
                int expected = getExpectedResult(trainLine);
                train(inputArray, expected);
            }
        }

        final List<String> checkLines = IOUtils.readLines(NeuralNetworkHelloWorld.class.getClassLoader().getResourceAsStream("check"), Charset.forName("UTF-8"));
        log.trace("Train data: {}", trainLines);
        log.debug("Check data: {}", checkLines);
        for (String checkLine : checkLines) {
            int[] inputArray = getInputArray(checkLine);
            final double check = check(inputArray);
            log.info("Result for {} = {}", checkLine, check);
        }
    }

    private static int[] getInputArray(String string) {
        int[] result = new int[NUMBER_OF_INPUTS];
        Scanner scanner = new Scanner(string);
        scanner.useDelimiter(",");
        for (int i = 0; i < NUMBER_OF_INPUTS; i++) {
            result[i] = scanner.nextInt();
        }
        return result;
    }

    private static int getExpectedResult(String string) {
        return Integer.parseInt(string.substring(string.lastIndexOf(",") + 1));
    }

    private static double check(int[] inputArray) {
        double totalResult = 0.0;
        for (int i = 0; i < inputArray.length; i++) {
            int input = inputArray[i];
            double rate = rates[i];
            double result = input * rate;
            totalResult += result;
        }
        return totalResult;
    }

    private static void train(int[] inputArray, int expected) {
        log.debug("Rates before training: {}", rates);
        double totalResult = 0.0;

        for (int i = 0; i < inputArray.length; i++) {
            int input = inputArray[i];
            double rate = rates[i];
            double result = input * rate;
            log.trace("Train for {}, {} element with rate {}, value {} and result {}", Arrays.asList(inputArray), i, rate, input, result);
            totalResult += result;
        }

        double error = expected - totalResult;
        log.trace("Train for {} totalResult is {}, error: {}", Arrays.asList(inputArray), totalResult, error);
        if (error > 0.0) {
            for (int i = 0; i < inputArray.length; i++) {
                if (inputArray[i] > 0) {
                    log.trace("Increasing rate for index {} from {} to {}", i, rates[i], rates[i] + learningRate);
                    rates[i] = rates[i] + learningRate;
                }
            }
        }

        log.debug("Rates after training: {}", rates);
    }
}
