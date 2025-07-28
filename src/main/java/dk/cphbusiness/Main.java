package dk.cphbusiness;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/esports_platform";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

    public static void main(String[] args) {
        System.out.println("Connecting...");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connection succesfull");
            boolean running = true;

            while (running) {
                System.out.println("\n=== E-sports Menu ===");
                System.out.println("1. Tilmeld spiller til turnering (stored procedure)");
                System.out.println("2. Tilmeld spiller til turnering (raw SQL)");
                System.out.println("3. Registrer kampresultat");
                System.out.println("4. Se antal sejre for spiller");
                System.out.println("0. Afslut");
                System.out.print("Vælg handling: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Spiller ID: ");
                        int playerId = scanner.nextInt();
                        System.out.print("Turnering ID: ");
                        int tournamentId = scanner.nextInt();
                        joinTournament(conn, playerId, tournamentId);
                    }
                    case 2 -> {
                        System.out.print("Spiller ID: ");
                        int playerId = scanner.nextInt();
                        System.out.print("Turnering ID: ");
                        int tournamentId = scanner.nextInt();
                        joinTournamentRawSQL(conn, playerId, tournamentId);
                    }
                    case 3 -> {
                        System.out.print("Match ID: ");
                        int matchId = scanner.nextInt();
                        System.out.print("Vinderens spiller ID: ");
                        int winnerId = scanner.nextInt();
                        submitMatchResult(conn, matchId, winnerId);
                    }
                    case 4 -> {
                        System.out.print("Spiller ID: ");
                        int playerId = scanner.nextInt();
                        getTotalWins(conn, playerId);
                    }
                    case 0 -> {
                        System.out.println("👋 Afslutter programmet.");
                        running = false;
                    }
                    default -> System.out.println("Ugyldigt valg. Prøv igen.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Fejl ved forbindelse eller forespørgsel:");
            e.printStackTrace();
        }
    }

    private static void joinTournament(Connection conn, int playerId, int tournamentId) {
        String sql = "CALL joinTournament(?, ?)";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, tournamentId);
            stmt.setEscapeProcessing(false); // PostgreSQL-specific
            stmt.execute();
            System.out.println("Stored procedure 'joinTournament' kaldt for spiller " + playerId);
        } catch (SQLException e) {
            System.err.println("Fejl ved kald af joinTournament:");
            e.printStackTrace();
        }
    }

    private static void joinTournamentRawSQL(Connection conn, int playerId, int tournamentId) {
        String sql = "INSERT INTO Tournament_Registrations (player_id, tournament_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            stmt.setInt(2, tournamentId);
            stmt.executeUpdate();
            System.out.println("Spiller " + playerId + " tilmeldt turnering " + tournamentId + " (raw SQL)");
        } catch (SQLException e) {
            System.err.println("Fejl ved raw SQL tilmelding:");
            e.printStackTrace();
        }
    }

    private static void submitMatchResult(Connection conn, int matchId, int winnerId) {
        String sql = "CALL submitMatchResult(?, ?)";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, matchId);
            stmt.setInt(2, winnerId);
            stmt.setEscapeProcessing(false);
            stmt.execute();
            System.out.println("🏁 submitMatchResult() kaldt for kamp " + matchId);
        } catch (SQLException e) {
            System.err.println("Fejl ved submitMatchResult:");
            e.printStackTrace();
        }
    }

    private static void getTotalWins(Connection conn, int playerId) {
        String sql = "SELECT getTotalWins(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int wins = rs.getInt(1);
                System.out.println("🏆 Spiller " + playerId + " har " + wins + " sejr(e)");
            }
        } catch (SQLException e) {
            System.err.println(" Fejl ved getTotalWins:");
            e.printStackTrace();
        }
    }
}
