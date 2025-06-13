# === Paths ===
# ========================== INSTRUCCIONES MAKEFILE ==========================
# make           => Compila todo el código fuente y los tests (por módulo)
# make test      => Ejecuta todos los tests JUnit y muestra resultados
# make run       => Ejecuta la aplicación principal (Main.java)
# make clean     => Borra los .class compilados en ../EXE excepto la carpeta ../lib
#
# Estructura:
# - Código fuente está en:         src/
# - Tests están en:                src/test/
# - Librerías (JARs) están en:     ../lib/
# - Archivos compilados van a:     ../EXE/NOMBRE_CLASE
# ===========================================================================

SRC := src
TEST := src/test
EXE := ../EXE
LIB := ../lib
JARS := $(LIB)/junit-4.13.2.jar:$(LIB)/hamcrest-core-1.3.jar

UTIL := $(SRC)/util/*.java
JAVAC := javac
JFLAGS := -cp $(JARS):.

# === Clases con test ===
MODEL_UNITS := Casella Fitxa Jugador Partida Tauler Diccionari Usuari Estadistiques Ranquing

# === Dependencias por clase ===
DEPENDENCIES_Casella := $(SRC)/model/Fitxa.java
DEPENDENCIES_Fitxa :=
DEPENDENCIES_Jugador := $(SRC)/model/Fitxa.java
DEPENDENCIES_Partida := \
	$(SRC)/model/Fitxa.java \
	$(SRC)/model/Diccionari.java \
	$(SRC)/model/Casella.java \
	$(SRC)/model/Jugador.java \
	$(SRC)/model/Tauler.java \
	$(SRC)/util/Pair.java
DEPENDENCIES_Tauler := \
	$(SRC)/model/Casella.java \
	$(SRC)/model/Fitxa.java \
	$(SRC)/util/Pair.java
DEPENDENCIES_Diccionari := $(SRC)/util/Node.java
DEPENDENCIES_Usuari := $(SRC)/model/Estadistiques.java
DEPENDENCIES_Estadistiques :=
DEPENDENCIES_Ranquing :=

# === Target principal ===
all: main $(MODEL_UNITS)

# === main: controladors i presentació ===
main:
	@echo "[main] Compilando presentacio + controladors..."
	mkdir -p $(EXE)/main
	$(JAVAC) -d $(EXE)/main $(JFLAGS) \
		$(UTIL) \
		$(SRC)/model/*.java \
		$(SRC)/controller/*.java \
		$(SRC)/presentacio/*.java
	@echo "[main] Hecho."

# === Regla dinámica por clase ===
define make-class
$1:
	@echo "\n[compilar] ====> $1"
	mkdir -p $(EXE)/$1 ; \
	echo "[+] Compilando $1.java + dependencias en $(EXE)/$1"; \
	$(JAVAC) -d $(EXE)/$1 $(JFLAGS) $(UTIL) $(SRC)/model/$1.java $$(DEPENDENCIES_$1) || echo "[ERROR] Falló compilación de $1"; \
	if [ -f $(TEST)/Test$1.java ]; then \
		echo "[✓] Compilando Test$1.java usando .class de $(EXE)/$1"; \
		javac -d $(EXE)/$1 -cp "$(EXE)/$1:$(JARS):." $(TEST)/Test$1.java || echo "[ERROR] Falló compilación del test de $1"; \
	else \
		echo "[skip] No se encontró Test$1.java"; \
	fi
endef

$(foreach class,$(MODEL_UNITS),$(eval $(call make-class,$(class))))

# === Ejecutar todos los tests ===
test:
	@echo "[test] Ejecutando tests JUnit..."
	@for d in $(EXE)/* ; do \
		for t in $$d/Test*.class ; do \
			if [ -f "$$t" ]; then \
				TESTNAME=$$(basename $$t .class); \
				echo " -> Ejecutando $$TESTNAME en $$d"; \
				java -cp "$$d:$(JARS):." org.junit.runner.JUnitCore $$TESTNAME || echo "  [!] Falló $$TESTNAME"; \
			fi \
		done \
	done

# === Limpiar ===
clean:
	@echo "[clean] Limpiando compilados en ../EXE (conservando ../lib)..."
	@find $(EXE) -mindepth 1 -maxdepth 1 ! -name "lib" -exec rm -rf {} +
	@echo "[clean] Hecho."
# === Ejecutar main ===
run:
	@echo "[run] Ejecutando el programa principal..."
	java -cp "$(EXE)/main:$(JARS):." presentacio.Main
