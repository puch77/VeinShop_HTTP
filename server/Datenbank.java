package t4WS7Server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import t4WS7Klassen.Wein;
import t4WS7Klassen.WeinList;
import t4WS7Klassen.Kunde;
import t4WS7Klassen.KundeList;
import t4WS7Klassen.WarenKorb;
import t4WS7Klassen.WarenkorbElement;

public class Datenbank {
	private static final String DB_LOCATION = "..\\resources\\DB_Vinothek";
	private static final String CONNECTION = "jdbc:derby:" + DB_LOCATION + ";create = true";

	private static final String VINOTHEK_TABLE = "Vinothek";
	private static final String VINOTHEK_TABLE_ID = "VinothekID";
	private static final String VINOTHEK_TABLE_NAME = "VinothekName";
	private static final String VINOTHEK_TABLE_DESCRIPTION = "VinothekDescription";
	private static final String VINOTHEK_TABLE_PREIS = "VinothekPreis";

	private static final String KUNDE_TABLE = "Kunde";
	private static final String KUNDE_TABLE_KUNDEID = "KundeId";
	private static final String KUNDE_TABLE_KUNDENAME = "KundeName";
	private static final String KUNDE_TABLE_KUNDEDATUM = "KundeDatum";

	private static final String KORB_TABLE = "Korb";
	private static final String KORB_TABLE_KORBID = "KorbId";
	private static final String KORB_TABLE_KORBUSERID = "KorbUserId";
	private static final String KORB_TABLE_KORBWEINID = "KorbKorbWeinId";

	public static WeinList leseWeine() throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		ArrayList<Wein> alWein = new ArrayList<>();
		String select = "SELECT * FROM " + VINOTHEK_TABLE;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			rs = stat.executeQuery();
			while (rs.next()) {
				alWein.add(new Wein(rs.getInt(VINOTHEK_TABLE_ID), rs.getString(VINOTHEK_TABLE_NAME),
						rs.getString(VINOTHEK_TABLE_DESCRIPTION), rs.getDouble(VINOTHEK_TABLE_PREIS)));
			}
			rs.close();
			return new WeinList(alWein);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static void createKundeTable() throws SQLException {
		Statement stat = null;
		ResultSet rs = null;

		try (Connection conn = DriverManager.getConnection(CONNECTION)) {
			stat = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, KUNDE_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				stat.executeUpdate("DROP TABLE " + KORB_TABLE);
				System.out.println(KORB_TABLE + " dropped");
				stat.executeUpdate("DROP TABLE " + KUNDE_TABLE);
				System.out.println(KUNDE_TABLE + " dropped");
			}
			String ct = "CREATE TABLE " + KUNDE_TABLE + " (" + KUNDE_TABLE_KUNDEID + " BIGINT, " + KUNDE_TABLE_KUNDENAME
					+ " VARCHAR(200), " + KUNDE_TABLE_KUNDEDATUM + " DATE, " + "PRIMARY KEY (" + KUNDE_TABLE_KUNDEID
					+ "))";

			stat.execute(ct);
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void createKorbTable() throws SQLException {
		Statement stat = null;
		ResultSet rs = null;

		try (Connection conn = DriverManager.getConnection(CONNECTION)) {
			stat = conn.createStatement();
			rs = conn.getMetaData().getTables(null, null, KORB_TABLE.toUpperCase(), new String[] { "TABLE" });
			if (rs.next()) {
				stat.executeUpdate("DROP TABLE " + KORB_TABLE);
				System.out.println(KORB_TABLE + " dropped");
			}
			String ct = "CREATE TABLE " + KORB_TABLE + " (" + KORB_TABLE_KORBID
					+ " INTEGER GENERATED ALWAYS AS IDENTITY, " + KORB_TABLE_KORBUSERID + " BIGINT, "
					+ KORB_TABLE_KORBWEINID + " INTEGER, " + "PRIMARY KEY (" + KORB_TABLE_KORBID + "),"
					+ "FOREIGN KEY (" + KORB_TABLE_KORBUSERID + ") REFERENCES " + KUNDE_TABLE + "("
					+ KUNDE_TABLE_KUNDEID + ")," + "FOREIGN KEY (" + KORB_TABLE_KORBWEINID + ") REFERENCES "
					+ VINOTHEK_TABLE + "(" + VINOTHEK_TABLE_ID + ")" + ")";

			stat.execute(ct);
		} catch (SQLException e) {
			throw e;
		}
	}

	public static void insertKorbElement(WarenkorbElement wk) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stat = null;
	    String insert = "INSERT INTO " + KORB_TABLE + " (" + KORB_TABLE_KORBUSERID + "," + KORB_TABLE_KORBWEINID
	            + ") VALUES (?, ?)";
	    try {
	        conn = DriverManager.getConnection(CONNECTION);
	        stat = conn.prepareStatement(insert);
	        System.out.println(wk.getKundenId());
	        System.out.println("Now comes weinId");
	        System.out.println(wk.toString());
	        // Assuming KORB_TABLE_KORBUSERID is for user ID and KORB_TABLE_KORBWEINID is for wine ID
	        stat.setLong(1, wk.getKundenId()); // Set user ID
	        stat.setInt(2, wk.getWein().getId()); // Set wine ID
	        System.out.println("2");
	        stat.executeUpdate();
	        System.out.println("Element inserted successfully");
	    } catch (SQLException e) {
	        throw e;
	    } finally {
	        try {
	            if (stat != null)
	                stat.close();
	            if (conn != null)
	                conn.close();
	        } catch (SQLException e) {
	            System.err.println("Error inserting element: " + e.getMessage());
	        }
	    }
	}


	public static void deleteKorbElement(int id) throws SQLException {
		try (Connection connection = DriverManager.getConnection(CONNECTION)) {
			String deleteQuery = "DELETE FROM " + KORB_TABLE + " WHERE " + KORB_TABLE_KORBID + " = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
				preparedStatement.setInt(1, id);
				int rowsAffected = preparedStatement.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("\nKorb mit ID " + id + " wurde enrfernt.");
				} else {
					System.out.println("Kein Korb mit ID " + id + " gefunden.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertKunde(Kunde k) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		String insert = "INSERT INTO " + KUNDE_TABLE + " (" + KUNDE_TABLE_KUNDEID + "," + KUNDE_TABLE_KUNDENAME + ","
				+ KUNDE_TABLE_KUNDEDATUM + ") VALUES (?, ?, ?)";
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(insert);
			stat.setLong(1, k.getUserId());
			stat.setString(2, k.getName());
			stat.setDate(3, Date.valueOf(k.getDatum()));
			stat.executeUpdate();
			System.out.println("Kunde inserted successfully");
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static KundeList leseKunden() throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;
		KundeList kl;

		ArrayList<Kunde> alKunden = new ArrayList<>();
		String select = "SELECT * FROM " + KUNDE_TABLE;

		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			rs = stat.executeQuery();
			while (rs.next()) {
				alKunden.add(new Kunde(rs.getLong(KUNDE_TABLE_KUNDEID), rs.getString(KUNDE_TABLE_KUNDENAME),
						rs.getDate(KUNDE_TABLE_KUNDEDATUM).toLocalDate()));
			}
			rs.close();
			kl = new KundeList(alKunden);
			return kl;
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

	public static WarenKorb leseWarenkorb(long kundeId) throws SQLException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet rs = null;

		ArrayList<WarenkorbElement> alleElemente = new ArrayList<>();
		String select = "SELECT * FROM " + KORB_TABLE + " INNER JOIN " + VINOTHEK_TABLE + " ON " + KORB_TABLE + "."
				+ KORB_TABLE_KORBWEINID + "=" + VINOTHEK_TABLE + "." + VINOTHEK_TABLE_ID + " WHERE "
				+ KORB_TABLE_KORBUSERID + "=" + kundeId;
		try {
			conn = DriverManager.getConnection(CONNECTION);
			stat = conn.prepareStatement(select);
			rs = stat.executeQuery();
			while (rs.next()) {
				WarenkorbElement wElem = new WarenkorbElement(rs.getInt(KORB_TABLE_KORBID),
						rs.getLong(KORB_TABLE_KORBUSERID),
						new Wein(rs.getInt(VINOTHEK_TABLE_ID), rs.getString(VINOTHEK_TABLE_NAME),
								rs.getString(VINOTHEK_TABLE_DESCRIPTION), rs.getDouble(VINOTHEK_TABLE_PREIS)));
				alleElemente.add(wElem);
			}
			rs.close();
			return new WarenKorb(alleElemente);
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (stat != null)
					stat.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}

}
