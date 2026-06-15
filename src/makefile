JAVAC   = javac
JAVA    = java
SRCDIR  = .
BINDIR  = bin
TARGET  = Main

SOURCES = Main.java \
          InterfaceGraphique.java \
          Simulateur.java \
          RessourcePartagee.java \
          Lecteur.java \
          Ecrivain.java \
          Log.java

CLASSES = $(addprefix $(BINDIR)/, $(SOURCES:.java=.class))

ifeq ($(OS),Windows_NT)
RM = rmdir /S /Q
MKDIR = if not exist $(BINDIR) mkdir $(BINDIR)
else
RM = rm -rf
MKDIR = mkdir -p $(BINDIR)
endif

all: clean compile

compile: $(BINDIR) $(CLASSES)

$(BINDIR):
	$(MKDIR)

$(BINDIR)/%.class: %.java
	$(JAVAC) -d $(BINDIR) $<

run: compile
	$(JAVA) -cp $(BINDIR) $(TARGET)

clean:
	@if exist "$(BINDIR)" $(RM) "$(BINDIR)"

.PHONY: all compile run clean
