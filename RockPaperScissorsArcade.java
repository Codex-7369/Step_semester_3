import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class RockPaperScissorsArcade {

    public static String normalizeAndValidateMove(String rawMove) {
        if (rawMove == null) {
            throw new IllegalArgumentException("Move cannot be null.");
        }
        String trimmedMove = rawMove.trim();
        if (trimmedMove.isEmpty()) {
            throw new IllegalArgumentException("Move cannot be empty.");
        }

        StringBuilder cleanedMove = new StringBuilder();
        for (int i = 0; i < trimmedMove.length(); i++) {
            char currentChar = trimmedMove.charAt(i);
            int asciiValue = (int) currentChar;

            if (asciiValue >= 65 && asciiValue <= 90) {
                if (i == 0) {
                    cleanedMove.append(currentChar);
                } else {
                    cleanedMove.append((char) (asciiValue + 32));
                }
            } else if (asciiValue >= 97 && asciiValue <= 122) {
                if (i == 0) {
                    cleanedMove.append((char) (asciiValue - 32));
                } else {
                    cleanedMove.append(currentChar);
                }
            }
        }

        String formattedMove = cleanedMove.toString();
        if (!formattedMove.equals("Rock") && !formattedMove.equals("Paper") && !formattedMove.equals("Scissors")) {
            throw new IllegalArgumentException("Invalid move: \"" + rawMove + "\"");
        }
        return formattedMove;
    }

    public static String generateComputerMove(String[] validMoves, Random randomGenerator) {
        int randomIndex = randomGenerator.nextInt(validMoves.length);
        return validMoves[randomIndex];
    }

    public static String playRound(String playerMove, String computerMove) throws IOException {
        if (playerMove == null || computerMove == null) {
            throw new IOException("Moves cannot be null during round evaluation.");
        }

        if (playerMove.equalsIgnoreCase(computerMove)) {
            return "Draw";
        } else if (
            (playerMove.equals("Rock") && computerMove.equals("Scissors")) ||
            (playerMove.equals("Paper") && computerMove.equals("Rock")) ||
            (playerMove.equals("Scissors") && computerMove.equals("Paper"))
        ) {
            return "Player Wins";
        } else {
            return "Computer Wins";
        }
    }

    public static double calculateWinPercentage(int totalWins, int totalRounds) {
        if (totalRounds <= 0) {
            throw new ArithmeticException("Total rounds must be greater than zero.");
        }
        return ((double) totalWins / totalRounds) * 100.0;
    }

    public static void displayScoreboard(String[][] roundHistory, int totalRounds, int totalWins, int totalLosses, int totalDraws) {
        System.out.println("\n\"The College Coding Arcade\" - Official Scoreboard");
        System.out.println("================================================================");
        System.out.printf("%-8s | %-14s | %-15s | %-15s%n", "Round", "Player Move", "Computer Move", "Result");
        System.out.println("----------------------------------------------------------------");

        for (int i = 0; i < totalRounds; i++) {
            System.out.printf("%-8s | %-14s | %-15s | %-15s%n",
                    roundHistory[i][0],
                    roundHistory[i][1],
                    roundHistory[i][2],
                    roundHistory[i][3]);
        }
        System.out.println("================================================================");

        try {
            double winPercentage = calculateWinPercentage(totalWins, totalRounds);
            System.out.printf("Final Summary (after %d rounds) Wins: %d | Losses: %d | Draws: %d | Win %% = %.1f%%%n",
                    totalRounds, totalWins, totalLosses, totalDraws, winPercentage);
        } catch (ArithmeticException arithmeticEx) {
            System.out.println("Error calculating statistics: " + arithmeticEx.getMessage());
        }
    }

    public static void executeArcadeMatch(int numberOfRounds, String[] predefinedMoves) {
        String[] availableMoves = new String[]{"Rock", "Paper", "Scissors"};
        String[][] matchRecords = new String[numberOfRounds][4];
        int winCount = 0;
        int lossCount = 0;
        int drawCount = 0;

        Scanner inputScanner = new Scanner(System.in);
        Random moveGenerator = new Random();

        for (int roundIndex = 0; roundIndex < numberOfRounds; roundIndex++) {
            String rawPlayerInput = "";
            String validatedPlayerMove = "Rock";

            try {
                if (predefinedMoves != null && roundIndex < predefinedMoves.length) {
                    rawPlayerInput = predefinedMoves[roundIndex];
                } else {
                    System.out.print("Round " + (roundIndex + 1) + " - Enter move (\"Rock\", \"Paper\", \"Scissors\"): ");
                    rawPlayerInput = inputScanner.nextLine();
                }
                validatedPlayerMove = normalizeAndValidateMove(rawPlayerInput);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException uncheckedEx) {
                System.out.println("Input Warning: " + uncheckedEx.getMessage() + ". Defaulting to \"Rock\".");
                validatedPlayerMove = "Rock";
            }

            String computerSelectedMove = generateComputerMove(availableMoves, moveGenerator);
            String roundResult = "Draw";

            try {
                roundResult = playRound(validatedPlayerMove, computerSelectedMove);
            } catch (IOException checkedEx) {
                System.out.println("Checked Exception Handled: " + checkedEx.getMessage());
            }

            if (roundResult.equals("Player Wins")) {
                winCount++;
            } else if (roundResult.equals("Computer Wins")) {
                lossCount++;
            } else {
                drawCount++;
            }

            matchRecords[roundIndex][0] = String.valueOf(roundIndex + 1);
            matchRecords[roundIndex][1] = validatedPlayerMove;
            matchRecords[roundIndex][2] = computerSelectedMove;
            matchRecords[roundIndex][3] = roundResult;

            System.out.println("Round " + (roundIndex + 1) + " — Player: " + validatedPlayerMove +
                    ", Computer: " + computerSelectedMove + " -> " + roundResult);
        }

        displayScoreboard(matchRecords, numberOfRounds, winCount, lossCount, drawCount);
        inputScanner.close();
    }

    public static void main(String[] args) {
        int totalRoundsToPlay = 5;
        String[] demoPlayerMoves = {"Rock", "Paper", "Scissors", "Rock", "Paper"};
        executeArcadeMatch(totalRoundsToPlay, demoPlayerMoves);
    }
}

