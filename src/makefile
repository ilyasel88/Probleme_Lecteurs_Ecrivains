JAVAC = javac
JAVA = java
SRCDIR = .
BINDIR = bin
TARGET = Main

SOURCES = Main.java \
          InterfaceGraphique.java \
          Simulateur.java \
          RessourcePartagee.java \
          Lecteur.java \
          Ecrivain.java

CLASSES = $(addprefix $(BINDIR)/, $(SOURCES:.java=.class))

all: clean compile

compile: $(BINDIR) $(CLASSES)

$(BINDIR):
	mkdir -p $(BINDIR)

$(BINDIR)/%.class: %.java
	$(JAVAC) -d $(BINDIR) $<

run: compile
	$(JAVA) -cp $(BINDIR) $(TARGET)

clean:
	rm -rf $(BINDIR)

.PHONY: all compile run clean