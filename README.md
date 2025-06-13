#  Scrabble - PROP Subgrup 11.1

Projecte de l'assignatura PROP a la FIB

Implementació completa del joc Scrabble en Java amb intel·ligència artificial, interfície gràfica, persistència de dades i sistema de rànquing.

---

##  Estructura del projecte

```
subgrup-prop11.1/
├── PrimeraEntrega/
││  ├── FONTS/
│   │   ├── src/
│   │   │   ├── model/          # Model del joc (Partida, Jugador, Tauler, Fitxa, etc.)
│   │   │   ├── controller/     # Controladors (Domini, Màquina/BOT)
│   │   │   ├── presentacio/    # Controlador per gestionar la vista de la terminal
│   │   │   ├── util/           # Utilitats (Pair, Node, Anchor, Play, etc.)
│   │   │   └── test/           # Tests unitaris de Junuit
│   │   └── Makefile            # Automatització de compilació i execució
│   ├── EXE/                    # Executables i classes compilades i tests compilats
│   ├── DOC/                    # Documentació inicial del projecte
│   └── DATA/                   # Diccionaris, alfabets i configuracions
├── SegonaEntrega/
│   ├── Index.txt
│   └── DOC/                   # Documentació intermèdia
├── TerceraEntrega/
│   ├── FONTS/
│   │   ├── src/
│   │   │   ├── model/          # Model del joc (Partida, Jugador, Tauler, Fitxa, etc.)
│   │   │   ├── controller/     # Controladors (Domini, Màquina/BOT, Persistència)
│   │   │   ├── presentacio/    # Interfície gràfica Swing i classe Main
│   │   │   ├── persistencia/   # Gestió de fitxers i serialització
│   │   │   ├── util/           # Utilitats (Pair, Node, Anchor, Play, etc.)
│   │   │   ├── exceptions/     # Excepcions personalitzades
│   │   │   └── test/           # Tests d'integració JUnit complerts
│   │   ├── Makefile            # Automatització de compilació i execució
│   │   └── Manual d'usuari     # Manual de usuari
│   ├── EXE/                    # Executables i classes compilades i les llibreries necesaries
│   ├── DOC/                    # Documentació final del projecte
│   └── DATA/                   # Diccionaris, alfabets i configuracions
└── Readme.md                   # Aquest fitxer
```



##  Funcionalitats principals (Tercera Entrega)

###  **Joc complert**
- **Regles oficials del Scrabble**: Multiplicadors, bonificacions, validació de paraules
- **3 idiomes**: Català, Castellà, Anglès (amb diccionaris reals), amb possibilitat d'afegir diccionaris personalitzats
- **3 dificultats**: Fàcil (8 fitxes), Normal (7 fitxes), Difícil (6 fitxes)
- **Modes de joc**: Contrarellotge vs Clàssic, Humà vs BOT vs Humà vs Humà

###  **BOT**
- **BOT avançat** amb algorisme de cross-checks i tries
- **Jugades òptimes** basades en anchors i navegació de diccionari
- **Gestió de comodins** i estratègies adaptatives

###  **Sistema d'usuaris**
- **Registre i autenticació** amb contrasenyes
- **Perfils persistents** amb estadístiques individuals
- **Sistema de rànquing** global actualitzat automàticament

###  **Persistència**
- **Guardar/carregar partides** en qualsevol moment
- **Estadístiques per usuari**: partides jugades, puntuació mitjana, rècords
- **Historial de partides** amb detalls complets

###  **Interfície gràfica**
- **Swing GUI** completa i intuïtiva
- **Tauler visual** amb multiplicadors i fitxes
- **Gestió d'atrils** amb drag & drop
- **Temporitzadors** i notificacions

---

##  Ús del Makefile

### **Compilació**
```bash
# Compilar tot el projecte
make

# Compilar només els components principals
make main

# Compilar i executar tests d'integració
make test
```

### **Execució**
```bash
# Executar l'aplicació Scrabble
make run

# Executar un test específic
make test-one TEST=TestAtrilMidaIT
```

### **Neteja**
```bash
# Netejar fitxers compilats (manté ../lib intacte)
make clean
```

---

##  Tests d'integració

El projecte inclou tests JUnit 4 exhaustius que validen:

### **Tests disponibles:**
- **TestUsuarisIT**: Registre, login, gestió de perfils
- **TestPartidaBasicaIT**: Creació de partides, configuracions
- **TestJugadesIT**: Regles del Scrabble, validació de paraules
- **TestAtrilMidaIT**: Gestió d'atrils segons dificultats
- **TestComodinsIT**: Funcionament de fitxes blanques
- **TestMultiplicadorsIT**: Caselles especials i bonificacions
- **TestValidacionsAvançadesIT**: Casos límit i connectivitat
- **TestCasosEspecialsIT**: Situacions excepcionals
- **TestBotIT**: Intel·ligència artificial
- **TestErrorsIT**: Gestió robusta d'errors
- **TestPersistenciaIT**: Guardar/carregar estat
- **TestEstadistiquesRanquingIT**: Sistema de puntuacions


##  Requisits del sistema

- **Java JDK 11 o superior**
- **Make** (per automatització)
- **Sistema operatiu**: Linux, macOS


### **Llibreries necessàries a `EXE/lib/`:**
- `junit-4.13.2.jar` - Framework de testing
- `hamcrest-core-1.3.jar` - Matchers per JUnit


##  Autors

**Subgrup 11.1 - PROP FIB**

- **Pau Serrano** 
- **Alexander de Jong** 
- **Ferran Blanchart** 
- **Marc Gil** -



##  Inici ràpid

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
## Canvis respecte la segona entrega
- S'han eliminat les funcions setNom i setCotrasenya d'Usuari.java ja que feien el mteix que canviarNom i canviarContrasenya.
**El projecte està llest per jugar! 🎯**