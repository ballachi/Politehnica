#!/bin/sh
num_repeats=5
num_threads=8
suma1=0
suma2=0
suma3=0

input1="input1"
input2="input2"
input3="big_input"

output1="output1"
output2="output2"
output3="big_output"

make
echo
for ((i=1; i<=$num_threads; i++)); do
	export OMP_NUM_THREADS=$i
	suma1="0.0"
	suma2=0
	suma3=0
	for ((j=1; j<=$num_repeats; j++)); do
		outTime1=$(./parallel_snake $input1 output 20000)
		diff output $output1
		#echo $outTime1
		suma1=$(bc -l <<< "$suma1 + $outTime1")
	done
	echo "Media pentru $input1 cu $i Thred-uri Timpul este " $(bc -l <<< "scale=9;$suma1/$num_repeats")

	for ((j=1; j<=$num_repeats; j++)); do
		outTime2=$(./parallel_snake $input2 output 20000)
		diff output $output2
		#echo $outTime2
		suma2=$(bc -l <<< "$suma2 + $outTime2")
	done
	echo "Media pentru $input2 cu $i Thred-uri Timpul este " $(bc -l <<< "scale=9;$suma2/$num_repeats")


	for ((j=1; j<=$num_repeats; j++)); do
		outTime3=$(./parallel_snake $input3 output 20000)
		diff output $output3
		#echo $outTime3
		suma3=$(bc -l <<< "$suma3 + $outTime3")
	done
	echo "Media pentru $input3 cu $i Thred-uri Timpul este " $(bc -l <<< "scale=9;$suma3/$num_repeats")

echo 
done

