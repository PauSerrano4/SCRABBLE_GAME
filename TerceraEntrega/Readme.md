#  Scrabble - PROP Subgrup 11.1

Projecte de l'assignatura PROP a la FIB

Implementacio completa del joc Scrabble en Java amb Jugador Maquina, interficie grafica, persistencia de dades i sistema de ranquing.

---

##  Autors

**Subgrup 11.1 - PROP FIB**

- **Pau Serrano** 
- **Alexander de Jong** 
- **Ferran Blanchart** 
- **Marc Gil** -

---


##  Estructura del projecte


```
Subgrup11.1/
├── TerceraEntrega/
│   ├── FONTS/
│   │   ├── src/
│   │   │   ├── model/          # Model del joc (Partida, Jugador, Tauler, Fitxa, etc.)
│   │   │   ├── controller/     # Controladors (Domini, Maquina/BOT, Persistencia)
│   │   │   ├── presentacio/    # Interficie grafica Swing i classe Main
│   │   │   ├── persistencia/   # Gestio de fitxers i serialitzacio
│   │   │   ├── util/           # Utilitats (Pair, Node, Anchor, Play, etc.)
│   │   │   ├── exceptions/     # Excepcions personalitzades
│   │   │   └── test/           # Tests d'integracio JUnit complerts
│   │   └── Makefile            # Automatitzacio de compilacio i execucio
│   ├── EXE/
│   │   └── lib/                # Llibreries necessaries (JUnit, Hamcrest)
│   ├── DOC/                    # Documentacio final del projecte
│   │   ├── Manual d'usuari     # Manual d'usuari complet
│   │   ├── Javadoc             # Documentació generada per javadoc
│   │   └── Documentacio tests  # Documentacio dels tests d'integracio
│   ├── DATA/                   # Diccionaris, alfabets i configuracions
│   ├── membres.txt             # Informacio dels membres del grup
│   ├── Readme.md               # Aquest fitxer
│   └── index.txt               # Index del projecte
                      
```

---

##  Funcionalitats principals (Tercera Entrega)

###  **Joc complert**
- **Regles oficials del Scrabble**: Multiplicadors, bonificacions, validacio de paraules
- **3 idiomes**: Catala, Castella, Angles (amb diccionaris reals), amb possibilitat d'afegir diccionaris personalitzats
- **3 dificultats**: Facil (8 fitxes), Normal (7 fitxes), Dificil (6 fitxes)
- **Modes de joc**: Contrarellotge vs Classic, Huma vs BOT vs Huma vs Huma

###  **BOT**
- **BOT avançat** amb algorisme de busqueda en profunditat (BDP) per jugar de manera intel·ligent
- **Jugades optimes** amb calcul de puntuacio de cada jugada i seleccio de la millor opcio.
- **Gestio de comodins** i estrategies adaptatives per jugar amb comodins de manera efectiva.

###  **Sistema d'usuaris**
- **Registre i autenticacio** amb contrasenyes
- **Perfils persistents** amb estadistiques individuals
- **Sistema de ranquing** global actualitzat automaticament

###  **Persistencia**
- **Guardar/carregar partides** en qualsevol moment
- **Estadistiques per usuari**: partides jugades, puntuacio mitjana, records
- **Historial de partides** amb detalls complets

###  **Interficie grafica**
- **Swing GUI** completa i intuitiva
- **Tauler visual** amb multiplicadors i fitxes
- **Gestio d'atrils** amb drag & drop
- **Temporitzadors** i notificacions

---

##  Us del Makefile

### **Compilacio**
```bash
# Compilar tot el projecte
make

# Compilar nomes els components principals
make main

# Compilar i executar tests d'integracio
make test
```

### **Execucio**
```bash
# Executar l'aplicacio Scrabble
make run

# Executar un test especific
make test-one TEST=TestAtrilMidaIT
```

### **Neteja**
```bash
# Netejar fitxers compilats (mante ../lib intacte)
make clean
```

---

##  Tests d'integracio

El projecte inclou tests JUnit 4 exhaustius que validen:

### **Tests disponibles:**
- **TestUsuarisIT**: Registre, login, gestio de perfils
- **TestPartidaBasicaIT**: Creacio de partides, configuracions
- **TestJugadesIT**: Regles del Scrabble, validacio de paraules
- **TestAtrilMidaIT**: Gestio d'atrils segons dificultats
- **TestComodinsIT**: Funcionament de fitxes blanques
- **TestMultiplicadorsIT**: Caselles especials i bonificacions
- **TestValidacionsAvançadesIT**: Casos limit i connectivitat
- **TestCasosEspecialsIT**: Situacions excepcionals
- **TestBotIT**: Interaccio amb el bot de la partida
- **TestErrorsIT**: Gestio robusta d'errors
- **TestPersistenciaIT**: Guardar/carregar estat de la partida.
- **TestEstadistiquesRanquingIT**: Sistema de puntuacions i estadistiques

##  Requisits del sistema

- **Java JDK 11 o superior**
- **Make** (per automatitzacio)
- **Sistema operatiu**: Linux, macOS, Windows

### **Llibreries necessaries a `EXE/lib/`:**
- `junit-4.13.2.jar` - Framework de testing
- `hamcrest-core-1.3.jar` - Matchers per JUnit

---


##  Inici rapid

```bash
# 1. Clonar o descomprimir el projecte
cd TerceraEntrega/FONTS

# 2. Compilar tot
make

# 3. Executar el joc
make run

# 4. (Opcional) Executar tests
make test
```

---

## Canvis respecte la segona entrega

- S'han eliminat les funcions `setNom` i `setCotrasenya` d'Usuari.java ja que feien el mateix que `canviarNom` i `canviarContrasenya`.
- **Nova interficie grafica millorada**: totes les vistes principals (menu, partida, pausa, gestio de partides, resultats, etc.) han estat redissenyades amb Swing per ser mes intuitives, modernes i atractives.
- **Gestio completa de partides guardades**: ara es poden guardar, carregar i esborrar partides des de la interficie grafica, amb dialogues visuals i confirmacions.
- **Sistema de ranquing i estadistiques**: s'ha afegit un ranquing global i estadistiques detallades per usuari, accessibles des del menu principal.
- **Millora de la gestio de diccionaris i idiomes**: ara es poden afegir nous diccionaris i alfabets de manera flexible, i el sistema detecta automaticament els digrafs de cada idioma.
- **Millores en el BOT**: el BOT juega de manera mes intel·ligent, utilitza estrategies avançades i gestiona millor els comodins i les jugades optimes.
- **Temporitzador i mode contrarellotge**: s'ha afegit un temporitzador configurable per partida i per torn, amb notificacions visuals.
- **Manual d'usuari i normes integrades**: la normativa oficial i el manual d'usuari son accessibles des de la propia aplicacio, amb una vista dedicada.
- **Gestio d'errors millorada**: es mostren missatges d'error clars i amigables a l'usuari per qualsevol problema de fitxers, diccionaris o accions incorrectes.
- **Persistencia millorada**: ara es guarda l'estat complet de la partida, incloent el tauler, atrils, bossa de fitxes, temps i torns.
- **Compatibilitat multiplataforma**: el projecte funciona tant en Windows com en Linux i macOS.
- **Tests d'integracio ampliats**: s'han afegit mes tests per cobrir casos limit, errors i funcionalitats avançades.
- **Millora de la flexibilitat per afegir idiomes**: ara el sistema pot detectar automaticament digrafs a partir de l'alfabet de cada idioma, facilitant l'extensio a nous idiomes sense modificar el codi font.
- **Refactoritzacio i neteja de codi**: s'han millorat estructures internes, modularitat i mantenibilitat del projecte.
- **Millora de la documentacio**: s'ha ampliat el manual d'usuari i la documentacio tecnica.

---

## Contacte

Per qualsevol dubte o suggeriment, contacta amb qualsevol membre del grup mitjançant el correu institucional.

**El projecte esta llest per jugar!**