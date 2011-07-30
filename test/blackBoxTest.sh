#!/bin/bash
result=successfully
# Preparing
echo Preparing for Test...
# - copy the Dataset Transformer to test to bin
rm DataSetTransformer.jar  2>/dev/null
cp ../rel/DataSetTransformer.jar .
rm lib/ -r  2>/dev/null
mkdir lib
cp ../rel/lib/* lib/
rm resultData/* -r 2>/dev/null
# Testing
echo starting test ...
for job in $(ls jobs/*.job)
do
	echo Testing $job
	java -jar DataSetTransformer.jar $job
done
# test results
echo Testing Results :
for res in $(ls shouldData/)
do
	echo Checking $res ...
	if [ -f resultData/$res ]
	then
		if diff shouldData/$res resultData/$res
		then
			echo Diff of $res is ok
		else
			echo Diff of $res failed !
			result=Failed
		fi
	else
		echo ERROR: The File $res has not been created !
		result=Failed
	fi
done
if [ successfully = $result ]
then
	echo "***************************************************"
	echo "*          Test has been successfully !           *"
	echo "***************************************************"

	# Cleanup
	echo starting cleanup ...
	rm resultData/* -r 2>/dev/null
	rm DataSetTransformer.jar  2>/dev/null
	rm lib/ -r  2>/dev/null
	echo Done.
else
	echo "***************************************************"
	echo "*              Test has Failed !                  *"
	echo "***************************************************"

fi
