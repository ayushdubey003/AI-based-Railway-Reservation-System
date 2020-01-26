import csv

tno=[]
tna=[]
days=[]
classes=[]
typ=[]
zone=[]
routen=[]
routec=[]
arr=[]
dept=[]

with open("../datasets/trains.csv","r") as readFile:
	file=csv.reader(readFile)
	for u in file:
		tno.append(u[0])
		tna.append(u[1])
		days.append(u[2])
		classes.append(u[3])
		typ.append(u[4])
		zone.append(u[5])
		routen.append(u[6])
		routec.append(u[7])
		arr.append(u[8])
		dept.append(u[9])

rt=[]
sld=[]
sd=[]
can=[]

with open("../datasets/trainHistory.csv","r") as readFile:
	file=csv.reader(readFile)
	for u in file:
		rt.append(u[0])
		sld.append(u[1])
		sd.append(u[2])
		can.append(u[3])

with open("../datasets/trainsFinal.csv","w") as writeFile:
	file=csv.writer(writeFile)
	for i in range(0,len(tno)):
		file.writerow([tno[i]]+[tna[i]]+[days[i]]+[classes[i]]+[typ[i]]+[zone[i]]+[routen[i]]+[routec[i]]+[arr[i]]+[dept[i]]+[rt[i]]+[sld[i]]+[sd[i]]+[can[i]])