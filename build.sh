#!/bin/bash

icons=($(ls icons))
extensions=($(ls extensions))
main_dir=$PWD

if [ ${#icons[@]} != ${#extensions[@]} ]
then
	echo "There aren't a matching number of extensions and icons"
	exit 1
fi

mkdir -p build/jars
cd build/jars

if [ ! -f okhttp.jar ]
then
	curl https://repo1.maven.org/maven2/com/squareup/okhttp3/okhttp/3.8.1/okhttp-3.8.1.jar -o okhttp.jar
fi

if [ ! -f jsoup.jar ]
then
	curl https://repo1.maven.org/maven2/org/jsoup/jsoup/1.10.2/jsoup-1.10.2.jar -o jsoup.jar
fi

cd ../..

stubs=($(find stubs -name *.kt))
cd build/jars

for index in ${!stubs[*]}
do
	stubs[$index]=$main_dir/${stubs[$index]}
done

kotlinc -cp okhttp.jar:jsoup.jar -d monke.jar ${stubs[@]}

mkdir -p ../extensions


for index in ${!extensions[*]}
do
	echo "Building ${extensions[$index]/.kt/} extension..."
	kotlinc -cp okhttp.jar:jsoup.jar:monke.jar -d ${extensions[$index]/.kt/.jar} ../../extensions/${extensions[$index]}
	dx --dex --output=../extensions/${extensions[$index]/.kt/.dex} ${extensions[$index]/.kt/.jar}
	rm ${extensions[$index]/.kt/.jar}

	zip -j ../extensions/${extensions[$index]/.kt/} ../extensions/${extensions[$index]/.kt/.dex} ../../icons/${extensions[$index]/.kt/.png}
	rm ../extensions/${extensions[$index]/.kt/.dex}
done
