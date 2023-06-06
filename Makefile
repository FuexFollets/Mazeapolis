SOURCE_DIRECTORY := ./src
SOURCE_FILES := $(wildcard $(SOURCE_DIRECTORY)/*.java)

.PHONY: build clean

clean:
	rm -f ./dist/*.class || :
	rm -f ./*.class || :

run:
	java -classpath ./dist Main

build: ./dist $(SOURCE_FILES)
	javac -classpath .:target/dependency/* -d ./dist $(wildcard src/*.java)

./dist:
	[ ! -d ./dist ] && mkdir ./dist || :