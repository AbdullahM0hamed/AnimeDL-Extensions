#!/bin/python 

import os
import subprocess
from pathlib import Path

icons = os.listdir("icons")
extensions = os.listdir("extensions")

if len(icons) != len(extensions):
    print("Icon-extension mismatch")
    exit(1)

rootdir = os.getcwd()

if not os.path.exists("build/jars"):
    os.makedirs("build/jars")

os.chdir("build/jars")

if 'okhttp.jar' not in os.listdir():
    subprocess.getoutput("curl https://repo1.maven.org/maven2/com/squareup/okhttp3/okhttp/3.8.1/okhttp-3.8.1.jar -o okhttp.jar")

if 'jsoup.jar' not in os.listdir():
    subprocess.getoutput("curl https://repo1.maven.org/maven2/org/jsoup/jsoup/1.10.2/jsoup-1.10.2.jar -o jsoup.jar")

stubs = " ".join([x.as_posix() for x in Path(os.path.join(rootdir, 'stubs')).rglob('*.kt')])

subprocess.getoutput(f'kotlinc -cp okhttp.jar:jsoup.jar -d monke.jar {stubs}')
os.makedirs("../extensions")

for index in range(len(extensions)):
    print(f'Building {os.path.basename(extensions[index]).replace(".kt", "")} extension...')
    subprocess.getoutput('kotlinc -cp okhttp.jar:jsoup.jar:monke.jar -d {os.path.basename(extensions[index]).replace(".kt", ".jar")} {extensions[index]}')
