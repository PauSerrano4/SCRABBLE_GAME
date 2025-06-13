# DOCUMENTACIO EXHAUSTIVA DE TESTS D'INTEGRACIO SCRABBLE

## RESUM GENERAL
Tests d'integracio que validen el funcionament complet del joc Scrabble. Utilitzen JUnit 4 per verificar que totes les funcionalitats principals funcionen correctament quan es combinen entre elles, simulant escenaris reals d'us.

## CONFIGURACIO TECNICA

### Fitxers de Dades Necessaris
- **catalan.txt**: Llista de paraules valides en catala (classe String)
- **castellano.txt**: Llista de paraules valides en castella (classe String)
- **english.txt**: Llista de paraules valides en angles (classe String)
- **letrasCATALAN.txt**: Alfabet catala amb quantitats i puntuacions 
- **letrasCASTELLANO.txt**: Alfabet castella amb quantitats i puntuacions 
- **letrasENGLISH.txt**: Alfabet angles amb quantitats i puntuacions 
- **usuaris.txt**: Base de dades d'usuaris registrats (classe Usuari)
- **ranquing.dat**: Dades de ranquing serializades (classe Ranquing)
- **estadistiques_*.ser**: Estadistiques per usuari serializades (classe Estadistiques)
- **partides_*.ser**: Partides guardades serializades (classe Partida)



---

## CLASSE BASE

### TestBaseIT
 Infraestructura comuna per tots els tests d'integracio.

**Fitxers de dades necessaris**: Tots els diccionaris i alfabets esmentats anteriorment.



**Efectes**
- Configuracio i neteja automatica d'entorns de prova
- Helpers per crear jugades horitzontals i verticals
- Calcul de puntuacions amb i sense multiplicadors
- Validacio de paraules contra diccionaris reals



## TESTS PER FUNCIONALITAT

### TestUsuarisIT
**Objecte de la prova**: Gestio completa d'usuaris, autenticacio i persistencia de perfils.

**Fitxers de dades necessaris**: 
- usuaris.txt (base de dades d'usuaris)
- estadistiques_*.ser (estadistiques individuals)
- ranquing.dat (ranquing global)

**Valors estudiats**:
- **Noms unics amb timestamp**: Evita conflictes entre execucions de tests
- **Credencials valides i invalides**: "testPass", "newPass", contrasenyes incorrectes
- **Multiples usuaris simultanis**: "user1_timestamp", "user2_timestamp"

**Efectes estudiats**:
- **testRegistreILogin**: Registre d'usuari nou i activacio automatica de sessio
- **testCanviNomUsuari**: Canvi de nom d'usuari i persistencia del canvi
- **testRegistreLoginMultiplesUsuaris**: Gestio independent de multiples perfils
- **testLoginCredencialsIncorrectes**: Rebuig de credencials invalides amb missatges d'error
- **testCanviarContrasenya**: Canvi de contrasenya i validacio posterior amb nova contrasenya

### TestPartidaBasicaIT
**Objecte de la prova**: Creacio i gestio basica de partides amb diferents configuracions.

**Fitxers de dades necessaris**: Tots els diccionaris i alfabets per validar combinacions d'idiomes.

**Valors estudiats**:
- **Totes les dificultats**: FACIL, NORMAL, DIFICIL
- **Tots els idiomes**: catala, castella, angles
- **Modes amb i sense temporitzador**: Contrarellotge vs Classic
- **Tipus de contrincants**: Huma vs Maquina, Huma vs Huma

**Efectes estudiats**:
- **testInicioPartidaModosDificultadesIdiomas**: Creacio de partides amb totes les combinacions possibles
- **testPartidaConYSinTemporizador**: Funcionament correcte de temporitzadors i partides sense limit
- **testPartidaDosJugadorsHumans**: Alternança de torns entre dos jugadors humans
- **testPuntuacionsExactesDificultats**: Consistencia de puntuacions entre diferents dificultats
- **testBonificacioTotesLesFitxesExacta**: Verificacio de bonificacio de 50 punts per usar totes les fitxes
- **testTemporitzadorExpira**: Finalitzacio automatica quan s'acaba el temps

### TestJugadesIT
**Objecte de la prova**: Validacio de regles de joc, col·locacio de fitxes i formacio de paraules.

**Fitxers de dades necessaris**: Diccionaris per validar paraules, alfabets per calcular puntuacions.

**Valors estudiats**:
- **Paraules reals**: "CASA", "ZERO", "EXEMPLE", "COTXE"
- **Coordenades especifiques**: Centre (7,7), posicions horitzontals i verticals
- **Lletres d'alt valor**: Z(8 punts), X(8 punts), Q(8 punts)
- **Comodins**: Fitxes amb valor 0 representades per '#'

**Efectes estudiats**:
- **testPrimeraJugadaValidaTocaCentro**: Obligatorietat de passar pel centre (7,7) en primera jugada
- **testPrimeraJugadaInvalidaNoCentro**: Rebuig de jugades inicials que no toquen el centre
- **testColocacionHorizontalVerticalDiagonal**: Acceptacio d'horitzontals/verticals, rebuig de diagonals
- **testPalabrasCruzadas**: Formacio correcta de multiples paraules en una jugada
- **testUsoFichaBlanca**: Us correcte de comodins amb valor zero punts
- **testJugadaAmbDigraf**: Gestio de caracters especials catalans (ny, l·l)
- **testJugadaInvalidaNoDiccionari**: Rebuig de paraules inexistents al diccionari
- **testPuntuacionsLletresAltValor**: Puntuacions amb lletres de valor alt
- **testPuntuacionsExactesIdiomes**: Diferencies de puntuacio entre idiomes
- **testBonificacioTotesLesFitxes**: Bonificacio especial quan s'usen totes les fitxes

### TestValidacionsAvançadesIT
**Objecte de la prova**: Regles complexes de connectivitat, validacio de paraules creuades i deteccio d'errors.

**Fitxers de dades necessaris**: Diccionaris per validar totes les paraules formades.

**Valors estudiats**:
- **Jugades aillades**: Posicions (0,0) desconnectades del tauler principal
- **Paraules invalides**: Combinacions com "QCTT", "ZZZZZZZ"
- **Coordenades fora de rang**: Posicions negatives (-1,-1), massa grans (15,15)
- **Gaps entre fitxes**: Espais buits en la mateixa jugada

**Efectes estudiats**:
- **testConnectivitateObligatoria**: Obligatorietat de connectar amb fitxes existents
- **testParaulesCreuadesValides**: Validacio de totes les paraules formades simultaniament
- **testJugadaAmbEspaisBuits**: Deteccio de gaps entre fitxes de la mateixa jugada
- **testDireccioUnica**: Rebuig de jugades diagonals o en multiple direccions
- **testSolapamentFitxes**: Deteccio de col·locacio sobre caselles ocupades
- **testLimitsTauler**: Validacio de limits del tauler 15x15
- **testGestioJugadesProblematiques**: Robustesa del sistema davant errors multiples

### TestComodinsIT
**Objecte de la prova**: Funcionament correcte de fitxes blanques (comodins) en totes les situacions.

**Fitxers de dades necessaris**: Diccionaris per validar paraules amb comodins, alfabets per puntuacions.

**Valors estudiats**:
- **Valor zero**: Comodins sempre valen 0 punts independentment de la lletra representada
- **Representacio flexible**: '#' pot representar qualsevol lletra (A-Z)
- **Multiples comodins**: Us de diversos comodins en la mateixa paraula
- **Combinacio amb multiplicadors**: Comodins en caselles DL, TL, DP, TP

**Efectes estudiats**:
- **testComodiValorZero**: Verificacio que comodins aporten 0 punts
- **testComodiPotSerQualsevolLletra**: Flexibilitat de representacio de lletres
- **testComodiEnMultiplicador**: Multiplicadors aplicats sobre valor 0
- **testMultiplesComodins**: Us de multiples comodins simultaniament
- **testComodiFormantParaules**: Comodins en paraules creuades
- **testComodinsFuncionalitat**: Test basic de funcionalitat general

### TestAtrilMidaIT
**Objecte de la prova**: Gestio correcta de la mida de l'atril segons dificultat i reompliment automatic.

**Fitxers de dades necessaris**: Alfabets per repartir fitxes correctes.

**Valors estudiats**:
- **Dificultats especifiques**: FACIL=8, NORMAL=7, DIFICIL=6 fitxes
- **Reompliment automatic**: Manteniment de mida despres de jugades
- **Intercanvi de fitxes**: Gestio d'intercanvis quan hi ha fitxes disponibles
- **Bossa buida**: Comportament quan s'acaben fitxes globals

**Efectes estudiats**:
- **testAtrilMidaFACIL**: Verificacio de 8 fitxes inicials per dificultat facil
- **testAtrilMidaNORMAL**: Verificacio de 7 fitxes inicials per dificultat normal
- **testAtrilMidaDIFICIL**: Verificacio de 6 fitxes inicials per dificultat dificil
- **testIntercanviFitxes**: Intercanvi de fitxes mantenint mida total
- **testMantenerMidaAtrilDespresJugada**: Reompliment automatic despres de jugades
- **testAtrilBossaBuida**: Gestio quan s'acaben fitxes disponibles

### TestValidacionsAvançadesIT
**Objecte de la prova**: Situacions especials, bonificacions i condicions de final de partida.

**Fitxers de dades necessaris**: Diccionaris i alfabets, fitxers de temporitzadors.

**Valors estudiats**:
- **Bonificacio de 50 punts**: Quan s'usen totes les fitxes de l'atril
- **Timeouts**: Comportament quan expira el temporitzador
- **Empats**: Situacions amb puntuacions identiques
- **Paraules invalides**: Combinacions que no existeixen al diccionari
- **Bossa gairebe buida**: Poques fitxes restants per repartir

**Efectes estudiats**:
- **testFinPerFitxesAcabades**: Bonificacio quan un jugador usa totes les seves fitxes
- **testLimitTemps**: Finalitzacio automatica quan s'acaba el temps
- **testEmpat**: Gestio correcta d'empats entre jugadors
- **testParaulaInvalida**: Rebuig de paraules inexistents
- **testBossaGairebeBuida**: Funcionament amb poques fitxes disponibles

### TestErrorsIT
**Objecte de la prova**: Gestio robusta d'errors i situacions excepcionals.

**Fitxers de dades necessaris**: Tots els fitxers per simular errors de carrega i persistencia.

**Valors estudiats**:
- **Fitxes no disponibles**: Intent d'usar lletres no presents a l'atril
- **Coordenades invalides**: Posicions negatives o fora del tauler 15x15
- **Fitxers inexistents**: Carrega de partides que no existeixen
- **Jugades buides**: Llistes de posicions sense contingut
- **Bossa buida**: Intercanvis quan no hi ha fitxes disponibles

**Efectes estudiats**:
- **testErrorFichaFueraAtril**: Deteccio d'us de fitxes no disponibles
- **testErrorCoordenadasFueraTablero**: Rebuig de coordenades fora de rang
- **testErrorCasellaOcupada**: Deteccio de problemes de connectivitat
- **testErrorCanviFitxesBossaBuida**: Gestio d'errors en intercanvis impossibles
- **testErrorCarregarPartidaInexistent**: Gestio elegant de fitxers inexistents
- **testErrorJugadaBuida**: Rebuig de jugades sense fitxes
- **testErrorCoordenadesNegatives**: Rebuig de coordenades negatives

### TestPersistenciaIT
**Objecte de la prova**: Guardat i carrega d'estat de partides, manteniment de consistencia.

**Fitxers de dades necessaris**: 
- Fitxers .ser per partides guardades
- Diccionaris i alfabets per recrear estat complet

**Valors estudiats**:
- **Estats complets**: Jugades realitzades, puntuacions, estat del tauler
- **Temporitzadors**: Recreacio de temporitzadors despra e e i o o us de carrega
- **Diferents idiomes**: Persistencia consistent entre idiomes
- **Eliminacio segura**: Neteja de fitxers sense corrompre sistema

**Efectes estudiats**:
- **testGuardarCarregarPartida**: Manteniment d'estat complet en guardar/carregar
- **testEliminarPartida**: Eliminacio segura de partides guardades
- **testPersistenciaTemporitzador**: Recreacio de temporitzadors actius
- **testPersistenciaDiferentsIdiomes**: Consistencia entre idiomes diferents

### TestEstadistiquesRanquingIT
**Objecte de la prova**: Actualitzacio correcta d'estada e e i o o ustiques i ranquing despra e e i o o us de partides.

**Fitxers de dades necessaris**: 
- ranquing.dat (ranquing serializat)
- estadistiques_*.ser (estada e e i o o ustiques individuals)

**Valors estudiats**:
- **Puntuacions reals**: Calcul correcte segons jugades realitzades
- **Ma e e i o o ultiples categories**: Total, per idioma, contrarellotge, record, percentatge
- **Mode test vs produccio**: Diferenciacio entre entorns de prova i reals

**Efectes estudiats**:
- **testActualitzacioRanquing**: Actualitzacio automatica despra e e i o o us de partides completades

### TestMultiplicadorsIT
**Objecte de la prova**: Aplicacio correcta de multiplicadors del tauler segons estandard Scrabble.

**Fitxers de dades necessaris**: Alfabets per calcular puntuacions base, layout del tauler per multiplicadors.

**Valors estudiats**:
- **Multiplicadors estandard**: DL (x2 lletra), TL (x3 lletra), DP (x2 paraula), TP (x3 paraula)
- **Posicions especa e e i o o ufiques**: Centre (7,7), cantonades, posicions intermedies
- **Combinacions**: Ma e e i o o ultiples multiplicadors en la mateixa jugada
- **Fitxes noves vs existents**: Multiplicadors noma e e i o o us s'apliquen a fitxes del torn actual

**Efectes estudiats**:
- **testMultiplicadorDobleParaulaCentre**: Verificacio de DP x2 al centre
- **testMultiplicadorDobleLetra**: Aplicacio correcta de DL x2
- **testMultiplicadorTripleLetra**: Aplicacio correcta de TL x3
- **testMultiplicadorTripleParaula**: Aplicacio correcta de TP x3
- **testCombinacioMultiplicadors**: Ma e e i o o ultiples multiplicadors simultanis
- **testMultiplicadorNoAplicatFitxesExistents**: Exclusio de fitxes previes

### TestBotIT
**Objecte de la prova**:  BOT, jugades valides i gestio auta e e i o o unoma.

**Fitxers de dades necessaris**: Diccionaris per validar jugades del BOT, alfabets per estrategies.

**Valors estudiats**:
- **Jugades automatiques**: Paraules generades pel BOT han de ser sempre valides
- **Temps de resposta**: Limits raonables per evitar timeouts
- **Situacions difa e e i o o ucils**: Gestio quan el BOT ta e e i o o u fitxes problematiques (Q, Z, X)
- **Estrategies**: Intercanvi vs jugada vs passar torn

**Efectes estudiats**:
- **testBOTFaJugadesValides**: Totes les jugades del BOT compleixen regles
- **testBOTGestionaAtril**: Manteniment correcte de fitxes
- **testDeterminacioGuanyador**: Resolucio correcta de victa e e i o o uries
- **testBOTTempsResposta**: Temps de resposta acceptables (<5 segons)
- **testBOTIntercanviaFitxes**: Capacitat d'intercanviar quan conva e e i o o u
- **testBOTNoPotFerJugada**: Passar torn quan no hi ha jugades possibles

**Test Netej Usuaris** Test per restablir tots els canvis fets a usuaris.txt



## METODOLOGIA DE TESTING

### Tipus de Tests
- **Integracio**: Tests que combinen ma e e i o o ultiples components del sistema
- **Casos La e e i o o umit**: Situacions extremes per validar robustesa
- **Dades Reals**: a e e i o o us de diccionaris i alfabets reals, no mock data

### Estrategies de Validacio
1. **Forçat d'Atrils**: Manipulacio directa per crear escenaris controlats
2. **Usuaris Temporals**: Creacio i neteja automatica per evitar interferencies
3. **Calculs Independents**: Verificacio de puntuacions amb calculs matematics separats
4. **Mode Test**: Separacio entre entorn de proves i produccio

### Cobertura de Funcionalitats
-  Gestio d'usuaris i autenticacio
-  Creacio i configuracio de partides
-  Regles completes del Scrabble
-  Validacio de paraules i diccionaris
-  Multiplicadors i bonificacions
-  Comodins i fitxes especials
-  Movimemnts del BOT
-  Persistencia i carrega d'estat
-  Estada e e i o o ustiques i ranquing
-  Gestio d'errors i robustesa

---

## EXECUCIo I RESULTATS

### Comandos d'Execucio
```bash
make test          # Executa tots els tests amb resum complet
make test-silent    # Executa en silenci amb noma e e i o o us resum final
make test-one TEST=TestNom  # Executa un test especa e e i o o ufic
```



### Entorn d'Execucio
- **JUnit 4**: Framework de testing utilitzat
- **Java 17+**: Versio ma e e i o o unima requerida
- **Sistema operatiu**: Compatible amb Linux, macOS


---

## CONCLUSIONS

Els tests d'integracio proporcionen una cobertura exhaustiva de totes les funcionalitats del joc Scrabble, validant tant el comportament individual de cada component com la seva interaccio correcta. La metodologia de 

La separacio entre mode test i produccio, juntament amb la gestio automatica d'usuaris temporals i la neteja posterior, garanteix que els tests no interfereixin amb l'a e e i o o us normal de l'aplicacio.