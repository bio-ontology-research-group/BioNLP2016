#!/bin/bash
#Example perl SR4GN.pl -s setup.txt -i input -o output
setup = $0
file = $1

cp $file ../../libs/sr4gn/SR4GN_unix/input
cd ../../libs/sr4gn/SR4GN_unix/
perl SR4GN.pl -s setup.txt -i input -o output