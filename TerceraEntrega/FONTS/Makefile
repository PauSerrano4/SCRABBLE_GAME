SHELL := /bin/bash

SRC  := src
EXE  := ../EXE
OUT  := $(EXE)/main

LIB  := ../EXE/lib
JARS := $(LIB)/junit-4.13.2.jar:$(LIB)/hamcrest-core-1.3.jar

JAVAC  := javac
JFLAGS := -cp $(JARS):.

UTIL := $(SRC)/util/*.java

# Carpetas a compilar en bloque
SRC_DIRS := $(SRC)/util \
            $(SRC)/model \
            $(SRC)/controller \
            $(SRC)/persistencia \
            $(SRC)/presentacio \
            $(SRC)/exceptions

SRC_FILES := $(foreach d,$(SRC_DIRS),$(wildcard $(d)/*.java))


.PHONY: all main clean run test units

all: main                             ## Compila toda la aplicación

main:
	@echo "Compilando todo..."
	@mkdir -p $(OUT)
	@$(JAVAC) -d $(OUT) $(JFLAGS) $(SRC_FILES)
	@echo "Clases en $(OUT)"

clean:
	@find $(EXE) -mindepth 1 -maxdepth 1 ! -name "lib" -exec rm -rf {} +
	@echo "  Limpio (lib preservado)"
run:                             ## Ejecuta la app principal

	@java -cp "$(OUT):$(JARS):." presentacio.Main

test: main                           ## Corre todos los tests JUnit amb resum final
	@echo " Executant tots els tests JUnit..."
	@total=0; passed=0; failed=0; \
	echo " Test: test.TestBaseIT (configuració inicial)"; \
	$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $(SRC)/test/TestBaseIT.java 2>/dev/null || true; \
	echo " TestBaseIT - Base configurada"; \
	for t in $$(find $(SRC)/test -name "Test*.java" | grep -v TestBaseIT | grep -v TestNetejaUsuarisIT | sort); do \
		class=$${t#$(SRC)/}; class=$${class%.java}; class=$${class//\//.}; \
		echo " Test: $${class}"; \
		$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $$t; \
		if java -cp "$(OUT):$(JARS):." org.junit.runner.JUnitCore $$class 2>/dev/null | grep -q "OK"; then \
			echo " $${class} - PASSAT"; \
			passed=$$((passed + 1)); \
		else \
			echo " $${class} - FALLAT"; \
			failed=$$((failed + 1)); \
		fi; \
		total=$$((total + 1)); \
	done; \
	if [ -f "$(SRC)/test/TestNetejaUsuarisIT.java" ]; then \
		echo " Test: test.TestNetejaUsuarisIT (neteja final)"; \
		$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $(SRC)/test/TestNetejaUsuarisIT.java; \
		if java -cp "$(OUT):$(JARS):." org.junit.runner.JUnitCore test.TestNetejaUsuarisIT 2>/dev/null | grep -q "OK"; then \
			echo " test.TestNetejaUsuarisIT - PASSAT"; \
			passed=$$((passed + 1)); \
		else \
			echo " test.TestNetejaUsuarisIT - FALLAT"; \
			failed=$$((failed + 1)); \
		fi; \
		total=$$((total + 1)); \
	fi; \
	echo ""; \
	echo " RESUM FINAL:"; \
	echo "   Total: $$total tests"; \
	echo "    Passats: $$passed"; \
	echo "    Fallats: $$failed"; \
	if [ $$failed -eq 0 ]; then \
		echo " TOTS ELS TESTS HAN PASSAT! "; \
		notify-send "Tests Scrabble" " Tots els tests han passat!" 2>/dev/null || echo " Tots els tests OK!"; \
	else \
		echo "  Hi ha tests que han fallat"; \
		notify-send "Tests Scrabble" " $$failed tests han fallat" 2>/dev/null || echo " Tests fallats: $$failed"; \
	fi

test-silent: main                    ## Executa tests en silenci i només mostra resum
	@echo " Executant tests en mode silenciós..."
	@total=0; passed=0; failed=0; failed_tests=""; \
	$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $(SRC)/test/TestBaseIT.java >/dev/null 2>&1; \
	printf "B"; \
	for t in $$(find $(SRC)/test -name "Test*.java" | grep -v TestBaseIT | sort); do \
		class=$${t#$(SRC)/}; class=$${class%.java}; class=$${class//\//.}; \
		$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $$t >/dev/null 2>&1; \
		if java -cp "$(OUT):$(JARS):." org.junit.runner.JUnitCore $$class >/dev/null 2>&1; then \
			passed=$$((passed + 1)); \
		else \
			failed=$$((failed + 1)); \
			failed_tests="$$failed_tests $$class"; \
		fi; \
		total=$$((total + 1)); \
		printf "."; \
	done; \
	echo ""; \
	echo " RESUM FINAL:"; \
	echo "   Total: $$total tests"; \
	echo "    Passats: $$passed"; \
	echo "    Fallats: $$failed"; \
	if [ $$failed -gt 0 ]; then \
		echo "   Tests fallats:$$failed_tests"; \
	fi; \
	if [ $$failed -eq 0 ]; then \
		echo " TOTS ELS TESTS HAN PASSAT! "; \
		notify-send "Tests Scrabble" " Tots els $$total tests han passat!" 2>/dev/null || true; \
	else \
		notify-send "Tests Scrabble" " $$failed de $$total tests han fallat" 2>/dev/null || true; \
	fi

test-one: main                       ## Ejecuta un test específico (usar TEST=nombreTest)
	@if [ -z "$(TEST)" ]; then \
		echo "Uso: make test-one TEST=TestCasosEspecialesIT"; \
		exit 1; \
	fi
	@echo "Ejecutando test: $(TEST)"
	@find $(SRC)/test -name "$(TEST).java" | while read t; do \
		class=$${t#$(SRC)/}; class=$${class%.java}; class=$${class//\//.}; \
		echo "  - Compilando y ejecutando $${class}"; \
		$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $$t; \
		java -cp "$(OUT):$(JARS):." org.junit.runner.JUnitCore $$class; \
	done

test-method: main                    ## Ejecuta un método específico (usar TEST=TestClass METHOD=testMethod)
	@if [ -z "$(TEST)" ] || [ -z "$(METHOD)" ]; then \
		echo "Uso: make test-method TEST=TestCasosEspecialsIT METHOD=testFinPerFitxesAcabades"; \
		exit 1; \
	fi
	@echo "Ejecutando $(TEST).$(METHOD)"
	@find $(SRC)/test -name "$(TEST).java" | while read t; do \
		class=$${t#$(SRC)/}; class=$${class%.java}; class=$${class//\//.}; \
		$(JAVAC) -d $(OUT) -cp "$(OUT):$(JARS):." $$t; \
		java -cp "$(OUT):$(JARS):." org.junit.runner.JUnitCore $$class#$(METHOD); \
	done


MODEL_UNITS := Casella Fitxa Jugador Partida Tauler Diccionari Usuari Estadistiques Ranquing
UTIL_UNITS  := Pair Node Play Anchor DigrafMapper Temporitzador TemporitzadorListener

define make-class
$(1):
	@mkdir -p $(EXE)/$(1)
	@$(JAVAC) -d $(EXE)/$(1) $(JFLAGS) $(UTIL) $(SRC)/model/$(1).java $$(DEPENDENCIES_$(1)) $(SRC)/exceptions/*.java || true
endef
$(foreach class,$(MODEL_UNITS),$(eval $(call make-class,$(class))))

define make-util-class
$(1):
	@mkdir -p $(EXE)/$(1)
	@$(JAVAC) -d $(EXE)/$(1) $(JFLAGS) $(UTIL) $(SRC)/util/$(1).java $(SRC)/exceptions/*.java || true
endef
$(foreach class,$(UTIL_UNITS),$(eval $(call make-util-class,$(class))))

units: $(MODEL_UNITS) $(UTIL_UNITS)   ## Compila cada classe amb dependències

# Classes de test d'integració
IT_TESTS = test.TestJugadesIT \
           test.TestPartidaBasicaIT \
           test.TestUsuarisIT \
           test.TestEstadistiquesRanquingIT \
           test.TestPersistenciaIT \
           test.TestCasosEspecialsIT \
           test.TestErrorsIT \
           test.TestNetejaUsuarisIT


