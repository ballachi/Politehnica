JC = javac
JCommand = java
FLAGS = -g:none


SourcePlace = src
Project = tema1


build:
	$(JC) $(FLAGS) *.java

rebuild:
	rm -rf $(Project)
	mkdir $(Project)
	$(JC) $(FLAGS) $(SourcePlace)/*/*.java -d $(Project)

run: 
	java Tema1

clean:
	rm -rf $(Project)
