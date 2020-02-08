import csv

trains = set()

with open ("routesData.csv","r") as readFile:
	file = csv.reader(readFile)
	for row in file:
		if len(trains) == 0:
			trains.add(row[0])
			with open("routesData1.csv","w") as writeFile:
				file=csv.writer(writeFile)
				file.writerow([row[0]]+[row[1]]+[row[2]]+[row[3]])
			continue

		if row[0] in trains:
			continue
		trains.add(row[0])
		with open("routesData1.csv","a") as writeFile:
			file=csv.writer(writeFile)
			file.writerow([row[0]]+[row[1]]+[row[2]]+[row[3]])