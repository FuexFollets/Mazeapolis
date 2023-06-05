clean:
	rm *.class

run:
	java -classpath .:target/dependency/* Main

build:
	javac -classpath .:target/dependency/* -d . $(find . -type f -name '*.java')