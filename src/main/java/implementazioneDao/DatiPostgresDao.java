package implementazioneDao;

import dao.DatiDAO;
import database_connection.ConnessioneDatabase;
import model.*;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatiPostgresDao implements DatiDAO {
    private Connection connection;

    public DatiPostgresDao() {
        try {
            connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ─── AULE ────────────────────────────────────────────────────────────

    @Override
    public void salvaAula(Aula aula) {
        String query = "INSERT INTO aula (nome) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, aula.getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Aula> caricaAule() {
        List<Aula> aule = new ArrayList<>();
        String query = "SELECT nome FROM aula";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                aule.add(new Aula(rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aule;
    }

    // ─── INSEGNAMENTI ────────────────────────────────────────────────────

    @Override
    public void salvaInsegnamento(Insegnamento ins) {
        String query = "INSERT INTO insegnamento (nome, cfu, anno_corso, docente_login) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, ins.getNome());
            ps.setInt(2, ins.getCFU());
            ps.setString(3, ins.getAnno().name());
            ps.setString(4, ins.getDocenteTitolare().getLogin());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Insegnamento> caricaInsegnamenti(List<Docente> docenti) {
        List<Insegnamento> lista = new ArrayList<>();
        String query = "SELECT nome, cfu, anno_corso, docente_login FROM insegnamento";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nome = rs.getString("nome");
                int cfu = rs.getInt("cfu");
                AnnoCorso anno = AnnoCorso.valueOf(rs.getString("anno_corso"));
                String loginDocente = rs.getString("docente_login");

                // Colleghiamo l'insegnamento al docente corretto
                Docente titolare = null;
                for (Docente d : docenti) {
                    if (d.getLogin().equals(loginDocente)) {
                        titolare = d;
                        break;
                    }
                }
                if (titolare != null) {
                    lista.add(new Insegnamento(nome, cfu, anno, titolare));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ─── LEZIONI ─────────────────────────────────────────────────────────

    @Override
    public void salvaLezione(Lezione lezione) {
        String query = "INSERT INTO lezione (giorno, ora_inizio, ora_fine, insegnamento_nome, aula_nome) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, lezione.getGiorno().name());
            ps.setTime(2, Time.valueOf(lezione.getOraInizio() + ":00")); // Conversione in Time SQL
            ps.setTime(3, Time.valueOf(lezione.getOraFine() + ":00"));
            ps.setString(4, lezione.getInsegnamento().getNome());
            ps.setString(5, lezione.getAula().getNome());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Lezione> caricaLezioni(List<Insegnamento> insegnamenti, List<Aula> aule) {
        List<Lezione> lista = new ArrayList<>();
        String query = "SELECT giorno, ora_inizio, ora_fine, insegnamento_nome, aula_nome FROM lezione";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                GiornoSettimana giorno = GiornoSettimana.valueOf(rs.getString("giorno"));
                LocalTime inizio = rs.getTime("ora_inizio").toLocalTime(); // Riconversione in LocalTime Java
                LocalTime fine = rs.getTime("ora_fine").toLocalTime();
                String nomeIns = rs.getString("insegnamento_nome");
                String nomeAula = rs.getString("aula_nome");

                // Ritroviamo l'oggetto Insegnamento originale
                Insegnamento insTrovato = null;
                for (Insegnamento i : insegnamenti) {
                    if (i.getNome().equals(nomeIns)) { insTrovato = i; break; }
                }

                // Ritroviamo l'oggetto Aula originale
                Aula aulaTrovata = null;
                for (Aula a : aule) {
                    if (a.getNome().equals(nomeAula)) { aulaTrovata = a; break; }
                }

                if (insTrovato != null && aulaTrovata != null) {
                    lista.add(new Lezione(giorno, inizio, fine, insTrovato, aulaTrovata));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ─── RICHIESTE E AGGIORNAMENTI ───────────────────────────────────────

    @Override
    public void salvaRichiesta(RichiestaSpostamento r) {
        // 1. Troviamo l'ID della lezione a cui la richiesta si riferisce
        String findLezione = "SELECT id FROM lezione WHERE giorno=? AND ora_inizio=? AND ora_fine=? AND insegnamento_nome=? AND aula_nome=?";
        String insertReq = "INSERT INTO richiesta_spostamento (giorno_proposto, ora_inizio_proposta, ora_fine_proposta, stato, lezione_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement psFind = connection.prepareStatement(findLezione)) {
            psFind.setString(1, r.getLezione().getGiorno().name());
            psFind.setTime(2, Time.valueOf(r.getLezione().getOraInizio()));
            psFind.setTime(3, Time.valueOf(r.getLezione().getOraFine()));
            psFind.setString(4, r.getLezione().getInsegnamento().getNome());
            psFind.setString(5, r.getLezione().getAula().getNome());

            ResultSet rs = psFind.executeQuery();
            if (rs.next()) {
                int lezioneId = rs.getInt("id");

                // 2. Inseriamo la richiesta con quell'ID
                try (PreparedStatement psIns = connection.prepareStatement(insertReq)) {
                    psIns.setString(1, r.getGiornoProposto().name());
                    psIns.setTime(2, Time.valueOf(r.getOraProposta()));
                    psIns.setTime(3, Time.valueOf(r.getOraFineProposta()));
                    psIns.setString(4, r.getStato().name());
                    psIns.setInt(5, lezioneId);
                    psIns.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RichiestaSpostamento> caricaRichieste(List<Lezione> lezioni) {
        List<RichiestaSpostamento> lista = new ArrayList<>();
        // Facciamo una JOIN per unire i dati della richiesta a quelli della lezione originale
        String sql = "SELECT r.giorno_proposto, r.ora_inizio_proposta, r.ora_fine_proposta, r.stato, " +
                "l.giorno, l.ora_inizio, l.ora_fine, l.insegnamento_nome, l.aula_nome " +
                "FROM richiesta_spostamento r JOIN lezione l ON r.lezione_id = l.id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                GiornoSettimana gProp = GiornoSettimana.valueOf(rs.getString("giorno_proposto"));
                LocalTime inProp = rs.getTime("ora_inizio_proposta").toLocalTime();
                LocalTime fineProp = rs.getTime("ora_fine_proposta").toLocalTime();
                StatoRichiesta stato = StatoRichiesta.valueOf(rs.getString("stato"));

                GiornoSettimana gLez = GiornoSettimana.valueOf(rs.getString("giorno"));
                LocalTime inLez = rs.getTime("ora_inizio").toLocalTime();
                LocalTime fineLez = rs.getTime("ora_fine").toLocalTime();
                String insNome = rs.getString("insegnamento_nome");
                String aulaNome = rs.getString("aula_nome");

                // Ritroviamo l'oggetto Lezione in memoria
                Lezione lezioneTrovata = null;
                for (Lezione l : lezioni) {
                    if (l.getGiorno() == gLez && l.getOraInizio().equals(inLez) && l.getOraFine().equals(fineLez)
                            && l.getInsegnamento().getNome().equals(insNome) && l.getAula().getNome().equals(aulaNome)) {
                        lezioneTrovata = l;
                        break;
                    }
                }
                if (lezioneTrovata != null) {
                    lista.add(new RichiestaSpostamento(gProp, inProp, fineProp, stato, lezioneTrovata));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public void aggiornaStatoRichiesta(RichiestaSpostamento r) {
        String sql = "UPDATE richiesta_spostamento SET stato = ? WHERE giorno_proposto = ? AND ora_inizio_proposta = ? AND ora_fine_proposta = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, r.getStato().name());
            ps.setString(2, r.getGiornoProposto().name());
            ps.setTime(3, Time.valueOf(r.getOraProposta()));
            ps.setTime(4, Time.valueOf(r.getOraFineProposta()));
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void aggiornaLezioneDopoSpostamento(Lezione vecchia, GiornoSettimana nGiorno, LocalTime nInizio, LocalTime nFine) {
        String sql = "UPDATE lezione SET giorno=?, ora_inizio=?, ora_fine=? WHERE giorno=? AND ora_inizio=? AND ora_fine=? AND insegnamento_nome=? AND aula_nome=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nGiorno.name());
            ps.setTime(2, Time.valueOf(nInizio));
            ps.setTime(3, Time.valueOf(nFine));
            // Condizioni per trovare la riga corretta nel DB usando i vecchi dati
            ps.setString(4, vecchia.getGiorno().name());
            ps.setTime(5, Time.valueOf(vecchia.getOraInizio()));
            ps.setTime(6, Time.valueOf(vecchia.getOraFine()));
            ps.setString(7, vecchia.getInsegnamento().getNome());
            ps.setString(8, vecchia.getAula().getNome());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}