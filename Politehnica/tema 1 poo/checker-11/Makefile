.PHONY: build clean run
build:
	mkdir classes;
	javac src/shape/*.java -d classes;
	javac -cp classes src/quadtree/*.java -d classes;
clean:
	rm -r classes
run:
	java -Xmx512m -cp classes quadtree.Main