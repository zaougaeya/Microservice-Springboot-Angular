package com.example.gestionmatch.service;

import com.example.gestionmatch.config.Header;
import com.example.gestionmatch.config.Reponse;
import com.example.gestionmatch.config.UserResponseDTO;
import com.example.gestionmatch.model.*;
import com.example.gestionmatch.repository.EquipeRepository;
import com.example.gestionmatch.repository.MatchRepository;
import com.example.gestionmatch.repository.TerrainRepository;
import com.example.gestionmatch.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class MatchService {

    private final RestTemplate restTemplate;


    private final MatchRepository matchRepository;
    private final EquipeRepository equipeRepository;
    private final TerrainRepository terrainRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    public MatchService(RestTemplate restTemplate, MatchRepository matchRepository,
                        EquipeRepository equipeRepository,
                        TerrainRepository terrainRepository,
                        UserRepository userRepository, UserService userService, EmailService emailService) {
        this.restTemplate = restTemplate;
        this.matchRepository = matchRepository;
        this.equipeRepository = equipeRepository;
        this.terrainRepository = terrainRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public Match createMatch(Match match) {
        try {
            match.setJoueurMax(11);
            match.setJoueurInscrit1(6);
            match.setJoueurInscrit2(6);
            return matchRepository.save(match);  // Sauvegarde du match et retour du match sauvegardé
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du match : " + e.getMessage());
        }
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }


    public Optional<Match> getMatchById(String id) {
        return matchRepository.findById(id);
    }


    public Match updateMatch(String id, Match matchDetails) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));

        if (match.isMatchJoue() && match.getScoreEquipe1() != null && match.getScoreEquipe2() != null) {
            throw new IllegalStateException("Impossible de modifier un match déjà joué avec scores renseignés.");
        }

        match.setDate(matchDetails.getDate());
        match.setStartDate(matchDetails.getStartDate());
        match.setEndDate(matchDetails.getEndDate());
        match.setType(matchDetails.getType());
        match.setIdEquipe1(matchDetails.getIdEquipe1());
        match.setIdEquipe2(matchDetails.getIdEquipe2());
        match.setIdTerrain(matchDetails.getIdTerrain());
        match.setScoreEquipe1(matchDetails.getScoreEquipe1());
        match.setScoreEquipe2(matchDetails.getScoreEquipe2());
        match.setCartonsJaunesEquipe1(matchDetails.getCartonsJaunesEquipe1());
        match.setCartonsRougesEquipe1(matchDetails.getCartonsRougesEquipe1());
        match.setCartonsJaunesEquipe2(matchDetails.getCartonsJaunesEquipe2());
        match.setCartonsRougesEquipe2(matchDetails.getCartonsRougesEquipe2());
        match.setFautesEquipe1(matchDetails.getFautesEquipe1());
        match.setFautesEquipe2(matchDetails.getFautesEquipe2());
        match.setMatchJoue(matchDetails.isMatchJoue());


        return matchRepository.save(match);
    }


    public boolean deleteMatch(String id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }


    public Match matchDetails(Match matchDetails) {

        Equipe equipe1 = equipeRepository.findById(matchDetails.getIdEquipe1())
                .orElseThrow(() -> new RuntimeException("Équipe 1 non trouvée"));

        Equipe equipe2 = equipeRepository.findById(matchDetails.getIdEquipe2())
                .orElseThrow(() -> new RuntimeException("Équipe 2 non trouvée"));
        Terrain terrain = terrainRepository.findById(matchDetails.getIdTerrain())
                .orElseThrow(() -> new RuntimeException("Terrain non trouvé"));
        matchDetails.setEquipe1(equipe1);
        matchDetails.setEquipe2(equipe2);
        matchDetails.setTerrain(terrain);
        return matchDetails;
    }


    public Match affecterEquipesAMatch(String matchId, String equipeId1, String equipeId2) {
        // Récupérer le match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));

        // Récupérer les équipes
        Equipe equipe1 = equipeRepository.findById(equipeId1)
                .orElseThrow(() -> new RuntimeException("Équipe 1 non trouvée"));
        Equipe equipe2 = equipeRepository.findById(equipeId2)
                .orElseThrow(() -> new RuntimeException("Équipe 2 non trouvée"));


        match.setIdEquipe1(equipeId1);
        match.setIdEquipe2(equipeId2);

        // Affecter les équipes au match
        match.setEquipe1(equipe1);
        match.setEquipe2(equipe2);

        // Sauvegarder le match mis à jour
        matchRepository.save(match);

        return match;
    }

    public Match desaffecterUneEquipeDuMatch(String matchId, String equipeId) {
        // Récupérer le match
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));

        // Comparaison des IDs directement
        if (match.getIdEquipe1() != null && match.getIdEquipe1().equals(equipeId)) {
            match.setIdEquipe1(null);
        } else if (match.getIdEquipe2() != null && match.getIdEquipe2().equals(equipeId)) {
            match.setIdEquipe2(null);
        } else {
            throw new RuntimeException("L'équipe spécifiée n'est pas affectée à ce match");
        }

        // Sauvegarde
        return matchRepository.save(match);
    }


    public Match affecterScores(String id, int scoreEquipe1, int scoreEquipe2) {
        return matchRepository.findById(id)
                .map(match -> {
                    match.setScoreEquipe1(scoreEquipe1);  // Mise à jour des scores
                    match.setScoreEquipe2(scoreEquipe2);  // Mise à jour des scores
                    return matchRepository.save(match);
                })
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));
    }


    public Match affecterTerrain(String matchId, String terrainId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));

        // Vérifie que le terrain existe
        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new RuntimeException("Terrain non trouvé"));

        // Affecte l'ID
        match.setIdTerrain(terrainId);

        // Enrichit l'objet transient pour le retour
        match.setTerrain(terrain);

        return matchRepository.save(match);
    }

    public Match affecterDateAuMatch(String matchId, Date nouvelleDate) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match non trouvé"));

        //match.setDate(nouvelleDate);
        return matchRepository.save(match);
    }

    public Map<String, Map<String, Integer>> calculerStatistiquesParEquipe() {
        List<Match> matchs = matchRepository.findAll();
        Map<String, Map<String, Integer>> statsParEquipe = new HashMap<>();

        for (Match match : matchs) {
            // Équipe 1
            String idEquipe1 = match.getIdEquipe1();
            statsParEquipe.putIfAbsent(idEquipe1, initStatsMap());
            Map<String, Integer> stats1 = statsParEquipe.get(idEquipe1);
            stats1.put("score", stats1.get("score") + getSafeValue(match.getScoreEquipe1()));
            stats1.put("cartonsJaunes", stats1.get("cartonsJaunes") + getSafeValue(match.getCartonsJaunesEquipe1()));
            stats1.put("cartonsRouges", stats1.get("cartonsRouges") + getSafeValue(match.getCartonsRougesEquipe1()));
            stats1.put("fautes", stats1.get("fautes") + getSafeValue(match.getFautesEquipe1()));


            // Équipe 2
            String idEquipe2 = match.getIdEquipe2();
            statsParEquipe.putIfAbsent(idEquipe2, initStatsMap());
            Map<String, Integer> stats2 = statsParEquipe.get(idEquipe2);
            stats2.put("score", stats2.get("score") + getSafeValue(match.getScoreEquipe2()));
            stats2.put("cartonsJaunes", stats2.get("cartonsJaunes") + getSafeValue(match.getCartonsJaunesEquipe2()));
            stats2.put("cartonsRouges", stats2.get("cartonsRouges") + getSafeValue(match.getCartonsRougesEquipe2()));
            stats2.put("fautes", stats2.get("fautes") + getSafeValue(match.getFautesEquipe2()));
        }

        return statsParEquipe;
    }

    public Map<String, Map<String, Integer>> calculerStatistiquesParEquipeEtDates(String dateD, String dateF) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date dateDeb = null;
        Date dateFin = null;

        try {
            dateDeb = format.parse(dateD);
            System.out.println("Converted Start Date: " + dateDeb);
        } catch (Exception e) {
            System.out.println("Erreur de conversion dateD: " + dateD);
            e.printStackTrace();
        }

        try {
            dateFin = format.parse(dateF);
            System.out.println("Converted End Date: " + dateFin);
        } catch (Exception e) {
            System.out.println("Erreur de conversion dateF: " + dateF);
            e.printStackTrace();
        }

        if (dateDeb == null || dateFin == null) {
            throw new IllegalArgumentException("Les dates fournies ne sont pas valides : " + dateD + " / " + dateF);
        }

        System.out.println(dateDeb + "............." + dateFin);
        List<Match> matchs = matchRepository.findByDateBetween(dateDeb, dateFin);
        Map<String, Map<String, Integer>> statsParEquipe = new HashMap<>();

        for (Match match : matchs) {
            // Équipe 1
            String idEquipe1 = match.getIdEquipe1();
            statsParEquipe.putIfAbsent(idEquipe1, initStatsMap());
            Map<String, Integer> stats1 = statsParEquipe.get(idEquipe1);
            stats1.put("score", stats1.get("score") + getSafeValue(match.getScoreEquipe1()));
            stats1.put("cartonsJaunes", stats1.get("cartonsJaunes") + getSafeValue(match.getCartonsJaunesEquipe1()));
            stats1.put("cartonsRouges", stats1.get("cartonsRouges") + getSafeValue(match.getCartonsRougesEquipe1()));

            // Équipe 2
            String idEquipe2 = match.getIdEquipe2();
            statsParEquipe.putIfAbsent(idEquipe2, initStatsMap());
            Map<String, Integer> stats2 = statsParEquipe.get(idEquipe2);
            stats2.put("score", stats2.get("score") + getSafeValue(match.getScoreEquipe2()));
            stats2.put("cartonsJaunes", stats2.get("cartonsJaunes") + getSafeValue(match.getCartonsJaunesEquipe2()));
            stats2.put("cartonsRouges", stats2.get("cartonsRouges") + getSafeValue(match.getCartonsRougesEquipe2()));
        }

        return statsParEquipe;
    }

    private Map<String, Integer> initStatsMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("score", 0);
        map.put("cartonsJaunes", 0);
        map.put("cartonsRouges", 0);
        return map;
    }

    private int getSafeValue(Integer value) {
        return value != null ? value : 0;
    }


    public List<Match> searchMatchs(LocalDate dateDebut, LocalDate dateFin, String type) {
        // Convertir LocalDate en Date
        LocalDateTime startLDT = dateDebut.atStartOfDay();
        LocalDateTime endLDT = dateFin.atTime(LocalTime.MAX);

        Date start = Date.from(startLDT.atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endLDT.atZone(ZoneId.systemDefault()).toInstant());

        return matchRepository.findByDateAndType(start, end, type)
                .stream()
                .filter(match -> match.getJoueurInscrit1() < 11
                        || match.getJoueurInscrit2() < 11)
                .collect(Collectors.toList());
    }

    public void rejoindreMatch(String idMatch, String idEquipe, String authHeader) {
        System.err.println("id" + idMatch);
        System.err.println("authHeader: " + authHeader);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authHeader);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        UserResponseDTO response = this.get(
                new ParameterizedTypeReference<UserResponseDTO>() {},
                "http://127.0.0.1:9090/",
                "api/users/me",
                null,
                headers
        );

        UserResponseDTO user = response;
        System.out.println("USER RECUPEREEEEEEEEEE : " + user);

        // Vérifie si l'utilisateur a déjà rejoint cette équipe
        Optional<User> existingUser = userService.findByEmailAndIdEquipe(user.mailuser(), idEquipe);
        if (existingUser.isPresent()) {
            System.out.println("Utilisateur déjà inscrit à ce match !");
            return; // ou lance une exception selon ton besoin
        }

        User userj = new User(null, user.nomuser(), user.prenomuser(), 0, null, null, null,
                user.mailuser(), user.job(), user.role(), idEquipe, null,user.id());
        userService.createUser(userj);

        Optional<Match> optionalMatch = matchRepository.findById(idMatch);
        if (optionalMatch.isPresent()) {
            Match match = optionalMatch.get();
            int i;
            if (match.getIdEquipe1().equals(idEquipe)) {
                i = match.getJoueurInscrit1();
                match.setJoueurInscrit1(++i);
            } else {
                i = match.getJoueurInscrit2();
                match.setJoueurInscrit2(++i);
            }
            matchRepository.save(match);
        }

        String subject = "Confirmation d'inscription au match";
        String body = String.format(
                "Bonjour %s %s,\n\nVous avez bien rejoint le match avec succès.\n\n Amuse-toi à fond!",
                user.prenomuser(),
                user.nomuser()
        );
        emailService.sendEmail(user.mailuser(), subject, body);
    }




    public <T> T get(ParameterizedTypeReference<T> returnType, String baseURI, String path,

                 Map<String, Object> parameters, Map<String, String> headers) throws IllegalStateException {

    return prepareRequest(returnType, baseURI, path, parameters, headers, HttpMethod.GET, null);
}


private <T> T prepareRequest(ParameterizedTypeReference<T> returnType, String baseURI, String path,

                             Map<String, Object> parameters, Map<String, String> headers, HttpMethod method, Object body)

        throws IllegalStateException {

    UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(baseURI).path(path);

    if (parameters != null)

        parameters.forEach(ucb::queryParam);

    T result = restTemplate

            .exchange(ucb.encode().toUriString(), method, (body == null) ? new HttpEntity<>(prepareHeaders(headers))

                    : new HttpEntity<>(body, prepareHeaders(headers)), returnType)

            .getBody();

    if (result instanceof Reponse) {

        Reponse<?> reponse = (Reponse<?>) result;

        if (reponse.getHeader().getCode() == 200)

            return result;

        throw new IllegalStateException(

                "Error " + reponse.getHeader().getCode() + ": " + reponse.getHeader().getLibelle());

    } else
        return result;
}

private HttpHeaders prepareHeaders(Map<String, String> headers) {

    HttpHeaders httpHeaders = new HttpHeaders();

    if (headers == null) {

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (sra != null) {

            HttpServletRequest hsr = sra.getRequest();

            httpHeaders.addIfAbsent("Authorization", hsr.getHeader("Authorization"));

            httpHeaders.addIfAbsent("Language", hsr.getHeader("Language"));

        }

    } else

        headers.forEach(httpHeaders::add);

    return httpHeaders;
}

public List<Match> getMatchsJoues() {
    return matchRepository.findByMatchJoueTrue();
}

}

