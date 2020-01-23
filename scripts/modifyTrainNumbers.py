import csv
with open("../datasets/trains.csv","r") as readFile :
	file = csv.reader(readFile)
	i=0
	for row in file:
		i=i+1
		if i==1:
			continue
		print(row[0])
		f = open("../datasets/modifiedTrainNumbers.txt","a")
		f.write(row[0]+"\n")